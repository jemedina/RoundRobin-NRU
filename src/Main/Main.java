package Main;

import java.util.Scanner;
import Simulation.*;
import Gui.*;
public class Main {
    public static void main(String[] args) {
        int WIDTH = 1000 , HEIGHT = 600;
        System.out.println("Enter the number of processes (8 - 15) > ");
        int numOfProcesses = new Scanner(System.in).nextInt();
        Canvas c = new Canvas(numOfProcesses,WIDTH,HEIGHT);
        Window w = new Window(c,WIDTH,HEIGHT);
        Simulation simulation = new Simulation(numOfProcesses,c);
        w.setVisible(true);
        simulation.start();
    }
}
