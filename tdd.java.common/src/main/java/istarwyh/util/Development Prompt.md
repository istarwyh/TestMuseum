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
- task
Write tests for given test.
- Test Code Requirements
  - Coverage goal: 100% branch
  - Testing framework: JUnit5
  - code style: 
    - Use class variables instead of local variables as much as possible
    - use static imports
  - Test types:
    - ParametrizedTest
    - DynamicTest(necessary for complex tests)
  - Library for mocking: Mockito
  - Naming convention: lowercase_underscore
  - Avoid unnecessary annotations: true
  - Additional dependencies:
    - Lombok
    - Guava
    - Other common dependencies
- Code
```java
public class Test {
  private static String createTestReSource(String resource) {
    String fileDirPath = RESOURCES_PATH_PREFIX + resource.substring(0, resource.lastIndexOf("/"));
    new File(fileDirPath).mkdirs();
    try {
      String moduleAbsoluteResource = RESOURCES_PATH_PREFIX + resource;
      File file = new File(moduleAbsoluteResource);
      file.createNewFile();
      try(BufferedWriter writer = new BufferedWriter(new FileWriter(moduleAbsoluteResource))){
        writer.write(JSON.toJSONString(
                new TestCase<>("This is your input","This is your expected output"))
        );
        writer.flush();
        writer.close();
        System.out.println(moduleAbsoluteResource + (file.exists() ? "\ncreated successfully" : "on way..."));
        return resource;
      }
    } catch (IOException e) {
      System.out.println("Error creating file: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }
}

```


### Code Review
- task: review code and modify it
- code_style:
  - clean
  - advanced
  - testable
  - functional
- code:
```java
public class TestUtil {
  private static String createTestReSource(String resource) {
    String fileDirPath = RESOURCES_PATH_PREFIX + resource.substring(0, resource.lastIndexOf("/"));
    new File(fileDirPath).mkdirs();
    try {
      String moduleAbsoluteResource = RESOURCES_PATH_PREFIX + resource;
      File file = new File(moduleAbsoluteResource);
      file.createNewFile();
      try(BufferedWriter writer = new BufferedWriter(new FileWriter(moduleAbsoluteResource))){
        writer.write(JSON.toJSONString(
                new TestCase<>("This is your input","This is your expected output"))
        );
        writer.flush();
        System.out.println(moduleAbsoluteResource + (file.exists() ? "\ncreated successfully" : "on way..."));
        return resource;
      }
    } catch (IOException e) {
      System.out.println("Error creating file: " + e.getMessage());
      throw new RuntimeException(e);
    }
  }
}
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