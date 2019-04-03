package com.hogae.generator.doc;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hoage.helper.ExcelUtil;
import com.hoage.helper.FileUtils;
import com.hoage.helper.JDBCHelper;
import com.sun.xml.internal.ws.api.ha.StickyFeature;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.internal.FileHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Spliterators;

import static sun.java2d.cmm.ColorTransform.Out;

public class GeneratorExcel implements GeneratorHandler {

    public static final Logger logger = LoggerFactory.getLogger(GeneratorExcel.class);

    @Override
    public void generator(){

        HSSFWorkbook wb = null;
        String fileName = "绿点数据表结构文档.xls";

        List<Map<String ,String>> tables = JDBCHelper.query("select TABLE_NAME,TABLE_COMMENT  from information_schema.tables where TABLE_SCHEMA=?;","cimslvd");

        String[] indexTitle = { "序号", "数据表名称", "描述"};
        // sheet名
        String sheetName = "索引";
        String[][] content = new String[tables.size()][indexTitle.length];

        for (int i = 0; i <tables.size() ; i++) {
            Map<String,String> map = tables.get(i);
            content[i][0] = String.valueOf(i+1);
            content[i][1] = map.get("TABLE_NAME");
            content[i][2] = map.get("TABLE_COMMENT");
        }

        wb = ExcelUtil.getHSSFWorkbookHyperlink(sheetName, indexTitle, content, null);
        // excel标题
        String[] sheetTitle = { "序号", "字段名", "类型"
                , "是否为空", "描述"
                , "备注"};
        for (int i = 0; i <tables.size() ; i++) {
            String tableName = tables.get(i).get("TABLE_NAME");
            List<Map<String,Object>> columnList = JDBCHelper.query(" select table_name , column_name ,  column_type , column_key , extra , is_nullable " +
                    ",column_comment from information_schema.columns where table_schema=? and TABLE_NAME = ?;","cimslvd",tableName);
            String[][] columnContent = new String[columnList.size()][sheetTitle.length];

            for (int j = 0; j <columnList.size() ; j++) {
                Map<String ,Object> map = columnList.get(j);
                columnContent[j][0] = String.valueOf(j+1);
                columnContent[j][1] = String.valueOf(map.get("COLUMN_NAME"));
                columnContent[j][2] = String.valueOf(map.get("COLUMN_TYPE"));
                columnContent[j][3] = String.valueOf(map.get("IS_NULLABLE"));
                columnContent[j][4] = String.valueOf(map.get("COLUMN_COMMENT"));
                columnContent[j][5] = String.valueOf("");
            }
            wb = ExcelUtil.getHSSFWorkbook(tableName, sheetTitle, columnContent, wb);
        }
        fileName=System.getProperty("user.dir")+ "\\"+fileName;
        logger.debug(fileName);
        FileUtils.write(fileName,wb);

    }
}
