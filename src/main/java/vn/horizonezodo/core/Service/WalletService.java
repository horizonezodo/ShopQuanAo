package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.User;
import vn.horizonezodo.core.Entity.Wallet;
import vn.horizonezodo.core.Input.WalletInput;
import vn.horizonezodo.core.Output.Message;
import vn.horizonezodo.core.Repo.WalletRepo;

import javax.transaction.Transactional;

@Service
public class WalletService {

    @Autowired
    private WalletRepo repo;

    @Autowired
    private UserService service;

    @Transactional
    public Message addCashToWallet(WalletInput walletInput){
        User user = service.getUserById(walletInput.getUserId()).get();
        Wallet wallet = user.getWallet();
        wallet.setAmount(walletInput.getPrice());
        repo.save(wallet);
        return new Message("Nạp tiền thành cồng");
    }
}
