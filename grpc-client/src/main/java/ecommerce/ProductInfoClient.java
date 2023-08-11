package ecommerce;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class ProductInfoClient {

  public static void main(String[] args) {
    ManagedChannel channel = ManagedChannelBuilder
        .forAddress("localhost", 50051) // 연결하려는 서버 주소와 포트를 지정해 gRPC 채널 생성. 포트가 50051인 서버에 연결
        .usePlaintext() // 평문을 사용해 보안되지 않은 연결 사용
        .build();

    // 새로 생성된 채널을 사용해 클라이언트 스텁을 만듦.
    // 2가지 유형 사용 가능
    // 1. 서버의 응답을 받을 때까지 대기하는 BlockingStub(여기서는 간단하게 구현)
    // 2. 서버의 응답을 기다리지 않고 옵저버를 등록해 응답을 받는 NonBlockingStub
    ProductInfoGrpc.ProductInfoBlockingStub stub = ProductInfoGrpc.newBlockingStub(channel);

    // 제품 정보를 만들어 제품 생성 메서드(addProduct)를 호출함. 응답값은 제품 ID
    // 서버에 요청하지만 메서드를 호출하듯이 사용할 수 있음
    ProductInfoOuterClass.ProductID productID = stub.addProduct(
        ProductInfoOuterClass.Product.newBuilder()
            .setName("Apple iPhone 13")
            .setDescription(
                "Meet Apple iPhone 13. All-new dual-camera system with Ultra Wide and Night mode.")
            .setPrice(1000.0f)
            .build()
    );
    System.out.println(productID.getValue());

    // 제품ID로 제품 조회 메서드(getProduct)를 호출함. 응답값은 제품 정보
    ProductInfoOuterClass.Product product = stub.getProduct(productID);
    System.out.println(product.toString());
    channel.shutdown(); // 모든 작업이 완료되면 연결을 종료해 사용한 네트워크 리소스를 반환함
  }
}
