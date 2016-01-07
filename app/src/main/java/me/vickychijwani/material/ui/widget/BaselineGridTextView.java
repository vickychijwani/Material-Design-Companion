package me.vickychijwani.material.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

// courtesy https://github.com/nickbutcher/plaid/blob/61d59644d5ae9e373f93cef10e0438c50e2eea6d/app/src/main/java/io/plaidapp/ui/widget/BaselineGridTextView.java
public class BaselineGridTextView extends TextView {

    private float lineHeightMultiplierHint = 1f;
    private float lineHeightHint = 0f;
    private int topPaddingHint = 0;

    public BaselineGridTextView(Context context) {
        this(context, null);
    }

    public BaselineGridTextView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public BaselineGridTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BaselineGridTextView(Context context, AttributeSet attrs,
                                int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
//
//        final TypedArray a = context.obtainStyledAttributes(
//                attrs, R.styleable.BaselineGridTextView, defStyleAttr, defStyleRes);
//
//        lineHeightMultiplierHint =
//                a.getFloat(R.styleable.BaselineGridTextView_lineHeightMultiplierHint, 1f);
//        lineHeightHint =
//                a.getDimensionPixelSize(R.styleable.BaselineGridTextView_lineHeightHint, 0);
//        topPaddingHint =
//                a.getDimensionPixelSize(R.styleable.BaselineGridTextView_topPaddingHint, 0);
//
//        a.recycle();

        setIncludeFontPadding(false);
        setElegantTextHeight(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        recomputeLineHeight();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void recomputeLineHeight() {
        float fourDip = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4,
                getResources().getDisplayMetrics());

        // Ensure that the first line's baselines sits on 4dp grid by setting the top padding
        Paint.FontMetricsInt fm = getPaint().getFontMetricsInt();
        int gridAlignedTopPadding = (int) (fourDip * (float)
                Math.ceil((topPaddingHint + Math.abs(fm.ascent)) / fourDip)
                - Math.ceil(Math.abs(fm.ascent)));
        setPadding(getPaddingLeft(), gridAlignedTopPadding, getPaddingRight(), getPaddingBottom());

        // Ensures line height is a multiple of 4dp
        int fontHeight = Math.abs(fm.ascent - fm.descent) + fm.leading;
        float desiredLineHeight = (lineHeightHint > 0)
                ? lineHeightHint
                : lineHeightMultiplierHint * fontHeight;

        int baselineAlignedLineHeight =
                (int) (fourDip * (float) Math.ceil(desiredLineHeight / fourDip));
        setLineSpacing(baselineAlignedLineHeight - fontHeight, 1f);
    }

    public float getLineHeightMultiplierHint() {
        return lineHeightMultiplierHint;
    }

    public void setLineHeightMultiplierHint(float lineHeightMultiplierHint) {
        this.lineHeightMultiplierHint = lineHeightMultiplierHint;
        recomputeLineHeight();
    }

    public float getLineHeightHint() {
        return lineHeightHint;
    }

    public void setLineHeightHint(float lineHeightHint) {
        this.lineHeightHint = lineHeightHint;
        recomputeLineHeight();
    }
}
