package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.OrderHistory;
import vn.horizonezodo.core.Entity.PAYMENTTYPE;
import vn.horizonezodo.core.Entity.User;
import vn.horizonezodo.core.Entity.Wallet;
import vn.horizonezodo.core.MongoRepo.OrderHistoryRepo;
import vn.horizonezodo.core.Output.Message;
import vn.horizonezodo.core.Input.OrderHistoryInput;

import javax.transaction.Transactional;

@Service
public class OrderHistoryService {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderHistoryRepo repo;

    @Transactional
    public Message addHistory(OrderHistoryInput input){
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
        repo.save(history);
        return new Message("Add history success");
    }
}
