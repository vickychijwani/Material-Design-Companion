package me.vickychijwani.material.util;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.squareup.picasso.Picasso;

import me.vickychijwani.material.BuildConfig;

public class ImageUtil {

    private static Picasso sPicasso = null;

    // NOTE a singleton Picasso is required to prevent OOM errors because multiple Picasso instances
    // do not share the memory cache among themselves, causing allocations to fail after a while
    public static Picasso getPicasso(@NonNull Context context) {
        if (sPicasso == null) {
            if (BuildConfig.DEBUG) {
                sPicasso = new Picasso.Builder(context).listener(new Picasso.Listener() {
                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                        Log.e("Picasso", Log.getStackTraceString(exception));
                        throw new RuntimeException("Forcing crash, image load failed!");
                    }
                }).build();
                sPicasso.setLoggingEnabled(true);
            } else {
                sPicasso = Picasso.with(context);
            }
        }
        return sPicasso;
    }

}
