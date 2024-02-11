package com.fastcampus.ecommerce.admin.domain.user;


import com.fastcampus.ecommerce.admin.service.dto.AdminUserFormDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "users", schema = "ecommerce")
public class AdminUser {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "username")
    private String username;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private AdminUserRole role;
    @Column(name = "permission")
    @Enumerated(value = EnumType.STRING)
    private AdminUserPermission permission;
    @Column(name = "is_deleted")
    private boolean isDeleted = false;
    @Column(name = "is_activated")
    private boolean isActivated = false;
    @Column(name = "created_at")
    private OffsetDateTime createdAt;
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    public static AdminUser createAdminUser(AdminUserFormDTO adminUserDTO, PasswordEncoder passwordEncoder) {
        AdminUser adminUser = new AdminUser();
        adminUser.setUsername(adminUserDTO.getUsername());
        adminUser.setPassword(passwordEncoder.encode(adminUserDTO.getPassword1()));
        adminUser.setEmail(adminUserDTO.getEmail());
        adminUser.setRole(AdminUserRole.USER);
        adminUser.setPermission(AdminUserPermission.ALL);

        OffsetDateTime now = OffsetDateTime.now();
        adminUser.setCreatedAt(now);
        adminUser.setUpdatedAt(now);
        adminUser.setActivated(true);

        return adminUser;
    }
}
