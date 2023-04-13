package istarwyh.util;

import istarwyh.page_module_loader.PageModuleConstructor;
import istarwyh.page_module_loader.constructor.PointConstructor;
import istarwyh.util.annotation.Scale;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AnnotationUtilTest {

    @Test
    void testGetClassesWithItsClass() {
        List<Class<?>> classList = ReflectionUtil.getClasses("istarwyh.util");
        assertTrue(classList.contains(AnnotationUtil.class));
    }

    @Test
    void getAllClassesImplementingInterface() {
        List<Class<? extends PageModuleConstructor>> interfaceClasses =
                ReflectionUtil.getAllClassesImplementingInterface(PageModuleConstructor.class);

        assertTrue(interfaceClasses.contains(PointConstructor.class));
    }

    @ParameterizedTest
    @CsvSource({
            "0.225,0.22",
            "0.226,0.23",
            "10,10.00"
    })
    void forScaleAnnotation(double value, double output) {
        Any any = new Any(BigDecimal.valueOf(value));
        AnnotationUtil.forScaleAnnotation(any);
        assertEquals(BigDecimal.valueOf(output).setScale(2, RoundingMode.HALF_EVEN), any.amount);
    }

    @AllArgsConstructor
    private static class Any {

        @Scale(value = 2)
        BigDecimal amount;
    }
}