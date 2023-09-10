### Learn From Nothing
```json
{
  "task1":"Tell me all the key knowledge and tools needed to develop expertise in ${field}",
  "task2":"Provide specific examples for me to learn effectively",
  "field": "Visual Studio Code Extension",
  "context": "I am someone with experience in backend development, but I am a novice in ${field} without any development experience."
  
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
```json
{
  "task": "Write tests for given test",
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
### Code Review
```json
{
  "task": "review code and modify it",
  "code_style": [
    "clean",
    "advanced",
    "testable",
    "functional"
  ],
  "code": ""
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