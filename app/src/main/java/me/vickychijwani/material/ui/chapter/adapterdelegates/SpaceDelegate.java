package me.vickychijwani.material.ui.chapter.adapterdelegates;

import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import me.vickychijwani.material.ui.chapter.ChapterAdapterDelegate;

public class SpaceDelegate extends ChapterAdapterDelegate {

    public SpaceDelegate() {
        super(ViewType.SPACE);
    }

    @Override
    public boolean isForViewType(@NonNull List<Object> items, int position) {
        return items.get(position) instanceof SpaceEntity;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View spaceView = new View(parent.getContext());
        spaceView.setPadding(0, 0, 0, 0);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 0);
        spaceView.setLayoutParams(lp);
        return new SpaceViewHolder(spaceView);
    }

    @Override
    public void onBindViewHolder(@NonNull List<Object> items, int position, @NonNull RecyclerView.ViewHolder holder) {
        SpaceViewHolder vh = (SpaceViewHolder) holder;
        SpaceEntity entity = (SpaceEntity) items.get(position);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) vh.space.getLayoutParams();
        lp.topMargin = vh.space.getContext().getResources().getDimensionPixelSize(entity.heightResId);
        vh.space.setLayoutParams(lp);
    }

    private static class SpaceViewHolder extends RecyclerView.ViewHolder {
        public final View space;
        public SpaceViewHolder(View itemView) {
            super(itemView);
            space = itemView;
        }
    }

    public static class SpaceEntity {
        @DimenRes public final int heightResId;
        public SpaceEntity(@DimenRes int heightResId) {
            this.heightResId = heightResId;
        }
    }

}
