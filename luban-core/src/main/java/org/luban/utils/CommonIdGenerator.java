package org.luban.utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author liguolin
 * 2019-04-01 09:53:39
 */
public class CommonIdGenerator {

    private static final AtomicLong id = new AtomicLong(0);


    public static long nextId(){
        return id.incrementAndGet();
    }
}
