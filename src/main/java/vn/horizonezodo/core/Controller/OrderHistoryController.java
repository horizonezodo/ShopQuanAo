package vn.horizonezodo.core.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.horizonezodo.core.Service.OrderHistoryService;


@RestController
@RequestMapping("/service/order-history")
public class OrderHistoryController {

    @Autowired
    private OrderHistoryService service;

    @GetMapping("/get-all-order/{id}")
    public ResponseEntity<?> getAll(@PathVariable("id")Long id){
        return new ResponseEntity<>(service.getAllOrderHistoryByUser(id), HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOrderHistory(@PathVariable("id")String id){
        return new ResponseEntity<>(service.getOrderHistoryById(id), HttpStatus.OK);
    }
}
