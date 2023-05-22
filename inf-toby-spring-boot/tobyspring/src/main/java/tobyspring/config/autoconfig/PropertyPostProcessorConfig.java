package tobyspring.config.autoconfig;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;

import tobyspring.config.MyAutoConfiguration;

@MyAutoConfiguration
public class PropertyPostProcessorConfig {

	@Bean
	BeanPostProcessor propertyPostProcessor(Environment environment) {
		return new BeanPostProcessor() {
			@Override
			public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
				MyConfigurationProperties annotation = AnnotationUtils.findAnnotation(bean.getClass(),
					MyConfigurationProperties.class);

				Map<String, Object> attrs = AnnotationUtils.getAnnotationAttributes(annotation);

				String prefix = (String)attrs.get("prefix");

				if (annotation == null) return bean;

				return Binder.get(environment).bindOrCreate(prefix, bean.getClass());
			}
		};
	}
}
