package com.fastcampus.helloecommerceservice.domain.customer;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@RequiredArgsConstructor
public class CustomerDetail implements UserDetails {

    private Customer customer;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 퍼미션 목록
        GrantedAuthority permissionAuthority = new SimpleGrantedAuthority(this.customer.getPermission().name());
        authorities.add(permissionAuthority);

        // 역할 목록
        GrantedAuthority roleAuthority = new SimpleGrantedAuthority("ROLE_" + this.customer.getRole().name());
        authorities.add(roleAuthority);

        return authorities;
    }

    @Override
    public String getPassword() {
        return this.customer.getPassword();
    }

    @Override
    public String getUsername() {
        return this.customer.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.customer.isActivated();
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.customer.isActivated();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.customer.isActivated();
    }

    @Override
    public boolean isEnabled() {
        return this.customer.isActivated();
    }
}
