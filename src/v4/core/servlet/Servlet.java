package v4.core.servlet;


import v4.core.request.Request;
import v4.core.response.Response;

public interface Servlet {
	void service(Request request, Response response);
}
