package ru.nsu.masolygin.oopchecker.dsl;

import groovy.lang.Closure;

/**
 * Привязывает Groovy-замыкание к делегату с {@code DELEGATE_FIRST} resolve strategy и вызывает его.
 * Без этого DSL-методы искались бы в owner'е (Script), а не в делегате.
 */
class ClosureBinder {

    /**
     * Привязывает замыкание к делегату и вызывает его.
     *
     * @param closure  замыкание из DSL-блока
     * @param delegate объект-делегат, обрабатывающий вызовы внутри блока
     */
    static void bindAndCall(Closure<?> closure, Object delegate) {
        closure.setDelegate(delegate);
        closure.setResolveStrategy(Closure.DELEGATE_FIRST);
        closure.call();
    }
}
