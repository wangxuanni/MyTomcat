package com.wxn.v1;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BioServer {
    private static final Logger logger = LoggerFactory.getLogger(BioServer.class);

    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        logger.info("服务器启动成功");

        while (!serverSocket.isClosed()) {
            Socket socket = serverSocket.accept();
            threadPool.execute(() -> {
                try {
                    InputStream inputStream = socket.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String msg = null;
                    while ((msg = reader.readLine()) != null) {
                        if (msg.length() == 0) {
                            break;
                        }
                        System.out.println(msg);
                    }

                    System.out.println("----------------end");

                    StringBuilder content =new StringBuilder();
                    content.append("<html>");
                    content.append("<head>");
                    content.append("<title>");
                    content.append("????????????");
                    content.append("</title>");
                    content.append("</head>");
                    content.append("<body>");
                    content.append("?????????????");
                    content.append("</body>");
                    content.append("</html>");
                    int size = content.toString().getBytes().length; //????????????

                    StringBuilder responseInfo =new StringBuilder();
                    String blank =" ";
                    String CRLF = "\r\n";
                    responseInfo.append("HTTP/1.1").append(blank);
                    responseInfo.append(200).append(blank);
                    responseInfo.append("OK").append(CRLF);
                    responseInfo.append("Date:").append(new Date()).append(CRLF);
                    responseInfo.append("Server:").append("wxn?Server/0.0.1;charset=GBK").append(CRLF);
                    responseInfo.append("Content-type:text/html").append(CRLF);
                    responseInfo.append("Content-length:").append(size).append(CRLF);
                    responseInfo.append(CRLF);

                    responseInfo.append(content.toString());

                    //д?????????
                    BufferedWriter bw =new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    bw.write(responseInfo.toString());
                    bw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
