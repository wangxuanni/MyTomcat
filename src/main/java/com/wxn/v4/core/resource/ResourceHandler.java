package com.wxn.v4.core.resource;




import com.sun.corba.se.impl.presentation.rmi.ExceptionHandler;
import com.wxn.v3.Request;
import com.wxn.v3.Response;

import java.io.IOException;


public class ResourceHandler {
    private ExceptionHandler exceptionHandler;

    public ResourceHandler(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
    }

    public void handle(Request request, Response response) {
        String url = request.getUrl();
        try {
            if (ResourceHandler.class.getResource(url) == null) {
                System.out.println("找不到该资源:{}"+url);
            }
            byte[] body = IOUtil.getBytesFromFile(url);

        } catch (IOException e) {
        }
    }
}
