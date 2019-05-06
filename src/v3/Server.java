package v3;

import basic.two.com.shsxt.server.Response;
import basic.two.com.shsxt.server.Server03;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
/*
* 版本三:幼猫
* 进行封装，request接受请求并打印
* response根据传入的状态码封装固定的头信息、推送响应信息
* server只关心内容和状态码
*
* */
public class Server {
    private ServerSocket serverSocket ;
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
    //启动服务，调用receive方法
    public void start() {
        try {
            serverSocket =  new ServerSocket(8080);
            receive();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("服务器启动失败....");
        }
    }
    //接受连接处理
    public void receive() {
        try {
            Socket client = serverSocket.accept();
            System.out.println("一个客户端建立了连接....");
            //获取请求协议
            InputStream is =client.getInputStream();
            byte[] datas = new byte[1024*1024];
            int len = is.read(datas);
            String requestInfo = new String(datas,0,len);
            System.out.println(requestInfo);

            basic.two.com.shsxt.server.Response response =new Response(client);
            //关注了内容
            response.print("<html>");
            response.print("<head>");
            response.print("<title>");
            response.print("服务器响应成功");
            response.print("</title>");
            response.print("</head>");
            response.print("<body>");
            response.print("Hello kitten");
            response.print("</body>");
            response.print("</html>");
            //关注了状态码
            response.pushToBrowser(200);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("客户端错误");
        }
    }
    //停止服务
    public void stop() {

    }
}
