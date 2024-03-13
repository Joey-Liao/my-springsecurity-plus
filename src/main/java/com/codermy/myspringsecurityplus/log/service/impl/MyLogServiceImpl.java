package com.codermy.myspringsecurityplus.log.service.impl;

import cn.hutool.json.JSONObject;
import com.codermy.myspringsecurityplus.common.utils.PojoConvertUtil;
import com.codermy.myspringsecurityplus.common.utils.Result;
import com.codermy.myspringsecurityplus.common.utils.ResultCode;
import com.codermy.myspringsecurityplus.log.dao.LogDao;
import com.codermy.myspringsecurityplus.log.dto.ErrorLogDto;
import com.codermy.myspringsecurityplus.log.dto.LogDto;
import com.codermy.myspringsecurityplus.log.dto.LogQuery;
import com.codermy.myspringsecurityplus.log.entity.MyLog;
import com.codermy.myspringsecurityplus.log.service.MyLogService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.elasticsearch.client.RestHighLevelClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * *
 */
@Slf4j
@Service
public class MyLogServiceImpl implements MyLogService {
    @Autowired
    private LogDao logDao;

    @Autowired(required = false)
    ModelMapper modelMapper;

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public Result<LogDto> getFuzzyInfoLogByPage(Integer offectPosition, Integer limit, LogQuery logQuery) {
        Page page = PageHelper.offsetPage(offectPosition, limit);
        Optional<String> userName = Optional.ofNullable(logQuery.getUserName());
        Optional<String> logType = Optional.ofNullable(logQuery.getLogType());
        List<MyLog> fuzzyLogByPage;
        if (userName.isPresent() && logType.isPresent()) {
            fuzzyLogByPage = logDao.findByUserNameAndType(userName.get(), logType.get());
        } else if (userName.isPresent()) {
            fuzzyLogByPage = logDao.findByUserName(userName.get());
        } else if (logType.isPresent()) {
            fuzzyLogByPage = logDao.findByType(logType.get());
        } else {
            fuzzyLogByPage = new ArrayList<>();
            Iterator<MyLog> iterator = logDao.findAll().iterator();
            while (iterator.hasNext()) {
                fuzzyLogByPage.add(iterator.next());
            }
        }

        List<LogDto> dtos= PojoConvertUtil.convertPojos(fuzzyLogByPage,LogDto.class);
        return Result.ok().count(page.getTotal()).data(dtos).code(ResultCode.TABLE_SUCCESS);
    }

    @Override
    public Result<ErrorLogDto> getFuzzyErrorLogByPage(Integer offectPosition, Integer limit, LogQuery logQuery) {
        Page page = PageHelper.offsetPage(offectPosition, limit);
        Optional<String> userName = Optional.ofNullable(logQuery.getUserName());
        Optional<String> logType = Optional.ofNullable(logQuery.getLogType());
        List<MyLog> fuzzyErrorLogByPage;
        if (userName.isPresent() && logType.isPresent()) {
            fuzzyErrorLogByPage = logDao.findByUserNameAndType(userName.get(), logType.get());
        } else if (userName.isPresent()) {
            fuzzyErrorLogByPage = logDao.findByUserName(userName.get());
        } else if (logType.isPresent()) {
            fuzzyErrorLogByPage = logDao.findByType(logType.get());
        } else {
            fuzzyErrorLogByPage = new ArrayList<>();
            Iterator<MyLog> iterator = logDao.findAll().iterator();
            while (iterator.hasNext()) {
                fuzzyErrorLogByPage.add(iterator.next());
            }
        }
        List<ErrorLogDto> dtos= PojoConvertUtil.convertPojos(fuzzyErrorLogByPage,ErrorLogDto.class);
        return Result.ok().count(page.getTotal()).data(dtos).code(ResultCode.TABLE_SUCCESS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(String userName, String browser, String ip, ProceedingJoinPoint joinPoint, MyLog log) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        com.codermy.myspringsecurityplus.log.aop.MyLog myLog = method.getAnnotation(com.codermy.myspringsecurityplus.log.aop.MyLog.class);
        // 方法路径
        String methodName = joinPoint.getTarget().getClass().getName() + "." + signature.getName() + "()";
        StringBuilder params = new StringBuilder("{");
        //参数值
        Object[] argValues = joinPoint.getArgs();
        //参数名称
        String[] argNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        if (argValues != null) {
            for (int i = 0; i < argValues.length; i++) {
                params.append(" ").append(argNames[i]).append(": ").append(argValues[i]);
            }
        }
        // 描述
        if (log != null) {
            log.setDescription(myLog.value());
        }
        assert log != null;
        log.setIp(ip);
        String loginPath = "login";
        if (loginPath.equals(signature.getName())) {
            try {
                assert argValues != null;
                userName = new JSONObject(argValues[0]).get("userName").toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.setMethod(methodName);
        log.setUserName(userName);
        log.setParams(params.toString() + " }");
        log.setBrowser(browser);
        logDao.save(log);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByError() {
        logDao.deleteAllByType("ERROR");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delAllByInfo() {
        logDao.deleteAllByType("INFO");
    }
}
