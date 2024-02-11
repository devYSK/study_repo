package com.fastcampus.ecommerce.admin.service;

import com.fastcampus.ecommerce.admin.domain.user.AdminUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class AdminDetailUserServiceTest {

    private AdminUserDetailService adminUserDetailService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void save() {
        // given
        String testerEmail = "test@test.com";
        AdminUser adminUser = new AdminUser();
        adminUser.setId(0L);
        adminUser.setEmail(testerEmail);
        adminUser.setPassword("1234567890");
        adminUser.setUsername("Tester");

        // when
        AdminUser savedAdminUser = adminUserDetailService.save(adminUser);

        // then
        assertThat(savedAdminUser).isNotNull();
        assertThat(savedAdminUser.getEmail()).isEqualTo(testerEmail);
    }

    @Test
    void exist() {
    }
}