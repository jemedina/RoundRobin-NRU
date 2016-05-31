package Simulation;

import Gui.Canvas;
import Gui.StatisticsDialog;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;

public class Simulation extends Thread {
    private int quantum;
    private int numOfProcesses;
    private boolean processEnded;
    private int sleepOption;
    private int currentProcess;
    private int cambiosDeContexto;
    public static boolean nextStep = false;
    private ArrayList<Process> processes;
    public static int quantumTranscurrido = 0;
    private PrincipalMemory principalMemory;
    private Canvas viewport;
    public Simulation(int numOfProcesses, Canvas canvas, int sleepOption) {
        Random ran = new Random();
        nextStep = false;
        this.numOfProcesses = numOfProcesses;
        this.sleepOption = sleepOption;
        principalMemory = new PrincipalMemory();
        processes = new ArrayList<>();
        this.viewport = canvas;
        ran.setSeed(System.currentTimeMillis());
        quantum = 3+ran.nextInt(3);

        cambiosDeContexto=0;
        for(int i = 0 ; i < numOfProcesses ; i++) {
            processes.add(new Process(principalMemory));
        }
        currentProcess = 0;
        viewport.setProcesses(processes);
        viewport.setQuantum(quantum);
    }
    public boolean work(){
        if(numOfProcesses > 0) {

            if(viewport.getQuantumTanscurrido() > 0 && viewport.getQuantumTanscurrido()%10 == 0) {
                principalMemory.resetReferenced();
            }
            viewport.repaint();
            processes.get(currentProcess).work();
            //CHECK THE END OF THE PROCESS
            if(processes.get(currentProcess).getIterations() <= 0){
                processes.get(currentProcess).setTR(quantumTranscurrido+1);
                principalMemory.clearMemory(processes.get(currentProcess).getProcessId());
                processes.get(currentProcess).setAlive(false);
                numOfProcesses--;
                processEnded = true;
                //REMOVE THE PAGES IN THE MEMORY OF THIS PROCESS
            }else {
                processEnded = false;
            }
            viewport.setCurrentProcess(processes.get(currentProcess).getProcessId());
            viewport.setCurrentPage(processes.get(currentProcess).getCurrentPage());
            viewport.setCurrentDirecition(processes.get(currentProcess).getCurrentDirection());
            viewport.setRealCurrentDirection(processes.get(currentProcess).getCurrentRealDirection());
            viewport.setRealPageIndex(processes.get(currentProcess).getRealIndex());
            viewport.setPages(principalMemory.getPages());

            return true;
        }
        else{
            return false;
        }

    }

    @Override
    public void run() {
        int elapsedCycles = 0;
        //System.out.println(quantumTranscurrido);
        processes.get(currentProcess).setUltimoTiempo(quantumTranscurrido);
        processes.get(currentProcess).updateStatistics();
        while(work()) {
            viewport.increadsQuantum();
            quantumTranscurrido++;
            if(!processEnded) {
                //printReport();
                viewport.repaint();
            }
            if(sleepOption == 1) {
                    while(true){
                        if(nextStep) break;
                    }
                    nextStep = false;
            }
            try {
                switch (sleepOption){
                    case 2:
                        Thread.sleep(4000);
                        break;
                    case 3:
                        Thread.sleep(1000);
                        break;
                    case 4:
                        Thread.sleep(100);
                        break;
                    case 5:
                        Thread.sleep(10);
                        break;
                }
            } catch (InterruptedException e) {}
            if(elapsedCycles < quantum-1 && !processEnded) {
                elapsedCycles++;
            } else {
               // System.out.println(quantumTranscurrido);

                processes.get(currentProcess).setUltimoTiempo(quantumTranscurrido);
                elapsedCycles = 0;
                boolean processFinded = false;
                while(numOfProcesses > 0) {
                    currentProcess++;
                    if(currentProcess > processes.size()-1 )
                        currentProcess = 0;
                    if(processes.get(currentProcess).isAlive()){
                        processFinded = true;
                        break;}
                }
                if(processFinded) {
                    cambiosDeContexto++;
                    processes.get(currentProcess).updateStatistics();
                }
            }
        }
        viewport.increadsQuantum();
        StatisticsDialog statisticsDialog = new StatisticsDialog();
        statisticsDialog.setStatistics(getStatistics());
    }
    private void printReport() {
        System.out.println("QUANTUM " + quantum);
        for(int i = 0;i < processes.size() ; i++) {
            System.out.print("Process " + processes.get(i).getProcessId() + ":");
            System.out.print(processes.get(i).getIterations() + " , ");
        }
        System.out.println("\n"+principalMemory);
        System.out.println("\n------------------------------------------");
    }

    public String getStatistics() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("=== ESTADISTICAS ===\n");
        float teProm = 0,trProm=0;
        for(Process p : processes) {
            stringBuffer.append("PID " + p.getProcessId() + ": TE " + p.getTE() + ", TR " + p.getTR()+"\n");
            teProm+=p.getTE();
            trProm+=p.getTR();
        }
        stringBuffer.append("\nTE PROM: " + teProm/processes.size());
        stringBuffer.append("\nTR PROM: " + trProm/processes.size());
        stringBuffer.append("\nCAMBIOS DE CONTEXTO: " + cambiosDeContexto);
        return stringBuffer.toString();
    }
}
