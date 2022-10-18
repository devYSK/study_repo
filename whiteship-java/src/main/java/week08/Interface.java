package week08;

public class Interface {

    public static void main(String[] args) {
        Person youngsoo = new Youngsoo();

        Person son = new Son();


        youngsoo.move();
        son.move();


//        youngsoo.develop(); // 불가능

//        son.clap(); // 불가능

        ((Youngsoo)youngsoo).develop(); // 가능

        ((Son)son).clap(); // 가능

        Person.print();
    }
}

interface Person {
    void move();

    static void print() {
        System.out.println("print");
    }
}

class Youngsoo implements Person {

    @Override
    public void move() {
        System.out.println("영수가 움직인다");
    }

    public void develop() {
        System.out.println("영수가 개발한다.");
    }
}

class Son implements Person {

    @Override
    public void move() {
        System.out.println("손씨가 움직인다");
    }

    public void clap() {
        System.out.println("손씨가 박수친다");
    }
}


