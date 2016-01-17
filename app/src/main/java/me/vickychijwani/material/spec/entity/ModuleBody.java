package me.vickychijwani.material.spec.entity;

public class ModuleBody implements ModulePart, HtmlTextEntity {

    public final String html;

    public ModuleBody(String html) {
        this.html = html;
    }

    @Override
    public String getHtml() {
        return html;
    }

}
