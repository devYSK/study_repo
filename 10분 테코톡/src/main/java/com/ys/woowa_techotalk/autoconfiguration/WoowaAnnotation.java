package com.ys.woowa_techotalk.autoconfiguration;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : ysk
 */

// 어노테이션 선언

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.CLASS)
public @interface WoowaAnnotation {
}

// 어노테이션 어떤 체리를 해줄지 작성
@Component
@Aspect
public class AnnotationAspect {
    @Around("@Annotation(com.ys.woowa_techotalk.WoowaAnnotation)")
    public Object printHeadAntTail(ProceedingJointPoint jointPoint) throws Throwable {
        // Do Something Before
        Object ret = jointPoint.proceed();
        // Do Something After

        return ret;
    }
}

