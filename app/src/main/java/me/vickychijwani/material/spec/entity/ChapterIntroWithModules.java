package me.vickychijwani.material.spec.entity;

public class ChapterIntroWithModules extends ChapterPart {

    public static final String TYPE = "intro";

    public final Module[] modules;

    public ChapterIntroWithModules(Module[] modules) {
        super(TYPE);
        this.modules = modules;
    }

}
