package com.ys.designpatterns.singleton;

/**
 * @author : ysk
 */
public class Singleton {

    private Singleton() {}

    private static class SingletonHolder {
        private static final Singleton SINGLETON = new Singleton();
    }

    public static Singleton getInstance() {
        return Singleton.getInstance();
    }
}
