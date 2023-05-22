package tobyspring.config.autoconfig;

import java.util.Map;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.util.ClassUtils;

public class MyOnClassCondition implements Condition {
	@Override
	public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
		Map<String, Object> annotationAttributes = metadata.getAnnotationAttributes(
			ConditionalMyOnClass.class.getName());

		String value = (String)annotationAttributes.get("value");
		return ClassUtils.isPresent(value, context.getClassLoader());
	}

}
