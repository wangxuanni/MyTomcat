package v3;

import basic.two.com.shsxt.server.Response;
import basic.two.com.shsxt.server.Server03;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
/*
* �汾��:��è
* ���з�װ��request�������󲢴�ӡ
* response���ݴ����״̬���װ�̶���ͷ��Ϣ��������Ӧ��Ϣ
* serverֻ�������ݺ�״̬��
*
* */
public class Server {
    private ServerSocket serverSocket ;
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
    //�������񣬵���receive����
    public void start() {
        try {
            serverSocket =  new ServerSocket(8080);
            receive();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("����������ʧ��....");
        }
    }
    //�������Ӵ���
    public void receive() {
        try {
            Socket client = serverSocket.accept();
            System.out.println("һ���ͻ��˽���������....");
            //��ȡ����Э��
            InputStream is =client.getInputStream();
            byte[] datas = new byte[1024*1024];
            int len = is.read(datas);
            String requestInfo = new String(datas,0,len);
            System.out.println(requestInfo);

            basic.two.com.shsxt.server.Response response =new Response(client);
            //��ע������
            response.print("<html>");
            response.print("<head>");
            response.print("<title>");
            response.print("��������Ӧ�ɹ�");
            response.print("</title>");
            response.print("</head>");
            response.print("<body>");
            response.print("Hello kitten");
            response.print("</body>");
            response.print("</html>");
            //��ע��״̬��
            response.pushToBrowser(200);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("�ͻ��˴���");
        }
    }
    //ֹͣ����
    public void stop() {

    }
}
