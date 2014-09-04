package org.deletethis.logfront.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper class to implement Listener lists
 *
 * Typical usage: MetaListener<SomeListener> meta = new
 * MetaListener(SomeListener.class); SomeListener a; SomeListener b;
 *
 * meta.addListener(a); meta.addListener(b);
 *
 * meta.getMetaListener().onSomething();
 *
 * It will invoke: a.onSomething(); b.onSomething();
 *
 * @author miko
 *
 * @param <T> Listener class
 */
public class MetaListener<T> {

    private T proxy;
    private List<T> listeners = new ArrayList<>();

    private class Handler implements InvocationHandler {

        @Override
        public Object invoke(Object proxy, Method method,
                Object[] params) throws Throwable {

            for(T listener : listeners) {
                method.invoke(listener, params);
            }
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    private void setProxy(Object proxy) {
        this.proxy = (T) proxy;
    }

    public MetaListener(Class<T> cls) {
        Object proxy = Proxy.newProxyInstance(cls.getClassLoader(),
                new Class[] {cls},
                new Handler());

        setProxy(proxy);

    }

    public void addListener(T listener) {
        if(listener == null) {
            throw new IllegalArgumentException("null listener");
        }

        listeners.add(listener);
    }

    public T getMetaListener() {
        return proxy;
    }
}
