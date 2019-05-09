package v4nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    Request request;
    Response response;

    public static void main(String[] args) throws IOException {
        NioServer nioServer = new NioServer();
        nioServer.start();

    }

    void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        /*
        ServerSocket serverSocket = new ServerSocket(8080);*/
        System.out.println("tomcat服务器启动成功");
        while (true) {
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
                if (request!=null&&response!=null){
                    System.out.println("-------------------------servlet执行了！！！------");
                    Servlet servlet=new LoginServlet();
                    servlet.service(this.request,this.response);
                    this.response.pushToBrowser(200);

                    this.request.closeSocketChannel();

                }
                iterator.remove();
            }
        }
    }

    private void acceptHandler(ServerSocketChannel serverSocketChannel, Selector selector) throws IOException {
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);

        this.response = new Response(socketChannel);

    }

    private void readHandler(SelectionKey selectionKey, Selector selector) throws IOException {
        this.request =new Request(selectionKey);

    }
}