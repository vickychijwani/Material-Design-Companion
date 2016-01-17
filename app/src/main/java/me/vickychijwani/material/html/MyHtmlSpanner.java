package me.vickychijwani.material.html;

import android.text.Html;
import android.text.Spanned;

public class MyHtmlSpanner {

    public static Spanned fromHtml(String source) {
        return Html.fromHtml(source, null, new MyHtmlTagHandler());
    }

}
