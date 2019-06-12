package com.wxn.v4.core.servlet;


import com.wxn.v4.core.request.Request;
import com.wxn.v4.core.response.Response;

public interface Servlet {
	void service(Request request, Response response);
}
