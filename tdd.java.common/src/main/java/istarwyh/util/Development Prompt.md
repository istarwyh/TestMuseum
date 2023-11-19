### Learn From Nothing
```json
{
  "task1":"Tell me all the key knowledge and tools needed to develop expertise in ${field}",
  "task2":"Provide specific examples for me to learn effectively",
  "field": "Visual Studio Code Extension",
  "context": "I am someone with experience in backend development, but I am a novice in ${field} without any development experience."
  
}
```

### Decompose Task
```json
{
  "tasks": {
    "task1": "Break down the given algorithm problem into smaller sub-problems with clear requirements",
    "task2": "Review the requirements, and if they aren't met, return to task1",
    "task3": "Combine the solutions to each sub-problem to create the final answer to the algorithm problem"
  },
  "role": "You are a senior Java algorithm instructor",
  "requirements": [
    "There should be at least 5 sub-problems",
    "Each sub-problem must have clear input and output",
    "The solution method for each sub-problem should follow the Google Code Format and be no more than 5 lines",
    "Each solution method should have detailed and meaningful Javadoc annotations",
    "The solutions to the sub-problems should be easily combined into a final, runnable solution"
  ],
  "algorithm_problem": "输入任意数字组成的数组,将该数组前半部分全部变成偶数,后半部分变成奇数输出",
  "context": "You're learning to solve algorithmic problems but only have limited, fragmented time to study. You need smaller problems that can be solved within the time you have available."
}
```
### MVP
```json
{
  "task1":"Give me a demo to help me understand ${field}",
  "demo_requirements":[
    "1. should be able to run in actual environment",
    "2. should teach me how to run your demo"
  ],
  "field": "Visual Studio Code Extension",
  "additional": [
    "1. If it's not possible to provide a demo all at once, it's okay to provide it in multiple parts, but please provide complete code and steps."
    ,"If there are any uncertain or questionable parts, please clarify them immediately so that I can confirm and continue writing."
  ]
}
```
### Demand -> Test
```json
{
  "task": "Only Write the test method",
  "context": "",
  "examples":{
    "input": "",
    "output": ""
  },
  "role": "You are a senior development engineer who wants to work with TDD",
  "programming_language": "Java",
  "language_version": "8",
  "code_style": [
    "clean",
    "advanced",
    "testable",
    "functional"
  ],
  "test_method_template": {
    "should_have_annotations": {
      "@DisplayName": [
        "1.XXX",
        "2.yyy"
      ]
    }
  },
  "functional_requirements_source": "@DisplayName",
  "test_code_requirements":{
    "coverage_goal":"100% branch",
    "testing_framework": "JUnit5",
    "test_types": [
      "ParametrizedTest",
      "DynamicTest"
    ],
    "Library_for_mocking": "Mockito",
    "naming_convention": "lowercase_underscore",
    "avoid_unnecessary_annotations": true,
    "additional_dependencies": [
      "Lombok",
      "other common dependencies"
    ]
  }
}
```
### Test -> Code
```json
{
  "task": "Implement the method According to the test method",
  "context": "",
  "examples":"",
  "role": "You are a senior development engineer, with a deep knowledge of computer fundamentals, data structures, and excellent coding taste.",
  "programming_language": "Java",
  "language_version": "8",
  "code_style": [
    "clean",
    "advanced",
    "testable",
    "functional"
  ],
  "test_method": ""
}
```
### Code -> Test
- task: Write tests for given test.
- Test Code Requirements
  - Coverage goal: 100% branch and tests can be run
  - Naming convention: lowercase_underscore
  - Testing framework: JUnit5
  - code style
    - Use class variables instead of local variables as much as possible
    - use static imports
  - Test types
    - ParametrizedTest
    - DynamicTest(necessary for complex tests)
  - Library for mocking: Mockito
    - mock requirements
      - if face with void methods, skip tests with `doNothing()` of Mockito
      - avoid to use `any()`directly instead of `anyString()` 、 `anyLong()` .etc
  - Avoid unnecessary annotations: true
  - Additional dependencies:
    - Lombok
    - Guava
    - Other common dependencies
- Code
```java


```


### Code Review
- task: review code and modify it
- role:你是一位优秀的软件工程师，擅长对代码进行重构.以下是你掌握的一些重构高质量代码的总结:
代码重构是对现有代码进行修改和优化，以改善其结构、可读性、可维护性和性能等方面的技术。重构的目的是在不改变代码外部行为的前提下，通过优化代码结构来提高代码质量。
总体而言，代码重构可以通过以下几个步骤进行:
  1. 理解代码: 首先要深入理解要重构的代码，包括其功能逻辑和结构等方面的特点。
  2. 设计重构计划: 根据代码的特点和需求，制定具体的重构计划。可以根据以下列举的重构方式和技术，选择适合的重构方法。
  3. 提取函数(Extract Function): 将一段代码提取为一个独立的函数，以提高代码的可读性和可维护性。
  4. 内联函数(Inline Function): 将某个函数调用的地方替换为函数本体，以减少不必要的函数调用开销。
  5. 封装字段(Encapsulate Field): 将类中的字段封装起来，通过提供访问器函数来访问和修改字段的值，以提高类的封装性和灵活性。
  6. 重命名(Rename): 通过修改标识符的名称来使代码更易于理解和维护。
  7. 拆分临时变量(Split Temporary Variable): 将一个临时变量拆分为多个，以减少代码的复杂度和提高可读性。
  8. 移除重复代码(Remove Duplicate Code): 通过抽象和封装来消除重复的代码，以减少代码量和提高代码的可维护性。
  9. 引入解释性变量(Introduce Explaining Variable): 将复杂的表达式或计算过程提取为一个变量，以增加代码的可读性和可维护性。
  10. 替换算法(Replace Algorithm): 通过使用更高效或更简洁的算法来替换现有的算法，以提高代码的性能。
  11. 简化条件表达式(Simplify Conditional Expressions): 简化复杂的条件表达式，以提高代码的可读性和可维护性。
  12. 简化函数调用(Simplify Function Calls): 简化函数调用的方式，以减少不必要的参数和提高代码的可读性。
  13. 搬移函数(Move Function): 将函数从一个类或模块中移动到另一个类或模块中，以减少代码的耦合性和提高代码的可维护性。
  14. 搬移字段(Move Field): 将字段从一个类中移动到另一个类中，以减少代码的耦合性和提高代码的可维护性。
  15. 提炼类(Extract Class): 将一个类中的一部分代码提取为一个新的类，以提高代码的模块化和可维护性。
  16. 提炼接口(Extract Interface): 将一个类的公共接口提取为一个独立的接口，以增加代码的灵活性和可扩展性。
  17. 以委托取代继承(Replace Inheritance with Delegation): 使用委托方式替代继承关系，以减少代码的耦合性和提高代码的可维护性。
  18. 抽象超类(Abstract Superclass): 将多个相关的子类中的共同部分抽象为一个超类，以减少代码的重复和提高代码的可维护性。

- code_style:
  - clean
  - advanced
  - testable
  - functional
- code:
```java

```

### Find Bugs
```json
{
  "task": "find the bugs of code according to the error_message",
  "error_message": "",
  "code": ""
}
```
### Fix the Bug
```json
{
  "task": "fix the bug",
  "error_message": "",
  "context": ""
}
```