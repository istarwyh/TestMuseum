package istarwyh.classloader.modifier.impl;

import istarwyh.classloader.model.CannotInitialLackMatchedConstructor;
import istarwyh.classloader.modifier.Modifier;
import istarwyh.util.JavassistUtil;
import javassist.CtClass;

public class CannotInitialDueToPrivateConstructorModifier implements Modifier<CannotInitialLackMatchedConstructor> {}
