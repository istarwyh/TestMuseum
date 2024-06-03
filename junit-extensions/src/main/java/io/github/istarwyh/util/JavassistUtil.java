package io.github.istarwyh.util;

import javassist.*;
import lombok.SneakyThrows;

/**
 * @author xiaohui
 */
public class JavassistUtil {

    /**
     * wipe all bytecode in the original Class file
     * @param ctClass ctClass info of the original class
     */
    public static void wipeOriginalByteCode(CtClass ctClass) throws CannotCompileException {
        if (ctClass.isInterface()) {
            return;
        }
        for (CtConstructor ctConstructor : ctClass.getConstructors()) {
            ctConstructor.setBody("{}");
        }
        if (ctClass.getClassInitializer() != null) {
            ctClass.getClassInitializer().setBody("{}");
        }
        for (CtMethod method : ctClass.getDeclaredMethods()) {
            method.setBody(returnEmptyStatement(method.getSignature()));
        }
    }

    /**
     * @param methodSignature methodSignature
     * @return when base data structure return the matched empty value and when object return null
     */
    private static String returnEmptyStatement(String methodSignature) {
        int methodSignatureReturnTypeByteCodeLength = 2;
        String endStr = methodSignature.substring(methodSignature.length() - methodSignatureReturnTypeByteCodeLength);
        String result;
        switch (endStr) {
            case ")V":
                result = "{}";
                break;
            case ")Z":
                result = "{return false;}";
                break;
            case ")B":
            case ")I":
            case ")S":
                result = "{return 0;}";
                break;
            case ")C":
                result = "{return '\\0';}";
                break;
            case ")D":
                result = "{return 0.0;}";
                break;
            case ")F":
                result = "{return 0.0f;}";
                break;
            case ")J":
                result = "{return 0L;}";
                break;
            default:
                result = "{return null;}";
                break;
        }
        return result;
    }


    @SneakyThrows
    public static void addNoArgsConstructorIfAbsent(CtClass ctClass){
        try{
            CtConstructor constructor = ctClass.getDeclaredConstructor(new CtClass[0]);
            constructor.setModifiers(Modifier.PUBLIC);
        }catch (NotFoundException notFoundException){
            CtConstructor newConstructor = CtNewConstructor.make(new CtClass[0], new CtClass[0], ctClass);
            newConstructor.setBody("{}");
            newConstructor.setModifiers(Modifier.PUBLIC);
            ctClass.addConstructor(newConstructor);
        }

    }
}
