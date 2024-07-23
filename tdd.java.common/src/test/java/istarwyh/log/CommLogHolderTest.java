package istarwyh.log;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

class CommLogHolderTest {

  @Test
  void getIfAbsentThenPut() {
    CommLogModel res = CommLogHolder.getCommLog();
    res.log();
    assertEquals("COMMLOG", res.getLogger().getName());
  }

  @Test
  void getIfAbsentThenPutSpecifyLogger() {
    CommLogHolder.getIfAbsentThenPut(LoggerFactory.getLogger("A COMMLOG"))
        .addContext("测试", "mytest")
        .log();
  }

  @Test
  void getIfAbsentThenPutSpecifyClassMethod() {
    CommLogModel res =
        CommLogHolder.getIfAbsentThenPut(
            "CommLogHolderTest.getIfAbsentThenPut", LoggerFactory.getLogger("B COMMLOG"));
    res.log();
    assertEquals("B COMMLOG", res.getLogger().getName());
  }

  @Test
  void getIfAbsentThenPutDummy() {
    CommLogModel res =
            CommLogHolder.getIfAbsentThenPut(
                    "CommLogHolderTest.dummyMethod", LoggerFactory.getLogger("C COMMLOG"));
    res.log();
    assertEquals("C COMMLOG", res.getLogger().getName());
  }
}
