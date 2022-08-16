package t;

/**
 * @author : ysk
 */
public class CallClass {

    private AnonymousClass anony1 = new AnonymousClass() {
        public void print2() {
            System.out.println("필드에요 저는");
        }
    };

    public void call() {
        AnonymousClass anony2 = new AnonymousClass() {

            public void print3() {  // 호출 안됌
                System.out.println("저는 메서드 안에 있어요, print3()");
            }

//            @Override             // 오버라이딩 해서 사용 가능
//            void print() {
//                super.print();
//            }
        };

        anony2.print();

    }

    public void call2(AnonymousClass anony3) {
        anony3.print();
    }

    public AnonymousClass getAnony1() {
        return anony1;
    }

    public static void main(String[] args) {
        CallClass callClass = new CallClass();

        callClass.getAnony1().print();  // print2() 메서드는 접근 불가

        callClass.call();   // 원래 클래스의 print()만 호출된다.

        callClass.call2(new AnonymousClass(){
            @Override
            void print() {
                //super.print(); // 원래 클래스의 메서드도 호출 가능.
                System.out.println("오버라이딩 했지롱.");
            }
        });

    }

}