package com.wxn.v4.core.request;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.*;

public class Request {
    //Э����Ϣ
    private String requestInfo;
    //����ʽ
    private String method;
    //����url
    private String url;
    //�������
    private String queryStr;
    //�洢����
    private Map<String, List<String>> parameterMap;
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
        System.out.println(requestInfo);
        parseRequestInfo();
        byteBuffer.clear();

    }

    public void closeSocketChannel() throws IOException {
        this.socketChannel.close();

    }

    private void parseRequestInfo() {

                //1����ȡ����ʽ: ��ͷ����һ��/
        this.method = this.requestInfo.substring(0,
                this.requestInfo.indexOf("/")).toLowerCase();
        this.method = this.method.trim();
        //2����ȡ����url: ��һ��/ �� HTTP/
        //���ܰ����������? ǰ���Ϊurl
        //1)����ȡ/��λ��
        int startIdx = this.requestInfo.indexOf("/") + 1;
        //2)����ȡ HTTP/��λ��
        int endIdx = this.requestInfo.indexOf("HTTP/");
        //3)���ָ��ַ���
        this.url = this.requestInfo.substring(startIdx, endIdx).trim();
        //4)����ȡ����λ��
        int queryIdx = this.url.indexOf("?");
        if (queryIdx >= 0) {//��ʾ�����������
            String[] urlArray = this.url.split("\\?");
            this.url = urlArray[0];
            queryStr = urlArray[1];
        }
        System.out.println(this.url);

        //3����ȡ�������:���Get�Ѿ���ȡ,�����post��������������

        if (method.equals("post")) {
            String qStr = this.requestInfo.substring(this.requestInfo.lastIndexOf(CRLF)).trim();
            System.out.println(qStr + "-->");
            if (null == queryStr) {
                queryStr = qStr;
            } else {
                queryStr += "&" + qStr;
            }
        }
        queryStr = null == queryStr ? "" : queryStr;
        System.out.println(method + "-->" + url + "-->" + queryStr);
        //ת��Map fav=1&fav=2&uname=shsxt&age=18&others=
        convertMap();
    }

    //�����������ΪMap
    private void convertMap() {
        //1���ָ��ַ��� &
        String[] keyValues = this.queryStr.split("&");
        for (String queryStr : keyValues) {
            //2���ٴηָ��ַ���  =
            String[] kv = queryStr.split("=");
            kv = Arrays.copyOf(kv, 2);
            //��ȡkey��value
            String key = kv[0];
            String value = kv[1] == null ? null : decode(kv[1], "utf-8");
            //�洢��map��
            if (!parameterMap.containsKey(key)) { //��һ��
                parameterMap.put(key, new ArrayList<String>());
            }
            parameterMap.get(key).add(value);
        }
    }

    /**
     * ��������
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
     * ͨ��name��ȡ��Ӧ�Ķ��ֵ
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
     * ͨ��name��ȡ��Ӧ��һ��ֵ
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
}
