package com.dstz.base.common.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import org.springframework.util.Assert;
import org.springframework.util.function.SingletonSupplier;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * 上下文清理工具类
 *
 * @author wacxhs
 */
public class ContextCleanUtils {

    private ContextCleanUtils() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    /**
     * 时机
     */
    public enum Phase {

        /**
         * 线程运行前后
         */
        THREAD,

        /**
         * web请求后
         */
        REQUEST_COMPLETE
    }

    private final static Map<Phase, List<Runnable>> CONTEXT_CLEAN_REGISTRY = MapUtil.newHashMap();

    /**
     * 提升性能，在第一次调用获取所有执行方法
     */
    private final static AtomicReference<SingletonSupplier<List<Runnable>>> CLEAN_ALL_REGISTRY = new AtomicReference<>(newCleanAllRegistrySupplier());

    private static SingletonSupplier<List<Runnable>> newCleanAllRegistrySupplier() {
        return SingletonSupplier.of(() -> CONTEXT_CLEAN_REGISTRY.values().stream().flatMap(List::stream).distinct().collect(Collectors.toList()));
    }

    /**
     * 注册清理方法
     *
     * @param runnable 清理运行方法
     */
    public static void register(Runnable runnable, Phase... phases) {
        Assert.state(ArrayUtil.isNotEmpty(phases), "phases parameter must");
        for (Phase phase : phases) {
            CONTEXT_CLEAN_REGISTRY.computeIfAbsent(phase, k -> new ArrayList<>()).add(runnable);
        }
        CLEAN_ALL_REGISTRY.lazySet(newCleanAllRegistrySupplier());
    }

    /**
     * 执行清理，根据时机调用
     *
     * @param phases 执行清理
     */
    public static void execute(Phase... phases) {
        if (phases == null) {
            throw new IllegalStateException("Phases parameter must");
        }
        Set<Runnable> onceSet = new HashSet<>();
        for (Phase phase : phases) {
            List<Runnable> runnableList = CONTEXT_CLEAN_REGISTRY.get(phase);
            if (runnableList != null) {
                onceSet.addAll(runnableList);
            }
        }
        onceSet.forEach(Runnable::run);
    }

    /**
     * 执行清理所有
     */
    public static void executeAll() {
        CLEAN_ALL_REGISTRY.get().obtain().forEach(Runnable::run);
    }
}
