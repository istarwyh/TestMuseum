###  Demand -> Test
Please write the full and good tests using JUnit5 and lowercase underscore style of test method names
with the following demand description firstly.
In all the process you can use lombok or other common dependencies to simplify code.

### Test -> Code
Please help me fulfill the following test according to the display description 
and achieve the method what is asserted in the test with the clean, advanced and functional style
```java
    @DisplayName("""
        1. if the field are builtin type, return true is default or random value
        2. if the field are String type, return a random not blank string
        """)
    @Test
    void generateValue(){
            int value = (int) ObjectInitUtil.generateValue(int.class,false);
        assertEquals(10,value);
        }

```
### Find Bugs
Please help me the bug of the following code and the error message is :
```java

```