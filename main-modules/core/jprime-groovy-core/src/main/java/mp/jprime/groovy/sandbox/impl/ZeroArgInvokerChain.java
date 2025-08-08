package mp.jprime.groovy.sandbox.impl;

import mp.jprime.groovy.sandbox.GroovyInterceptor;

/**
 * {@link GroovyInterceptor.Invoker} that chains multiple {@link GroovyInterceptor} instances.
 * <p>
 * This version expects no arguments.
 */
abstract class ZeroArgInvokerChain extends InvokerChain {
  protected ZeroArgInvokerChain(Object receiver) {
    super(receiver);
  }

  public final Object call(Object receiver, String method, Object arg1) throws Throwable {
    throw new UnsupportedOperationException();
  }

  public final Object call(Object receiver, String method, Object arg1, Object arg2) throws Throwable {
    throw new UnsupportedOperationException();
  }

  public final Object call(Object receiver, String method, Object... args) throws Throwable {
    if (args.length != 0)
      throw new UnsupportedOperationException();
    return call(receiver, method);
  }
}
