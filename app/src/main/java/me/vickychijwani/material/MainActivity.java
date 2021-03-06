package me.vickychijwani.material;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import me.vickychijwani.material.spec.SpecReader;
import me.vickychijwani.material.spec.entity.Index;
import me.vickychijwani.material.spec.entity.IndexSection;
import me.vickychijwani.material.spec.entity.IndexSubsection;
import me.vickychijwani.material.ui.chapter.ChapterFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private NavigationView mNavigationView;
    private WeakReference<MenuItem> mSelectedNavItem = null;
    private Fragment mCurrentFragment = null;
    private IndexSubsection mCurrentIndexSubsection = null;
    private static final String CHAPTER_FRAGMENT_TAG = "tag:chapter_fragment";

    private static final Pattern URL_PATH_SEPARATOR_PATTERN = Pattern.compile("/");
    private static final Pattern URL_ANCHOR_SEPARATOR_PATTERN = Pattern.compile("#");

    private static final String PREF_LAST_VIEWED_CHAPTER = "pref:last-viewed-chapter";

    private final SpecReader mSpecReader = new SpecReader();
    private final Map<Integer, IndexSubsection> mMenuItemIdToIndexSubsection = new HashMap<>();
    private final Map<IndexSubsection, Integer> mIndexSubsectionToMenuItemId = new HashMap<>();
    private final Map<String, IndexSubsection> mRelativeHrefToIndexSubsection = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        Index specIndex = populateIndex(mNavigationView);
        mNavigationView.setNavigationItemSelectedListener(this);

        Intent intent = getIntent();
        if (savedInstanceState != null) {
            mCurrentFragment = getSupportFragmentManager()
                    .findFragmentByTag(CHAPTER_FRAGMENT_TAG);
        } else if (intent != null && intent.getData() != null &&
                intent.getData().toString().startsWith("http")) {
            String fullHref = intent.getData().toString();
            String relativeHref = specHrefToRelativeHref(fullHref);
            IndexSubsection selectedSubsection = mRelativeHrefToIndexSubsection.get(relativeHref);
            if (selectedSubsection != null) {
                loadChapter(selectedSubsection);
            } else {
                Log.e(TAG, "No index subsection found for URL " + fullHref);
                throw new RuntimeException("Forcing crash, index subsection not found for URL " +
                        fullHref);
            }
        } else {
            IndexSubsection subsectionToLoad = specIndex.sections[0].sections[0];
            String relHrefOfLastViewedSubsection = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_LAST_VIEWED_CHAPTER, null);
            if (relHrefOfLastViewedSubsection != null) {
                subsectionToLoad = mRelativeHrefToIndexSubsection.get(relHrefOfLastViewedSubsection);
            }
            loadChapter(subsectionToLoad);
        }
    }

    private void loadChapter(@NonNull IndexSubsection subsection) {
        Fragment fragment = ChapterFragment.newInstance(subsection);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment != null) {
            transaction.remove(mCurrentFragment);
        }
        transaction
                .add(R.id.fragment_container, fragment, CHAPTER_FRAGMENT_TAG)
                // TODO addToBackStack
                .commit();
        mCurrentFragment = fragment;
        mCurrentIndexSubsection = subsection;
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(subsection.title);
        mNavigationView.setCheckedItem(mIndexSubsectionToMenuItemId.get(subsection));
    }

    private Index populateIndex(NavigationView navigationView) {
        Index specIndex = mSpecReader.getIndex(getAssets());
        Menu menu = navigationView.getMenu();
        @IdRes final int STARTING_MENU_ITEM_ID = 1;
        @IdRes int menuItemId = STARTING_MENU_ITEM_ID;
        for (IndexSection section : specIndex.sections) {
            String sectionTitle = section.title;
            SubMenu submenu = menu.addSubMenu(sectionTitle);
            for (IndexSubsection subsection : section.sections) {
                String subsectionTitle = subsection.title;
                submenu
                        .add(Menu.NONE, menuItemId, Menu.NONE, subsectionTitle)
                        .setCheckable(true);
                mMenuItemIdToIndexSubsection.put(menuItemId, subsection);
                mIndexSubsectionToMenuItemId.put(subsection, menuItemId);
                mRelativeHrefToIndexSubsection.put(specHrefToRelativeHref(subsection.href), subsection);
                ++menuItemId;
            }
        }
        navigationView.setCheckedItem(STARTING_MENU_ITEM_ID);
        return specIndex;
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCurrentIndexSubsection != null) {
            SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
            pref.edit().putString(PREF_LAST_VIEWED_CHAPTER, specHrefToRelativeHref(mCurrentIndexSubsection.href)).apply();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_view_on_website) {
            Uri webUri = Uri.parse(mCurrentIndexSubsection.href);
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, webUri);
            PackageManager pm = getPackageManager();
            List<ResolveInfo> activities = pm.queryIntentActivities(browserIntent, 0);
            List<Intent> targetIntents = new ArrayList<>();
            String myPackageName = getPackageName();
            if (! activities.isEmpty()) {
                for (ResolveInfo info : activities) {
                    String packageName = info.activityInfo.packageName;
                    if (!myPackageName.equals(packageName)) {
                        Intent targetIntent = new Intent(Intent.ACTION_VIEW, webUri);
                        targetIntent.setPackage(packageName);
                        targetIntents.add(targetIntent);
                    }
                }
                Intent chooserIntent = Intent.createChooser(targetIntents.remove(0), "View on website");
                //noinspection ToArrayCallWithZeroLengthArrayArgument
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
            } else {
                Toast.makeText(this, R.string.view_on_website_error, Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        IndexSubsection subsection = mMenuItemIdToIndexSubsection.get(item.getItemId());
        loadChapter(subsection);
        if (mSelectedNavItem != null && mSelectedNavItem.get() != null) {
            mSelectedNavItem.get().setChecked(false);
        }
        item.setChecked(true);
        mSelectedNavItem = new WeakReference<>(item);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private String specHrefToRelativeHref(String fullSpecHref) {
        // limit = 6 means "http://www.google.com/design/spec/animation/authentic-motion.html"
        // will have "animation/authentic-motion.html" as the last entry
        String[] hrefParts = URL_PATH_SEPARATOR_PATTERN.split(fullSpecHref, 6);
        String relativeHrefWithOptionalAnchor = hrefParts[hrefParts.length-1];
        String[] relativeHrefAnchorParts = URL_ANCHOR_SEPARATOR_PATTERN
                .split(relativeHrefWithOptionalAnchor);
        return relativeHrefAnchorParts[0];
    }

}
