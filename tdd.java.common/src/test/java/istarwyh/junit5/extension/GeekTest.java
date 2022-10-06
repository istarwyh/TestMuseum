package istarwyh.junit5.extension;

import istarwyh.classloader.model.CannotInitialDueToLoadingClassError;
import istarwyh.classloader.model.CannotInitialDueToPrivateConstructor;
import istarwyh.classloader.model.CannotInitialFromConstructor;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static istarwyh.util.UnsafeUntil.unsafe;
import static java.lang.ClassLoader.getSystemClassLoader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(InitialExtension.class)
@ExtendWith(MockitoExtension.class)
public class GeekTest {

    @Test
    @SneakyThrows
    void should_get_instance_by_skipping_constructor(){
        CannotInitialFromConstructor instance =
                (CannotInitialFromConstructor) unsafe().allocateInstance(CannotInitialFromConstructor.class);
        instance.setDesc("skipping_constructor");
        assertEquals("skipping_constructor",instance.desc);
    }

    @Test
    @SneakyThrows
    void should_initial_by_add_no_args_constructor(){
        // The following statement cannot show what happen using no args constructor what is absent originally,
        // because the compiler will prevent this.
        var cannotInitialDueToPrivateConstructor =
                spy(unsafe().allocateInstance(CannotInitialDueToPrivateConstructor.class));
        doReturn("I am mock").when(cannotInitialDueToPrivateConstructor).toString();
        assertEquals("I am mock",cannotInitialDueToPrivateConstructor.toString());
    }

    @Test
    void should_initial_CannotInitialDueToLoadingClassError_by_my_classloader(){
        var spy = spy(new CannotInitialDueToLoadingClassError());
        when(spy.toString()).thenReturn("I am mock");

        assertNull(CannotInitialDueToLoadingClassError.id);
        assertEquals("I am mock",spy.toString());
        assertEquals("app",getSystemClassLoader().getName());
        assertEquals("app",Mockito.class.getClassLoader().getName());
        assertEquals("my",CannotInitialDueToLoadingClassError.class.getClassLoader().getName());
    }


}
