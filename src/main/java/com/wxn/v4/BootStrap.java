package com.wxn.v4;
import com.wxn.v4.core.network.Endpoint;
import com.wxn.v4.core.util.PropertyUtil;
public class BootStrap {

    /**
     * 服务器启动入口
     */
    public static void run() {
        String connector = PropertyUtil.getProperty("server.connector");
        String port = PropertyUtil.getProperty("server.port");
        Endpoint server = Endpoint.getInstance(connector);
        server.start(Integer.parseInt(port));
    }

    public static void main(String[] args) {
        BootStrap.run();
    }
}
