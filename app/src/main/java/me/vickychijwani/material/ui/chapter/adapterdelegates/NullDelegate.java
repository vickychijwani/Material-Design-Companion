package me.vickychijwani.material.ui.chapter.adapterdelegates;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.vickychijwani.material.BuildConfig;
import me.vickychijwani.material.ui.chapter.ChapterAdapterDelegate;

// dummy delegate for handling null items due to deserialization errors
public class NullDelegate extends ChapterAdapterDelegate {

    public NullDelegate() {
        super(ViewType.NULL);
    }

    @Override
    public boolean isForViewType(@NonNull List<Object> items, int position) {
        boolean isNull = items.get(position) == null;
        if (BuildConfig.DEBUG && isNull) {
            throw new RuntimeException("NullDelegate: null item detected at position " + position);
        }
        return isNull;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        return new RecyclerView.ViewHolder(new View(parent.getContext())) {};
    }

    @Override
    public void onBindViewHolder(@NonNull List<Object> items, int position, @NonNull RecyclerView.ViewHolder holder) {
        // NO-OP
    }

}
