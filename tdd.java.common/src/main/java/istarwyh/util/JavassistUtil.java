package istarwyh.util;

import javassist.*;
import lombok.SneakyThrows;

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
        return switch (endStr) {
            case ")V" -> "{}";
            case ")Z" -> "{return false;}";
            case ")B", ")I", ")S" -> "{return 0;}";
            case ")C" -> "{return '\\0;}";
            case ")D" -> "{return 0.0;}";
            case ")F" -> "{return 0.0f;}";
            case ")J" -> "{return 0L;}";
            default -> "{return null;}";
        };
    }

    @SneakyThrows
    public static void addNoArgsConstructorIfAbsent(CtClass ctClass){
        try{
            var constructor = ctClass.getDeclaredConstructor(new CtClass[0]);
            constructor.setModifiers(Modifier.PUBLIC);
        }catch (NotFoundException notFoundException){
            CtConstructor newConstructor = CtNewConstructor.make(new CtClass[0], new CtClass[0], ctClass);
            newConstructor.setBody("{}");
            newConstructor.setModifiers(Modifier.PUBLIC);
            ctClass.addConstructor(newConstructor);
        }

    }
}
