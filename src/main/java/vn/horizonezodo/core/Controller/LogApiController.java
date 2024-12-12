package vn.horizonezodo.core.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.horizonezodo.core.Service.LogAPIService;

@RestController
@RequestMapping("/service/logApi")
public class LogApiController {
    @Autowired
    private LogAPIService service;

    @GetMapping("/get-all")
    public ResponseEntity<?> getALLLog(@RequestParam(name = "pageSize", defaultValue ="25") int pageSize, @RequestParam(name = "page",defaultValue = "0") int page){
        return new ResponseEntity<>(service.getAllAPI(pageSize,page), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getLog(@PathVariable("id")String id){
        return new ResponseEntity<>(service.getLogAPI(id), HttpStatus.OK);
    }
}
