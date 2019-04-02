package org.luban.rpc;

import java.util.Date;

/**
 * @author liguolin
 * 2019-04-02 14:39:47
 */
public interface DemoService {

    DemoResult hello(String name, int count);
}
