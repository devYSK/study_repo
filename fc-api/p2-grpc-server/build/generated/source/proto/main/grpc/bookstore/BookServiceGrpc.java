package bookstore;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * 책서비스
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.60.1)",
    comments = "Source: bookstore.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class BookServiceGrpc {

  private BookServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "bookstore.BookService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<bookstore.Bookstore.AddBookRequest,
      bookstore.Bookstore.Book> getAddBookMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AddBook",
      requestType = bookstore.Bookstore.AddBookRequest.class,
      responseType = bookstore.Bookstore.Book.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<bookstore.Bookstore.AddBookRequest,
      bookstore.Bookstore.Book> getAddBookMethod() {
    io.grpc.MethodDescriptor<bookstore.Bookstore.AddBookRequest, bookstore.Bookstore.Book> getAddBookMethod;
    if ((getAddBookMethod = BookServiceGrpc.getAddBookMethod) == null) {
      synchronized (BookServiceGrpc.class) {
        if ((getAddBookMethod = BookServiceGrpc.getAddBookMethod) == null) {
          BookServiceGrpc.getAddBookMethod = getAddBookMethod =
              io.grpc.MethodDescriptor.<bookstore.Bookstore.AddBookRequest, bookstore.Bookstore.Book>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AddBook"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  bookstore.Bookstore.AddBookRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  bookstore.Bookstore.Book.getDefaultInstance()))
              .setSchemaDescriptor(new BookServiceMethodDescriptorSupplier("AddBook"))
              .build();
        }
      }
    }
    return getAddBookMethod;
  }

  private static volatile io.grpc.MethodDescriptor<bookstore.Bookstore.GetBookDetailsRequest,
      bookstore.Bookstore.Book> getGetBookDetailsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetBookDetails",
      requestType = bookstore.Bookstore.GetBookDetailsRequest.class,
      responseType = bookstore.Bookstore.Book.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<bookstore.Bookstore.GetBookDetailsRequest,
      bookstore.Bookstore.Book> getGetBookDetailsMethod() {
    io.grpc.MethodDescriptor<bookstore.Bookstore.GetBookDetailsRequest, bookstore.Bookstore.Book> getGetBookDetailsMethod;
    if ((getGetBookDetailsMethod = BookServiceGrpc.getGetBookDetailsMethod) == null) {
      synchronized (BookServiceGrpc.class) {
        if ((getGetBookDetailsMethod = BookServiceGrpc.getGetBookDetailsMethod) == null) {
          BookServiceGrpc.getGetBookDetailsMethod = getGetBookDetailsMethod =
              io.grpc.MethodDescriptor.<bookstore.Bookstore.GetBookDetailsRequest, bookstore.Bookstore.Book>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetBookDetails"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  bookstore.Bookstore.GetBookDetailsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  bookstore.Bookstore.Book.getDefaultInstance()))
              .setSchemaDescriptor(new BookServiceMethodDescriptorSupplier("GetBookDetails"))
              .build();
        }
      }
    }
    return getGetBookDetailsMethod;
  }

  private static volatile io.grpc.MethodDescriptor<bookstore.Bookstore.ListBooksRequest,
      bookstore.Bookstore.Book> getListBooksMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ListBooks",
      requestType = bookstore.Bookstore.ListBooksRequest.class,
      responseType = bookstore.Bookstore.Book.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<bookstore.Bookstore.ListBooksRequest,
      bookstore.Bookstore.Book> getListBooksMethod() {
    io.grpc.MethodDescriptor<bookstore.Bookstore.ListBooksRequest, bookstore.Bookstore.Book> getListBooksMethod;
    if ((getListBooksMethod = BookServiceGrpc.getListBooksMethod) == null) {
      synchronized (BookServiceGrpc.class) {
        if ((getListBooksMethod = BookServiceGrpc.getListBooksMethod) == null) {
          BookServiceGrpc.getListBooksMethod = getListBooksMethod =
              io.grpc.MethodDescriptor.<bookstore.Bookstore.ListBooksRequest, bookstore.Bookstore.Book>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ListBooks"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  bookstore.Bookstore.ListBooksRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  bookstore.Bookstore.Book.getDefaultInstance()))
              .setSchemaDescriptor(new BookServiceMethodDescriptorSupplier("ListBooks"))
              .build();
        }
      }
    }
    return getListBooksMethod;
  }

  private static volatile io.grpc.MethodDescriptor<bookstore.Bookstore.SearchBooksByAuthorRequest,
      bookstore.Bookstore.Book> getSearchBooksByAuthorMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "SearchBooksByAuthor",
      requestType = bookstore.Bookstore.SearchBooksByAuthorRequest.class,
      responseType = bookstore.Bookstore.Book.class,
      methodType = io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
  public static io.grpc.MethodDescriptor<bookstore.Bookstore.SearchBooksByAuthorRequest,
      bookstore.Bookstore.Book> getSearchBooksByAuthorMethod() {
    io.grpc.MethodDescriptor<bookstore.Bookstore.SearchBooksByAuthorRequest, bookstore.Bookstore.Book> getSearchBooksByAuthorMethod;
    if ((getSearchBooksByAuthorMethod = BookServiceGrpc.getSearchBooksByAuthorMethod) == null) {
      synchronized (BookServiceGrpc.class) {
        if ((getSearchBooksByAuthorMethod = BookServiceGrpc.getSearchBooksByAuthorMethod) == null) {
          BookServiceGrpc.getSearchBooksByAuthorMethod = getSearchBooksByAuthorMethod =
              io.grpc.MethodDescriptor.<bookstore.Bookstore.SearchBooksByAuthorRequest, bookstore.Bookstore.Book>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.SERVER_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "SearchBooksByAuthor"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  bookstore.Bookstore.SearchBooksByAuthorRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  bookstore.Bookstore.Book.getDefaultInstance()))
              .setSchemaDescriptor(new BookServiceMethodDescriptorSupplier("SearchBooksByAuthor"))
              .build();
        }
      }
    }
    return getSearchBooksByAuthorMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BookServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookServiceStub>() {
        @java.lang.Override
        public BookServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookServiceStub(channel, callOptions);
        }
      };
    return BookServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BookServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookServiceBlockingStub>() {
        @java.lang.Override
        public BookServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookServiceBlockingStub(channel, callOptions);
        }
      };
    return BookServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BookServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BookServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BookServiceFutureStub>() {
        @java.lang.Override
        public BookServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BookServiceFutureStub(channel, callOptions);
        }
      };
    return BookServiceFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * 책서비스
   * </pre>
   */
  public interface AsyncService {

    /**
     */
    default void addBook(bookstore.Bookstore.AddBookRequest request,
        io.grpc.stub.StreamObserver<bookstore.Bookstore.Book> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAddBookMethod(), responseObserver);
    }

    /**
     */
    default void getBookDetails(bookstore.Bookstore.GetBookDetailsRequest request,
        io.grpc.stub.StreamObserver<bookstore.Bookstore.Book> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetBookDetailsMethod(), responseObserver);
    }

    /**
     */
    default void listBooks(bookstore.Bookstore.ListBooksRequest request,
        io.grpc.stub.StreamObserver<bookstore.Bookstore.Book> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getListBooksMethod(), responseObserver);
    }

    /**
     */
    default void searchBooksByAuthor(bookstore.Bookstore.SearchBooksByAuthorRequest request,
        io.grpc.stub.StreamObserver<bookstore.Bookstore.Book> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getSearchBooksByAuthorMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service BookService.
   * <pre>
   * 책서비스
   * </pre>
   */
  public static abstract class BookServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return BookServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service BookService.
   * <pre>
   * 책서비스
   * </pre>
   */
  public static final class BookServiceStub
      extends io.grpc.stub.AbstractAsyncStub<BookServiceStub> {
    private BookServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookServiceStub(channel, callOptions);
    }

    /**
     */
    public void addBook(bookstore.Bookstore.AddBookRequest request,
        io.grpc.stub.StreamObserver<bookstore.Bookstore.Book> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAddBookMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getBookDetails(bookstore.Bookstore.GetBookDetailsRequest request,
        io.grpc.stub.StreamObserver<bookstore.Bookstore.Book> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetBookDetailsMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void listBooks(bookstore.Bookstore.ListBooksRequest request,
        io.grpc.stub.StreamObserver<bookstore.Bookstore.Book> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getListBooksMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void searchBooksByAuthor(bookstore.Bookstore.SearchBooksByAuthorRequest request,
        io.grpc.stub.StreamObserver<bookstore.Bookstore.Book> responseObserver) {
      io.grpc.stub.ClientCalls.asyncServerStreamingCall(
          getChannel().newCall(getSearchBooksByAuthorMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service BookService.
   * <pre>
   * 책서비스
   * </pre>
   */
  public static final class BookServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<BookServiceBlockingStub> {
    private BookServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public bookstore.Bookstore.Book addBook(bookstore.Bookstore.AddBookRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAddBookMethod(), getCallOptions(), request);
    }

    /**
     */
    public bookstore.Bookstore.Book getBookDetails(bookstore.Bookstore.GetBookDetailsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetBookDetailsMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<bookstore.Bookstore.Book> listBooks(
        bookstore.Bookstore.ListBooksRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getListBooksMethod(), getCallOptions(), request);
    }

    /**
     */
    public java.util.Iterator<bookstore.Bookstore.Book> searchBooksByAuthor(
        bookstore.Bookstore.SearchBooksByAuthorRequest request) {
      return io.grpc.stub.ClientCalls.blockingServerStreamingCall(
          getChannel(), getSearchBooksByAuthorMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service BookService.
   * <pre>
   * 책서비스
   * </pre>
   */
  public static final class BookServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<BookServiceFutureStub> {
    private BookServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BookServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BookServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<bookstore.Bookstore.Book> addBook(
        bookstore.Bookstore.AddBookRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAddBookMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<bookstore.Bookstore.Book> getBookDetails(
        bookstore.Bookstore.GetBookDetailsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetBookDetailsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_ADD_BOOK = 0;
  private static final int METHODID_GET_BOOK_DETAILS = 1;
  private static final int METHODID_LIST_BOOKS = 2;
  private static final int METHODID_SEARCH_BOOKS_BY_AUTHOR = 3;

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
        case METHODID_ADD_BOOK:
          serviceImpl.addBook((bookstore.Bookstore.AddBookRequest) request,
              (io.grpc.stub.StreamObserver<bookstore.Bookstore.Book>) responseObserver);
          break;
        case METHODID_GET_BOOK_DETAILS:
          serviceImpl.getBookDetails((bookstore.Bookstore.GetBookDetailsRequest) request,
              (io.grpc.stub.StreamObserver<bookstore.Bookstore.Book>) responseObserver);
          break;
        case METHODID_LIST_BOOKS:
          serviceImpl.listBooks((bookstore.Bookstore.ListBooksRequest) request,
              (io.grpc.stub.StreamObserver<bookstore.Bookstore.Book>) responseObserver);
          break;
        case METHODID_SEARCH_BOOKS_BY_AUTHOR:
          serviceImpl.searchBooksByAuthor((bookstore.Bookstore.SearchBooksByAuthorRequest) request,
              (io.grpc.stub.StreamObserver<bookstore.Bookstore.Book>) responseObserver);
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
          getAddBookMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              bookstore.Bookstore.AddBookRequest,
              bookstore.Bookstore.Book>(
                service, METHODID_ADD_BOOK)))
        .addMethod(
          getGetBookDetailsMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              bookstore.Bookstore.GetBookDetailsRequest,
              bookstore.Bookstore.Book>(
                service, METHODID_GET_BOOK_DETAILS)))
        .addMethod(
          getListBooksMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              bookstore.Bookstore.ListBooksRequest,
              bookstore.Bookstore.Book>(
                service, METHODID_LIST_BOOKS)))
        .addMethod(
          getSearchBooksByAuthorMethod(),
          io.grpc.stub.ServerCalls.asyncServerStreamingCall(
            new MethodHandlers<
              bookstore.Bookstore.SearchBooksByAuthorRequest,
              bookstore.Bookstore.Book>(
                service, METHODID_SEARCH_BOOKS_BY_AUTHOR)))
        .build();
  }

  private static abstract class BookServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BookServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return bookstore.Bookstore.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BookService");
    }
  }

  private static final class BookServiceFileDescriptorSupplier
      extends BookServiceBaseDescriptorSupplier {
    BookServiceFileDescriptorSupplier() {}
  }

  private static final class BookServiceMethodDescriptorSupplier
      extends BookServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    BookServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (BookServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BookServiceFileDescriptorSupplier())
              .addMethod(getAddBookMethod())
              .addMethod(getGetBookDetailsMethod())
              .addMethod(getListBooksMethod())
              .addMethod(getSearchBooksByAuthorMethod())
              .build();
        }
      }
    }
    return result;
  }
}
