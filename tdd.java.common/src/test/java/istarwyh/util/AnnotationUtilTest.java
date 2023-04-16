package istarwyh.util;

import istarwyh.page_module_loader.PageModuleConstructor;
import istarwyh.page_module_loader.constructor.PointConstructor;
import istarwyh.util.annotation.Scale;
import jakarta.validation.constraints.AssertTrue;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static istarwyh.util.AnnotationUtil.scaleFields;
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
    void scaleFields_happy_case_2(double value, double output) {
        Any any = new Any(BigDecimal.valueOf(value));
        Any.decimalList.add(BigDecimal.valueOf(value));

        scaleFields(any);

        assertEquals(BigDecimal.valueOf(output).setScale(2, RoundingMode.HALF_EVEN), any.amount);
        assertEquals(
                Any.decimalList.stream().map(it -> it.setScale(2, RoundingMode.HALF_EVEN)).collect(Collectors.toList()),
                Any.decimalList);
    }

    @Test void should_throw_exception_with_not_supported_type() {
        UnsupportedFieldType unsupportedFieldType = new UnsupportedFieldType(1.23);
        UnsupportedOperationException exception =
                assertThrows(UnsupportedOperationException.class, () -> scaleFields(unsupportedFieldType));
        assertTrue(exception.getMessage().contains("not supported"));
    }

    @AllArgsConstructor
    private static class Any {

        @Scale(value = 2)
        BigDecimal amount;

        @Scale(value = 2)
        static List<BigDecimal> decimalList = new ArrayList<>();
    }

    @AllArgsConstructor
    private static class UnsupportedFieldType{

        @Scale
        Double value;
    }
}