package v5.user;


import v5.core.servlet.Servlet;
import v5.core.request.Request;
import v5.core.response.Response;

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
