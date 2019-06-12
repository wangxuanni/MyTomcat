package com.wxn.v4.core.network;

import com.sun.xml.internal.ws.util.StringUtils;

import java.lang.reflect.InvocationTargetException;

public abstract class Endpoint {
 public abstract void start(int port);

    private volatile boolean running = true;


    public boolean isRunning() {
        return running;
    }

    /**
     * 关闭服务器
     */
    public abstract void close();

    /**
     * 根据传入的字符串获取相对应的实例
     * @param connector
     * @return
     */
    public static Endpoint getInstance(String connector) {
        StringBuilder sb = new StringBuilder();
        sb.append("com.wxn.v4.core.network")
                .append(".")
                /*.append(connector)
                .append(".")
                .append(StringUtils.capitalize(connector))*/
                .append("NioEndpoint");
        try {
            return (Endpoint)Class.forName(sb.toString()).getConstructor().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        throw new IllegalArgumentException(connector);
    }


}
