package org.luban;

import org.luban.utils.NativeSupport;

public interface Endpoint {

    void init();

    void start();

    void shutdown();

    default boolean isNativeEt() {
        return NativeSupport.isSupportNativeET();
    }
}
