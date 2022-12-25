package istarwyh.util;

import org.junit.jupiter.api.Test;

import static istarwyh.util.ReflectionUtilTest.*;

class AssertPlusTest {


    @Test
    void should_compare_two_object_just_with_their_field_value() throws NoSuchFieldException {
        WhoIAm lele = new WhoIAm();
        WhoIAm dudu = new WhoIAm();
        ReflectionUtil.setField(lele,"name","me");
        ReflectionUtil.setField(dudu,"name","me");
        AssertPlus.compareFields(lele,dudu);
    }
}