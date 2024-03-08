package cn.noy.javahw;

import cn.noy.javahw.web.WebServer;

public class Main {
    public static void main(String[] args) {
        WebServer webServer = new WebServer(8080);
        webServer.run();
    }
}