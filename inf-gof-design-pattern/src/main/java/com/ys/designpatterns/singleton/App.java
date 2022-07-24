package com.ys.designpatterns.singleton;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author : ysk
 */
public class App {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, IOException, ClassNotFoundException {

//        Settings settings = Settings.getInstance();
//
//        Constructor<Settings> constructors = Settings.class.getDeclaredConstructor();
//
//        constructors.setAccessible(true);
//
//        Settings newInstance = constructors.newInstance();

        Settings settings = Settings.getInstance();

        Settings settings1 = null;
        try (ObjectOutput out = new ObjectOutputStream(new FileOutputStream("settings.obj"))) {
            out.writeObject(settings);
        }
        try (ObjectInput in = new ObjectInputStream(new FileInputStream("settings.obj"))) {
            settings1 = (Settings) in.readObject();
        }

        System.out.println(settings == settings1);
    }
}
