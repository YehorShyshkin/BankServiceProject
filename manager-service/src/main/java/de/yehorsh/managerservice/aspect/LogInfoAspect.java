package de.yehorsh.managerservice.aspect;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogInfoAspect {
    private final MeterRegistry meterRegistry;

    @Around("@annotation(logInfo)")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint, LogInfo logInfo) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String metricBaseName = logInfo.name().isEmpty() ? methodName : logInfo.name();

        String counterName = metricBaseName + "_count";
        String timerName = metricBaseName + "_timer";

        Counter methodCounter = meterRegistry.counter(counterName);
        methodCounter.increment();

        Timer timer = Timer.builder(timerName)
                .description("Time taken to execute " + methodName)
                .register(meterRegistry);

        Object result = timer.recordCallable(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw rethrowUnchecked(e);
            }
        });

        long count = timer.count();
        double totalTimeSeconds = timer.totalTime(java.util.concurrent.TimeUnit.SECONDS);
        double averageExecutionTime = (count > 0) ? totalTimeSeconds / count : 0;

        log.info("Method: {}, Total Calls: {}, Average Execution Time (seconds): {}",
                metricBaseName, count, averageExecutionTime);

        Gauge.builder(metricBaseName + "_avg_time", timer, t ->
                        (count > 0) ? totalTimeSeconds / count : 0
                )
                .description("Average time taken to execute " + methodName)
                .register(meterRegistry);
        return result;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Throwable> RuntimeException rethrowUnchecked
            (Throwable throwable) throws T {
        throw (T) throwable;
    }
}
