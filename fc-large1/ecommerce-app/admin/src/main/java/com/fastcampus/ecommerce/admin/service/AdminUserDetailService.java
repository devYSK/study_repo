package com.fastcampus.ecommerce.admin.service;

import com.fastcampus.ecommerce.admin.domain.user.AdminUser;
import com.fastcampus.ecommerce.admin.domain.user.AdminUserDetail;
import com.fastcampus.ecommerce.admin.exception.DuplicatedAdminUserException;
import com.fastcampus.ecommerce.admin.exception.NotFoundAdminUserException;
import com.fastcampus.ecommerce.admin.repository.AdminUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AdminUserDetailService implements UserDetailsService {

    private final AdminUserRepository adminUserRepository;

    public AdminUser save(AdminUser adminUser) {
        Optional<AdminUser> optionalAdminUser = this.adminUserRepository.findByEmailAndIsActivated(adminUser.getEmail(), true);
        if (optionalAdminUser.isPresent()) {
            throw new DuplicatedAdminUserException("Already register admin user, {}" + adminUser.getEmail());
        }
        return this.adminUserRepository.save(adminUser);
    }

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
        log.info(">>> loadUserByUsername, {}", userEmail);
        Optional<AdminUser> optionalAdminUser = this.adminUserRepository.findByEmailAndIsActivated(userEmail, true);
        if (optionalAdminUser.isEmpty()) {
            throw new NotFoundAdminUserException("Not found admin user with " + userEmail);
        }

        AdminUser adminUser = optionalAdminUser.get();
        AdminUserDetail adminUserDetail = new AdminUserDetail();
        adminUserDetail.setAdminUser(adminUser);
        adminUserDetail.setRoles(Arrays.asList(adminUser.getRole()));
        adminUserDetail.setPermissions(Arrays.asList(adminUser.getPermission()));

        return adminUserDetail;
    }
}
