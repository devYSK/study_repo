package com.fastcampus.ecommerce.admin.domain.user;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class AdminUserDetail implements UserDetails {
    private AdminUser adminUser;
    private List<AdminUserRole> roles;
    private List<AdminUserPermission> permissions;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 권한 목록 - 실습용 권한은 1개
        GrantedAuthority permissionAuthority = new SimpleGrantedAuthority(this.adminUser.getPermission().name());
        authorities.add(permissionAuthority);

        // 역할 목록 - 실습용 역할은 1개
        GrantedAuthority roleAuthority = new SimpleGrantedAuthority("ROLE_" + this.adminUser.getRole().name());
        authorities.add(roleAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.adminUser.getPassword();
    }

    public String getUsername() {
        return this.adminUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.adminUser.isActivated();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.adminUser.isActivated();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.adminUser.isActivated();
    }

    @Override
    public boolean isEnabled() {
        return this.adminUser.isActivated();
    }

    public Iterable<String> getPermissionList() {
        return roles.stream().map(r -> r.name()).collect(Collectors.toList());
    }

    public Iterable<String> getRoleList() {
        return permissions.stream().map(p -> p.name()).collect(Collectors.toList());
    }
}
