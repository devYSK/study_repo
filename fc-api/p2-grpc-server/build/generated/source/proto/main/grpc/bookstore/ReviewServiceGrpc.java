package bookstore;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * 리뷰 서비스
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.60.1)",
    comments = "Source: bookstore.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class ReviewServiceGrpc {

  private ReviewServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "bookstore.ReviewService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<bookstore.Bookstore.GetReviewsRequest,
      bookstore.Bookstore.Review> getGetReviewsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetReviews",
      requestType = bookstore.Bookstore.GetReviewsRequest.class,
      responseType = bookstore.Bookstore.Review.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<bookstore.Bookstore.GetReviewsRequest,
      bookstore.Bookstore.Review> getGetReviewsMethod() {
    io.grpc.MethodDescriptor<bookstore.Bookstore.GetReviewsRequest, bookstore.Bookstore.Review> getGetReviewsMethod;
    if ((getGetReviewsMethod = ReviewServiceGrpc.getGetReviewsMethod) == null) {
      synchronized (ReviewServiceGrpc.class) {
        if ((getGetReviewsMethod = ReviewServiceGrpc.getGetReviewsMethod) == null) {
          ReviewServiceGrpc.getGetReviewsMethod = getGetReviewsMethod =
              io.grpc.MethodDescriptor.<bookstore.Bookstore.GetReviewsRequest, bookstore.Bookstore.Review>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetReviews"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  bookstore.Bookstore.GetReviewsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  bookstore.Bookstore.Review.getDefaultInstance()))
              .setSchemaDescriptor(new ReviewServiceMethodDescriptorSupplier("GetReviews"))
              .build();
        }
      }
    }
    return getGetReviewsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static ReviewServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReviewServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReviewServiceStub>() {
        @java.lang.Override
        public ReviewServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReviewServiceStub(channel, callOptions);
        }
      };
    return ReviewServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static ReviewServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReviewServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReviewServiceBlockingStub>() {
        @java.lang.Override
        public ReviewServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReviewServiceBlockingStub(channel, callOptions);
        }
      };
    return ReviewServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static ReviewServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<ReviewServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<ReviewServiceFutureStub>() {
        @java.lang.Override
        public ReviewServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new ReviewServiceFutureStub(channel, callOptions);
        }
      };
    return ReviewServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * 리뷰 서비스
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void getReviews(bookstore.Bookstore.GetReviewsRequest request,
        io.grpc.stub.StreamObserver<bookstore.Bookstore.Review> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetReviewsMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service ReviewService.
   * <pre>
   * 리뷰 서비스
   * </pre>
   */
  public static abstract class ReviewServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return ReviewServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service ReviewService.
   * <pre>
   * 리뷰 서비스
   * </pre>
   */
  public static final class ReviewServiceStub
      extends io.grpc.stub.AbstractAsyncStub<ReviewServiceStub> {
    private ReviewServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReviewServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReviewServiceStub(channel, callOptions);
    }

    /**
     */
    public void getReviews(bookstore.Bookstore.GetReviewsRequest request,
        io.grpc.stub.StreamObserver<bookstore.Bookstore.Review> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getGetReviewsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service ReviewService.
   * <pre>
   * 리뷰 서비스
   * </pre>
   */
  public static final class ReviewServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<ReviewServiceBlockingStub> {
    private ReviewServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReviewServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReviewServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public java.util.Iterator<bookstore.Bookstore.Review> getReviews(
        bookstore.Bookstore.GetReviewsRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getGetReviewsMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service ReviewService.
   * <pre>
   * 리뷰 서비스
   * </pre>
   */
  public static final class ReviewServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<ReviewServiceFutureStub> {
    private ReviewServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected ReviewServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new ReviewServiceFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_GET_REVIEWS = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_REVIEWS:
          serviceImpl.getReviews((bookstore.Bookstore.GetReviewsRequest) request,
              (io.grpc.stub.StreamObserver<bookstore.Bookstore.Review>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getGetReviewsMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              bookstore.Bookstore.GetReviewsRequest,
              bookstore.Bookstore.Review>(
                service, METHODID_GET_REVIEWS)))
        .build();
  }

  private static abstract class ReviewServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    ReviewServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return bookstore.Bookstore.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("ReviewService");
    }
  }

  private static final class ReviewServiceFileDescriptorSupplier
      extends ReviewServiceBaseDescriptorSupplier {
    ReviewServiceFileDescriptorSupplier() {}
  }

  private static final class ReviewServiceMethodDescriptorSupplier
      extends ReviewServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    ReviewServiceMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (ReviewServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new ReviewServiceFileDescriptorSupplier())
              .addMethod(getGetReviewsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
