package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.LogAPI;
import vn.horizonezodo.core.Entity.LogAPIType;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.LogAPIInput;
import vn.horizonezodo.core.MongoRepo.LogAPIRepo;
import vn.horizonezodo.core.Output.LogAPIOutput;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LogAPIService {
    @Autowired
    private LogAPIRepo repo;

    public void createLog(String type, String url, String request, String response, long beginTime, long endTime){
        LogAPI logAPI = new LogAPI();
        logAPI.setType(getType(type));
        logAPI.setUrl(url);
        logAPI.setBeginTime(beginTime);
        logAPI.setEndTime(endTime);
        logAPI.setRequest(request);
        logAPI.setResponse(response);
        repo.save(logAPI);
    }

    public Page<LogAPIOutput> getAllAPI(int pageSize,int page){
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<LogAPI> logAPIPage = repo.findAll(pageable);
        List<LogAPIOutput> logAPIOutputs = logAPIPage.stream()
                .map(l -> {
                    LogAPIOutput logAPIOutput = new LogAPIOutput();
                    logAPIOutput.setId(l.getId());
                    logAPIOutput.setUrl(l.getUrl());
                    logAPIOutput.setType(l.getType());
                    logAPIOutput.setRequest(l.getRequest());
                    logAPIOutput.setResponse(l.getResponse());
                    logAPIOutput.setBeginTime(l.getBeginTime());
                    logAPIOutput.setEndTime(l.getEndTime());
                    return logAPIOutput;
                }).collect(Collectors.toList());
        return new PageImpl<>(logAPIOutputs, pageable, logAPIPage.getTotalElements());
    }

    public LogAPIOutput getLogAPI(String id){
        LogAPI logAPI = repo.findById(id).orElseThrow(()-> new MessageException("Không tìm thấy logAPI theo id: " + id));
        LogAPIOutput logAPIOutput = new LogAPIOutput();
        logAPIOutput.setId(logAPI.getId());
        logAPIOutput.setUrl(logAPI.getUrl());
        logAPIOutput.setType(logAPI.getType());
        logAPIOutput.setRequest(logAPI.getRequest());
        logAPIOutput.setResponse(logAPI.getResponse());
        logAPIOutput.setBeginTime(logAPI.getBeginTime());
        logAPIOutput.setEndTime(logAPI.getEndTime());
        return logAPIOutput;
    }

    private LogAPIType getType(String type){
        type = type.toUpperCase();
        switch (type){
            case "ADD_PRODUCT_TO_ORDER":
                return LogAPIType.ADD_PRODUCT_TO_ORDER;
            case  "GET_ALL_ORDER_BY_USER_ID":
                return LogAPIType.GET_ALL_ORDER_BY_USER_ID;
            case "GET_ORDER_BY_ID":
                return LogAPIType.GET_ORDER_BY_ID;
            case "ADD_ORDER":
                return LogAPIType.ADD_ORDER;
            case "CHANGE_ORDER_STATUS":
                return LogAPIType.CHANGE_ORDER_STATUS;
            case "UPDATE_ORDER_ITEM_QUANTITY":
                return LogAPIType.UPDATE_ORDER_ITEM_QUANTITY;
            case "GET_ALL_ORDER_ITEM_BY_ORDER":
                return LogAPIType.GET_ALL_ORDER_ITEM_BY_ORDER;
            case "DELETE_ORDER_ITEM":
                return LogAPIType.DELETE_ORDER_ITEM;
            case "ADD_TO_USER_ORDER":
                return LogAPIType.ADD_TO_USER_ORDER;
            case "GET_ALL_PRODUCT_BY_CATEGORY":
                return LogAPIType.GET_ALL_PRODUCT_BY_CATEGORY;
            case "GET_PRODUCT_BY_ID":
                return LogAPIType.GET_PRODUCT_BY_ID;
            case "GET_ALL_VARIANT_PRODUCT":
                return LogAPIType.GET_ALL_VARIANT_PRODUCT;
            case "UPDATE_PRODUCT_VARIANT":
                return LogAPIType.UPDATE_PRODUCT_VARIANT;
            case "ACTIVATE_PRODUCT":
                return LogAPIType.ACTIVATE_PRODUCT;
            case "DEACTIVATE_PRODUCT":
                return LogAPIType.DEACTIVATE_PRODUCT;
            case "ADD_SIZE":
                return LogAPIType.ADD_SIZE;
            case "ADD_COLOR":
                return LogAPIType.ADD_COLOR;
            case "CHANGE_COLOR":
                return LogAPIType.CHANGE_COLOR;
            case "CHANGE_SIZE":
                return LogAPIType.CHANGE_SIZE;
            case "CREATE_ACCESS_TOKEN":
                return LogAPIType.CREATE_ACCESS_TOKEN;
            case "CREATE_REFRESH_TOKEN":
                return LogAPIType.CREATE_REFRESH_TOKEN;
            case "REFRESH_TOKEN":
                return LogAPIType.REFRESH_TOKEN;
            case "ADD_USER":
                return LogAPIType.ADD_USER;
            case "UPDATE_USER_INFO":
                return LogAPIType.UPDATE_USER_INFO;
            case "CREATE_ORDER_HISTORY":
                return LogAPIType.CREATE_ORDER_HISTORY;
            case "GET_ALL_ORDER_HISTORY_BY_USER":
                return LogAPIType.GET_ALL_ORDER_HISTORY_BY_USER;
            case "CREATE_USER_WALLET":
                return LogAPIType.CREATE_USER_WALLET;
            case "ADD_MONEY_TO_WALLET":
                return LogAPIType.ADD_MONEY_TO_WALLET;
            case "SEND_MALL_ACTIVATE_USER":
                return LogAPIType.SEND_MALL_ACTIVATE_USER;
            case "ACTIVATE_USER":
                return LogAPIType.ACTIVATE_USER;
            case "LOCK_USER":
                return LogAPIType.LOCK_USER;
            case "LOCK_WALLET":
                return LogAPIType.LOCK_WALLET;
        }
        return null;
    }
}
