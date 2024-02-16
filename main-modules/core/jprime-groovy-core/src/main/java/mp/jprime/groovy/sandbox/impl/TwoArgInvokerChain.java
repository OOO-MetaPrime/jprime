package mp.jprime.groovy.sandbox.impl;

import mp.jprime.groovy.sandbox.GroovyInterceptor;

/**
 * {@link GroovyInterceptor.Invoker} that chains multiple {@link GroovyInterceptor} instances.
 *
 * This version expects two arguments.
 */
abstract class TwoArgInvokerChain extends InvokerChain {
    protected TwoArgInvokerChain(Object receiver) {
        super(receiver);
    }

    public final Object call(Object receiver, String method) throws Throwable {
        throw new UnsupportedOperationException();
    }

    public final Object call(Object receiver, String method, Object arg1) throws Throwable {
        throw new UnsupportedOperationException();
    }

    public final Object call(Object receiver, String method, Object... args) throws Throwable {
        if (args.length!=2)
            throw new UnsupportedOperationException();
        return call(receiver,method,args[0],args[1]);
    }
}

