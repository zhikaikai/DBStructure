package com.hoage.helper;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hoage.helper.db.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;


public class JDBCHelper {

    public static final Logger logger = LoggerFactory.getLogger(JDBCHelper.class);


	public static List query(String sql,Object... args) {
        logger.debug("开始查询");
        logger.debug("sql:"+sql);
        logger.debug("args:"+args.toString());
		PreparedStatement ps = null;
		ResultSet rs = null;
		List result = new ArrayList();
        Connection conn = DBConnection.getConnection();
		try {
            ps = conn.prepareStatement(sql);

            for (int i=0;i<args.length;i++) {
                ps.setObject(i+1, args[i]);
            }
            rs = ps.executeQuery();
            ResultSetMetaData rem = rs.getMetaData();
            while (rs.next()) {
                Map<String,String> map = new LinkedHashMap<String, String>();
                for (int i = 1; i <= rem.getColumnCount(); i++) {
                    String key = rem.getColumnName(i);
                    String value = rs.getString(key);//不做过多处理了，全部转string
                    map.put(key,value);
                }
                result.add(map);
            }
            logger.debug("result:");
            logger.debug(JSON.toJSONString(result, SerializerFeature.PrettyFormat));
		}catch (SQLException e) {
            logger.error("查询失败");
            e.printStackTrace();
        } finally {
            free(rs, ps);
            if (conn!=null){
                try {
                    conn.close();
                } catch (SQLException e) {
                    logger.error("关闭数据库连接失败");
                    e.printStackTrace();
                }
            }
            logger.debug("查询完毕");
		}
		return result;
	}


	// 释放相关资源对象
	private static void free(ResultSet rs, Statement smt){
		if (rs!=null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error("关闭ResultSet失败");
                e.printStackTrace();

            }
        }
		
		if (smt!=null) {
            try {
                smt.close();
            } catch (SQLException e) {
                logger.error("关闭Statement失败");
                e.printStackTrace();
            }
        }
	}

}

