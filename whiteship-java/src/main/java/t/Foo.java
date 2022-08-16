package t;

import java.time.LocalDateTime;

/**
 * @author : ysk
 */
class Foo {
    private int age;            // 인스턴스 변수
    private String name;        // 인스턴스 변수

    private String foobar;      // 인스턴스 변수

    private static String bar;  // 클래스변수

    public static final String FINAL_BAR = "finalBar";

    static {                    // 클래스 초기화 블록
        bar = "bar";
    }

    {                           // 인스턴스 초기화 블록
        foobar = "foobar";
    }

    public Foo() {              // 매개변수 없는 기본 생성자

    }

    public Foo(int age) {       // 매개변수 있는 생성자.
        this.age = age;
    }

    public void printAge() {    // 인스턴스 메서드
        System.out.println("age : " + age);
    }
    public static void printBar() { //클래스 메서드
        System.out.println("bar : " + bar);
    }

    public static void main(String[] args) {
        Foo.bar = "adlfasdlf"; // 접근가능해서 수정 가능.
//        Foo.FINAL_BAR = "ㅁㅇㄹㅁㅇㄹ;"; // 컴파일 에러. final이라 수정 불가능


        Foo foo = new Foo();// 매개변수 없는 기본생성자. printAge() 호출하면 age가 초기화 안되어서 NullPointerException
        foo.printAge(); // 에러. age가 초기화 안됨
        Foo foo2 = new Foo(18); // 매개변수 있는 생성자. age가 초기화 되어서 printAge() 호출해도 에러 안남
        foo2.printAge(); // 에러 아님. 생성자로 age를 초기화함.
    }
}
