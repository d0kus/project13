package app;

import api.ApiServer;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        System.out.println("""
                Choose mode:
                1) Console demo (tasks 1-4)
                2) Start REST API server
                0) Exit
                """);
        System.out.print("Enter: ");
        String choice = sc.nextLine().trim();

        switch (choice) {
            case "1" -> ConsoleDemoApp.main(new String[0]);
            case "2" -> ApiServer.main(new String[0]);
            case "0" -> { return; }
            default -> System.out.println("Unknown option");
        }
    }
}