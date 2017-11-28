package com.dailintong;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 
 * <p>
 * Title: 以jdbc方式获取数据库连接类
 * </p>
 * 
 * <p>
 * Description: 以jdbc方式获取数据库连接
 * </p>
 *  
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * 
 * <p>
 * Company:
 * </p>
 * 
 * @author cjp
 * @version 1.0
 */
public class JDBCConnection  {
	/**
	 * Connection类的实例
	 */
	// private Connection conn = null;


	private static ComboPooledDataSource dbp = null;

	public JDBCConnection() {
		initDataSource();
	}

	private void initDataSource() {

		if (dbp != null) {
			return;
		}
		try {
			dbp = new ComboPooledDataSource();
			dbp.setDriverClass(ConfigManager.getProperty(Environment.DB_DRIVER));
			dbp.setJdbcUrl(ConfigManager.getProperty(Environment.DB_URL));
			dbp.setUser(ConfigManager.getProperty(Environment.DB_USER));
			dbp.setPassword(ConfigManager.getProperty(Environment.DB_PSW));
			dbp.setInitialPoolSize(Integer.parseInt(ConfigManager
					.getProperty(Environment.DB_INITIAL_SIZE)));
			dbp
					.setMaxPoolSize(Integer.parseInt(ConfigManager
							.getProperty(Environment.DB_MAX_SIZE)));
			dbp
					.setMinPoolSize(Integer.parseInt(ConfigManager
							.getProperty(Environment.DB_MIN_SIZE)));
			dbp.setMaxIdleTime(Integer.parseInt(ConfigManager
					.getProperty(Environment.DB_MAX_IDLE_TIME)));
			dbp.setMaxStatements(Integer.parseInt(ConfigManager
					.getProperty(Environment.DB_MAX_STMT)));

		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取连接
	 * 
	 * @return Connection 据库连接类Connection的实例
	 */
	private Connection getJDBCConnection() {
		Connection conn = null;
		try {
			if (dbp == null) {
				synchronized (new byte[0]) {
					initDataSource();
				}
			}
			conn = dbp.getConnection();
			while (conn.isClosed()) {
				conn = dbp.getConnection();
			}

		} catch (SQLException ex) {
			throw new RuntimeException("未能通过JDBC方式获取数据库连接: " + ex);
		}

		return conn;
	}
	
	 /**
     * 获取连接
     * @return Connection 数据库连接类Connection的实例
     */
	public static JDBCConnection provider=null;
	private static Object objLock = new Object();
    public static Connection getConnection() {
        if (null == provider) {
            synchronized (objLock) {
                if (null == provider) {
                        provider = new JDBCConnection();
                }
            }
        }
        Connection conn = provider.getJDBCConnection();
        if (null == conn) {
            System.out.println("不能获取数据库连接！");
            throw new RuntimeException("不能获取数据库连接！");
        }
        return conn;
    }
}
