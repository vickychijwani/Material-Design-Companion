package me.vickychijwani.material.media;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

public class MyMediaMetadataRetriever {

    private static final MyMediaMetadataRetriever sInstance = new MyMediaMetadataRetriever();

    private MyMediaMetadataRetriever() {}

    public static MyMediaMetadataRetriever getInstance() {
        return sInstance;
    }

    public void getImageMetadata(Context context, final String imageSrc,
                                 @NonNull final GetImageMetadataTask.DoneListener listener) {
        GetImageMetadataTask task = new GetImageMetadataTask(context, listener);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, imageSrc);
    }

    public void getVideoMetadata(Context context, final String videoPath,
                                 @NonNull final GetVideoMetadataTask.DoneListener listener) {
        GetVideoMetadataTask task = new GetVideoMetadataTask(context, listener);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, videoPath);
    }

}
