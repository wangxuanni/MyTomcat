package com.wxn.v4.user;


import com.wxn.v4.core.request.Request;
import com.wxn.v4.core.response.Response;
import com.wxn.v4.core.servlet.Servlet;

public class LoginServlet implements Servlet {
	@Override
	public void  service(Request request, Response response) {
		response.print("<html>"); 
		response.print("<head>"); 
		response.print("<meta http-equiv=\"content-type\" content=\"text/html;charset=utf-8\">" ); 
		response.print("<title>");
		response.print("第一个servlet");
		response.print("</title>");
		response.print("</head>");
		response.print("<body>");
		response.print("欢迎来到测试页面");
		response.print("</body>");
		response.print("</html>");
	}

}
