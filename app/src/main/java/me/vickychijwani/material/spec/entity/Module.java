package me.vickychijwani.material.spec.entity;

public class Module {

    public final ModuleTitle title;
    public final ModulePart[] content;

    public Module(String title, ModulePart[] content) {
        if (title != null) {
            this.title = new ModuleTitle(title);
        } else {
            this.title = null;
        }
        this.content = content;
    }

}
