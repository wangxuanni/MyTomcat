package v2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {


    public static void main(String[] args) throws IOException {

        NioServer nioServer = new NioServer();
        nioServer.start();

    }

    void start() throws IOException {
        Selector selector = Selector.open();
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(7777));
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
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector, SelectionKey.OP_READ);

                    Response response = new Response(socketChannel);

                    response.print("<html>");
                    response.print("<head>");
                    response.print("<title>");
                    response.print("服务器响应成功");
                    response.print("</title>");
                    response.print("</head>");
                    response.print("<body>");
                    response.print("来而不往非礼也");
                    response.print("</body>");
                    response.print("</html>");

                    response.pushToBrowser(200);

                }

                if (selectionKey.isReadable()) {
                    Request request = new Request(selectionKey);
                }
                iterator.remove();
            }

        }
    }


}