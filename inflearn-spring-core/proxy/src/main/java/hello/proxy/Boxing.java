package hello.proxy;

public class Boxing {


    public void boxingTest() {
        int value = 999;

        // boxing
        Integer integerObj = value;
    }

    public void unboxingTest() {
        Integer integerObj = 100;

        //unboxing
        int value = integerObj;
    }

}
