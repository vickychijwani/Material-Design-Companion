package me.vickychijwani.material.spec.entity;

public class ModuleBody extends ModulePart {

    public static final String TYPE = "module-body";

    public final String html;

    public ModuleBody(String html) {
        super(TYPE);
        this.html = html;
    }

}
