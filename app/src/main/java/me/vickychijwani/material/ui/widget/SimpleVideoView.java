package me.vickychijwani.material.ui.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.Map;

public class SimpleVideoView extends ScalableVideoView {

    private boolean mIsPrepared = false;

    public SimpleVideoView(Context context) {
        super(context);
    }

    public SimpleVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onDetachedFromWindow() {
        // NO-OP
    }

    public void uninitialize() {
        if (mMediaPlayer == null) {
            return;
        }

        if (isPlaying()) {
            stop();
        }
        release();
        mMediaPlayer = null;
    }

    public boolean isPrepared() {
        return mIsPrepared;
    }

    @Override
    public void setDataSource(@NonNull String path) throws IOException {
        mIsPrepared = false;
        super.setDataSource(path);
    }

    @Override
    public void setDataSource(@NonNull Context context, @NonNull Uri uri, @Nullable Map<String, String> headers) throws IOException {
        mIsPrepared = false;
        super.setDataSource(context, uri, headers);
    }

    @Override
    public void setDataSource(@NonNull Context context, @NonNull Uri uri) throws IOException {
        mIsPrepared = false;
        super.setDataSource(context, uri);
    }

    @Override
    public void setDataSource(@NonNull FileDescriptor fd, long offset, long length) throws IOException {
        mIsPrepared = false;
        super.setDataSource(fd, offset, length);
    }

    @Override
    public void setDataSource(@NonNull FileDescriptor fd) throws IOException {
        mIsPrepared = false;
        super.setDataSource(fd);
    }

    @Override
    public void prepare(@Nullable final MediaPlayer.OnPreparedListener listener)
            throws IOException, IllegalStateException {
        super.prepare(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsPrepared = true;
                if (listener != null) {
                    listener.onPrepared(mp);
                }
            }
        });
    }

    @Override
    public void prepareAsync(@Nullable final MediaPlayer.OnPreparedListener listener)
            throws IllegalStateException {
        super.prepareAsync(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsPrepared = true;
                if (listener != null) {
                    listener.onPrepared(mp);
                }
            }
        });
    }

    @Override
    public void prepare() throws IOException, IllegalStateException {
        mMediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsPrepared = true;
            }
        });
        super.prepare();
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        super.prepareAsync(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsPrepared = true;
            }
        });
    }

    @Override
    public void stop() {
        super.stop();
        mIsPrepared = false;
    }

}
