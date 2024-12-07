package vn.horizonezodo.core.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.horizonezodo.core.Entity.OrderHistory;
import vn.horizonezodo.core.Input.OrderHistoryInput;
import vn.horizonezodo.core.Input.OrderInput;
import vn.horizonezodo.core.Input.OrderItemInput;
import vn.horizonezodo.core.Output.Message;
import vn.horizonezodo.core.Output.OrderOutput;
import vn.horizonezodo.core.Service.OrderHistoryService;
import vn.horizonezodo.core.Service.OrderService;


@RestController
@RequestMapping("/service/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderHistoryService orderHistoryService;

    @PostMapping("/add-order")
    public ResponseEntity<?> addOrder(@RequestBody OrderInput input){
        orderService.AddProductToOrder(input);
        return new ResponseEntity<>(new Message("Add to order thành công"), HttpStatus.OK);
    }

    @PostMapping("/cancel-order/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable("id") Long id){
        orderService.canceledOrder(id);
        return new ResponseEntity<>(new Message("Cancel order thành công"), HttpStatus.OK);
    }

    @PostMapping("/delivered-order/{id}")
    public ResponseEntity<?> deliveredOrder(@PathVariable("id") Long id){
        orderService.deliveredOrder(id);
        return new ResponseEntity<>(new Message("Giao hàng đến khách thành công"), HttpStatus.OK);
    }

    @PostMapping("/success-order/{id}")
    public ResponseEntity<?> successOrder(@PathVariable("id") Long id, @RequestBody OrderHistoryInput input){
        orderService.successOrder(id);
        orderHistoryService.addHistory(input);
        return new ResponseEntity<>(new Message("Xác nhận hoàn thiện đơn"), HttpStatus.OK);
    }

    @PostMapping("/update-order-status/{id}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable("id") Long id, @RequestBody OrderInput input){
        orderService.updateOrderStatus(input, id);
        return new ResponseEntity<>(new Message("Update order status thành công"), HttpStatus.OK);
    }

    @GetMapping("/get-all")
    public ResponseEntity<?> getAllOrder(@RequestBody OrderInput input, @RequestParam(name = "pageSize", defaultValue = "25") int pageSize, @RequestParam(name = "page", defaultValue = "0")int page){
        Page<OrderOutput> orderOutputList = orderService.getAllOrderByUserId(input, pageSize, page);
        return new ResponseEntity<>(orderOutputList, HttpStatus.OK);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getOrder(@PathVariable("id")Long id){
        OrderOutput orderOutput = orderService.getOrder(id);
        return new ResponseEntity<>(orderOutput, HttpStatus.OK);
    }

    @PostMapping("/delete-order-item")
    public ResponseEntity<?> deleteOrderItem(@RequestParam Long orderId ,@RequestParam Long orderItemId){
        orderService.deleteOrderItem(orderId, orderItemId);
        return new ResponseEntity<>(new Message("Xóa orderItem thành công"), HttpStatus.OK);
    }

    @PostMapping("/update-order-item-quantity")
    public ResponseEntity<?> updateOrderItemQuantity(@RequestBody OrderItemInput input){
        orderService.updateQuantityOrderItem(input);
        return new ResponseEntity<>(new Message("Update quantity thành công"), HttpStatus.OK);
    }

}
