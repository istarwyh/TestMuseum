package istarwyh.moduleloader.util;

import com.google.common.base.CaseFormat;
import org.jetbrains.annotations.NotNull;

public class NameConverter {

    @NotNull
    public static String toUpperUnderScoreName(String moduleClass) {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, moduleClass);
    }

}
