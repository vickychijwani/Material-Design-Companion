package me.vickychijwani.material.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;

import me.vickychijwani.material.BuildConfig;
import me.vickychijwani.material.util.DeviceUtil;

public class GetVideoMetadataTask extends AsyncTask<String, Void, GetVideoMetadataTask.Result> {

    private final WeakReference<Context> mContext;
    private final DoneListener mDoneListener;

    public GetVideoMetadataTask(Context context, DoneListener doneListener) {
        mContext = new WeakReference<>(context);
        mDoneListener = doneListener;
    }

    @Override
    protected Result doInBackground(String... params) {
        String videoSrc = params[0];
        if (mContext.get() == null) {
            return new Result(videoSrc, 0);
        }
        Context context = mContext.get();
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        if (BuildConfig.DEBUG) {
            String filepath = DeviceUtil.getMaterialMediaPath(videoSrc).getAbsolutePath();
            retriever.setDataSource(filepath);
        } else {
            String filepath = "file:///android_asset/" + videoSrc;
            try {
                AssetFileDescriptor afd = context.getAssets().openFd(filepath);
                retriever.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
                        afd.getLength());
            } catch (IOException e) {
                return new Result(videoSrc, 0);
            }
        }
        String wStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String hStr = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        if (wStr != null && hStr != null) {
            return new Result(videoSrc, Float.parseFloat(wStr) / Float.parseFloat(hStr));
        }
        return new Result(videoSrc, 0);
    }

    @Override
    protected void onPostExecute(Result result) {
        mDoneListener.done(result);
    }

    public static class Result {
        public final String videoSrc;
        public final float aspectRatio;

        public Result(String videoSrc, float aspectRatio) {
            this.videoSrc = videoSrc;
            this.aspectRatio = aspectRatio;
        }
    }

    public interface DoneListener {
        void done(Result result);
    }

}
