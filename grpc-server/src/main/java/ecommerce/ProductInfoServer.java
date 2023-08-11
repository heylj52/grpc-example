package ecommerce;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;

public class ProductInfoServer {

  public static void main(String[] args) throws IOException, InterruptedException {
    int port = 50051;
    Server server = ServerBuilder
        .forPort(port) // 해당 포트로 서버가 바인딩해 메시지를 수신함
        .addService(new ProductInfoImpl()) // 비즈니스 로직을 구현한 서비스 추가
        .build().start();
    System.out.println("Server started, listening on " + port);

    Runtime.getRuntime().addShutdownHook(new Thread(() -> { // JVM 종료 시 gRPC 서버를 종료하기 위한 런타임 종료 훅 추가
      System.err.println("Shutting down gRPC server since JVM is shutting down");
      if (server != null) {
        server.shutdown();
      }
      System.err.println("Server shut down");
    }));
    server.awaitTermination(); // 서버 스레드는 서버가 종료될 때까지 대기
  }

}
