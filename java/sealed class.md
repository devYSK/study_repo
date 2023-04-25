# Sealed class

Sealed 클래스/인터페이스 는 상속하거나(extends), 구현(implements)할 클래스를 지정하여 해당 클래스들만 상속/구현이 가능하도록 제한하는 기능입니다.

JDK 15부터 추가된 키워드로, 자바 17에 정식으로 확정되었습니다. 



**Sealed Class를 지원하는 목표** 

- 개발자가 어떤 class 또는 interface가 해당 클래스를 상속받는지를 쉽게 알 수 있고 제한할 수 있다.
- class 또는 interface 작성자가 구현을 담당하는 코드를 제어할 수 있게 한다.
- superclass 사용을 제한하기 위해 access modifier 보다 더 선언적인 방법을 제공한다.
- pattern의 철저한 분석을 위한 기반을 제공하여 pattern matching을 지원한다.



### 사용방법

super-class 에 sealed 키워드를 사용하고, permits 키워드 뒤에 해당 클래스를 상속받을 sub-class 를 선언합니다.
Sealed 된 클래스를 활용하기 위해서는 같은 모듈 혹은 같은 패키지 안에 존재 해야 합니다.

활용 권한(permits)을 받은 sub-class 는 sealed, non-sealed , final 총 세가지 종류로 나누어집니다

```java
public sealed interface CarBrand permits Hyundai, Kia{}

public final class Hyundai implements CarBrand {}
public non-sealed class Kia implements CarBrand {}
```

Sealed Class는 몇 가지 제약 사항을 두고 있습니다.

[사용 권한을 받은 하위 클래스를 Permitted Subclass 라고 합니다.]

- 상속/구현하는 클래스는 final, non-sealed, sealed 중 하나로 선언되어야 한다.
- Permitted Subclass들은 동일한 module에 속해야 하며 명시되지 않은(이름이 지정되지 않은) module에 선언 시에는 동일한 package 내에 속해야 한다.
- Permitted Subclass는 Sealed Class를 직접 확장해야 한다.
  - 모든 permit 된 subclass는 modifier를 사용하여 superclass에 의해 시작된 봉인을 전파하는 방법을 설명해야 한다.
    - permit 된 subclass는 class 계층 구조의 더 이상 확장되지 않도록 하기 위해 final로 선언할 수 있다. (record class는 암시적으로 final 선언됨)
    - permit 된 subclass는 sealed를 선언하여 superclass가 의도한 것보다 더 확장될 수 있지만 제한된 방식이다.
    - permit 된 subclass는 알 수 없는 subclass에 의한 확장에 대해 열린 상태로 되돌아가도록 non-sealed로 선언될 수 있다. (non-sealed modifier는 Java에 의해 제안된 최초의 하이픈 연결 키워드이다.)





class가 sealed class를 extends 하지만 permit 대상 class가 아닌 경우 컴파일 에러가 발생합니다.

또한.  sealed 또는 non-sealed 인 class는 abstract class일 수 있으며 abstract member를 가질 수 있습니다.

그리고, sealed class는 final이 아닌 sealed 또는 non-sealed인 경우 abstract subclass를 허용할 수 있습니다.



### Non-Sealed Class

non-sealed Class로 선언하고 이를 상속하는 클래스는 전혀 에러를 발생시키지 않습니다.

```java
public non-sealed class Kia implements CarBrand {}

public class K8 extends Kia {}

// 가능
CarBrand brand = new K8();
```

개발자가 인지하지도 못할 무분별한 확장을 막은 것이지 제한적이고 인식범위 내에 있는 확장은 막지 않은 것 같습니다. 



## switch와 Sealed의 조합

[JEP406](#https://openjdk.org/jeps/406) 의 switch 문 패턴 매칭 덕분에 sealed 클래스를 편하게 사용할 수 있습니다.

f-else chain으로 sealed class의 instance를 검사하는 대신 사용자 코드는 pattern으로 강화된 switch를 사용할 수 있습니다.

다음 예제를 봅시다

```java
Shape rotate(Shape shape, double angle) {
        if (shape instanceof Circle) return shape;
        else if (shape instanceof Rectangle) return shape;
        else if (shape instanceof Square) return shape;
        else throw new IncompatibleClassChangeError();
}
```

Java Compiler는 test instance가 Shape의 허용된 모든 subclass를 포함하는지 확인할 수 없습니다.
마지막 else 절은 실제로 연결할 수 없지만 compiler에서 확인할 수 없습니다.
또한 Square에 대한 if절이 없어도 compie-time error가 발생하지도 않습니다.



하지만 다음과 같이 사용할 수 있습니다.

```java
Shape rotate(Shape shape, double angle) {
    return switch (shape) {   // pattern matching switch
        case Circle c    -> c; 
        case Rectangle r -> shape.rotate(angle);
        case Square s    -> shape.rotate(angle);
        // no default needed!
    }
}
```

switch에 대한 pattern matching (JEP 406)과 함께 compiler는 Shape의 모든 허용된 subclass가 포함됨을 확인할 수 있으므로 default 절이나 다른 전체 pattern이 필요하지 않게 됩니다.

## Sealed 클래스의 장단점

실드 클래스의 장점

1. 높은 안정성: 실드 클래스를 사용하면 하위 클래스의 목록을 명시적으로 지정할 수 있으므로, 상속 구조에서의 불안정성을 줄일 수 있습니다.
2. 새로운 유형의 추상화: 실드 클래스는 추상 클래스와 인터페이스 사이의 새로운 유형의 추상화를 제공합니다. 이를 통해 더욱 유연한 코드를 작성할 수 있습니다.
3. 코드 가독성 향상: 실드 클래스를 사용하면 하위 클래스의 목록을 명시적으로 지정하기 때문에 코드의 가독성이 향상됩니다.
4. 개발자의 실수 방지: 하위 클래스 목록을 명시적으로 지정하므로, 하위 클래스를 추가하거나 삭제하는 과정에서 개발자의 실수를 방지할 수 있습니다.

실드 클래스의 단점

1. 복잡성 증가: 하위 클래스의 목록을 명시적으로 지정해야 하므로, 클래스 계층 구조가 복잡해질 수 있습니다.
2. 유지보수의 어려움: 하위 클래스의 목록이 변경될 경우, 해당 목록을 참조하는 코드를 모두 수정해야 합니다. 이로 인해 유지보수가 어려워질 수 있습니다.
3. 유연성 감소: 실드 클래스를 사용하면 하위 클래스를 제한할 수 있지만, 이는 유연성을 감소시킬 수 있습니다. 따라서 실드 클래스는 모든 경우에 적합하지는 않습니다.



### 참조

* https://luvstudy.tistory.com/152

* https://marrrang.tistory.com/82