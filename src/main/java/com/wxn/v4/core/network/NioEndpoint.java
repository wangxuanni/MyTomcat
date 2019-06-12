package com.wxn.v4.core.network;


import com.wxn.v4.core.context.WebApp;
import com.wxn.v4.core.request.Request;
import com.wxn.v4.core.response.Response;
import com.wxn.v4.core.servlet.Servlet;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioEndpoint extends Endpoint {
    private static Logger logger = Logger.getLogger(NioEndpoint.class);

    Request request;
    Response response;

    void initSeverSocket(int port) throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(port));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        logger.info("服务器启动成功");

        while (true) {
            int readyChannels = selector.select();
            if (readyChannels == 0) {
                continue;
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator iterator = selectionKeys.iterator();

            while (iterator.hasNext()) {

                SelectionKey selectionKey = (SelectionKey) iterator.next();
                iterator.remove();

                if (selectionKey.isAcceptable()) {
                    acceptHandler(serverSocketChannel, selector);
                }

                if (selectionKey.isReadable()) {
                    readHandler(selectionKey, selector);

                }
            }


        }

    }

    @Override
    public void start(int port) {
        try {
            initSeverSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void close() {

    }

    private void acceptHandler(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        this.response = new Response(socketChannel);

    }

    private void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {

        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
        this.request = new Request(socketChannel);

        //如果是长连接，把socketChannel再注册为可读事件

        if (this.request.isKeepAlive()) {
            socketChannel.register(selector, SelectionKey.OP_READ);
        }


        if (request != null && response != null) {

            Servlet servlet = (Servlet) WebApp.getServletFromUrl(this.request.getUrl());
            if (null != servlet) {
                servlet.service(this.request, this.response);
                //关注了状态码
                response.pushToBrowser(200);
                this.request.closeSocketChannel();

            } else {
                //错误....
            }
        }


    }
}
