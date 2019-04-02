package org.luban.exception;

/**
 * @author liguolin
 * 2019-04-02 09:52:37
 */
public class NoFoundRpcServiceProviderException extends RuntimeException {

    public NoFoundRpcServiceProviderException(String msg){
        super(msg);
    }

    public NoFoundRpcServiceProviderException(){
        super();
    }
}
