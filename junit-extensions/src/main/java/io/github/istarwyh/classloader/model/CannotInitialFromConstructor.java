package io.github.istarwyh.classloader.model;

import lombok.Setter;

/**
 * @author xiaohui
 */
@Setter
public class CannotInitialFromConstructor {

    public String desc;

    private CannotInitialFromConstructor(String desc) throws IllegalAccessException {
        this.desc = desc;
        throw new IllegalAccessException(String.valueOf(desc));
    }

}
