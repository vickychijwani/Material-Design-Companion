package me.vickychijwani.material.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;

public class DeviceUtil {

    public static int dpToPx(float dp, Resources resources) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                resources.getDisplayMetrics());
    }

    public static int getScreenWidth(@NonNull Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
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
