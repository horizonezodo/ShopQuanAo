package vn.horizonezodo.core.Service;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.spel.ast.OpOr;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.Role;
import vn.horizonezodo.core.Entity.User;
import vn.horizonezodo.core.Exception.MessageException;
import vn.horizonezodo.core.Input.UserInput;
import vn.horizonezodo.core.Output.Message;
import vn.horizonezodo.core.Output.UserOutput;
import vn.horizonezodo.core.Repo.UserRepo;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo repo;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private RoleService roleService;

    public Optional<User> getUserByInfo(String info){
        return Optional.ofNullable(repo.findByEmail(info))
                .or(() -> Optional.ofNullable(repo.findByPhone(info)))
                .orElse(Optional.empty());
    }

    public Optional<User> getUserById(Long id){
        return Optional.ofNullable(repo.findById(id))
                .orElse(Optional.empty());
    }

    public boolean checkUser(String info){
        return repo.existsByEmail(info) || repo.existsByPhone(info);
    }

    public UserOutput saveUser(UserInput input){
        User user = new User();
        user.setUsername(input.getUserName());
        user.setEmail(input.getEmail());
        user.setPassword(encoder.encode(input.getPassword()));
        Role role = roleService.findByRoleName(input.getRole()).orElseThrow(() -> new MessageException("Role not found exception"));
        user.setRole(role);
        user.setDayCreate(System.currentTimeMillis());
        user.setLock(true);
        repo.save(user);
        UserOutput userOutput = new UserOutput(user.getUsername(),user.getEmail());
        return userOutput;
    }

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

    public Message ActivateUser(UserInput input){
        User user = getUserByInfo(input.getEmail()).orElseThrow(() -> new MessageException("Không tìm thấy user theo email"));
        user.setActivate(true);
        user.setLock(false);
        repo.save(user);
        return new Message("Active user success");
    }

    public Message VerifyUserEmail(UserInput input){
        User user = getUserByInfo(input.getEmail()).orElseThrow(() -> new MessageException("Không tìm thấy user theo email"));
        user.setEmailVerifired(true);
        repo.save(user);
        return new Message("Verify user email success");
    }

    public Message VerifyUserPhone(UserInput input){
        User user = getUserByInfo(input.getEmail()).orElseThrow(() -> new MessageException("Không tìm thấy user theo email"));
        user.setPhoneVerifired(true);
        repo.save(user);
        return new Message("Verify user phone success");
    }

    public Message changePassword(UserInput input){
        User user = getUserByInfo(input.getEmail()).orElseThrow(() -> new MessageException("Không tìm thấy user theo email"));
        if(encoder.matches(input.getPassword(), user.getPassword())){
            return new Message("It is your old pass");
        }
        user.setPassword(encoder.encode(input.getPassword()));
        repo.save(user);
        return new Message("Change user pass success");
    }

    public Message updateUserRole(UserInput input){
        User user = getUserByInfo(input.getEmail()).orElseThrow(() -> new MessageException("Không tìm thấy user theo email"));
        Role newRole = roleService.findByRoleName(input.getRole()).orElseThrow(() -> new MessageException("Không tìm thấy role"));
        user.setRole(newRole);
        repo.save(user);
        return new Message("Change user role success");
    }

    public Message lockUser(UserInput input){
        User user = getUserByInfo(input.getEmail()).orElseThrow(() -> new MessageException("Không tìm thấy user theo email"));
        user.setLock(true);
        repo.save(user);
        return new Message("Lock user success");
    }
}
