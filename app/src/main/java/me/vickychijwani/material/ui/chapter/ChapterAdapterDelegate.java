package me.vickychijwani.material.ui.chapter;

import android.support.v7.widget.RecyclerView;

import com.hannesdorfmann.adapterdelegates.AbsAdapterDelegate;

import java.util.List;

public abstract class ChapterAdapterDelegate extends AbsAdapterDelegate<List<Object>> {

    public enum ViewType {
        NULL,       // dummy delegate for handling null items due to deserialization errors
        SPACE,
        GENERIC_HTML_TEXT,
        GENERIC_PLAIN_TEXT,
        IMAGE,
        VIDEO,
    }

    protected ChapterAdapterDelegate(ViewType viewType) {
        super(viewType.ordinal());
    }

    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {}

}
