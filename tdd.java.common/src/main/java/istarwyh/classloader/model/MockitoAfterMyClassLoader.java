package istarwyh.classloader.model;

public class MockitoAfterMyClassLoader {

    public MockitoAfterMyClassLoader instance;

    public boolean mockEquals(Object o) {
        return this == o;
    }

}
