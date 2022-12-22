package istarwyh.util;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static istarwyh.util.ThreadLocalMap.getMap;
import static istarwyh.util.ThreadLocalMap.getThreadLocalMap;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class ThreadLocalMapTest {

    // 创建固定大小的线程池
    private final ExecutorService threadPool = Executors.newFixedThreadPool(3);

    @RepeatedTest(3) @Execution(ExecutionMode.SAME_THREAD)
    void should_share_thread_local_map_in_the_same_thread_without_remove() throws ExecutionException, InterruptedException {
        Map<String, String> map = changeMap();
        assertNotNull(map);
    }


    @Disabled
    @RepeatedTest(3) @Execution(ExecutionMode.CONCURRENT)
    void should_not_share_thread_local_map_in_the_different_threads() throws ExecutionException, InterruptedException {
        Map<String, String> map = changeMap();
        assertNull(map);
    }

    @Disabled
    @RepeatedTest(3) @Execution(ExecutionMode.SAME_THREAD)
    void should_not_share_thread_local_map_in_self_define_different_threads() throws ExecutionException, InterruptedException {
        var changeMap1 = supplyAsync(this::changeMap, threadPool);
        Map<String, String> map1 = changeMap1.get();
        assertNull(map1);
    }



    @RepeatedTest(300) @Execution(ExecutionMode.CONCURRENT)
    void should_share_static_map_in_different_threads() throws ExecutionException, InterruptedException {
        var changeMap1 = supplyAsync(this::changeMap1,threadPool);
        CompletableFuture<Map<String, String>> changeMap2 = supplyAsync(this::changeMap2,threadPool);
        CompletableFuture<Map<String, String>> changeMap3 = supplyAsync(this::changeMap3,threadPool);
        Map<String, String> map3 = changeMap3.get();
        Map<String, String> map1 = changeMap1.get();
        Map<String, String> map2 = changeMap2.get();
        assertEquals(6,map1.size());
        assertEquals(6,map2.size());
        assertEquals(6,map3.size());
    }

    public Map<String,String> changeMap1(){
        return changeMap1(getMap());
    }

    public Map<String,String> changeMap2(){
        return changeMap2(getMap());
    }

    public Map<String,String> changeMap3(){
        return changeMap3(getMap());
    }

    @Nullable
    private Map<String, String> changeMap() {
        Map<String, String> map = getThreadLocalMap();
        log.info("------------- map ------------- map:{}", map);
        ofNullable(map).ifPresent(it -> it.put("change" + new Random().nextInt(100),"map1"));
        log.info("------------- map ------------- map:{}", map);
        return map;
    }

    public Map<String,String> changeMap1(Map<String, String> map){
        log.info("------------- changeMap1 ------------- map:{}", map);
        ofNullable(map).ifPresent(it -> it.put("change1","map1"));
        log.info("------------- changeMap1 ------------- map:{}", map);
        return map;
    }

    public Map<String,String> changeMap2(Map<String, String> map){
        log.info("------------- changeMap2 ------------- map:{}", map);
        ofNullable(map).ifPresent(it -> it.put("change2","map2"));
        log.info("------------- changeMap2 ------------- map:{}", map);
        return map;
    }

    public Map<String,String> changeMap3(Map<String, String> map){
        log.info("------------- changeMap3 ------------- map:{}", map);
        ofNullable(map).ifPresent(it -> it.put("change3","map3"));
        log.info("------------- changeMap3 ------------- map:{}", map);
        return map;
    }

}