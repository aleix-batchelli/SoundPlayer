package org.example;
import java.io.IOException;
import org.example.Presentation.Controller;

public class App {

    public static void main(String[] args) {
        try {
            Controller controller = new Controller();
            controller.start();
        } catch (IOException e) {
            System.err.println("Error initializing application: " + e.getMessage());
        }
    }
}