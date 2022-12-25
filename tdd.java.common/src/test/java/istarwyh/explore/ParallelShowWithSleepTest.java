package istarwyh.explore;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Slf4j
class ParallelShowWithSleepTest {

    private ParallelShowWithSleep parallelShowWithSleep;

    @BeforeEach
    void setUp() {
        parallelShowWithSleep = new ParallelShowWithSleep();
    }

    @RepeatedTest(5) @Execution(ExecutionMode.CONCURRENT)
    void testAsyncConcurrently(TestInfo testInfo){
        testInfo.getTestMethod().ifPresent(it -> log.info(it.getName()));
        parallelShowWithSleep.async();
    }

    @RepeatedTest(5) @Execution(ExecutionMode.SAME_THREAD)
    void testAsyncWithSameMethod(TestInfo testInfo){
        testInfo.getTestMethod().ifPresent(it -> log.info(it.getName()));
        parallelShowWithSleep.async();
    }

}