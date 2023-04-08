package istarwyh.util;

import istarwyh.page_module_loader.PageModuleConstructor;
import istarwyh.page_module_loader.constructor.PointConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationUtilTest {

    @Test
    void testGetClassesWithItsClass() {
        List<Class<?>> classList = FindClassesUtil.getClasses("istarwyh.util");
        assertTrue(classList.contains(FindClassesUtil.class));
    }

    @Test
    void getAllClassesImplementingInterface() {
        List<Class<? extends PageModuleConstructor>> interfaceClasses =
                FindClassesUtil.getAllClassesImplementingInterface(PageModuleConstructor.class);

        assertTrue(interfaceClasses.contains(PointConstructor.class));
    }
}