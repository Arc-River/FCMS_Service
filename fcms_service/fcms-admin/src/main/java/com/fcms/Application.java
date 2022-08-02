package com.fcms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 启动程序
 * 
 * @author 
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class Application
{
    static Logger logger= LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) throws UnknownHostException {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        ConfigurableApplicationContext application = SpringApplication.run(Application.class, args);

        Environment env = application.getEnvironment();
        String host = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String name = env.getProperty("fcms.name");
        String path = env.getProperty("server.servlet.context-path");

        logger.warn("\n--------------------------------------------------------------\n\t" +
                        "Application '{}' is running!\n\t" +
                        "Local: \t\thttp://localhost:{}{}\n\t" +
                        "External: \thttp://{}:{}{}\n\t"+
                        "Document: \thttp://{}:{}{}/doc.html"+
                        "\n--------------------------------------------------------------\n\t",
                name,
                port, path,
                host, port, path,
                host, port, path);
    }
}
