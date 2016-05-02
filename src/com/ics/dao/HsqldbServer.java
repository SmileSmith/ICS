package com.ics.dao;
import org.hsqldb.Server;

/**
 * 启动Hsqldb数据库
 */
public class HsqldbServer {  /*implements ServletContextListener*/
    public static HsqldbServer hsqldbServer = new HsqldbServer();  
    private  HsqldbServer(){  
    }  
    public void startServer() {  
        Server  server = new Server();  
        server.setDatabaseName(0, "mydb");  
        server.setDatabasePath(0, "./db/mydb");  
        //server.setPort(9002);
        server.setSilent(true);
        server.start();         //自动多线程运行
        System.out.println("hsqldb started...");
    }  
      
    public static HsqldbServer getInstance(){  
        return hsqldbServer;  
    }

}  