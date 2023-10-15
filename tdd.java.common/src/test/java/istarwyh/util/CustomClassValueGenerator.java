package istarwyh.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class CustomClassValueGenerator implements ObjectInitUtil.ValueGenerator<CustomClassValueGenerator.CustomClass> {

    @Override
    public CustomClass generateValue(boolean useDefaultValues) {
        return new CustomClass(10,new CustomClassValueGenerator.CustomClass(11,null));
    }

    @Getter
    @AllArgsConstructor
    public static final class CustomClass {
        private int value;
        private CustomClass customClass;
    }
}
