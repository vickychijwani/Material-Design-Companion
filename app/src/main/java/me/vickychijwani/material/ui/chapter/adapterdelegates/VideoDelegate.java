package me.vickychijwani.material.ui.chapter.adapterdelegates;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.vickychijwani.material.BuildConfig;
import me.vickychijwani.material.R;
import me.vickychijwani.material.spec.entity.Video;
import me.vickychijwani.material.ui.chapter.ChapterAdapterDelegate;
import me.vickychijwani.material.ui.widget.BaselineGridTextView;
import me.vickychijwani.material.ui.widget.SimpleVideoView;
import me.vickychijwani.material.util.DeviceUtil;

public class VideoDelegate extends ChapterAdapterDelegate implements View.OnClickListener {

    private final LayoutInflater mInflater;
    private final Picasso mPicasso;

    public VideoDelegate(@NonNull Context context) {
        super(ViewType.VIDEO);
        mInflater = LayoutInflater.from(context);
        if (BuildConfig.DEBUG) {
            mPicasso = new Picasso.Builder(context).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Log.e("Picasso", Log.getStackTraceString(exception));
                    throw new RuntimeException("Forcing crash, image load failed!");
                }
            }).build();
            mPicasso.setLoggingEnabled(true);
        } else {
            mPicasso = Picasso.with(context);
        }
    }

    @Override
    public boolean isForViewType(@NonNull List<Object> items, int position) {
        return items.get(position) instanceof Video;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = mInflater.inflate(R.layout.chapter_video, parent, false);
        return new VideoVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull List<Object> items, int position,
                                 @NonNull RecyclerView.ViewHolder holder) {
        VideoVH vh = (VideoVH) holder;
        Video video = (Video) items.get(position);
        loadVideo(video.src, vh.video);
        if (video.caption != null) {
            vh.caption.setText(Html.fromHtml(video.caption));
            vh.caption.setVisibility(View.VISIBLE);
        } else {
            vh.caption.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
        VideoVH vh = (VideoVH) viewHolder;
        vh.video.uninitialize();
    }

    private void loadVideo(String videoSrc, SimpleVideoView videoView) {
        try {
            if (BuildConfig.DEBUG) {
                videoView.setDataSource(DeviceUtil.getMaterialMediaPath(videoSrc).getAbsolutePath());
            } else {
                videoView.setAssetData(videoSrc);
            }
        } catch (IOException e) {
            if (BuildConfig.DEBUG) {
                throw new RuntimeException(e);
            } else {
                Log.e("Video", Log.getStackTraceString(e));
            }
        }
        videoView.setLooping(true);
        videoView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        final SimpleVideoView videoView = (SimpleVideoView) v;
        if (videoView.isPrepared() && videoView.isPlaying()) {
            videoView.pause();
        } else if (videoView.isPrepared()) {
            videoView.start();
        } else {
            videoView.prepareAsync(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                }
            });
        }
    }



    static final class VideoVH extends RecyclerView.ViewHolder {
        @Bind(R.id.figure)      SimpleVideoView video;
        @Bind(R.id.caption)     BaselineGridTextView caption;

        public VideoVH(android.view.View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
