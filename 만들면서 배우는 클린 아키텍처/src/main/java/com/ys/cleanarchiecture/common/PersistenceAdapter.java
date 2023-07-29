package com.ys.cleanarchiecture.common;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import com.ys.cleanarchiecture.adapter.persistence.AccountMapper;
import com.ys.cleanarchiecture.adapter.persistence.AccountPersistenceAdapter;
import com.ys.cleanarchiecture.adapter.persistence.ActivityRepository;
import com.ys.cleanarchiecture.domain.AccountRepository;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface PersistenceAdapter {

	/**
	 * The value may indicate a suggestion for a logical component name,
	 * to be turned into a Spring bean in case of an autodetected component.
	 *
	 * @return the suggested component name, if any (or empty String otherwise)
	 */
	@AliasFor(annotation = Component.class)
	String value() default "";

}

@Configuration
@EnableJpaRepositories
class PersistenceAdapterConfiguration {

	@Bean
	AccountPersistenceAdapter accountPersistenceAdapter(
		AccountRepository accountRepository,
		ActivityRepository activityRepository,
		AccountMapper accountMapper) {

		return new AccountPersistenceAdapter(
		accountRepository, activityRepository, accountMapper);
	}

	@Bean
	AccountMapper accountMapper() {
		return new AccountMapper();
	}
}