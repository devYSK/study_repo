package test;

import java.util.Objects;

/**
 * @author : ysk
 */

class Car {
    private String name;
    private int speed;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return speed == car.speed && Objects.equals(name, car.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, speed);
    }
}