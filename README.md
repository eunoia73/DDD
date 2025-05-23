## 1. 도메인 모델 시작하기 

`도메인` - 해결하고자 하는 문제 영역 <br>
`도메인 모델` - 도메인 자체를 이해하기 위한 개념 모델

프로젝트 초기에는 개요 수준의 개념 모델로 도메인에 대한 전체 윤곽을 이해하는데 집중하고, 구현하는 과정에서 개념 모델을 구현 모델로 점짐적 발전시켜야한다.

### 객체 기반 주문 도메인 모델

<img src="https://github.com/user-attachments/assets/90a52a2b-440b-4ba8-bc8e-096276b893e4" width="80%" height="80%">

> #### 요구사항
> * 최소 한 종류 이상의 상품을 주문해야 한다.
> * 한 상품을 한 개 이상 주문할 수 있다.
> * 총 주문 금액은 각 상품의 구매 가격 합을 모두 더한 금액이다.
> * 주문할 때 배송지 정보를 반드시 지정해야 한다.
> * 배송지 정보는 받는 사람 이름, 전화번호, 주소로 구성된다. 
> * 출고를 하면 배송지를 변경할 수 없다.
> * 출고 전에 주문을 취소할 수 있다. <br> → 출고 상태 표현이 필요하다
> * 고객이 결제를 완료하기 전에는 상품을 준비하지 않는다. 
<br> → 결제 완료 전, 후 상태가 필요하다. 

핵심 규칙을 구현한 코드는 도메인 모델에만 위치하기 때문에 규칙이 바뀌거나 규칙을 확장해야 할 때 다른 코드에 영향을 덜 주고 변경 내역을 모델에 반영할 수 있게 된다.

### 엔티티와 벨류
엔티티
* 각 엔티티는 서로 다른 식별자를 가진다. 고유한 식별자(UUID, 일련번호 등)로 구분한다.
* 필드 값이 달라도 id가 같으면 같은 객체로 간주한다.

값 객체 (Value Object)
* 값 자체가 동일하면 같은 객체로 간주한다.
* 개념적으로 완전한 하나를 표현할 때 사용한다.
* 값으로 취급하기 위해서 불변성(final, setter 금지), 동등성(equals, hashCode 재정의 필요), 유효성 검증 등을 보장해야한다.

## 2. 아키텍처 개요
표현 → 응용 → 도메인 → 인프라스트럭처 <br>
1) 표현 - Controller, View
2) 응용 - Service
3) 도메인 - Entity, Value Object, Domain Service
4) 인프라스트럭처 - Repository 구현체, 외부 API, DB <br>

#### * DIP 의존성 역전 원칙
CalculateDiscountService(고수준)
1. 고객 정보를 구한다. → RDBMS에서 JPA로 구한다.(저수준)
2. 룰을 이용해서 할인 금액을 구한다. → Drools로 룰을 적용한다.(저수준)
→ 고수준의 모듈이 저수준의 모듈에 의존하고 있다.

> 인프라스트럭처에 의존하게 되면 <br> 1.테스트의 어려움 <br> 2.기능 확장의 어려움(구현 방식 변경 어려움) <br> → DIP로 해결 가능하다. 

CalculateDiscountService(고수준) → (interface)RuleDiscounter(고수준) <br>
`                                           `↑ <br>
`                                `DroolsRuleDiscounter(구현체, 저수준)
 → 고수준 모듈은 저수준 모듈에 의존하지 않고 추상화된 인터페이스에 의존한다.
 
> * 고수준 모듈 -  핵심로직. 추상적 
> * 저수준 모듈 -  구현 세부 사항. 구체적

### * 도베인 영역의 주요 구성요소
| 요소           | 설명                       | 예시                         |
|---------------------|--------------------------------------------|------------------------------------------|
| **엔티티**            | 고유의 식별자를 갖는 객체. 도메인의 고유한 개념 표현 | 주문, 회원, 상품 등     |
| **벨류**          | 식별자를 갖지 않는 객체. 개념적으로 하나인 값을 표현. 불변   | 주소Address, 금액Money 등       |
| **애그리거트**            | 연관된 엔티티와 벨류 객체를 개념적으로 하나로 묶은 것     | 주문 애그리거트 - Order 엔티티, OrderLine 벨류, Orderer 벨류 |
| **리포지터리**          | 도메인 모델의 영속성 처리          |     |
| **도메인 서비스** | 특정 엔티티에 속하지 않은 도메인 로직 제공  | 할인 금액 계산 - 상품, 쿠폰, 회원등급, 구매금액 등 여러 엔티티와 벨류 필요로하는 경우 |

