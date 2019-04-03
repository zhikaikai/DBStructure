package com.hoage.helper.db;


import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


public class DBProperties {

    public static final org.slf4j.Logger logger = LoggerFactory.getLogger(DBProperties.class);

    public static final String JDBC_FILE_NAME = "jdbc.properties";

    public static Properties props = null;

    static {
        logger.debug("开始加载jdbc.properties");
        props = new Properties();
        ClassLoader  loader  =  Thread.currentThread().getContextClassLoader();
        InputStream  input  =  loader.getResourceAsStream(JDBC_FILE_NAME);
        InputStream  in = new BufferedInputStream(input);
        try {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            logger.debug("加载完毕jdbc.properties");
        }
    }
    public static String getValue(String key) {
        return props.getProperty(key);
	}



}
