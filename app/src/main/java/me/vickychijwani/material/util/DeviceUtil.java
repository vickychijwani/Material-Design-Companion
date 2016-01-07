package me.vickychijwani.material.util;

import android.content.res.Resources;
import android.os.Environment;
import android.util.TypedValue;

import java.io.File;

public class DeviceUtil {

    public static int dpToPx(float dp, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                resources.getDisplayMetrics());
    }

    public static File getMaterialMediaPath(String materialMediaPath) {
        File baseDir = new File(Environment.getExternalStorageDirectory(),
                "material");
        String[] imagePathParts = materialMediaPath.split("/");
        File mediaFile = new File(baseDir, imagePathParts[4] + File.separator +
                imagePathParts[5]);
        if (! mediaFile.exists()) {
            throw new RuntimeException("File " + mediaFile.getAbsolutePath() +
                    " not found");
        }
        return mediaFile;
    }

}
