package com.hoage.helper.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.LoggerFactory;

public class DBConnection {
    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(DBConnection.class);
	// 数据库连接字符串
	private static String url;
	// 数据库用户名
	private static String user;
	// 数据库口令
	private static String password;

	static {
        String DriverName = null;
        try {
            logger.debug("开始加载数据库驱动");
            DriverName = DBProperties.getValue("driverClassName");
            Class.forName(DriverName);
            url = DBProperties.getValue("url");
            user = DBProperties.getValue("username");
            password = DBProperties.getValue("password");
            logger.debug("url:"+url);
            logger.debug("user:"+user);
            logger.debug("password:"+password);
            logger.debug("加载数据库驱动完毕");
        } catch (ClassNotFoundException e) {
            logger.error("无法加载驱动类:"+DriverName);
            e.printStackTrace();
        }

	}

	public static Connection getConnection(){
        logger.debug("开始获取数据库连接");
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);

        } catch (SQLException e) {
            logger.error("获取数据库连接失败");
            e.printStackTrace();
        }
        logger.debug("获取数据库连接完成");
        return conn;
    }

}
