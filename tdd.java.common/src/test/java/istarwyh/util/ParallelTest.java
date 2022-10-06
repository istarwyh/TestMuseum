package istarwyh.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Slf4j
class ParallelTest {

    private Parallel parallel;

    @BeforeEach
    void setUp() {
        parallel = new Parallel();
    }

    @RepeatedTest(5) @Execution(ExecutionMode.CONCURRENT)
    void testAsyncConcurrently(TestInfo testInfo){
        testInfo.getTestMethod().ifPresent(it -> log.info(it.getName()));
        parallel.async();
    }

    @RepeatedTest(5) @Execution(ExecutionMode.SAME_THREAD)
    void testAsyncWithSameMethod(TestInfo testInfo){
        testInfo.getTestMethod().ifPresent(it -> log.info(it.getName()));
        parallel.async();
    }

}