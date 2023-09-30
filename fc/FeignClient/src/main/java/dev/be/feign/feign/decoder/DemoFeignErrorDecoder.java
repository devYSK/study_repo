package dev.be.feign.feign.decoder;

import org.springframework.http.HttpStatus;

import feign.Response;
import feign.codec.ErrorDecoder;

public final class DemoFeignErrorDecoder implements ErrorDecoder {
    private final ErrorDecoder errorDecoder = new Default();

    @Override
    public Exception decode(String methodKey, Response response) {
        final HttpStatus httpStatus = HttpStatus.resolve(response.status());

        /**
         * [참고]
         * 외부 컴포넌트와 통신 시
         * 정의해놓은 예외 코드 일 경우엔 적절하게 핸들링하여 처리한다.
         */
        if (httpStatus == HttpStatus.NOT_FOUND) {
            System.out.println("[DemoFeignErrorDecoder] Http Status = " + httpStatus);
            throw new RuntimeException(String.format("[RuntimeException] Http Status is %s", httpStatus));
        }

        return errorDecoder.decode(methodKey, response);
    }

}
