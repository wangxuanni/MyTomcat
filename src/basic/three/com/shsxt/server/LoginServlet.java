package basic.three.com.shsxt.server;

public class LoginServlet implements Servlet {
	@Override
	public void  service(Request request, Response response) {
		response.print("<html>"); 
		response.print("<head>"); 
		response.print("<title>");
		response.print("第一个servlet");
		response.print("</title>");
		response.print("</head>");
		response.print("<body>");
		response.print("欢迎回来:"+request.getParameter("uname"));
		response.print("</body>");
		response.print("</html>");
	}

}
