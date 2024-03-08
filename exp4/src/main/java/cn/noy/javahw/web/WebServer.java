package cn.noy.javahw.web;

import cn.noy.expr.structure.ExpressionParser;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * WebServer<br>
 * 一个简单的Web服务器，可以处理${var}和<%expr%>
 */
public class WebServer implements Runnable {

    private final int port;

    public WebServer(int port){
        this.port = port;
    }

    @Override
    public void run() {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("WebServer started at port "+port);
            while (true) {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(1);
                System.out.println("Client connected from " + socket.getInetAddress() + ":" + socket.getPort());
                SocketHandler handler = new SocketHandler(socket);
                Thread thread = new Thread(handler);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * SocketHandler<br>
     * 处理Socket请求的线程<br>
     * 一个SocketHandler只处理一个请求
     */
    public static class SocketHandler implements Runnable{
        private final Socket socket;

        public SocketHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String requestLine;
                try{
                    requestLine = br.readLine();
                }catch (SocketTimeoutException ignored){
                    socket.close();
                    return;
                }
                System.out.println("requestLine: " + requestLine);
                if (requestLine != null) {
                    String[] parts = requestLine.split(" ");
                    // 获取请求方法，一般为GET或POST
                    String method = parts[0];
                    // 获取请求资源，一般为一个文件名或一个路径
                    String resource = parts[1];
                    // 如果请求资源以/开头，表示是一个绝对路径，需要去掉第一个/
                    if (resource.startsWith("/")) {
                        resource = resource.substring(1);
                    }
                    Map<String, String> data = new HashMap<>();
                    if (resource.contains("?")){
                        int index = resource.indexOf("?");
                        String[] params = resource.substring(index+1).split("&");
                        resource = resource.substring(0, index);
                        for (String param : params) {
                            String[] kv = param.split("=");
                            data.put(kv[0], URLDecoder.decode(kv[1], StandardCharsets.UTF_8));
                        }
                    }

                    //获取resource中对应的文件
                    URL url = WebServer.class.getClassLoader().getResource(resource);
                    if (url!=null) {
                        URLConnection connection = url.openConnection();

                        InputStream in = url.openStream();
                        String s = new String(in.readAllBytes(), StandardCharsets.UTF_8);
                        if(resource.endsWith(".zup")){
                            //替换变量
                            for (Map.Entry<String, String> entry : data.entrySet()) {
                                s = s.replace("${"+entry.getKey()+"}", entry.getValue());
                                System.out.println(entry.getKey()+" : "+entry.getValue());
                            }
                            s = s.replaceAll("\\$\\{.+?\\}", "");
                            //替换表达式
                            Pattern pattern = Pattern.compile("<%(.*?\\r*?\\n*?)*?%>");
                            Matcher matcher = pattern.matcher(s);
                            StringBuilder sb2 = new StringBuilder();
                            while(matcher.find()){
                                String expr = matcher.group();
                                expr = expr.substring(2,expr.length()-2);
                                String value;
                                try{
                                    value = ExpressionParser.createDefault()
                                            .parse(Arrays.stream(expr.split("\n"))
                                                    .map(s1 -> s1.replaceAll("\r",""))
                                                    .toList())
                                            .getValue()
                                            .getStringValue();
                                }catch (Exception e){
                                    value = "NaN";
                                }
                                System.out.println("value: " + value);
                                matcher.appendReplacement(sb2, value);
                            }
                            matcher.appendTail(sb2);
                            s = sb2.toString();
                        }
                        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);

                        // 如果文件存在且可读，表示请求成功，返回200状态码和OK状态描述
                        String responseLine = "HTTP/1.1 200 OK\r\n";
                        os.write(responseLine.getBytes());
                        String contentType = "Content-Type: " + connection.getContentType() + "\r\n";
                        os.write(contentType.getBytes());
                        String contentLength = "Content-Length: " + bytes.length + "\r\n";
                        os.write(contentLength.getBytes());
                        // 设置一个空行，表示响应头结束
                        os.write("\r\n".getBytes());
                        // 打印响应正文
                        os.write(bytes);
                        in.close();
                    } else {
                        // 如果文件不存在或不可读，表示请求失败，返回404状态码和Not Found状态描述
                        System.out.println("resource not found: " + resource);
                        String responseLine = "HTTP/1.1 404 Not Found\r\n";
                        os.write(responseLine.getBytes());
                        String contentType = "Content-Type: text/html\r\n";
                        os.write(contentType.getBytes());
                        os.write("\r\n".getBytes());
                        String content = "<html><head><title>404 Not Found</title></head><body><h1>404 Not Found</h1><p>The requested resource " + resource + " was not found on this server.</p></body></html>";
                        os.write(content.getBytes());
                    }
                }
                is.close();
                os.close();
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
