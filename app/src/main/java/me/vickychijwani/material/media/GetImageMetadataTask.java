package me.vickychijwani.material.media;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.lang.ref.WeakReference;

import me.vickychijwani.material.BuildConfig;
import me.vickychijwani.material.util.DeviceUtil;

public class GetImageMetadataTask extends AsyncTask<String, Void, GetImageMetadataTask.Result> {

    private final WeakReference<Context> mContext;
    private final DoneListener mDoneListener;

    public GetImageMetadataTask(Context context, DoneListener doneListener) {
        mContext = new WeakReference<>(context);
        mDoneListener = doneListener;
    }

    @Override
    protected Result doInBackground(String... params) {
        String imageSrc = params[0];
        if (mContext.get() == null) {
            return new Result(imageSrc, 0);
        }
        Context context = mContext.get();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        if (BuildConfig.DEBUG) {
            String filepath = DeviceUtil.getMaterialMediaPath(imageSrc).getAbsolutePath();
            BitmapFactory.decodeFile(filepath, options);
        } else {
            String filepath = "file:///android_asset/" + imageSrc;
            try {
                AssetFileDescriptor afd = context.getAssets().openFd(filepath);
                BitmapFactory.decodeFileDescriptor(afd.getFileDescriptor(), null, options);
            } catch (IOException e) {
                return new Result(imageSrc, 0);
            }
        }
        float aspectRatio = (1.0f * options.outWidth) / options.outHeight;
        return new Result(imageSrc, aspectRatio);
    }

    @Override
    protected void onPostExecute(Result result) {
        mDoneListener.done(result);
    }

    public static class Result {
        public final String imageSrc;
        public final float aspectRatio;

        public Result(String imageSrc, float aspectRatio) {
            this.imageSrc = imageSrc;
            this.aspectRatio = aspectRatio;
        }
    }

    public interface DoneListener {
        void done(Result result);
    }

}
