package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.*;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.MongoRepo.OrderHistoryRepo;
import vn.horizonezodo.core.Output.Message;
import vn.horizonezodo.core.Input.OrderHistoryInput;
import vn.horizonezodo.core.Output.OrderHistoryOutput;
import vn.horizonezodo.core.Output.OrderItemOutput;
import vn.horizonezodo.core.Output.OrderOutput;
import vn.horizonezodo.core.Repo.OrderItemRepo;
import vn.horizonezodo.core.Repo.OrderRepo;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderHistoryService {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderHistoryRepo repo;

    @Autowired
    private OrderItemRepo itemRepo;

    @Autowired
    private ProductService productService;

    @Autowired
    private VariantService variantService;

    @Autowired
    private OrderRepo orderRepo;

    @Transactional
    public void addHistory(OrderHistoryInput input){
        User user = userService.getUserById(input.getUserId()).get();
        Wallet wallet = user.getWallet();
        OrderHistory history = new OrderHistory();
        history.setOrderId(input.getOrderId());
        history.setDayPayment(System.currentTimeMillis());
        switch (input.getPaymentType().toLowerCase()){
            case "cash":
                history.setPaymenttype(PAYMENTTYPE.CASH);
                break;
            case "qrcode":
                history.setPaymenttype(PAYMENTTYPE.QRCODE);
                break;
            case "online":
                history.setPaymenttype(PAYMENTTYPE.ONLINE_TRANSFER);
                break;
        }
        history.setWalletId(wallet.getId());
        history.setUserId(user.getId());
        history.setPaymentAmount(input.getPaymentAmount());
        repo.save(history);
    }

    public List<OrderHistoryOutput> getAllOrderHistoryByUser(Long userId){
        List<OrderHistory> orderHistories = repo.findAllByUserId(userId);
        List<OrderHistoryOutput> outputs = new ArrayList<>();
        for (OrderHistory orderHistory: orderHistories){
            OrderHistoryOutput out = new OrderHistoryOutput();
            out.setId(orderHistory.getId());
            out.setDayPayment(orderHistory.getDayPayment());
            out.setPaymenttype(orderHistory.getPaymenttype());
            out.setPaymentAmount(orderHistory.getPaymentAmount());
            Orders orders = orderRepo.findById(orderHistory.getOrderId()).orElseThrow(()-> new MessageException("Không tìm thấy order theo id" + orderHistory.getOrderId()));
            List<OrderItem> orderItems = itemRepo.findAllByOrder(orders);
            List<OrderItemOutput> ouputs = new ArrayList<>();
            for(OrderItem oder: orderItems){
                OrderItemOutput o = new OrderItemOutput();
                o.setId(oder.getId());
                o.setProduct(productService.getProductOutput(oder.getProductId()));
                o.setVariant(variantService.getById(oder.getVariantId()));
                o.setPrice(oder.getPrice());
                o.setQuantity(oder.getQuantity());
                o.setTotalPrice(oder.getTotalPrice());
                ouputs.add(o);
            }
            out.setOrderItemOutput(ouputs);
            outputs.add(out);
        }
        return outputs;
    }

    public OrderHistoryOutput getOrderHistoryById(String id){
        OrderHistory history = repo.findById(id).orElseThrow(() -> new MessageException("Không tìm thấy order history theo id: " + id));
        OrderHistoryOutput out = new OrderHistoryOutput();
        out.setId(history.getId());
        out.setDayPayment(history.getDayPayment());
        out.setPaymenttype(history.getPaymenttype());
        out.setPaymentAmount(history.getPaymentAmount());
        Orders orders = orderRepo.findById(history.getOrderId()).orElseThrow(()-> new MessageException("Không tìm thấy order theo id" + history.getOrderId()));
        List<OrderItem> orderItems = itemRepo.findAllByOrder(orders);
        List<OrderItemOutput> ouputs = new ArrayList<>();
        for(OrderItem oder: orderItems){
            OrderItemOutput o = new OrderItemOutput();
            o.setId(oder.getId());
            o.setProduct(productService.getProductOutput(oder.getProductId()));
            o.setVariant(variantService.getById(oder.getVariantId()));
            o.setPrice(oder.getPrice());
            o.setQuantity(oder.getQuantity());
            o.setTotalPrice(oder.getTotalPrice());
            ouputs.add(o);
        }
        out.setOrderItemOutput(ouputs);
        return out;
    }
}
