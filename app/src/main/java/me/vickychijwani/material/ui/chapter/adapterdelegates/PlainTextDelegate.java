package me.vickychijwani.material.ui.chapter.adapterdelegates;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.vickychijwani.material.R;
import me.vickychijwani.material.spec.entity.ArticleTitle;
import me.vickychijwani.material.spec.entity.ModuleTitle;
import me.vickychijwani.material.spec.entity.PlainTextEntity;

public class PlainTextDelegate extends AbsTextDelegate<PlainTextEntity> {

    private static final Map<Class<? extends PlainTextEntity>, Integer> LINE_HEIGHT_HINTS;
    private static final Map<Class<? extends PlainTextEntity>, Integer> TEXT_APPEARANCE;
    private static final Map<Class<? extends PlainTextEntity>, Integer> TOP_MARGINS;
    private static final Map<Class<? extends PlainTextEntity>, Integer> BOTTOM_MARGINS;

    static {
        LINE_HEIGHT_HINTS = new HashMap<>();
        TEXT_APPEARANCE = new HashMap<>();
        TOP_MARGINS = new HashMap<>();
        BOTTOM_MARGINS = new HashMap<>();

        LINE_HEIGHT_HINTS.put(ArticleTitle.class, R.dimen.article_title_line_height);
        TEXT_APPEARANCE.put(ArticleTitle.class, R.style.TextAppearance_ArticleTitle);
        TOP_MARGINS.put(ArticleTitle.class, R.dimen.article_title_margin_top_bottom);
        BOTTOM_MARGINS.put(ArticleTitle.class, R.dimen.article_title_margin_top_bottom);

        LINE_HEIGHT_HINTS.put(ModuleTitle.class, R.dimen.module_title_line_height);
        TEXT_APPEARANCE.put(ModuleTitle.class, R.style.TextAppearance_ModuleTitle);
        TOP_MARGINS.put(ModuleTitle.class, R.dimen.module_title_margin_bottom);
        BOTTOM_MARGINS.put(ModuleTitle.class, 0);
    }

    public PlainTextDelegate(@NonNull Context context) {
        super(context, ViewType.GENERIC_PLAIN_TEXT);
    }

    @Override
    protected String getTextAsCharSequence(PlainTextEntity entity) {
        return entity.getText();
    }

    @Override
    public boolean isForViewType(@NonNull List<Object> items, int position) {
        return items.get(position) instanceof PlainTextEntity;
    }

    @Override
    protected int getLayout() {
        return R.layout.chapter_generic_text;
    }

    @Override
    protected int getLineHeightHint(PlainTextEntity entity) {
        return LINE_HEIGHT_HINTS.get(entity.getClass());
    }

    @Override
    protected int getTopMargin(PlainTextEntity entity) {
        return TOP_MARGINS.get(entity.getClass());
    }

    @Override
    protected int getBottomMargin(PlainTextEntity entity) {
        return BOTTOM_MARGINS.get(entity.getClass());
    }

    @Override
    protected int getTextAppearance(PlainTextEntity entity) {
        return TEXT_APPEARANCE.get(entity.getClass());
    }

}
