package me.vickychijwani.material.ui.chapter.adapterdelegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.vickychijwani.material.R;
import me.vickychijwani.material.spec.entity.ChapterIntroWithHtml;
import me.vickychijwani.material.spec.entity.HtmlTextEntity;
import me.vickychijwani.material.spec.entity.ModuleBody;

public class HtmlTextDelegate extends AbsTextDelegate<HtmlTextEntity> {

    private static final Map<Class<? extends HtmlTextEntity>, Integer> LINE_HEIGHT_HINTS;
    private static final Map<Class<? extends HtmlTextEntity>, Integer> TEXT_APPEARANCE;
    private static final Map<Class<? extends HtmlTextEntity>, Integer> TOP_MARGINS;
    private static final Map<Class<? extends HtmlTextEntity>, Integer> BOTTOM_MARGINS;

    static {
        LINE_HEIGHT_HINTS = new HashMap<>();
        TEXT_APPEARANCE = new HashMap<>();
        TOP_MARGINS = new HashMap<>();
        BOTTOM_MARGINS = new HashMap<>();

        LINE_HEIGHT_HINTS.put(ChapterIntroWithHtml.class, R.dimen.chapter_intro_line_height);
        TEXT_APPEARANCE.put(ChapterIntroWithHtml.class, R.style.TextAppearance_ChapterIntro);
        TOP_MARGINS.put(ChapterIntroWithHtml.class, 0);
        BOTTOM_MARGINS.put(ChapterIntroWithHtml.class, R.dimen.chapter_intro_margin_bottom);

        LINE_HEIGHT_HINTS.put(ModuleBody.class, R.dimen.module_body_line_height);
        TEXT_APPEARANCE.put(ModuleBody.class, R.style.TextAppearance_ModuleBody);
        TOP_MARGINS.put(ModuleBody.class, 0);
        BOTTOM_MARGINS.put(ModuleBody.class, R.dimen.module_body_margin_bottom);
    }

    public HtmlTextDelegate(@NonNull Context context) {
        super(context, ViewType.GENERIC_HTML_TEXT);
    }

    @Override
    public boolean isForViewType(@NonNull List<Object> items, int position) {
        return items.get(position) instanceof HtmlTextEntity;
    }

    @Override
    protected int getLayout() {
        return R.layout.chapter_generic_text;
    }

    @Override
    protected int getLineHeightHint(HtmlTextEntity entity) {
        return LINE_HEIGHT_HINTS.get(entity.getClass());
    }

    @Override
    protected int getTopMargin(HtmlTextEntity entity) {
        return TOP_MARGINS.get(entity.getClass());
    }

    @Override
    protected int getBottomMargin(HtmlTextEntity entity) {
        return BOTTOM_MARGINS.get(entity.getClass());
    }

    @Override
    protected int getTextAppearance(HtmlTextEntity entity) {
        return TEXT_APPEARANCE.get(entity.getClass());
    }

    protected final CharSequence getTextAsCharSequence(HtmlTextEntity entity) {
        return Html.fromHtml(entity.getHtml());
    }

}
