package com.wxn.v4.user;


import com.wxn.v3.Request;
import com.wxn.v3.Response;
import com.wxn.v3.Servlet;

public class LoginServlet implements Servlet {
	@Override
	public void  service(Request request, Response response) {
		response.print("<html>"); 
		response.print("<head>"); 
		response.print("<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">" ); 
		response.print("<title>");
		response.print("��һ��servlet");
		response.print("</title>");
		response.print("</head>");
		response.print("<body>");
		response.print("��ӭ��������ҳ��");
		response.print("</body>");
		response.print("</html>");
	}

}
