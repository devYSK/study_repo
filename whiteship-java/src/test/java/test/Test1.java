package test;

import org.junit.jupiter.api.Test;

import java.util.Comparator;

/**
 * @author : ysk
 */
public class Test1 {

    private void test() {
        Integer a = 1;

        long b = 50L;

        a = (int)b;
        String c = "s";

    }

    @Test
    void test2() {
        Parent parent = new Parent();
        Child child = new Child();

        Parent p2 = new Child();

        Child c2 = (Child) p2;


        child.print();

        System.out.println(parent instanceof Parent);// true
        System.out.println(parent instanceof Child);// false
        System.out.println(child instanceof Parent);// true
        System.out.println(child instanceof Child); // true
        System.out.println(p2 instanceof Parent);   // true
        System.out.println(p2 instanceof Child);    //true
        System.out.println(c2 instanceof Child);    //true
        System.out.println(c2 instanceof Parent);   //true
    }
}

class Parent {

    protected void print() {
        System.out.println("parent");
    }
}

class Child extends Parent{

    @Override
    public void print() {
        super.print();
//        System.out.println("child");

    }
}

