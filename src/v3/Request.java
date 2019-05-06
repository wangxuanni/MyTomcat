package v3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Request {
    Socket serverSocket;

    public Request(InputStream inputStream) throws IOException {

    }

    public Request(Socket request) throws IOException {


        try {
            InputStream inputStream = request.getInputStream();
            System.out.println(" ’µΩ«Î«Û");
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
        }
    }
}




