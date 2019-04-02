package org.luban.exception;

/**
 * @author liguolin
 * 2019-04-02 09:52:46
 */
public class ServiceHasMultiInstantiationException extends RuntimeException {

    public ServiceHasMultiInstantiationException(String msg){
        super(msg);
    }
}
