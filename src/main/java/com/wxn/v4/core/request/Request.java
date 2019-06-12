package com.wxn.v4.core.request;

import com.wxn.v4.core.util.RequestUtil;
import org.apache.log4j.Logger;

import javax.servlet.http.Cookie;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

public class Request {
    private static Logger logger = Logger.getLogger(Request.class);


    //协议信息
    private String requestInfo;
    //请求方式
    private String method;
    //请求url
    private String url;
    //请求参数
    private String queryStr;
    //是否长连接
    public boolean keepAlive = false;
    private boolean requestedSessionCookie;
    private String requestedSessionId;
    private boolean requestedSessionURL;
    //存储参数
    private Map<String, List<String>> parameterMap;
    protected ArrayList cookies = new ArrayList();
    private final String CRLF = "\r\n";
    SocketChannel socketChannel;

    public Request(SocketChannel socketChannel) throws IOException {
        this.parameterMap = new HashMap<String, List<String>>();
        this.socketChannel = socketChannel;
        readHandler(this.socketChannel);

    }

    void readHandler(SocketChannel socketChannel) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        while (socketChannel.read(byteBuffer) > 0) {
            byteBuffer.flip();
            requestInfo += Charset.forName("UTF-8").decode(byteBuffer);

        }
        logger.info("HTTP报文内容\n" + requestInfo);
        parseRequestInfo();
        byteBuffer.clear();

    }

    public void closeSocketChannel() throws IOException {
        this.socketChannel.close();

    }

    public boolean isKeepAlive() {
        return keepAlive;
    }


    private void parseRequestInfo() {

        //1、获取请求方式: 开头到第一个/
        this.method = this.requestInfo.substring(0,
                this.requestInfo.indexOf("/")).toLowerCase();
        this.method = this.method.trim();
        //根据报文设置是否为keepAlive
        if (this.requestInfo.contains("keep-alive") && this.requestInfo.contains("HTTP/1.1")) {
            this.keepAlive = true;
        } else {
            this.keepAlive = false;

        }
        //2、获取请求url: 第一个/ 到 HTTP/
        //可能包含请求参数? 前面的为url
        //1)、获取/的位置
        int startIdx = this.requestInfo.indexOf("/") + 1;
        //2)、获取 HTTP/的位置
        int endIdx = this.requestInfo.indexOf("HTTP/");
        //3)、分割字符串
        this.url = this.requestInfo.substring(startIdx, endIdx).trim();
        //4)、获取？的位置
        int queryIdx = this.url.indexOf("?");
        if (queryIdx >= 0) {//表示存在请求参数
            String[] urlArray = this.url.split("\\?");
            this.url = urlArray[0];
            queryStr = urlArray[1];
        }
        logger.info("{method=" + this.method + ",url=" + this.url + ",queryStr=" + queryStr + ")");

        //3、获取请求参数:如果Get已经获取,如果是post可能在请求体中

        if (method.equals("post")) {
            String qStr = this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
            logger.info("qStr:" + qStr);
            if (null == queryStr) {
                queryStr = qStr;
            } else {
                queryStr += "&" + qStr;
            }
        }
        queryStr = null == queryStr ? "" : queryStr;
        convertMap();

        if (this.requestInfo.equals("Cookie")) {
            Cookie cookies[] = RequestUtil.parseCookieHeader(this.requestInfo);
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals("jsessionid")) {
                    // Override anything requested in the URL
                    if (!this.isRequestedSessionIdFromCookie()) {
                        // Accept only the first session id cookie
                        setRequestedSessionId(cookies[i].getValue());
                        setRequestedSessionCookie(true);
                        setRequestedSessionURL(false);
                        logger.info("requestedSessionCookie="+requestedSessionCookie+"requestedSessionId+"+requestedSessionId
                                +"requestedSessionURL="+requestedSessionURL);
                    }
                }
                addCookie(cookies[i]);
            }
            logger.info("cookie:"+cookies.toString());
        }
    }

    //处理请求参数为Map
    private void convertMap() {
        //1、分割字符串 &
        String[] keyValues = this.queryStr.split("&");
        for (String queryStr : keyValues) {
            //2、再次分割字符串  =
            String[] kv = queryStr.split("=");
            kv = Arrays.copyOf(kv, 2);
            //获取key和value
            String key = kv[0];
            String value = kv[1] == null ? null : decode(kv[1], "utf-8");
            //存储到map中
            if (!parameterMap.containsKey(key)) { //第一次
                parameterMap.put(key, new ArrayList<String>());
            }
            parameterMap.get(key).add(value);
        }
    }

    /**
     * 处理中文
     *
     * @return
     */
    private String decode(String value, String enc) {
        try {
            return java.net.URLDecoder.decode(value, enc);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过name获取对应的多个值
     *
     * @param key
     * @return
     */
    public String[] getParameterValues(String key) {
        List<String> values = this.parameterMap.get(key);
        if (null == values || values.size() < 1) {
            return null;
        }
        return values.toArray(new String[0]);
    }

    /**
     * 通过name获取对应的一个值
     *
     * @param key
     * @return
     */
    public String getParameter(String key) {
        String[] values = getParameterValues(key);
        return values == null ? null : values[0];
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getQueryStr() {
        return queryStr;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return false;
    }
    public void setRequestedSessionCookie(boolean flag) {
        this.requestedSessionCookie = flag;
    }

    public void setRequestedSessionId(String requestedSessionId) {
        this.requestedSessionId = requestedSessionId;
    }

    public void setRequestedSessionURL(boolean flag) {
        requestedSessionURL = flag;
    }

    public void addCookie(Cookie cookie) {
        synchronized (cookies) {
            cookies.add(cookie);
        }
    }
}
