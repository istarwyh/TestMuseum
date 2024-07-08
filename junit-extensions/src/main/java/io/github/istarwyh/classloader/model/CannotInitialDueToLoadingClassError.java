package io.github.istarwyh.classloader.model;

/**
 * @author xiaohui
 */
public class CannotInitialDueToLoadingClassError {
    public static final Integer id;

    static {
        id = 1 / 0;
    }
}
