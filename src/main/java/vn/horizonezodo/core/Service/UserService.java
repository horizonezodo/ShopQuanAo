package vn.horizonezodo.core.Service;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.Role;
import vn.horizonezodo.core.Entity.User;
import vn.horizonezodo.core.Entity.Wallet;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.UserInput;
import vn.horizonezodo.core.Output.Message;
import vn.horizonezodo.core.Output.UserOutput;
import vn.horizonezodo.core.Repo.UserRepo;
import vn.horizonezodo.core.Repo.WalletRepo;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private RoleService roleService;

    @Autowired
    private WalletRepo walletRepo;

    public Optional<User> getUserByInfo(String info){
        return Optional.ofNullable(repo.findByEmail(info))
                .or(() -> Optional.ofNullable(repo.findByPhone(info)))
                .orElse(Optional.empty());
    }

    public Optional<User> getUserById(Long id){
        return Optional.ofNullable(repo.findById(id))
                .orElse(Optional.empty());
    }

    public boolean checkUser(String email,String phone){
        return repo.existsByEmail(email) || repo.existsByPhone(phone);
    }

    @Transactional
    public void saveUser(UserInput input){
        Wallet wallet = new Wallet();
        wallet.setAmount(0);
        wallet.setLockWallet(false);
        walletRepo.save(wallet);

        User user = new User();
        user.setUsername(input.getUserName());
        user.setEmail(input.getEmail());
        user.setPassword(input.getPassword());
        user.setAvatarImg(input.getImage());
        user.setAddress(input.getAddress());
        user.setBrithDay(input.getBirthDay());
        Role role = roleService.findByRoleName(input.getRole()).orElseThrow(() -> new MessageException("Role not found exception"));
        user.setRole(role);
        user.setWallet(wallet);
        user.setDayCreate(System.currentTimeMillis());
        user.setLock(false);
        user.setActivate(false);
        repo.save(user);
//        UserOutput userOutput = new UserOutput(user.getUsername(),user.getEmail());
//        return userOutput;
    }

    @Transactional
    public Message updateUser(UserInput input){
        User user = repo.findById(input.getId()).orElseThrow(() -> new MessageException("User not found with id: "+ input.getId()));
        user.setUsername(input.getUserName());
        user.setAvatarImg(input.getImg());
        user.setBrithDay(input.getBirthDay());
        user.setDayUpdate(System.currentTimeMillis());
        if(Strings.isNullOrEmpty(user.getNote())){
            user.setNoteCreate(System.currentTimeMillis());
        }
        if(!Strings.isNullOrEmpty(input.getNote())){
            user.setNote(input.getNote());
            user.setNoteUpdate(System.currentTimeMillis());
        }
        user.setAddress(input.getAddress());
        repo.save(user);
        return new Message("Update info success");
    }

    @Transactional
    public Message ActivateUser(UserInput input){
        User user = getUserByInfo(input.getEmail()).orElseThrow(() -> new MessageException("Không tìm thấy user theo email"));
        user.setActivate(true);
        user.setLock(false);
        repo.save(user);
        return new Message("Active user success");
    }

    @Transactional
    public Message VerifyUserEmail(UserInput input){
        User user = getUserByInfo(input.getEmail()).orElseThrow(() -> new MessageException("Không tìm thấy user theo email"));
        user.setEmailVerifired(true);
        repo.save(user);
        return new Message("Verify user email success");
    }

    @Transactional
    public Message VerifyUserPhone(UserInput input){
        User user = getUserByInfo(input.getEmail()).orElseThrow(() -> new MessageException("Không tìm thấy user theo email"));
        user.setPhoneVerifired(true);
        repo.save(user);
        return new Message("Verify user phone success");
    }

    @Transactional
    public Message changePassword(UserInput input){
        User user = getUserByInfo(input.getEmail()).orElseThrow(() -> new MessageException("Không tìm thấy user theo email"));
        user.setPassword(input.getPassword());
        user.setDayUpdate(System.currentTimeMillis());
        repo.save(user);
        return new Message("Change user pass success");
    }

    @Transactional
    public Message updateUserRole(UserInput input){
        User user = getUserByInfo(input.getEmail()).orElseThrow(() -> new MessageException("Không tìm thấy user theo email"));
        Role newRole = roleService.findByRoleName(input.getRole()).orElseThrow(() -> new MessageException("Không tìm thấy role"));
        user.setRole(newRole);
        repo.save(user);
        return new Message("Change user role success");
    }

    @Transactional
    public Message lockUser(UserInput input){
        User user = getUserByInfo(input.getEmail()).orElseThrow(() -> new MessageException("Không tìm thấy user theo email"));
        user.setLock(true);
        Wallet wallet = user.getWallet();
        wallet.setLockWallet(true);
        walletRepo.save(wallet);
        repo.save(user);
        return new Message("Lock user success");
    }

    public boolean checkLockUser(User user){
        return user.isLock();
    }
}
