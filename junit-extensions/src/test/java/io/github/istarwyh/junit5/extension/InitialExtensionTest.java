package io.github.istarwyh.junit5.extension;

import static io.github.istarwyh.util.UnsafeUtils.unsafe;
import static java.lang.ClassLoader.getSystemClassLoader;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.spy;

import io.github.istarwyh.classloader.model.CannotInitialDueToLoadingClassError;
import io.github.istarwyh.classloader.model.CannotInitialFromConstructor;
import io.github.istarwyh.classloader.model.CannotInitialLackMatchedConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith({InitialExtension.class, MockitoExtension.class})
public class InitialExtensionTest {

  @Test
  @SneakyThrows
  void should_get_instance_by_skipping_constructor() {
    CannotInitialFromConstructor instance =
        (CannotInitialFromConstructor)
            unsafe().allocateInstance(CannotInitialFromConstructor.class);
    instance.setDesc("skipping_constructor");
    assertEquals("skipping_constructor", instance.desc);
  }

  @Test
  @SneakyThrows
  void should_initial_by_add_no_args_constructor() {
    // The following statement cannot show what happen using no args constructor what is absent
    // originally,
    // because the compiler will prevent this.
    CannotInitialLackMatchedConstructor cannotInitialDueToPrivateConstructor =
        (CannotInitialLackMatchedConstructor)
            spy(unsafe().allocateInstance(CannotInitialLackMatchedConstructor.class));
    doReturn("I am mock").when(cannotInitialDueToPrivateConstructor).toString();
    assertEquals("I am mock", cannotInitialDueToPrivateConstructor.toString());
  }

  @Test
  void should_initial_CannotInitialDueToLoadingClassError_by_my_classloader() {
    val spy = spy(new CannotInitialDueToLoadingClassError());
    when(spy.toString()).thenReturn("I am mock");

    assertNull(CannotInitialDueToLoadingClassError.id);
    assertEquals("I am mock", spy.toString());
    assertEquals("AppClassLoader", getSystemClassLoader().getClass().getSimpleName());
    assertEquals("AppClassLoader", Mockito.class.getClassLoader().getClass().getSimpleName());
    assertEquals(
        "MyClassLoader",
        CannotInitialDueToLoadingClassError.class.getClassLoader().getClass().getSimpleName());
  }
}
