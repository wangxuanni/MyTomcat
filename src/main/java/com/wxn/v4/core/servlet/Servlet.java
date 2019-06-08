package com.wxn.v4.core.servlet;


import com.wxn.v3.Request;
import com.wxn.v3.Response;

public interface Servlet {
	void service(Request request, Response response);
}
