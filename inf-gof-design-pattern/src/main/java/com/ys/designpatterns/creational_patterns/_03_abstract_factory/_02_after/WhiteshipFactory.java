package com.ys.designpatterns.creational_patterns._03_abstract_factory._02_after;

import com.ys.designpatterns.creational_patterns._02_factory_method._02_after.DefaultShipFactory;
import com.ys.designpatterns.creational_patterns._02_factory_method._02_after.Ship;
import com.ys.designpatterns.creational_patterns._02_factory_method._02_after.Whiteship;

public class WhiteshipFactory extends DefaultShipFactory {

    private ShipPartsFactory shipPartsFactory;

    public WhiteshipFactory(ShipPartsFactory shipPartsFactory) {
        this.shipPartsFactory = shipPartsFactory;
    }

    @Override
    public Ship createShip() {
        Ship ship = new Whiteship();
        ship.setAnchor(shipPartsFactory.createAnchor());
        ship.setWheel(shipPartsFactory.createWheel());
        return ship;
    }
}
