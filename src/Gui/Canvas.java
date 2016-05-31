package Gui;

import Simulation.Page;
import Simulation.Process;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Canvas extends java.awt.Canvas {
    private BufferedImage bufferedImage;
    private Graphics2D buffer;
    public int numOfProcess;
    private int currentProcess;
    private int quantum;
    private int quantumTanscurrido;
    private int realPageIndex;
    private Page [] pages;
    private int currentPage;
    private int realCurrentDirection;
    private final Font big = new Font("Arial",Font.PLAIN,16), medium = new Font("Arial",Font.PLAIN,14),small = new Font("Arial",Font.PLAIN,10);
    private ArrayList<Process> processes;
    private int currentDirecition;

    public Canvas(int numOfProcess, int width, int height) {
        setSize(width,height);
        quantumTanscurrido = 0;
        this.numOfProcess = numOfProcess;
        bufferedImage = new BufferedImage(getWidth(),getHeight(),BufferedImage.TYPE_INT_RGB);
        buffer = (Graphics2D) bufferedImage.getGraphics();
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public void setPages(Page[] pages) {
        this.pages = pages;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getQuantumTanscurrido() {
        return quantumTanscurrido;
    }

    public void setRealCurrentDirection(int realCurrentDirection) {
        this.realCurrentDirection = realCurrentDirection;
    }

    public void increadsQuantum() {
        quantumTanscurrido++;
    }
    @Override
    public void paint(Graphics g) {
        update(g);
    }
    public void setProcesses(ArrayList<Process> p) {
        this.processes = p;
    }
    @Override
    public void update(Graphics g) {
        clearBuffer();
        pintarDatos();
        pintarTabla();
        pintarRam();
        g.drawImage(bufferedImage,0,0,null);
    }

    private void pintarRam() {
        buffer.setStroke(new BasicStroke(2));
        int y = 50;
        int width = 100;

        int x = getWidth()-width-50;
        int height = 15;
        buffer.setFont(new Font("Arial",Font.BOLD,20));
        buffer.drawString("MEMORIA RAM",x-15,y-15);
        for(int i = 0 ; i < 32 ; i++) {
            buffer.drawRect(x,y+(i*height),width,height);
        }
        buffer.setFont(medium);
        buffer.drawString("CAT",x+width,y);
        for(int i = 0 ; i < 32 ; i++) {
            buffer.drawRect(x,y+(i*height),width,height);
            buffer.drawString((pages[i] == null)?"" : String.valueOf(pages[i].getProcess()) + " [" + pages[i].getPageId() + "]",x+(width/2)-15,y+(i*height)+13);

            if(pages[i] != null){
                int category = 2*(pages[i].isReferenced()?1:0) + (pages[i].isModifyied()?1:0);
                buffer.drawString(String.valueOf(category),x+(width)+10,y+(i*height)+13);
            }
        }

    }

    private void pintarDatos() {
        buffer.setColor(Color.BLACK);
        buffer.setFont(big);
        buffer.drawString("Numero de procesos: " + numOfProcess,10,12);
        buffer.drawString("Quantum: " + quantum ,10,32);
        buffer.drawString("Ciclos transcurridos: " + quantumTanscurrido ,10,52);
        buffer.drawString("Virtual: " + currentDirecition + " -> " + currentPage + " Fisica: " + realCurrentDirection + " -> " + realPageIndex ,10,72);
    }

    private void clearBuffer() {
        buffer.setColor(Color.WHITE);
        buffer.fillRect(0,0,getWidth(),getHeight());
    }
    public void pintarDatosTablas(int x, int w, int y , int nH) {
        //WRITE DATA
        buffer.setFont(big);
        for(int i = 0 ; i < numOfProcess ; i ++) {
            buffer.drawString("ID: " + (i+1),x+(i*w)+2,y+nH-1);
        }
        buffer.setFont(medium);
        for(int i = 0 ; i < numOfProcess ; i ++) {
            buffer.drawString("Q: " + processes.get(i).getNumOfIterations(),x+(i*w)+2,y+(2*nH)-1);
        }
        for(int i = 0 ; i < numOfProcess ; i ++) {
            buffer.drawString("QR: " + processes.get(i).getIterations(),x+(i*w)+2,y+(3*nH)-1);
        }
    }

    public void setRealPageIndex(int realPageIndex) {
        this.realPageIndex = realPageIndex;
    }

    private void pintarTabla() {
        int w = 800 / numOfProcess;
        int h = 400;
        int x = 30;
        int y = 100;

        buffer.setColor(Color.BLACK);
        //DRAW COLS
        for(int i = 0 ; i < numOfProcess ; i++) {
            buffer.drawRect(x+(i*w),y,w,h);
        }
        int nH = h/19;
        //DRAW FILLS
        for(int j = 0; j < numOfProcess; j++){
            for(int i = 0 ; i < 19; i++) {
                buffer.drawRect(x+(j*w),y+(i*nH),w,nH);
            }
        }
        pintarDatosTablas(x,w,y,nH);
        buffer.setStroke(new BasicStroke(3));
        //DRAW ARROW TO CURRENT PROCESS
        buffer.drawLine(x+(currentProcess-1)*w+(w/2),y-15,x+(currentProcess-1)*w+(w/2),y-1);
        buffer.drawLine(x+(currentProcess-1)*w+(w/2)-4,y-4,x+(currentProcess-1)*w+(w/2),y-1);
        buffer.drawLine(x+(currentProcess-1)*w+(w/2)+4,y-4,x+(currentProcess-1)*w+(w/2),y-1);
        //DRAW ARROW TO CURRENT PAGE
        buffer.drawLine(x-20,y+nH*currentPage + (nH*3) + nH/2,x,y+nH*currentPage+(nH*3) + nH/2);
        buffer.drawLine(x-4,y+nH*currentPage + (nH*3)+4 ,x,y+nH*currentPage+(nH*3) + nH/2);
        buffer.drawLine(x-4,y+nH*currentPage + (nH*3) + nH-4,x,y+nH*currentPage+(nH*3) + nH/2);
        buffer.setStroke(new BasicStroke(1));
        //DRAW PAGE's process
        int n;
        for(int i = 0 ; i < numOfProcess ; i++) {
            n = 0;
            for(Page page : processes.get(i).getPages()) {
                if(page.isReferenced())
                    buffer.setColor(Color.LIGHT_GRAY);
                else
                    buffer.setColor(Color.DARK_GRAY);
                buffer.fillRect(x+(i*w)+1,y+nH*n+(nH*3)+1,(w/3)-1,nH-1);

                if(page.isModifyied())
                    buffer.setColor(Color.LIGHT_GRAY);
                else
                    buffer.setColor(Color.DARK_GRAY);
                buffer.fillRect(x+(i*w)+(w/3),y+nH*n+(nH*3)+1,(w/3)+1,nH-1);


                if(page.isCharged())
                    buffer.setColor(Color.BLUE);
                else
                    buffer.setColor(Color.RED);
                buffer.fillRect(x+(i*w)+((2*w)/3),y+nH*n+(nH*3)+1,(w/3),nH-1);

                buffer.setColor(Color.WHITE);
                buffer.setFont(small);
                buffer.drawString("R",x+(i*w)+5,y+nH*n+(nH*3)+16);
                buffer.drawString("M",x+(i*w)+(w/3)+5,y+nH*n+(nH*3)+16);
                buffer.drawString("C",x+(i*w)+(2*w/3)+5,y+nH*n+(nH*3)+16);
                n++;
            }
        }
        buffer.setColor(Color.BLACK);
        for(int i = 0 ; i < numOfProcess ; i++) {
            buffer.drawLine(x+(i*w)+(w/3),y+nH*3,x+(i*w)+(w/3),500);
            buffer.drawLine(x+(i*w)+(2*w/3),y+nH*3,x+(i*w)+(2*w/3),500);
        }
    }

    public void setCurrentDirecition(int currentDirecition) {
        this.currentDirecition = currentDirecition;
    }
    public void setCurrentProcess(int currentProcess) {
        this.currentProcess = currentProcess;
    }


}
