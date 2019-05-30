package v2;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Date;

public class    Response {
    SocketChannel socketChannel;
    private StringBuilder content;
    private StringBuilder headInfo;
    private int len; //正文的字节数

    private final String BLANK = " ";
    private final String CRLF = "\r\n";

    public Response(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        content =new StringBuilder();
        headInfo=new StringBuilder();
        len =0;

    }

    public void pushToBrowser(int code) throws IOException {
        if (null == headInfo) {
            code = 505;
        }
        createHeadInfo(code);
        socketChannel.write(Charset.forName("UTF-8").encode(headInfo.toString()));
        socketChannel.write(Charset.forName("UTF-8").encode(content.toString()));

    }

    //构建头信息
    private void createHeadInfo(int code) {
        //1、响应行: HTTP/1.1 200 OK
        headInfo.append("HTTP/1.1").append(BLANK);
        headInfo.append(code).append(BLANK);
        switch (code) {
            case 200:
                headInfo.append("OK").append(CRLF);
                break;
            case 404:
                headInfo.append("NOT FOUND").append(CRLF);
                break;
            case 505:
                headInfo.append("SERVER ERROR").append(CRLF);
                break;
        }
        //2、响应头(最后一行存在空行):
        headInfo.append("Date:").append(new Date()).append(CRLF);
        headInfo.append("Server:").append("shsxt?Server/0.0.1;charset=GBK").append(CRLF);
        headInfo.append("Content-type:text/html").append(CRLF);
        headInfo.append("Content-length:").append(len).append(CRLF);
        headInfo.append(CRLF);
    }

    public Response print(String info) {
        content.append(info);
        len += info.getBytes().length;
        return this;
    }

    public Response println(String info) {
        content.append(info).append(CRLF);
        len += (info + CRLF).getBytes().length;
        return this;
    }

}
