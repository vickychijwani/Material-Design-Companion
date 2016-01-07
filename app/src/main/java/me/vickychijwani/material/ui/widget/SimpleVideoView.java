package me.vickychijwani.material.ui.widget;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;

public class SimpleVideoView extends ScalableVideoView {

    protected boolean mIsPrepard = false;

    public SimpleVideoView(Context context) {
        super(context);
    }

    public SimpleVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleVideoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isPrepared() {
        return mIsPrepard;
    }

    @Override
    public void prepare(@Nullable final MediaPlayer.OnPreparedListener listener)
            throws IOException, IllegalStateException {
        super.prepare(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsPrepard = true;
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
                mIsPrepard = true;
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
                mIsPrepard = true;
            }
        });
        super.prepare();
    }

    @Override
    public void prepareAsync() throws IllegalStateException {
        super.prepareAsync(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mIsPrepard = true;
            }
        });
    }

    @Override
    public void stop() {
        super.stop();
        mIsPrepard = false;
    }

//    public void allocate() {
//        if (mMediaPlayer != null) {
//            mMediaPlayer.reset();
//            mMediaPlayer.release();
//        }
//        mMediaPlayer = new MediaPlayer();
//        mMediaPlayer.setOnVideoSizeChangedListener(this);
//        setSurfaceTextureListener(this);
//    }

//    @Override
//    public void release() {
//        mMediaPlayer.stop();
//        mMediaPlayer.reset();
//        mMediaPlayer.release();
//        mMediaPlayer = null;
//    }

}
