package org.imaginnovate;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class ResourcesApp {

    public static void main(String[] args) {
        System.out.println("----------------------------------------------------------------------------");
        Quarkus.run(args);
    }

}
