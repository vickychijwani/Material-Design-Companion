package me.vickychijwani.material.task;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

public class VideoMetadataRetriever {

    private static final VideoMetadataRetriever sInstance = new VideoMetadataRetriever();

    private VideoMetadataRetriever() {}

    public static VideoMetadataRetriever getInstance() {
        return sInstance;
    }

    public void create(Context context, final String videoPath,
                       @NonNull final GetVideoMetadataTask.DoneListener listener) {
        GetVideoMetadataTask task = new GetVideoMetadataTask(context, listener);
        task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, videoPath);
    }

}
