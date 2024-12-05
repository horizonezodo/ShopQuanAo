package vn.horizonezodo.core.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.horizonezodo.core.Entity.EROLE;
import vn.horizonezodo.core.Entity.Role;
import vn.horizonezodo.core.Repo.RoleRepo;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepo repo;

    public Optional<Role> findByRoleName(String roleName){
        switch (roleName.toLowerCase()){
            case "user":
                return repo.findByRoleName(EROLE.ROLE_USER);
            case "staff":
                return repo.findByRoleName(EROLE.ROLE_STAFF);
            case "manager":
                return repo.findByRoleName(EROLE.ROLE_MANAGER);
            case "admin" :
                return repo.findByRoleName(EROLE.ROLE_ADMIN);
            default:
                return Optional.empty();
        }
    }
}
