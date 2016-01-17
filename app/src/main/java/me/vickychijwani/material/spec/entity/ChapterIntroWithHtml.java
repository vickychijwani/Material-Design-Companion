package me.vickychijwani.material.spec.entity;

public class ChapterIntroWithHtml implements ChapterIntro, HtmlTextEntity {

    public final String html;

    public ChapterIntroWithHtml(String html) {
        this.html = html;
    }

    @Override
    public String getHtml() {
        return html;
    }

}
