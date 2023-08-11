package ecommerce;

import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 플러그인으로 생성된 추상클래스(ProductInfoGrpc.ProductInfoImplBase)를 상속받아 메서드에 비즈니스 로직 추가
 */
public class ProductInfoImpl extends ProductInfoGrpc.ProductInfoImplBase {

  private Map productMap = new HashMap<String, ProductInfoOuterClass.Product>();

  /**
   * 제품 추가 메서드
   * @param request          : 추가할 제품
   * @param responseObserver : 클라이언트에게 응답을 보내고 스트림을 닫는데 사용
   */
  @Override
  public void addProduct(ProductInfoOuterClass.Product request,
      StreamObserver<ProductInfoOuterClass.ProductID> responseObserver) {
    UUID uuid = UUID.randomUUID();
    String randomUUIDString = uuid.toString();
    request = request.toBuilder().setId(randomUUIDString).build();
    productMap.put(randomUUIDString, request);
    ProductInfoOuterClass.ProductID id = ProductInfoOuterClass.ProductID.newBuilder()
        .setValue(randomUUIDString).build();
    responseObserver.onNext(id); // 클라이언트에게 응답을 보냄
    responseObserver.onCompleted(); // 스트림을 닫아 클라이언트 호출을 종료
  }

  /**
   * 제품 조회 메서드
   * @param request
   * @param responseObserver
   */
  @Override
  public void getProduct(ProductInfoOuterClass.ProductID request,
      StreamObserver<ProductInfoOuterClass.Product> responseObserver) {
    String id = request.getValue();
    if (productMap.containsKey(id)) {
      responseObserver.onNext((ProductInfoOuterClass.Product) productMap.get(id));
      responseObserver.onCompleted();
    } else {
      responseObserver.onError(new StatusException(Status.NOT_FOUND)); // 클라이언트에게 에러를 보냄
    }
  }

}
