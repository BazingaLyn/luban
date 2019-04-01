package org.luban.rpc;

/**
 * @author liguolin
 * 2019-03-29 10:11:15
 *
 */
public class RpcResponse {

    private long id;

    private Object result;

    private Throwable throwable;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }
}
