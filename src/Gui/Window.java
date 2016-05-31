package Gui;

import javax.swing.*;

public class Window extends JFrame {
    public Window(Canvas canvas,int w, int h) {
        setBounds(0,0,w,h);
        setResizable(false);
        setTitle("SIMULATION");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(canvas);
    }
}
