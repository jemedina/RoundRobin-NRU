package Simulation;

import java.util.ArrayList;
import java.util.Random;

public class Process {
    private static int processCounter=1;
    private int processId;
    private static final int VIRTUAL_MEMORY = 65536;
    private int realIndex;
    private int iterations;
    private int numOfIterations;
    private boolean alive;
    private int currentDirection;
    private int currentPage;
    private int currentRealDirection;
    private ArrayList<Page> pages;
    private PrincipalMemory principalMemory;
    private int TE, TR, ultimoTiempo; //TIEMPO ESPERA, TIEMPO RESPUESTA
    public Process(PrincipalMemory principalMemory) {
        this.principalMemory = principalMemory;
        Random random = new Random();
        pages = new ArrayList<>();
        alive = true;
        processId = processCounter++;
        iterations = numOfIterations = random.nextInt(21)+30;
        //Create virtual pages
        Page.pageCounter = 0;
        for(int i = 0 ; i  < VIRTUAL_MEMORY ; i+=4096) {
            pages.add(new Page(i,i+(4095),processId));
        }
    }

    public void setUltimoTiempo(int ultimoTiempo) {
        this.ultimoTiempo = ultimoTiempo;
    }

    public void updateStatistics() {
        TE+= Simulation.quantumTranscurrido-ultimoTiempo;
        System.out.println("PID " + processId);
        System.out.println(Simulation.quantumTranscurrido-ultimoTiempo);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public ArrayList<Page> getPages() {
        return pages;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
        if(alive == false) {
            unloadMemory();
        }
    }

    public int getNumOfIterations() { return numOfIterations; }
    public int getIterations() { return iterations; }

    public void work() {
        iterations-=1;
        int dir = (int) (Math.random()*VIRTUAL_MEMORY);
        currentDirection = dir;
        referenciatePage(dir);
    }

    public int getCurrentDirection() {
        return currentDirection;
    }

    public void unloadMemory() {
        for(Page p : pages) {
            p.unModify();
            p.dereference();
            p.unLoad();
        }
    }
    public int getProcessId() {
        return processId;
    }

    private void referenciatePage(int direction) {
        int i = 0;
        boolean ok = false;
        for(Page page : pages) {
            if (page.containsDirection(direction)) {
                page.reference(principalMemory);
                ok = true;
            }
            if(!ok) i++;
        }
        currentPage = i;
        int calc = (direction - pages.get(i).getVirtualStart())+((principalMemory.getPageIndex(pages.get(i))+1)*4096);
        realIndex = principalMemory.getPageIndex(pages.get(i))+1;
        currentRealDirection = calc;
    }

    public void setTR(int TR) {
        this.TR = TR;
    }

    public int getCurrentRealDirection() {
        return currentRealDirection;
    }

    public int getRealIndex() {
        return realIndex;
    }

    public int getTE() {
        return TE;
    }

    public int getTR() {
        return TR;
    }
}
