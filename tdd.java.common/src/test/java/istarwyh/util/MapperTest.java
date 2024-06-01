package istarwyh.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapperTest {

    private final String id = "id";
    private final String address = "B地";

    @Test
    @DisplayName("当a.name 为 null,b.name不为null,a.name 应该被b.name更新")
    void testCoalesceUpdate1(){
        A a = new A(null, id);
        B b = new B("b", address);
        coalesceUpdateA(a, b);
        assertEquals("b",a.name);
        assertEquals(id,a.id);
    }


    @Test
    @DisplayName("当a.name 不为 null,b.name 为null,a.name 不应该被b.name更新")
    void testCoalesceUpdate2(){
        A a = new A("a", id);
        B b = new B(null, address);
        coalesceUpdateA(a, b);
        assertEquals("a",a.name);
        assertEquals(id,a.id);

    }

    /**
     * 实现当a的字段为null时,取b的字段
     */
    private static void coalesceUpdateA(A a, B b) {
        B clone = SerializationUtils.clone(b);
        Converter.INSTANCE.updateB(clone, a);
        Converter.INSTANCE.updateA(a,clone);
    }

    @Test
    @DisplayName("当a.name 不为 null,b.name 不为null,但不与 a.name相等,a.name 不应该被b.name更新")
    void testCoalesceUpdate3(){
        A a = new A("a", id);
        B b = new B("b", address);
        coalesceUpdateA(a, b);
        assertEquals("a",a.name);
        assertEquals(id,a.id);
    }

    @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
    interface Converter{
        Converter INSTANCE = Mappers.getMapper(Converter.class);


        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void updateB(@MappingTarget B b,A a);

        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void updateA(@MappingTarget A a,B b);
    }



    @Data
    @AllArgsConstructor
    public static class A {

        String name;

        String id;
    }

    @Data
    @AllArgsConstructor
    public static class B implements Serializable {

        String name;

        String address;
    }
}
