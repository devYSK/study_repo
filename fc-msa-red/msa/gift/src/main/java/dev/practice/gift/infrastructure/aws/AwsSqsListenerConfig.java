package dev.practice.gift.infrastructure.aws;

import static io.awspring.cloud.sqs.listener.acknowledgement.handler.AcknowledgementMode.*;

import java.util.Collection;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.awspring.cloud.sqs.config.SqsMessageListenerContainerFactory;
import io.awspring.cloud.sqs.listener.errorhandler.ErrorHandler;
import io.awspring.cloud.sqs.support.converter.SqsMessagingMessageConverter;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsSqsListenerConfig {

    private final ObjectMapper objectMapper;

    @Bean("defaultSqsListenerContainerFactory")
    public SqsMessageListenerContainerFactory<Object> defaultSqsListenerContainerFactory(
        SqsAsyncClient sqsAsyncClient,
        ErrorHandler<Object> sqsErrorHandler
    ) {
        return SqsMessageListenerContainerFactory.builder()
            .sqsAsyncClient(sqsAsyncClient)
            .configure(options -> options.acknowledgementMode(ALWAYS))
            .errorHandler(sqsErrorHandler)
            .build();
    }

    @Bean
    public ErrorHandler<Object> sqsErrorHandler(ApplicationEventPublisher eventPublisher) {
        return new ErrorHandler<>() {
            @SneakyThrows
            @Override
            public void handle(
                @Nonnull Message<Object> message,
                @Nonnull Throwable cause
            ) {
                var payload = objectMapper.writeValueAsString(message.getPayload());

                var messageTemplate = "SQS 메시지 리스닝 중 예외 발생. payload={{}}";

                log.error(messageTemplate, payload, cause);

            }

            @SneakyThrows
            @Override
            public void handle(
                @Nonnull Collection<Message<Object>> messages,
                @Nonnull Throwable cause
            ) {
                var payload = objectMapper.writeValueAsString(messages.stream().map(Message::getPayload).toList());
                var messageTemplate = "SQS 메시지 리스닝 중 예외 발생. payload={{}}";

                log.error(messageTemplate, payload, cause);
            }
        };
    }

    @Bean
    public SqsMessagingMessageConverter sqsMessagingMessageConverter(ObjectMapper objectMapper) {
        var sqsMessagingMessageConverter = new SqsMessagingMessageConverter();

        var jackson2MessageConverter = new MappingJackson2MessageConverter();
        jackson2MessageConverter.setObjectMapper(objectMapper);
        jackson2MessageConverter.setSerializedPayloadClass(String.class);
        sqsMessagingMessageConverter.setPayloadMessageConverter(jackson2MessageConverter);

        return sqsMessagingMessageConverter;
    }

    @Bean
    public ThreadPoolTaskExecutor messageThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setThreadNamePrefix("sqs-");
        taskExecutor.setCorePoolSize(8);
        taskExecutor.setMaxPoolSize(100);
        taskExecutor.afterPropertiesSet();
        return taskExecutor;
    }
}