### DB 테이블의 엔티티 VS 도메인 모델의 엔티티 <br>
* 도메인 모델의 엔티티
  1) 데이터와 함께 도메인 기능을 함께 제공
  2) 두 개 이상의 데이터가 개념적으로 하나인 경우 벨류 타입을 이용해서 표현가능 → 다른 테이블에 벨류 타입 정보 분리해서 저장

## 3. 애그리거트
관련된 객체를 하나의 군으로 묶어주는 것. 모델을 이해하는데 도움을 주고 일관성을 관리하는 기준이 됨 <br>
한 애그리거트에 속한 구성요소는 다른 애그리거트에 속하지 않는다. 주로 함께 생성되고 함께 제거된다. <br>
`루트 엔티티` - 애그리거트에 속한 모든 객체가 일관된 상태를 유지하기 위해 애그리거트 전체를 관리할 주체

### 애그리거트 루트를 통해서만 도메인 로직을 구현하게하기 위해 도메인 모델에 습관적으로 적용해야 할 것
> * set메서드를 public 범위로 만들지 않는다. → 애그리거트 외부에서 애그리거트의 객체를 직접 변경하면 안 된다.
> * 밸류 타입은 불변으로 구현한다. → 밸류 객체의 값을 변경하는 방법은 애그리거트 루트의 메서드에 새로운 밸류 객체를 전달해서 값을 변경하는 방법밖에 없다.

``` java
public class Order {
    private ShippingInfo shippingInfo;

    public void changeShippingInfo(ShippingInfo newShippingInfo) {
        verifyNotYetShipped();
        setShippingInfo(newShippingInfo);
    }

    // set 메서드의 접근 허용 범위는 private이다.
    private void setShippingInfo(ShippingInfo newShippingInfo) {
        if (newShippingInfo == null) {
            throw new IllegalArgumentException("배송지 정보가 없습니다.");
        }
        // 벨류 타입의 데이터를 변경할 때는 새로운 객체로 교환한다.
        this.shippingInfo = newShippingInfo;
    }
}
 

```

### 필드를 이용해서 다른 애그리거트를 직접 참조하는 것의 위험성
JPA는 @ManyToOne, @OneToOne 같이 연관된 객체를 로딩할 수 있으므로 다른 애그리거트를 쉽게 참조할 수 있다.
```java
order.getOrderer().getMember().getId()
```
1) 편한 탐색 오용 - 다른 애그리거트의 상태를 변경하는 유혹에 빠지기 쉽다. 하지만, 한 애그리거트가 관리하는 범위는 자기 자신으로 한정해야한다.
2) 성능에 대한 고민 - JPA 지연(Lazy) 로딩, 즉시(Eager) 로딩
3) 확장 어려움 - 도메인별로 다른 DBMS를 사용할 경우 JPA와 같은 단일 기술 사용할 수 없음 <br>
→ ID를 이용해서 다른 애그리거트 참조하는 것이 적절할 수 있다.

### ID를 이용한 다른 애그리거트 참조
- 애그리거트 간의 의존 제거하므로 응집도를 높여줌
- 응용 서비스에서 필요한 애그리거트를 로딩하므로 애그리거트 수준에서 지연 로딩을 하는 것과 동일한 결과를 만든다.
- 애그리거트별로 다른 구현 기술 사용 가능(조회 성능 중요한 상품 애그리거트 - NoSQL)

** N + 1 조회 문제
조회 대상이 N개일 때 N개를 읽어오는 한 번의 쿼리와 연관된 데이터를 읽어오는 쿼리를 N번 실행한다.
→ 조회 전용 쿼리를 사용하면 된다.



<hr>
[도메인 주도 개발 시작하기] 책을 읽고 정리한 내용입니다.
