package v1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/*版本一：从C/S到B/S——收到请求
* 启动服务器
* 打开浏览器http://localhost:8080对应端口
*控制台接到http请求，并打印
* */
public class TomcatServerV1 {
    private static ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8080);
        System.out.println("tomcat服务器启动成功");
        while (!serverSocket.isClosed()) {
            Socket request = serverSocket.accept();
            threadPool.execute(() -> {
                try {
                    InputStream inputStream = request.getInputStream();
                    System.out.println("收到请求");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String msg = null;
                    while ((msg = reader.readLine()) != null) {
                        if (msg.length() == 0) {
                            break;
                        }
                        System.out.println(msg);
                    }
                    System.out.println("----------------end");
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        request.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
