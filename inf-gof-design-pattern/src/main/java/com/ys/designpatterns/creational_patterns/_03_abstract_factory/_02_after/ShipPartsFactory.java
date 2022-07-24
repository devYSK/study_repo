package com.ys.designpatterns.creational_patterns._03_abstract_factory._02_after;

public interface ShipPartsFactory {

    Anchor createAnchor();

    Wheel createWheel();

}
