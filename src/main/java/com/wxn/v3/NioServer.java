package com.wxn.v3;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    private static final Logger logger = LoggerFactory.getLogger(NioServer.class);

    Request request;
    Response response;
    private boolean shutdown = false;
    private static final String SHUTDOWN_COMMAND = "/SHUTDWON";

    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        nioServer.start();

    }

    void start() {
        try {


            Selector selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(8080));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            logger.info("服务器启动成功");

            while (!shutdown) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator iterator = selectionKeys.iterator();

                while (iterator.hasNext()) {

                    SelectionKey selectionKey = (SelectionKey) iterator.next();

                    if (selectionKey.isAcceptable()) {
                        acceptHandler(serverSocketChannel, selector);

                    }

                    if (selectionKey.isReadable()) {
                        readHandler(selectionKey, selector);
                    }
                    if (request != null && response != null) {
                        System.out.println("request url:" + request.getUrl());
//request url:loginservlet
                        if (request.getUrl().endsWith("servlet")) {
                            Servlet servlet = new LoginServlet();
                            servlet.service(this.request, this.response);
                            response.pushToBrowser(200);
                            this.request.closeSocketChannel();
                        } else {
                            response.setRequest(request);
                            response.sendStaticResource(request.getUrl());
                            this.request.closeSocketChannel();


                        }


                    }

                    shutdown = request.getUrl().equals(SHUTDOWN_COMMAND);
                    iterator.remove();

                }
            }
            selector.close();
            serverSocketChannel.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void acceptHandler(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        this.response = new Response(socketChannel);

    }

    private void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        this.request = new Request(selectionKey);

    }
}