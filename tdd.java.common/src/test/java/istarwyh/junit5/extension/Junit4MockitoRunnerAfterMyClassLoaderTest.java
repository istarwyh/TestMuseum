package istarwyh.junit5.extension;


import istarwyh.junit5.extension.model.MockitoAfterMyClassLoader;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;

import static java.lang.ClassLoader.getSystemClassLoader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class Junit4MockitoRunnerAfterMyClassLoaderTest {

    @Mock
    MockitoAfterMyClassLoader instance;

    @InjectMocks
    MockitoAfterMyClassLoader mockitoAfterMyClassLoader;
    private String iWasMocked;

    @Before
    public void setUp() {
        iWasMocked = "I was mocked";
    }

    @Test
    @SneakyThrows
    public void should_pass_when_mock_toString(){
        MockitoAfterMyClassLoader instance = Mockito.mock(MockitoAfterMyClassLoader.class);
        MockitoAfterMyClassLoader mockitoAfterMyClassLoader = new MockitoAfterMyClassLoader();
        Field instanceMethod = MockitoAfterMyClassLoader.class.getDeclaredField("instance");
        instanceMethod.setAccessible(true);
        instanceMethod.set(mockitoAfterMyClassLoader,instance);

        when(instance.toString()).thenReturn(iWasMocked);

        assertEquals(iWasMocked,mockitoAfterMyClassLoader.instance.toString());
        assertEquals(getSystemClassLoader(), MockitoAfterMyClassLoader.class.getClassLoader());
        assertEquals(getSystemClassLoader(),instance.getClass().getClassLoader());
        assertEquals(getSystemClassLoader(), Mockito.class.getClassLoader());
    }

    @Test
    public void should_pass_when_mock_boolean_method(){
        when(instance.mockEquals(any(Object.class))).thenReturn(true);

        assertTrue(mockitoAfterMyClassLoader.instance.mockEquals(""));
        assertEquals(getSystemClassLoader(), MockitoAfterMyClassLoader.class.getClassLoader());
        assertEquals(getSystemClassLoader(),instance.getClass().getClassLoader());
        assertEquals(getSystemClassLoader(), Mockito.class.getClassLoader());
    }
}
