package org.luban;

import org.luban.utils.Proxies;

public interface RpcProxy<T> {

    default T newInstance(){

        Object handler = createHandler();

        Class<T> cls = getDefaultClass();

        return Proxies.getDefault().newProxy(cls,handler);

    }


    Class<T> getDefaultClass();


    Object createHandler();
}
