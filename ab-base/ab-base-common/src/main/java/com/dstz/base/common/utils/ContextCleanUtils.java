package com.dstz.base.common.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import org.springframework.util.Assert;
import org.springframework.util.function.SingletonSupplier;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Context cleaning tool class
 *
 */
public class ContextCleanUtils {

    private ContextCleanUtils() throws IllegalAccessException {
        throw new IllegalAccessException();
    }

    /**
     * Timing
     */
    public enum Phase {

        /**
         * Before and after thread running
         */
        THREAD,

        /**
         * After web request
         */
        REQUEST_COMPLETE
    }

    private final static Map<Phase, List<Runnable>> CONTEXT_CLEAN_REGISTRY = MapUtil.newHashMap();

    /**
     * Improve performance, get all executed methods in the first call
     */
    private final static AtomicReference<SingletonSupplier<List<Runnable>>> CLEAN_ALL_REGISTRY = new AtomicReference<>(newCleanAllRegistrySupplier());

    private static SingletonSupplier<List<Runnable>> newCleanAllRegistrySupplier() {
        return SingletonSupplier.of(() -> CONTEXT_CLEAN_REGISTRY.values().stream().flatMap(List::stream).distinct().collect(Collectors.toList()));
    }

    /**
     * Registration cleanup method
     *
     * @param runnable Cleanup run method
     */
    public static void register(Runnable runnable, Phase... phases) {
        Assert.state(ArrayUtil.isNotEmpty(phases), "phases parameter must");
        for (Phase phase : phases) {
            CONTEXT_CLEAN_REGISTRY.computeIfAbsent(phase, k -> new ArrayList<>()).add(runnable);
        }
        CLEAN_ALL_REGISTRY.lazySet(newCleanAllRegistrySupplier());
    }

    /**
     * Perform cleanup and call according to the timing
     *
     * @param phases Perform cleanup
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
     * Execute clean all
     */
    public static void executeAll() {
        CLEAN_ALL_REGISTRY.get().obtain().forEach(Runnable::run);
    }
}
