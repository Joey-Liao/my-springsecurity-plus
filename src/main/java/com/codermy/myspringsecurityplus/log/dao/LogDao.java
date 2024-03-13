package com.codermy.myspringsecurityplus.log.dao;

import com.codermy.myspringsecurityplus.log.entity.MyLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 *  *
 */
@Mapper
public interface LogDao extends ElasticsearchRepository<MyLog,String> {


//    @Insert("insert into my_log(user_name,ip,description,params,type,exception_detail,browser,method,time,create_time)values(#{userName},#{ip},#{description},#{params},#{type},#{exceptionDetail},#{browser},#{method},#{time},now())")
//    void save(MyLog log);
//

    List<MyLog> findByUserNameAndType(String userName,String type);

    List<MyLog> findByUserName(String userName);

    List<MyLog> findByType(String type);
//    /**
//     * 分页返回所有用户日志
//     * @param logQuery 查询条件
//     * @return
//     */
//    List<LogDto> getFuzzyLogByPage(LogQuery logQuery);
//
//
    void deleteAllByType(String type);





}
