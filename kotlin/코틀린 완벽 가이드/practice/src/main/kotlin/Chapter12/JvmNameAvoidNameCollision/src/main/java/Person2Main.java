package Chapter12.JvmNameAvoidNameCollision.src.main.java;

public class Person2Main {
    public static void main(String[] args) {
        Person2 person = new Person2("John", 25);
        person.changeName("Harry");
        System.out.println(person.getName());
    }
}
