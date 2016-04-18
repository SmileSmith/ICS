package com.ics.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeoutException;


public class ConnectPool {
	private String driver;
	private String url;
	private String username;
	private String password;
	
	private int maxActive;
	private int maxIdle;
    private long maxWait;
    
    private static int active = 0;
    private static int idle = 0;
    private static long wait = 0;
    
    private static Object key  = "";
    
	private static Set<Connection> set = new HashSet<Connection>();

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public long getMaxWait() {
		return maxWait;
	}

	public void setMaxWait(long maxWait) {
		this.maxWait = maxWait;
	}

	public static int getActive() {
		return active;
	}

	public static void setActive(int active) {
		ConnectPool.active = active;
	}

	public static int getIdle() {
		return idle;
	}

	public static void setIdle(int idle) {
		ConnectPool.idle = idle;
	}

	public static long getWait() {
		return wait;
	}

	public static void setWait(long wait) {
		ConnectPool.wait = wait;
	}

	public static Object getKey() {
		return key;
	}

	public static void setKey(Object key) {
		ConnectPool.key = key;
	}

	public static Set<Connection> getSet() {
		return set;
	}

	public static void setSet(Set<Connection> set) {
		ConnectPool.set = set;
	}
	
	
	public Connection getConnection() throws Exception{
		synchronized (key) {
			if(active <=  maxActive){
				//如果活动数等于最大活动数       执行等待
				if(active ==  maxActive){
					//设置等待时间
					try {
						key.wait(maxWait);
						throw new TimeoutException();
					} catch (TimeoutException e) {
						e.printStackTrace();
						return null;
					} catch (InterruptedException e){
						System.out.println("this exception");
						e.printStackTrace();
						return null;
					}
					
				}
				
				//如果连接池不为空，取出连接
				if(!set.isEmpty()){
					Iterator<Connection> its = set.iterator();
					Connection con = its.next();
					set.remove(con);
					active++;
					return con;
				}
				
			    //连接池为空，就创建新的连接
				ConnectManager cm = new ConnectManager();
				Class.forName(driver);
				Connection con = DriverManager.getConnection(url, username, password);
				active++;
				return (Connection) cm.bind(con, maxIdle);
			}
			return null;
		}
	}
	
}