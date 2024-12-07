package vn.horizonezodo.core.Service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.*;

import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.OrderInput;
import vn.horizonezodo.core.Input.OrderItemInput;
import vn.horizonezodo.core.Output.Message;
import vn.horizonezodo.core.Output.OrderItemOutput;
import vn.horizonezodo.core.Output.OrderOutput;
import vn.horizonezodo.core.Output.ProductOutput;
import vn.horizonezodo.core.Repo.OrderItemRepo;
import vn.horizonezodo.core.Repo.OrderRepo;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {
    @Autowired
    private OrderRepo repo;

    @Autowired
    private OrderItemRepo itemRepo;

    @Autowired
    private UserService service;

    @Autowired
    private ProductService productService;

    @Autowired
    private VariantService variantService;

    @Transactional
    public Message AddProductToOrder(OrderInput input){
        Optional<Orders> orderOpt = repo.findByUserAndOrderstatus(input.getUserId(), ORDERSTATUS.OPEN);
        ProductOutput product = productService.getProductOutput(input.getProductId());
        Variant variant = variantService.getById(input.getVariantId());
        if(orderOpt.isPresent()){
            Orders order = orderOpt.get();
            List<OrderItem> orderItemList = order.getOrderItems();
            Optional<OrderItem> orderItemOpt = itemRepo.findByOrOrderAndProductId(order.getId(), input.getProductId());
            if(orderItemOpt.isPresent()){
                OrderItem orderItem = orderItemOpt.get();
                orderItem.setQuantity(orderItem.getQuantity() + input.getQuantity());
                orderItem.setTotalPrice(orderItem.getPrice() * (orderItem.getQuantity() + input.getQuantity()));
                itemRepo.save(orderItem);
            }else{
                OrderItem orderItem = new OrderItem();
                orderItem.setOrder(order);
                orderItem.setProductId(input.getProductId());
                orderItem.setVariantId(input.getVariantId());
                orderItem.setQuantity(input.getQuantity());
                orderItem.setPrice(variant.getPrice());
                orderItem.setTotalPrice(input.getQuantity() * variant.getPrice());
                itemRepo.save(orderItem);
                orderItemList.add(orderItem);
            }
            order.setUpdateAt(System.currentTimeMillis());
            order.setShippingAddress(input.getShipingAddress());
            order.setTotalPrice(updateTotalPrice(order));
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
            orderItem.setVariantId(input.getVariantId());
            orderItem.setQuantity(input.getQuantity());
            orderItem.setPrice(variant.getPrice());
            orderItem.setTotalPrice(variant.getPrice() * input.getQuantity());
            itemRepo.save(orderItem);
            orderItems.add(orderItem);
            orders.setOrderstatus(ORDERSTATUS.OPEN);
            orders.setOrderItems(orderItems);
            orders.setShippingAddress(user.getAddress());
            orders.setUserNote(input.getNote());
            orders.setOrderDate(System.currentTimeMillis());
            orders.setTotalPrice(updateTotalPrice(orders));
            repo.save(orders);
            return new Message("Thêm vào order thành công");
        }
    }

    private double updateTotalPrice(Orders order) {
       List<OrderItem> orderItems = order.getOrderItems();
       double total = 0;
       for (OrderItem orderItem: orderItems){
           total += orderItem.getTotalPrice();
       }
       return total;
    }

    public Message canceledOrder(Long id){
        Optional<Orders> orderOpt = repo.findById(id);
        if(orderOpt.isPresent()){
            Orders orders = orderOpt.get();
            orders.setOrderstatus(ORDERSTATUS.CANCELLED);
        }
        return new Message("Hủy đơn thành công");
    }

    public Message deliveredOrder(Long id){
        Optional<Orders> orderOpt = repo.findById(id);
        if(orderOpt.isPresent()){
            Orders orders = orderOpt.get();
            orders.setOrderstatus(ORDERSTATUS.DELIVERED);
        }
        return new Message("Giao đơn đến tay khách thành công");
    }

    public Message successOrder(Long id){
        Optional<Orders> orderOpt = repo.findById(id);
        if(orderOpt.isPresent()){
            Orders orders = orderOpt.get();
            orders.setOrderstatus(ORDERSTATUS.SUCCESS);
        }
        return new Message("Xác nhận hoàn thành thành công");
    }
    @Transactional
    public Message updateOrderStatus(OrderInput input,Long id){
        Orders orders = repo.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy order"));
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

    public Page<OrderOutput> getAllOrderByUserId(OrderInput input, int pageSize, int page){
        Pageable pageable = PageRequest.of(page,pageSize);
        Page<Orders> orders = repo.findAllByUserAndOrderstatus(input.getUserId(), ORDERSTATUS.OPEN, pageable);
        List<OrderOutput> orderOutputs = orders.stream()
                .map(o -> {
                    OrderOutput orderOutput = new OrderOutput();
                    orderOutput.setId(o.getId());
                    orderOutput.setOrderDate(o.getOrderDate());
                    orderOutput.setUserNote(o.getUserNote());
                    orderOutput.setOrderstatus(o.getOrderstatus());
                    orderOutput.setTotalPrice(o.getTotalPrice());
                    orderOutput.setShippingAddress(o.getShippingAddress());
                    List<OrderItem> orderItems = itemRepo.findAllByOrder(o.getId());
                    List<OrderItemOutput> ouputs = new ArrayList<>();
                    for(OrderItem oder: orderItems){
                        OrderItemOutput out = new OrderItemOutput();
                        out.setId(oder.getId());
                        out.setProduct(productService.getProductOutput(oder.getProductId()));
                        out.setVariant(variantService.getById(oder.getVariantId()));
                        out.setPrice(oder.getPrice());
                        out.setQuantity(oder.getQuantity());
                        out.setTotalPrice(oder.getTotalPrice());
                        ouputs.add(out);
                    }
                    orderOutput.setOrderItemList(ouputs);
                    return orderOutput;
                }).collect(Collectors.toList());
        return new PageImpl<>(orderOutputs, pageable, orders.getTotalElements());
    }

    public OrderOutput getOrder(Long id){
        Orders order = repo.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy order theo id: " + id));
        OrderOutput orderOutput = new OrderOutput();
        orderOutput.setId(order.getId());
        orderOutput.setOrderDate(order.getOrderDate());
        orderOutput.setUserNote(order.getUserNote());
        orderOutput.setTotalPrice(order.getTotalPrice());
        orderOutput.setShippingAddress(order.getShippingAddress());
        List<OrderItem> orderItems = itemRepo.findAllByOrder(id);
        List<OrderItemOutput> outputs = new ArrayList<>();
        for(OrderItem o: orderItems){
            OrderItemOutput ouput = new OrderItemOutput();
            ouput.setId(o.getId());
            ouput.setProduct(productService.getProductOutput(o.getProductId()));
            ouput.setVariant(variantService.getById(o.getVariantId()));
            ouput.setQuantity(o.getQuantity());
            ouput.setPrice(o.getPrice());
            ouput.setTotalPrice(o.getTotalPrice());
            outputs.add(ouput);
        }
        orderOutput.setOrderItemList(outputs);
        return orderOutput;
    }

    @Transactional
    public void deleteOrderItem(Long orderId ,Long orderItemId){
        OrderItem orderItem = itemRepo.findById(orderItemId).orElseThrow(() -> new MessageException("Không thể tìm thấy orderItem theo id: " + orderItemId));
        itemRepo.delete(orderItem);
        Orders orders = repo.findById(orderId).orElseThrow(()-> new MessageException("Không tìm thấy order theo id: " + orderId));
        orders.setTotalPrice(updateTotalPrice(orders));
        orders.setUpdateAt(System.currentTimeMillis());
        repo.save(orders);
    }

    @Transactional
    public void updateQuantityOrderItem(OrderItemInput input){
        OrderItem orderItem = itemRepo.findById(input.getOrderItemId()).orElseThrow(() -> new MessageException("Không tìm thấy orderItem theo id: " + input.getOrderItemId()));
        orderItem.setQuantity(input.getQuantity());
        orderItem.setTotalPrice(orderItem.getTotalPrice() * input.getQuantity());
        itemRepo.save(orderItem);
        Orders orders = repo.findById(input.getOrderId()).orElseThrow(()-> new MessageException("Không thể tìm thấy order theo id: " + input.getOrderId()));
        orders.setTotalPrice(updateTotalPrice(orders));
        repo.save(orders);
    }
}
