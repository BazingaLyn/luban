package org.luban.rpc;

/**
 * @author liguolin
 * @create 2018-11-20 15:16
 **/
public class RpcRequest {

    private long requestId;

    private String group;

    private String version;

    private String serviceName;

    private String methodName;

    private Object[] parameters;

    private Class<?>[] parameterClass;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    public Class<?>[] getParameterClass() {
        return parameterClass;
    }

    public void setParameterClass(Class<?>[] parameterClass) {
        this.parameterClass = parameterClass;
    }
}
