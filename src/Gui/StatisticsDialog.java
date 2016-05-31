package Gui;

import javax.swing.*;

public class StatisticsDialog extends JFrame {
    private JTextArea textArea;
    public StatisticsDialog() {
        setTitle("ESTADISTICAS");
        setBounds(200,200,300,350);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        textArea = new JTextArea();
        textArea.setEditable(false);
        add(textArea);
        setVisible(true);
    }
    public void setStatistics(String s) {
        textArea.setText(s);
    }
}
