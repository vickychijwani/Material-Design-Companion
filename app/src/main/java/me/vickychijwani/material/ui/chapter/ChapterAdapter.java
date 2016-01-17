package me.vickychijwani.material.ui.chapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.vickychijwani.material.R;
import me.vickychijwani.material.spec.entity.Article;
import me.vickychijwani.material.spec.entity.ArticleList;
import me.vickychijwani.material.spec.entity.Chapter;
import me.vickychijwani.material.spec.entity.ChapterIntroWithHtml;
import me.vickychijwani.material.spec.entity.ChapterIntroWithModules;
import me.vickychijwani.material.spec.entity.ChapterPart;
import me.vickychijwani.material.spec.entity.Figure;
import me.vickychijwani.material.spec.entity.FigureGroup;
import me.vickychijwani.material.spec.entity.Module;
import me.vickychijwani.material.spec.entity.ModuleBody;
import me.vickychijwani.material.spec.entity.ModulePart;
import me.vickychijwani.material.ui.chapter.adapterdelegates.HtmlTextDelegate;
import me.vickychijwani.material.ui.chapter.adapterdelegates.ImageDelegate;
import me.vickychijwani.material.ui.chapter.adapterdelegates.PlainTextDelegate;
import me.vickychijwani.material.ui.chapter.adapterdelegates.SpaceDelegate;
import me.vickychijwani.material.ui.chapter.adapterdelegates.VideoDelegate;

class ChapterAdapter extends RecyclerView.Adapter {

    private static final String TAG = ChapterAdapter.class.getSimpleName();

    private final List<Object> mItems;
    private final ChapterAdapterDelegatesManager mDelegatesManager;

    public ChapterAdapter(@NonNull Context context, @NonNull Chapter chapter) {
        mItems = ItemsBuilder.build(chapter);
        mDelegatesManager = new ChapterAdapterDelegatesManager();

        addDelegate(new SpaceDelegate());
        addDelegate(new HtmlTextDelegate(context));
        addDelegate(new PlainTextDelegate(context));
        addDelegate(new ImageDelegate(context));
        addDelegate(new VideoDelegate(context));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return mDelegatesManager.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mDelegatesManager.onBindViewHolder(mItems, position, holder);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        mDelegatesManager.onViewRecycled(holder);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        Log.e(TAG, "ChapterAdapter#onFailedToRecycleView called");
        return false;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mDelegatesManager.getItemViewType(mItems, position);
    }

    private void addDelegate(ChapterAdapterDelegate delegate) {
        mDelegatesManager.addDelegate(delegate);
    }



    private static final class ItemsBuilder {

        private static List<Object> build(Chapter chapter) {
            List<Object> items = new ArrayList<>();
            if (chapter.content.length > 0 && !(chapter.content[0] instanceof Figure)) {
                items.add(new SpaceDelegate.SpaceEntity(R.dimen.chapter_intro_margin_top));
            }
            for (ChapterPart part : chapter.content) {
                if (part instanceof ChapterIntroWithHtml) {
                    add((ChapterIntroWithHtml) part, items);
                } else if (part instanceof ChapterIntroWithModules) {
                    add((ChapterIntroWithModules) part, items);
                } else if (part instanceof Figure) {
                    add((Figure) part, items);
                } else if (part instanceof ArticleList) {
                    add((ArticleList) part, items);
                } else {
                    Log.e(TAG, "Skipped ChapterPart of type " + part.getClass().getSimpleName());
                }
            }
            return items;
        }

        private static void add(ChapterIntroWithHtml intro, List<Object> items) {
            items.add(intro);
        }

        private static void add(ChapterIntroWithModules intro, List<Object> items) {
            for (Module module : intro.modules) {
                add(module, items);
            }
        }

        private static void add(Figure figure, List<Object> items) {
            items.add(figure);
        }

        private static void add(ArticleList articleList, List<Object> items) {
            for (Article article : articleList.articles) {
                add(article, items);
            }
        }

        private static void add(Article article, List<Object> items) {
            items.add(article.title);
            for (Module module : article.modules) {
                add(module, items);
            }
        }

        private static void add(Module module, List<Object> items) {
            if (module.title != null) {
                items.add(module.title);
            }
            for (ModulePart modulePart : module.content) {
                if (modulePart instanceof FigureGroup) {
                    add((FigureGroup) modulePart, items);
                } else if (modulePart instanceof ModuleBody) {
                    add((ModuleBody) modulePart, items);
                } else {
                    throw new RuntimeException("Unknown ModulePart of type: " + modulePart.getClass().getSimpleName());
                }
            }
        }

        private static void add(ModuleBody moduleBody, List<Object> items) {
            items.add(moduleBody);
        }

        private static void add(FigureGroup figureGroup, List<Object> items) {
            for (Figure figure : figureGroup.figures) {
                add(figure, items);
            }
        }

    }

}
