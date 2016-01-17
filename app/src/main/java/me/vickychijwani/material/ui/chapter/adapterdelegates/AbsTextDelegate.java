package me.vickychijwani.material.ui.chapter.adapterdelegates;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.DimenRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.vickychijwani.material.R;
import me.vickychijwani.material.ui.chapter.ChapterAdapterDelegate;
import me.vickychijwani.material.ui.widget.BaselineGridTextView;

abstract class AbsTextDelegate<ENTITY> extends ChapterAdapterDelegate {

    private final LayoutInflater mInflater;

    public AbsTextDelegate(@NonNull Context context, ViewType viewType) {
        super(viewType);
        mInflater = LayoutInflater.from(context);
    }

    public abstract boolean isForViewType(@NonNull List<Object> items, int position);

    @NonNull
    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(getLayout(), parent, false);
        return new HtmlVH(view);
    }

    @LayoutRes
    protected abstract int getLayout();

    protected abstract CharSequence getTextAsCharSequence(ENTITY entity);

    @DimenRes
    protected abstract int getLineHeightHint(ENTITY entity);

    @DimenRes
    protected abstract int getTopMargin(ENTITY entity);

    @DimenRes
    protected abstract int getBottomMargin(ENTITY entity);

    @StyleRes
    protected abstract int getTextAppearance(ENTITY entity);

    @Override
    public final void onBindViewHolder(@NonNull List<Object> items, int position,
                                       @NonNull RecyclerView.ViewHolder holder) {
        HtmlVH vh = (HtmlVH) holder;
        //noinspection unchecked
        ENTITY entity = (ENTITY) items.get(position);
        BaselineGridTextView v = vh.textView;
        Context context = v.getContext();
        Resources resources = context.getResources();

        v.setText(trim(getTextAsCharSequence(entity)));
        v.setLineHeightHintId(getLineHeightHint(entity));
        v.setTextAppearance(context, getTextAppearance(entity));

        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
        int textMargin = resources.getDimensionPixelOffset(R.dimen.text_margin_left_right);
        lp.leftMargin = textMargin;
        lp.rightMargin = textMargin;
        int topMarginResId = getTopMargin(entity);
        if (topMarginResId > 0) {
            lp.topMargin = resources.getDimensionPixelOffset(topMarginResId);
        }
        int bottomMarginResId = getBottomMargin(entity);
        if (bottomMarginResId > 0) {
            lp.bottomMargin = resources.getDimensionPixelOffset(bottomMarginResId);
        }
        v.setLayoutParams(lp);
    }

    private static CharSequence trim(CharSequence cs) {
        if (cs == null) {
            return "";
        }
        int i = cs.length();
        do {
            --i;
        } while (i >= 0 && Character.isWhitespace(cs.charAt(i)));
        return cs.subSequence(0, i+1);
    }



    static final class HtmlVH extends RecyclerView.ViewHolder {
        @Bind(R.id.text_view)       BaselineGridTextView textView;

        public HtmlVH(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
