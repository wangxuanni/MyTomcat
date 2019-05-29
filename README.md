# MyTomcat
![进度](http://progressed.io/bar/70?title=done)


迭代手写一个自己的tomcat

 版本一：收到请求
 
 启动服务器
 打开浏览器http://localhost:8080
 
 控制台接到http请求，并打印




  版本二：收到请求并返回响应
  
  启动服务器
  打开浏览器http://localhost:8080。
  控制台接到http请求，并打印
  封装输出流，返回http响应，浏览器打印"Hello  MyTomcat"


 
 
  版本三：封装
  
  进行封装，request接受请求并打印。response根据传入的状态码封装固定的头信息、推送响应信息。server只关心内容和状态码
  
  
  版本四：升级为nio
 
  版本五：servlet容器
  解析web.xml，返回url对应的servlet。启动项目打开浏览器http://localhost:8080/login
