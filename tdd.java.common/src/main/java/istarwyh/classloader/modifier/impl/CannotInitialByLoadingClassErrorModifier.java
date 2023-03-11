package istarwyh.classloader.modifier.impl;

import istarwyh.classloader.model.CannotInitialDueToLoadingClassError;
import istarwyh.classloader.modifier.Modifier;
import javassist.CtClass;
import lombok.SneakyThrows;

import static istarwyh.util.JavassistUtil.wipeOriginalByteCode;

public class CannotInitialByLoadingClassErrorModifier implements Modifier<CannotInitialDueToLoadingClassError> {}
