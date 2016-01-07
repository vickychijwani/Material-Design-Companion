package me.vickychijwani.material.ui;

import android.content.Context;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.DimenRes;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.IOException;
import java.lang.ref.WeakReference;

import me.vickychijwani.material.BuildConfig;
import me.vickychijwani.material.R;
import me.vickychijwani.material.network.ScaleToFitWidthHeightTransformation;
import me.vickychijwani.material.spec.entity.Article;
import me.vickychijwani.material.spec.entity.ArticleList;
import me.vickychijwani.material.spec.entity.Chapter;
import me.vickychijwani.material.spec.entity.ChapterIntro;
import me.vickychijwani.material.spec.entity.ChapterIntroWithModules;
import me.vickychijwani.material.spec.entity.ChapterPart;
import me.vickychijwani.material.spec.entity.Figure;
import me.vickychijwani.material.spec.entity.FigureGroup;
import me.vickychijwani.material.spec.entity.Module;
import me.vickychijwani.material.spec.entity.ModuleBody;
import me.vickychijwani.material.spec.entity.ModulePart;
import me.vickychijwani.material.ui.widget.BaselineGridTextView;
import me.vickychijwani.material.ui.widget.SimpleVideoView;
import me.vickychijwani.material.util.DeviceUtil;

public class UiBuilder {

    private static final String TAG = "UiBuilder";

    private final Context mContext;
    private final Picasso mPicasso;

    private WeakReference<SimpleVideoView> mPlayingVideo = null;

    public UiBuilder(Context context) {
        mContext = context;
        if (BuildConfig.DEBUG) {
            mPicasso = new Picasso.Builder(context).listener(new Picasso.Listener() {
                @Override
                public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    Log.e(TAG, Log.getStackTraceString(exception));
                    throw new RuntimeException("Forcing crash, image load failed!");
                }
            }).build();
            mPicasso.setLoggingEnabled(true);
        } else {
            mPicasso = Picasso.with(context);
        }
    }

    public void add(Chapter chapter, ViewGroup container) {
        for (ChapterPart chapterPart : chapter.content) {
            if (chapterPart instanceof Figure) {
                add((Figure) chapterPart, container);
            } else if (chapterPart instanceof ChapterIntro) {
                add((ChapterIntro) chapterPart, container);
            } else if (chapterPart instanceof ChapterIntroWithModules) {
                add((ChapterIntroWithModules) chapterPart, container);
            } else if (chapterPart instanceof ArticleList) {
                for (Article article : ((ArticleList) chapterPart).articles) {
                    add(article, container);
                }
            } else {
                Log.e(TAG, "Skipped ChapterPart of type " + chapterPart.getClass().getSimpleName());
            }
        }
    }

    public void add(final Figure figure, ViewGroup container) {
        View figureView;
        if ("image".equals(figure.mediaType)) {
            figureView = getViewForImage(figure);
        } else if ("video".equals(figure.mediaType)) {
            figureView = getViewForVideo(figure, container);
        } else {
            Log.e(TAG, String.format("Unknown media type '%s'", figure.mediaType));
            return;
        }

        container.addView(figureView);

        // caption
        if (figure.caption != null) {
            TextView captionTextView = getHtmlTextView(figure.caption, R.dimen.figcaption_line_height);
            captionTextView.setTextAppearance(mContext, R.style.TextAppearance_Figcaption);
            container.addView(captionTextView);
        }
    }

    private View getViewForVideo(Figure figure, ViewGroup container) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup videoContainer = (ViewGroup) inflater.inflate(R.layout.video, container, false);
        final SimpleVideoView videoView = (SimpleVideoView) videoContainer.findViewById(R.id.video);
        try {
            if (BuildConfig.DEBUG) {
                videoView.setDataSource(DeviceUtil.getMaterialMediaPath(figure.src).getAbsolutePath());
            } else {
                videoView.setAssetData(figure.src);
            }
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        videoView.setLooping(true);
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                } else if (videoView.isPrepared()) {
                    videoView.start();
                } else {
                    videoView.prepareAsync(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            videoView.start();
                        }
                    });
                }
            }
        });

