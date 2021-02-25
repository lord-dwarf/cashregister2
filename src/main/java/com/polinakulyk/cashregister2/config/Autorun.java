package com.polinakulyk.cashregister2.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Autorun implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
//        // initialize Log4j
//        ServletContext context = event.getServletContext();
//        String log4jConfigFile = context.getInitParameter("log4j-config-location");
//        String fullPath = context.getRealPath("") + File.separator + log4jConfigFile;
//
//        PropertyConfigurator.configure(fullPath);
    }
}
