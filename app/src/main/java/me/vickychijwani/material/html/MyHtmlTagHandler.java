package me.vickychijwani.material.html;

import android.text.Editable;
import android.text.Html;

import org.xml.sax.XMLReader;

class MyHtmlTagHandler implements Html.TagHandler {
    boolean first = true;
    String parent = null;
    int index = 1;

    @Override
    public void handleTag(boolean opening, String tag, Editable output, XMLReader xmlReader) {
        if (tag.equals("ul")) {
            parent = "ul";
        } else if (tag.equals("ol")) {
            parent = "ol";
            index = 1;
        } else if (tag.equals("li")) {
            handleLi(output);
        }
    }

    private void handleLi(Editable output) {
        if (parent.equals("ul")) {
            if (first) {
                output.append("\n\t• ");
                first = false;
            } else {
                first = true;
            }
        } else {
            if (first) {
                output.append("\n\t").append(String.valueOf(index)).append(". ");
                first = false;
                index++;
            } else {
                first = true;
            }
        }
    }
}
