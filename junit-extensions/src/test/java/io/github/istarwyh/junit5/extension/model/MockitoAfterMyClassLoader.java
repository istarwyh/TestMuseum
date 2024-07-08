package io.github.istarwyh.junit5.extension.model;

public class MockitoAfterMyClassLoader {

    public MockitoAfterMyClassLoader instance;

    public boolean mockEquals(Object o) {
        return this == o;
    }

}
