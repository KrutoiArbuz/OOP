package ru.nsu.masolygin.oopchecker.dsl;

import groovy.lang.Closure;

/**
 * Утилита для однотипной настройки Closure: подменяем delegate и переключаем resolve strategy в
 * DELEGATE_FIRST, чтобы DSL-методы искались в делегате, а не в owner'е (Script).
 */
final class DslSupport {

    private DslSupport() {
    }

    static void runClosure(Closure<?> closure, Object delegate) {
        closure.setDelegate(delegate);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }
}
