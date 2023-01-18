# [Java] Nested Enum



Enum 안에 Enum 을 정의하려면 인터페이스를 활용해야한다.

```java
interface Country {
    Country USA = Americas.USA;

    enum Asia implements Country {
        Indian,
        China,
        SriLanka
    }
    enum Americas implements Country {
        USA,
        Brazil
    }
    enum Europe implements Country {
        UK,
        Ireland,
        France
    }
}
```



Enum을 바로 JSON으로 리턴하게 되면 **상수 name만 출력**이 됩니다.
저에게 필요했던건 **DB의 컬럼값으로 사용될 Enum의 name과 View Layer에서 출력될 title** 2개의 값이기 때문에 Enum을 인스턴스로 생성하기 위한 클래스 선언이 필요했습니다.

먼저 클래스의 생성자로 일관된 타입을 받기 위해 인터페이스를 하나 생성하였습니다.

![case4](https://techblog.woowahan.com/wp-content/uploads/img/2017-07-10/case4_enum_mapper_type.png)

값을 담을 클래스(VO)는 이 인터페이스를 생성자 인자로 받아 인스턴스를 생성하도록 합니다.

![case4](https://techblog.woowahan.com/wp-content/uploads/img/2017-07-10/case4_enum_mapper_value.png)

Enum은 미리 선언한 인터페이스를 구현(`implements`)만 하면 됩니다.

![case4](https://techblog.woowahan.com/wp-content/uploads/img/2017-07-10/case4_fee.png)

이젠 필요한 곳에서 Enum을 Value 클래스로 변환한 후, 전달하기만 하면 됩니다.

![case4](https://techblog.woowahan.com/wp-content/uploads/img/2017-07-10/case4_no_bean.png)

원했던대로, JSON 결과가 나오는 것을 확인할 수 있습니다.

![case4](https://techblog.woowahan.com/wp-content/uploads/img/2017-07-10/case4_result.png)

Enum을 중심으로 해서 View Layer와 Application, DB가 하나로 관리되도록 변경은 되었지만 한가지 아쉬운 점이 발견되었습니다.
필요할 때마다 Enum.values를 통해 **Value 인스턴스를 생성하는 과정이 반복되는 것** 이였습니다.
런타임에 Enum의 상수들이 변경될 일이 없기에, 관리 대상인 Enum들은 미리 Bean에 등록하여 사용하도록 변경해보았습니다.
Enum Value들을 담을 팩토리 클래스를 생성하고,

![case4](https://techblog.woowahan.com/wp-content/uploads/img/2017-07-10/case4_enum_mapper.png)

이를 Bean으로 등록하였습니다.

![case4](https://techblog.woowahan.com/wp-content/uploads/img/2017-07-10/case4_bean.png)

View Layer에서 사용하길 원하는 Enum 타입들은 `EnumMapper`라는 Bean에 등록하기만 하면 됩니다.