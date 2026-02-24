package Pathology.Care.Platform.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import Pathology.Care.Platform.Model.AdminUser;

public interface AdminRepo  extends JpaRepository<AdminUser, Long> {
       
}
