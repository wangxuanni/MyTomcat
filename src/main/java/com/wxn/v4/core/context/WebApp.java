package com.wxn.v4.core.context;



import com.wxn.v4.core.servlet.Servlet;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * ��SAX����
 * ͨ��url�����ȡ�����ļ���Ӧ��servlet
 */
public class WebApp {
	private static WebContext webContext ;
	static {
		try {
			//SAX����
			//1����ȡ��������
			SAXParserFactory factory=SAXParserFactory.newInstance();
			//2���ӽ���������ȡ������
			SAXParser parse =factory.newSAXParser();
			//3����д������
			//4�������ĵ�?Document?ע�ᴦ����
			WebHandler handler=new WebHandler();
			//5������ֱ�Ӵ�web.xml���ļ�
			parse.parse(Thread.currentThread().getContextClassLoader()
			.getResourceAsStream("com/wxn/v4/web.xml")
			,handler);
			//��ȡ����
			webContext = new WebContext(handler.getEntitys(),handler.getMappings());
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	/**
	 * ͨ��url�����ȡ�����ļ���Ӧ��servlet
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
