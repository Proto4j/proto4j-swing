package org.proto4j.test.swing.annotation; //@date 04.09.2022

import org.proto4j.swing.annotation.AnnotationContext;
import org.proto4j.swing.laf.LAFProvider;

import java.lang.annotation.Annotation;

public class ContextTest {

    public static void main(String[] args) throws ReflectiveOperationException {
        AnnotationContext<Annotation> context = AnnotationContext.exchange(null ,null);

        context.requireNonNull();

    }
}
