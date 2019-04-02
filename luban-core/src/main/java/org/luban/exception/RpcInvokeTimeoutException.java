package org.luban.exception;

/**
 * @author liguolin
 * 2019-04-02 11:27:37
 */
public class RpcInvokeTimeoutException extends RuntimeException {

    public RpcInvokeTimeoutException(String msg){
        super(msg);
    }

    public RpcInvokeTimeoutException(){
        super();
    }
}
