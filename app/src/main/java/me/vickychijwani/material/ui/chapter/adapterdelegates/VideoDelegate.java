package me.vickychijwani.material.ui.chapter.adapterdelegates;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.vickychijwani.material.BuildConfig;
import me.vickychijwani.material.R;
import me.vickychijwani.material.spec.entity.Video;
import me.vickychijwani.material.task.GetVideoMetadataTask;
import me.vickychijwani.material.task.VideoMetadataRetriever;
import me.vickychijwani.material.ui.chapter.ChapterAdapterDelegate;
import me.vickychijwani.material.ui.widget.BaselineGridTextView;
import me.vickychijwani.material.ui.widget.SimpleVideoView;
import me.vickychijwani.material.util.DeviceUtil;

public class VideoDelegate extends ChapterAdapterDelegate implements View.OnClickListener {

    private final LayoutInflater mInflater;
    private WeakReference<SimpleVideoView> mPlayingVideo = null;
    private final VideoMetadataRetriever mMetadataRetriever = VideoMetadataRetriever.getInstance();
    private final int SCREEN_WIDTH;

    public VideoDelegate(@NonNull Context context) {
        super(ViewType.VIDEO);
        mInflater = LayoutInflater.from(context);
        SCREEN_WIDTH = DeviceUtil.getScreenWidth(context);
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
        vh.video.setVisibility(View.GONE);
        vh.overlay.setVisibility(View.VISIBLE);
        vh.playBtn.setVisibility(View.VISIBLE);
        vh.videoContainer.setOnClickListener(this);
        final WeakReference<View> containerRef = new WeakReference<View>(vh.videoContainer);
        mMetadataRetriever.create(vh.video.getContext(), video.src, new GetVideoMetadataTask.DoneListener() {
            @Override
            public void done(GetVideoMetadataTask.Result result) {
                float aspectRatio = result.aspectRatio;
                if (containerRef.get() == null || aspectRatio < 0.1f) {
                    return;
                }
                ViewGroup.LayoutParams lp = containerRef.get().getLayoutParams();
                lp.width = SCREEN_WIDTH;
                lp.height = (int) (SCREEN_WIDTH / aspectRatio);
                containerRef.get().setLayoutParams(lp);
            }
        });
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
    }

    @Override
    public void onClick(View v) {
        ViewGroup videoContainer = (ViewGroup) v;
        final SimpleVideoView videoView = (SimpleVideoView) videoContainer.findViewById(R.id.video);
        View overlayView = videoContainer.findViewById(R.id.video_overlay);
        View playBtnView = videoContainer.findViewById(R.id.video_play);
        if (videoView.isPrepared() && videoView.isPlaying()) {
            videoView.pause();
            mPlayingVideo = null;
        } else if (videoView.isPrepared()) {
            pausePlayingVideo();
            videoView.setVisibility(View.VISIBLE);
            overlayView.setVisibility(View.GONE);
            playBtnView.setVisibility(View.GONE);
            videoView.start();
            mPlayingVideo = new WeakReference<>(videoView);
        } else {
            pausePlayingVideo();
            videoView.setVisibility(View.VISIBLE);
            overlayView.setVisibility(View.GONE);
            playBtnView.setVisibility(View.GONE);
            videoView.prepareAsync(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                }
            });
            mPlayingVideo = new WeakReference<>(videoView);
        }
    }

    private void pausePlayingVideo() {
        if (mPlayingVideo != null && mPlayingVideo.get() != null
                && mPlayingVideo.get().isPrepared() && mPlayingVideo.get().isPlaying()) {
            mPlayingVideo.get().pause();
        }
    }



    static final class VideoVH extends RecyclerView.ViewHolder {
        @Bind(R.id.video_container)     FrameLayout videoContainer;
        @Bind(R.id.video)               SimpleVideoView video;
        @Bind(R.id.video_overlay)       View overlay;
        @Bind(R.id.video_play)          View playBtn;
        @Bind(R.id.caption)             BaselineGridTextView caption;

        public VideoVH(android.view.View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
