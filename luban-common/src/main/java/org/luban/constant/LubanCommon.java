package org.luban.constant;


/**
 * @author liguolin
 * 2019-04-01 13:42:32
 */
public class LubanCommon {

    public static int DEFAULT_SERVICE_WEIGHT = 50;

    public static int DEFAULT_MAX_SERVICE_WEIGHT = 100;

    public static int DEFAULT_MIN_SERVICE_WEIGHT = 0;

    public static String DEFAULT_GROUP = "default";

    public static String DEFAULT_VERSION = "1.0.0";

    public static long DEFAULT_INVOKE_TIME_OUT = 5000;

    public static long DEFAULT_CREATE_CHANNEL_LOCK_TIME = 3000;

    public static int DEFAULT_CONNECT_TIMEOUT_MILLIS = 2000;


    public static String completeServiceName(String group,String serviceName,String version){
        return String.format("/%s/%s/%s",group,serviceName,version);
    }

    public static String declareMethodName(String serviceName,String methodName){
        return String.format("%s.%s",serviceName,methodName);
    }

}
