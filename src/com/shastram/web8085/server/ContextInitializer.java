package com.shastram.web8085.server;

import java.util.logging.Logger;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.googlecode.objectify.ObjectifyService;
import com.shastram.web8085.server.db.ServerFileData;

public class ContextInitializer implements ServletContextListener {
    private static Logger logger = Logger.getLogger(ContextInitializer.class.getName());
    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        logger.info("Registering with Objectify service");
        ObjectifyService.register(ServerFileData.class);
    }

}
