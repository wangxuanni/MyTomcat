package basic.three.com.shsxt.server;

public class OthersServlet implements Servlet {

	@Override
	public void service(Request request, Response response) {
		response.print("其他测试页面"); 
	}

}
