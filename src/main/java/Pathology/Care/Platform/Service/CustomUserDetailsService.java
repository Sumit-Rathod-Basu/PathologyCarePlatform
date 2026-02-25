package Pathology.Care.Platform.Service;

import Pathology.Care.Platform.Model.AdminUser;
import Pathology.Care.Platform.Repository.AdminRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {
  @Autowired
    private  AdminRepo adminRepo;

    public CustomUserDetailsService(AdminRepo adminRepo) {
        this.adminRepo = adminRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        AdminUser admin = adminRepo.findByEmail(email);
                
        return new User(
                admin.getEmail(),
                admin.getPassword(),
                Collections.emptyList()
        );
    }
}