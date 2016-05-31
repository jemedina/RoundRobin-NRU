package Main;

import java.util.InputMismatchException;
import java.util.Scanner;
import Simulation.*;
import Gui.*;
public class Main {
    public static void main(String[] args) {
        int WIDTH = 1000 , HEIGHT = 600;
        boolean ok = false;
        int numOfProcesses = 0;
        do {
            System.out.println("Enter the number of processes (8 - 15) > ");

            try {
                numOfProcesses = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.err.println("Solo puedes ingresar numeros");
            }

            if(numOfProcesses >= 8 && numOfProcesses <= 15) {
                ok = true;
            } else {
                System.err.println("Debes ingresar un numero entre el 8 y el 15");
            }
        } while(!ok);
        ok = false;
        Canvas c = new Canvas(numOfProcesses,WIDTH,HEIGHT);
        Window w = new Window(c,WIDTH,HEIGHT);
        int speedOption = 0;
        do{
            System.out.print("Ingrese velosidad de simulacion:\n1.- Step by step\n2.- Lenta\n3.- Normal\n4.- Rapida\n5.- Ultra\n> ");
            try {
                speedOption = new Scanner(System.in).nextInt();
            } catch (InputMismatchException ex) {
                System.err.println("Solo puedes ingresar numeros");
            }
            if(speedOption >= 1 && speedOption <= 5) {
                ok = true;
            }
            else {
                System.err.println("Debes ingresar un numero entre 1 y 5");
            }
        } while(!ok);
        Simulation simulation = new Simulation(numOfProcesses,c,speedOption);
        w.setVisible(true);
        simulation.start();
    }
}
