package me.vickychijwani.material.network;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;

// courtesy https://gist.github.com/jpardogo/7146143
public class ScaleToFitWidthHeightTransformation implements Transformation {

    private int mSize;
    private boolean mFitWidth;
    private WeakReference<ImageView> mImageView;

    public ScaleToFitWidthHeightTransformation(ImageView imageView, boolean fitWidth) {
        this.mSize = fitWidth ? imageView.getWidth() : imageView.getHeight();
        this.mFitWidth = fitWidth;
        this.mImageView = new WeakReference<>(imageView);
    }

    @Override
    public Bitmap transform(Bitmap source) {
        float scale;
        int newSize;
        Bitmap scaleBitmap;
        ViewGroup.LayoutParams lp = null;
        if (mImageView.get() != null) {
            lp = mImageView.get().getLayoutParams();
        }
        if (mFitWidth) {
            scale = (float) mSize / source.getWidth();
            newSize = Math.round(source.getHeight() * scale);
            scaleBitmap = Bitmap.createScaledBitmap(source, mSize, newSize, true);
            if (lp != null) {
                lp.height = newSize;
                Log.d("Picasso", "Resizing ImageView height to " + newSize + "px");
            }
        } else {
            scale = (float) mSize / source.getHeight();
            newSize = Math.round(source.getWidth() * scale);
            scaleBitmap = Bitmap.createScaledBitmap(source, newSize, mSize, true);
            if (lp != null) {
                lp.width = newSize;
                Log.d("Picasso", "Resizing ImageView width to " + newSize + "px");
            }
        }
        if (mImageView.get() != null) {
            final ViewGroup.LayoutParams finalLp = lp;
            mImageView.get().post(new Runnable() {
                @Override
                public void run() {
                    mImageView.get().setLayoutParams(finalLp);
                }
            });
        }

        if (scaleBitmap != source) {
            source.recycle();
        }

        return scaleBitmap;
    }

    @Override
    public String key() {
        return "scaleRespectRatio" + mSize + mFitWidth;
    }

}
