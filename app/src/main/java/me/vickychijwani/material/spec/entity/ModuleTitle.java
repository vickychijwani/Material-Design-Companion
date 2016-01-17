package me.vickychijwani.material.spec.entity;

public class ModuleTitle implements PlainTextEntity {

    public final String text;

    public ModuleTitle(String text) {
        this.text = text;
    }

    @Override
    public String getText() {
        return text;
    }

}
