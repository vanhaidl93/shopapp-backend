package com.hainguyen.shop.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    /*
    @Pointcut("within(com.project.shopapp.controllers.*)")
    @Pointcut("within(com.project.shopapp.controllers.CategoryController)")
    --> match all methods within specific package/name.
    org.springframework.web.bind.annotation
    */

    // match all methods within any class name ( *) that is annotated with @RestController (@...).
    @Pointcut("within(@org.springframework.web.bind.annotation.RestController *)")
    public void  restControllerMethods() {};

    @Before("restControllerMethods()")
    public void beforeMethodExecution(JoinPoint joinPoint) {
        log.info("Starting execution of {}", joinPoint.getSignature().getName());
    }

    @After("restControllerMethods()")
    public void afterMethodExecution(JoinPoint joinPoint) {
        log.info("Finished execution of {}", joinPoint.getSignature().getName());
    }

    @Around("restControllerMethods()")
    public Object measureControllerMethodExecutionTime(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long start = System.nanoTime();
        // trigger join point
        Object result = proceedingJoinPoint.proceed();
        long end = System.nanoTime();
        String methodName = proceedingJoinPoint.getSignature().getName();
        log.info("Execution of {} took {} ms", methodName, TimeUnit.NANOSECONDS.toMillis(end - start));
        return result;
    }


}
