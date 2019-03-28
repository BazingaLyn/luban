package org.luban.exception;

public class ServiceHasMultiInstantiationException extends RuntimeException {

    public ServiceHasMultiInstantiationException(String msg){
        super(msg);
    }
}
