package io.github.istarwyh.junit5.extension;

import static java.lang.ClassLoader.getSystemClassLoader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import io.github.istarwyh.junit5.extension.model.MockitoAfterMyClassLoader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class Junit5MockitoExtensionAfterMyClassLoaderTest {

    @Mock
    MockitoAfterMyClassLoader instance;

    @InjectMocks
    MockitoAfterMyClassLoader mockitoAfterMyClassLoader;
    private String iWasMocked;

    @BeforeEach
    void setUp() {
        iWasMocked = "I was mocked";
    }

    @Test
    void should_pass_when_mock_toString(){
        when(instance.toString()).thenReturn(iWasMocked);

        assertEquals(iWasMocked,mockitoAfterMyClassLoader.instance.toString());
        assertEquals(getSystemClassLoader(), MockitoAfterMyClassLoader.class.getClassLoader());
        assertEquals(getSystemClassLoader(), Mockito.class.getClassLoader());
    }

    @Test
    void should_pass_when_mock_boolean_method(){
        when(instance.mockEquals(any(Object.class))).thenReturn(true);

        assertTrue(mockitoAfterMyClassLoader.instance.mockEquals(""));
        assertEquals(getSystemClassLoader(), MockitoAfterMyClassLoader.class.getClassLoader());
        assertEquals(getSystemClassLoader(), Mockito.class.getClassLoader());
    }
}
