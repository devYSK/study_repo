package hello.advanced.trace;

public interface LogTrace { // 향후 다양한 구현체로 변경

    TraceStatus begin(String message);

    void end(TraceStatus status);

    void exception(TraceStatus status, Exception e);

}
