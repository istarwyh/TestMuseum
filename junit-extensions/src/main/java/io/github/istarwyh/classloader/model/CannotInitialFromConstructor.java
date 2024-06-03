package io.github.istarwyh.classloader.model;

public class CannotInitialFromConstructor {

    public String desc;

    private CannotInitialFromConstructor(String desc) throws IllegalAccessException {
        this.desc = desc;
        throw new IllegalAccessException(String.valueOf(desc));
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
