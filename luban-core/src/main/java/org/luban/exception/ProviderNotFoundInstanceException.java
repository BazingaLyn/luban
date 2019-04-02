package org.luban.exception;

/**
 * @author liguolin
 * 2019-04-02 14:26:26
 */
public class ProviderNotFoundInstanceException extends RuntimeException {

    public ProviderNotFoundInstanceException(String msg){
        super(msg);
    }

    public ProviderNotFoundInstanceException(){
        super();
    }
}
