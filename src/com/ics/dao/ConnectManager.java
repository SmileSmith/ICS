package com.ics.dao;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.Set;

public class ConnectManager implements InvocationHandler{
    private Object conn;
    private int maxIdle;
    private Set<Connection> set =  ConnectPool.getSet();
    private Object key = ConnectPool.getKey();
    
    public ConnectManager(){
    	super();
    }
	
    /*
     * 绑定方法
     */
    public Connection bind(Connection conn,int maxIdle){
    	this.conn = conn;
    	this.maxIdle = maxIdle;
    	return (Connection)Proxy.newProxyInstance(conn.getClass().getClassLoader(),
    			new Class[] {Connection.class}, this);
    }
	
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		//当Close时，查看连接池数量 
		if(method.getName().equalsIgnoreCase("close")){
			Object result = null; 
			synchronized (key) {
				if(set.size() < maxIdle){
					//如果Set的SIZE小于最大可保存数，则add到集合，否则执行close方法关闭连接
					set.add((Connection) proxy);
				} else {
					if(conn != null){
						result = method.invoke(conn, args);
					}
				}
				
				//执行完毕，释放连接池
				int active = ConnectPool.getActive();
				active--;
				ConnectPool.setActive(active);
				key.notifyAll();
				return result;
			}
		} else {
			Object result = method.invoke(conn, args);
			return result;
		}
		
	}

}
