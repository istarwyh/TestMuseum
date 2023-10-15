package istarwyh.util;

/**
 * @author mac
 */
public interface ValueGenerator<T> {

    /**
     * Returns the value
     *
     * @param useDefaultValues whether to use default values
     * @return the value
     */
    T generateValue(boolean useDefaultValues);



    /**
     * choose which classloader should use this modifier
     */
    default void register(){
        ObjectInitUtil.specifyCustomValueGenerator(
                ReflectionUtil.getInterfaceFirstGenericClazz(ValueGenerator.class,this.getClass())
                ,this);
    }

}
