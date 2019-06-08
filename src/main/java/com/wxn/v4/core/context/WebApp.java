package com.wxn.v4.core.context;


import com.wxn.v3.Servlet;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * 用SAX解析
 * 通过url反射获取配置文件对应的servlet
 */
public class WebApp {
	private static WebContext webContext ;
	static {
		try {
			//SAX解析
			//1、获取解析工厂
			SAXParserFactory factory=SAXParserFactory.newInstance();
			//2、从解析工厂获取解析器
			SAXParser parse =factory.newSAXParser();
			//3、编写处理器
			//4、加载文档?Document?注册处理器
			WebHandler handler=new WebHandler();
			//5、解析直接从web.xml导文件
			parse.parse(Thread.currentThread().getContextClassLoader()
			.getResourceAsStream("v4/web.xml")
			,handler);
			//获取数据
			webContext = new WebContext(handler.getEntitys(),handler.getMappings());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * 通过url反射获取配置文件对应的servlet
	 */
	public static Servlet getServletFromUrl(String url) {

		String className = webContext.getClz("/"+url);
		System.out.println("classname:"+className);
		Class clz;
		try {
			System.out.println(url+"-->"+className+"-->");
			clz = Class.forName(className);
			Servlet servlet =(Servlet)clz.getConstructor().newInstance();
			return servlet;
		} catch (Exception e) {
			
		}
		
		return null;
		
	}
}
