package org.luban.rpc;

import org.luban.annotation.RpcService;

import java.util.Date;

/**
 * @author liguolin
 * 2019-04-02 14:41:56
 */
@RpcService
public class DemoServiceImpl implements DemoService {

    @Override
    public DemoResult hello(String name, int count) {
        DemoResult demoResult = new DemoResult();
        demoResult.setCount(count);
        demoResult.setName(name);
        return demoResult;
    }
}
