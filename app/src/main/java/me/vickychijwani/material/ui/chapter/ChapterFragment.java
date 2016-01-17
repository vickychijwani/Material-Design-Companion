package me.vickychijwani.material.ui.chapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.vickychijwani.material.R;
import me.vickychijwani.material.spec.SpecReader;
import me.vickychijwani.material.spec.entity.Chapter;
import me.vickychijwani.material.spec.entity.IndexSubsection;

public class ChapterFragment extends Fragment {

    private static final String TAG = "ChapterFragment";
    private static final String KEY_CHAPTER_FILE_PATH = "chapter_file_path";

    @Bind(R.id.chapter_content)     RecyclerView mContentRecyclerView;

    private String mChapterFilepath;

    public ChapterFragment() {}

    public static ChapterFragment newInstance(IndexSubsection subsection) {
        ChapterFragment fragment = new ChapterFragment();
        Bundle args = new Bundle();
        args.putString(KEY_CHAPTER_FILE_PATH, subsection.filepath);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mChapterFilepath = getArguments().getString(KEY_CHAPTER_FILE_PATH);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter, container, false);
        ButterKnife.bind(this, view);
        Activity activity = getActivity();
        Chapter chapter = new SpecReader().getChapter(mChapterFilepath, activity.getAssets());
        mContentRecyclerView.setHasFixedSize(true);
        mContentRecyclerView.setAdapter(new ChapterAdapter(activity, chapter));
        return view;
    }

}
