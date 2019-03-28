package org.luban.rpc;

/**
 * @author liguolin
 * @create 2018-11-20 15:16
 **/
public interface RpcRequest {

    String group();

    String version();

    long requestId();

    String interfaceName();

    String methodName();

    Object[] parameters();

    Class<?>[] parameterClass();

}
