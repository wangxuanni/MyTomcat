package v5.core.servlet;


import v5.core.request.Request;
import v5.core.response.Response;

public interface Servlet {
	void service(Request request, Response response);
}
