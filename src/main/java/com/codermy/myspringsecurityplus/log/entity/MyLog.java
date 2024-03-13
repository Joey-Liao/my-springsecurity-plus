package com.codermy.myspringsecurityplus.log.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.io.Serializable;
import java.util.Date;

/**
 *  * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "mylog")
public class MyLog implements Serializable {
    @Id
    @Field(store = true, type = FieldType.Keyword)
    private String logId;

    /** 操作用户 */
    @Field(store = true, type = FieldType.Keyword)
    private String userName;

    /** 描述 */
    @Field(store = true, type = FieldType.Text, analyzer = "standard")
    private String description;

    /** 方法名 */
    @Field(store = true, type = FieldType.Keyword)
    private String method;

    /** 请求ip */
    @Field(store = true, type = FieldType.Keyword)
    private String ip;

    /** 异常详细  */
    @Field(store = true, type = FieldType.Text, analyzer = "standard")
    private String exceptionDetail;

    /** 异常类型 */
    @Field(store = true, type = FieldType.Keyword,name = "type1")
    private String type;

    // /** 地址 */
    // private String ipAddress;

    /** 参数*/
    @Field(store = true, type = FieldType.Keyword)
    private String params;

    /** 浏览器  */
    @Field(store = true, type = FieldType.Keyword)
    private String browser;

    /** 请求耗时 */
    @Field(store = true, type = FieldType.Long)
    private Long time;

    /** 创建日期 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    @Field(index = false, store = true, type = FieldType.Date, format = DateFormat.custom,pattern = "yyyy-MM-dd HH:mm:ss:SSS")
    private Date createTime;

    public MyLog( String type,Long time) {
        this.type = type;
        this.time = time;
    }

}
