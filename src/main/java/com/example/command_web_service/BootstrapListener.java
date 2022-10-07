package com.example.command_web_service;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.xml.ws.Endpoint;

@WebListener
public class BootstrapListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        new Thread(() -> {
            //для localhost
//                Endpoint.publish("http://localhost:8082/wss/first", new CommandWsImpl());
            //для облачного сервера
            Endpoint.publish("http://localhost:8081/ws/service-command", new CommandWsImpl());
        }).start();
    }
}
