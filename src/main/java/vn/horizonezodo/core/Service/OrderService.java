package vn.horizonezodo.core.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.ORDERSTATUS;

import vn.horizonezodo.core.Entity.OrderItem;
import vn.horizonezodo.core.Entity.Orders;
import vn.horizonezodo.core.Entity.User;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.OrderInput;
import vn.horizonezodo.core.Output.Message;
import vn.horizonezodo.core.Repo.OrderItemRepo;
import vn.horizonezodo.core.Repo.OrderRepo;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private OrderRepo repo;

    @Autowired
    private OrderItemRepo itemRepo;

    @Autowired
    private UserService service;

    @Transactional
    public Message AddProductToOrder(OrderInput input){
        Optional<Orders> orderOpt = repo.findByUserAndOrderstatus(input.getUserId(), ORDERSTATUS.OPEN);
        if(orderOpt.isPresent()){
            Orders order = orderOpt.get();
            List<OrderItem> orderItemList = order.getOrderItems();
            Optional<OrderItem> orderItemOpt = itemRepo.findByOrOrderAndProductId(order.getId(), input.getProductId());
            if(orderItemOpt.isPresent()){
                OrderItem orderItem = orderItemOpt.get();
                orderItem.setQuantity(input.getQuantity());
                orderItem.setPrice(input.getPrice());
                itemRepo.save(orderItem);
            }else{
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProductId(input.getProductId());
                orderItem.setQuantity(input.getQuantity() + orderItem.getQuantity());
                orderItem.setPrice(orderItem.getPrice().add(input.getPrice().multiply(BigDecimal.valueOf(input.getQuantity()))));
                itemRepo.save(orderItem);
                orderItemList.add(orderItem);
            }
            order.setUpdateAt(System.currentTimeMillis());
            order.setShippingAddress(input.getShipingAddress());
            BigDecimal totalPrice = updateTotalPrice(order);
            order.setTotalPrice(totalPrice);
            repo.save(order);
            return new Message("Cập nhật order thành công");
        }else{
            Orders orders = new Orders();
            orders.setCreateAt(System.currentTimeMillis());
            User user = service.getUserById(input.getUserId()).orElseThrow(() -> new MessageException("Lỗi không tìm thấy user"));
            orders.setUser(user);
            List<OrderItem> orderItems = new ArrayList<>();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(orders);
            orderItem.setProductId(input.getProductId());
            orderItem.setQuantity(input.getQuantity());
            orderItem.setPrice(input.getPrice().multiply(BigDecimal.valueOf(input.getQuantity())));
            itemRepo.save(orderItem);
            orderItems.add(orderItem);
            orders.setOrderstatus(ORDERSTATUS.OPEN);
            orders.setOrderItems(orderItems);
            orders.setShippingAddress(user.getAddress());
            orders.setUserNote(input.getNote());
            orders.setOrderDate(System.currentTimeMillis());
            BigDecimal totalPrice = updateTotalPrice(orders);
            orders.setTotalPrice(totalPrice);
            repo.save(orders);
            return new Message("Thêm vào order thành công");
        }
    }

    private BigDecimal updateTotalPrice(Orders order) {
        BigDecimal totalPrice = order.getOrderItems().stream()
                .map(item -> item.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return totalPrice;
    }


    public Message canceledOrder(OrderInput input){
        Optional<Orders> orderOpt = repo.findByUserAndOrderstatus(input.getUserId(), ORDERSTATUS.OPEN);
        if(orderOpt.isPresent()){
            Orders orders = orderOpt.get();
            orders.setOrderstatus(ORDERSTATUS.CANCELLED);
        }
        return new Message("Hủy đơn thành công");
    }

    public Message deliveredOrder(OrderInput input){
        Optional<Orders> orderOpt = repo.findByUserAndOrderstatus(input.getUserId(), ORDERSTATUS.OPEN);
        if(orderOpt.isPresent()){
            Orders orders = orderOpt.get();
            orders.setOrderstatus(ORDERSTATUS.DELIVERED);
        }
        return new Message("Giao đơn đến tay khách thành công");
    }

    public Message successOrder(OrderInput input){
        Optional<Orders> orderOpt = repo.findByUserAndOrderstatus(input.getUserId(), ORDERSTATUS.OPEN);
        if(orderOpt.isPresent()){
            Orders orders = orderOpt.get();
            orders.setOrderstatus(ORDERSTATUS.SUCCESS);
        }
        return new Message("Xác nhận hoàn thành thành công");
    }
    @Transactional
    public Message updateOrderStatus(OrderInput input){
        Orders orders = repo.findById(input.getOrderId()).orElseThrow(() -> new MessageException("Không tìm thấy order"));
        switch (input.getOrderStatus().toLowerCase()){
            case "purchased":
                orders.setOrderstatus(ORDERSTATUS.PURCHASED);
                break;
            case "delivery":
                orders.setOrderstatus(ORDERSTATUS.DELIVERY);
                break;
            case "delivered":
                orders.setOrderstatus(ORDERSTATUS.DELIVERED);
                break;
            case "canceled":
                orders.setOrderstatus(ORDERSTATUS.CANCELLED);
                break;
            case "lost":
                orders.setOrderstatus(ORDERSTATUS.LOST);
                break;
            case "return":
                orders.setOrderstatus(ORDERSTATUS.RETURN);
                break;
            case "success":
                orders.setOrderstatus(ORDERSTATUS.SUCCESS);
                break;
            default:
                orders.setOrderstatus(ORDERSTATUS.OPEN);
        };
        repo.save(orders);
        return new Message("Thay đổi trang thái thành công");
    }
}
