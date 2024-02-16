package mp.jprime.groovy.sandbox.impl;

import mp.jprime.groovy.sandbox.GroovyInterceptor.Invoker;

/**
 * Packs argument of the super method call for {@link Invoker}
 */
public final class Super {
    final Class senderType;
    final Object receiver;

    public Super(Class senderType, Object receiver) {
        this.senderType = senderType;
        this.receiver = receiver;
    }
}
