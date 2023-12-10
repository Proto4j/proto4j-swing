package io.github.proto4j.test.swing.annotation; //@date 04.09.2022

import io.github.proto4j.swing.annotation.AnnotationContext;

import java.lang.annotation.Annotation;

public class ContextTest {

    public static void main(String[] args) throws ReflectiveOperationException {
        AnnotationContext<Annotation> context = AnnotationContext.exchange(null ,null);

        context.requireNonNull();

    }
}
