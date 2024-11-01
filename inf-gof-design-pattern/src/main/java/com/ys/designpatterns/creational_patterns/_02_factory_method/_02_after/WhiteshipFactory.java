package com.ys.designpatterns.creational_patterns._02_factory_method._02_after;

public class WhiteshipFactory extends DefaultShipFactory {

    @Override
    public Ship createShip() {
        return new Whiteship();
    }
}
