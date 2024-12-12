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

    public void createLog(LogAPIType type, String url, String request, String response, long beginTime, long endTime){
        LogAPI logAPI = new LogAPI();
        logAPI.setType(type);
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
}
