package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.User;

import java.util.Optional;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private UserService service;

    @Override
    public UserDetails loadUserByUsername(String info) throws UsernameNotFoundException {
        Optional<User> userOpt = service.getUserByInfo(info);
        if(userOpt.isPresent()) return UserDetailImpl.build(userOpt.get());
        return null;
    }
}
