package istarwyh.log;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

class CommLogHolderTest {

  @Test
  void getIfAbsentThenPut() {
    CommLogModel res = CommLogHolder.commLog();
    res.log();
    assertEquals("B COMMLOG", res.getLogger().getName());
  }

  @Test
  void getCustomLogTest() {
    CommLogModel aCommlog = CommLogHolder.customLog(LoggerFactory.getLogger("A COMMLOG"));
    aCommlog.addContext("测试", "mytest").log();
    assertEquals("CommLogHolderTest.getCustomLogTest", aCommlog.getClassMethodName());
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
                    (String) null, LoggerFactory.getLogger("C COMMLOG"));
    res.log();
    assertEquals("C COMMLOG", res.getLogger().getName());
    assertEquals("CommLogHolderTest.getIfAbsentThenPutDummy", res.getClassMethodName());
  }
}
