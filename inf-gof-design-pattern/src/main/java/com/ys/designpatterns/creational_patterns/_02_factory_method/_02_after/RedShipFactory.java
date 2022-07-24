package com.ys.designpatterns.creational_patterns._02_factory_method._02_after;

/**
 * @author : ysk
 */
public class RedShipFactory implements ShipFactory{

    @Override
    public void sendEmailTo(String email, Ship ship) {
        System.out.println(ship.getName() + " 다 만들었습니다.");
        System.out.println("RedShip");
    }

    @Override
    public Ship createShip() {
        return new RedShip();
    }

}
