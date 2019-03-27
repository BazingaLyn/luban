package org.luban.core;

import org.luban.Endpoint;
import org.luban.transports.LubanRpcNettyServer;

public class LubanRpcServer implements Endpoint,RpcServer {

    private LubanRpcNettyServer lubanRpcNettyServer;

    private int serverPort;

    public LubanRpcServer(int serverPort){
        this.serverPort = serverPort;
        init();
    }


    public void init() {

    }

    public void start() {


    }

    public void shutdown() {

    }

    public void publishService(Object o) {

    }
}
