package me.vickychijwani.material.ui.chapter.adapterdelegates;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.vickychijwani.material.BuildConfig;
import me.vickychijwani.material.R;
import me.vickychijwani.material.spec.entity.Image;
import me.vickychijwani.material.ui.chapter.ChapterAdapterDelegate;
import me.vickychijwani.material.ui.widget.BaselineGridTextView;
import me.vickychijwani.material.util.DeviceUtil;
import me.vickychijwani.material.util.ImageUtil;

public class ImageDelegate extends ChapterAdapterDelegate {

    private final LayoutInflater mInflater;
    private final Picasso mPicasso;

    public ImageDelegate(@NonNull Context context) {
        super(ViewType.IMAGE);
        mInflater = LayoutInflater.from(context);
        mPicasso = ImageUtil.getPicasso(context);
    }

    @Override
    public boolean isForViewType(@NonNull List<Object> items, final int position) {
        return items.get(position) instanceof Image;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.chapter_image, parent, false);
        return new ImageVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull List<Object> items, final int position,
                                 @NonNull RecyclerView.ViewHolder holder) {
        ImageVH vh = (ImageVH) holder;
        Image image = (Image) items.get(position);
        vh.image.setImageDrawable(null);
        loadImage(image.src, vh.image);
        if (image.caption != null) {
            vh.caption.setText(Html.fromHtml(image.caption));
            vh.caption.setVisibility(View.VISIBLE);
        } else {
            vh.caption.setVisibility(View.GONE);
        }
    }

    private void loadImage(String imageSrc, ImageView imageView) {
        RequestCreator imageRequest;
        if (BuildConfig.DEBUG) {
            imageRequest = mPicasso.load(DeviceUtil.getMaterialMediaPath(imageSrc));
        } else {
            imageRequest = mPicasso.load("file:///android_asset/" + imageSrc);
        }
        imageRequest
                .resize(DeviceUtil.getScreenWidth(mInflater.getContext()), 0)
                .into(imageView);
    }



    static final class ImageVH extends RecyclerView.ViewHolder {
        @Bind(R.id.figure)      ImageView image;
        @Bind(R.id.caption)     BaselineGridTextView caption;

        public ImageVH(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