//            figureLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
//            figureLp.setMargins(0, 0, 0, mContext.getResources()
//                    .getDimensionPixelOffset(R.dimen.figure_media_margin_bottom));
//            videoView.setLayoutParams(figureLp);

        return videoContainer;
    }

    private View getViewForImage(final Figure figure) {
        final ImageView imageView = new ImageView(mContext);
        LinearLayout.LayoutParams figureLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        figureLp.setMargins(0, 0, 0, mContext.getResources()
                .getDimensionPixelOffset(R.dimen.figure_media_margin_bottom));
        imageView.setLayoutParams(figureLp);
        imageView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        imageView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        RequestCreator imageRequest;
                        if (BuildConfig.DEBUG) {
                            imageRequest = mPicasso.load(DeviceUtil.getMaterialMediaPath(figure.src));
                        } else {
                            imageRequest = mPicasso.load("file:///android_asset/" + figure.src);
                        }
                        imageRequest
                                .transform(new ScaleToFitWidthHeightTransformation(imageView, true))
                                .into(imageView);
                    }
                });
        return imageView;
    }

    public void add(ChapterIntro chapterIntro, ViewGroup container) {
        TextView textView = getHtmlTextView(chapterIntro.html, R.dimen.chapter_intro_line_height);
        textView.setTextAppearance(mContext, R.style.TextAppearance_ChapterIntro);
        container.addView(textView);
    }

    private void add(ChapterIntroWithModules chapterIntroWithModules, ViewGroup container) {
        for (Module module : chapterIntroWithModules.modules) {
            add(module, container);
        }
    }

    public void add(Article article, ViewGroup container) {
        TextView articleTitleView = getHtmlTextView(article.title, R.dimen.article_title_line_height);
        articleTitleView.setTextAppearance(mContext, R.style.TextAppearance_ArticleTitle);
        ViewGroup.MarginLayoutParams lp = (LinearLayout.LayoutParams) articleTitleView.getLayoutParams();
        int verticalTitleMargin = mContext.getResources()
                .getDimensionPixelOffset(R.dimen.article_title_margin_top_bottom);
        lp.setMargins(0, verticalTitleMargin, 0, verticalTitleMargin);
        container.addView(articleTitleView);
        for (Module module : article.modules) {
            add(module, container);
        }
    }

    private void add(Module module, ViewGroup container) {
        if (module.title != null) {
            container.addView(getHtmlTextView(module.title, R.dimen.module_title_line_height));
        }
        for (ModulePart modulePart : module.content) {
            switch (modulePart.type) {
                case FigureGroup.TYPE:
                    add((FigureGroup) modulePart, container);
                    break;
                case ModuleBody.TYPE:
                    add((ModuleBody) modulePart, container);
                    break;
                default:
                    Log.e(TAG, "Skipped ModulePart of type " + modulePart.type);
            }
        }
    }

    private void add(FigureGroup figureGroup, ViewGroup container) {
        for (Figure figure : figureGroup.figures) {
            add(figure, container);
        }
    }

    private void add(ModuleBody moduleBody, ViewGroup container) {
        TextView moduleBodyView = getHtmlTextView(moduleBody.html, R.dimen.module_body_line_height);
        moduleBodyView.setTextAppearance(mContext, R.style.TextAppearance_ModuleBody);
        container.addView(moduleBodyView);
    }


    // private methods
    private TextView getHtmlTextView(String html, @DimenRes int lineHeightHintRes) {
        BaselineGridTextView textView = new BaselineGridTextView(mContext);
        Resources resources = mContext.getResources();
        int lineHeightHint = resources.getDimensionPixelSize(lineHeightHintRes);
        textView.setLineHeightHint(lineHeightHint);

        int textMargin = resources.getDimensionPixelOffset(R.dimen.text_margin);
        textView.setPadding(textMargin, 0, textMargin, 0);

        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setText(Html.fromHtml(html));
        return textView;
    }

}
