package Simulation;

import java.io.*;

public class PrincipalMemory {
    private static final int TOTAL_MEMORY = 131072;//Bytes
    private static final int PAGE_SIZE = 4*1024;
    private static final int NUM_OF_PAGES = TOTAL_MEMORY/PAGE_SIZE;
    private Page [] pages;
    public PrincipalMemory() {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new FileOutputStream(new File("paginas.txt"),false));
            writer.print("");
            writer.close();} catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        pages = new Page[NUM_OF_PAGES];
        //RESERVATE THE FIRST 7 PAGES
        for(int i = 0 ; i < 7 ; i++) {
            pages[i] = new Page(i*PAGE_SIZE,((i+1)*PAGE_SIZE)-1,0);
        }
    }

    public int getPageIndex(Page p ){
        int i = -1;
        for(Page page : pages){
            if(page == p) {
                break;
            }
            i++;
        }
        return i;
    }
    public Page[] getPages() {
        return pages;
    }

    public void loadPage(Page page) {
        boolean pageLoaded = false;
        for(int i = 7 ; i < NUM_OF_PAGES ; i++) {
            if(pages[i] == null) {
                pages[i] = page;
                pageLoaded = true;
                break;
            }
        }
        if(!pageLoaded){
            changePageNRU(page);
        }
    }
    public void clearMemory(int process) {
        for(int i = 0 ; i < NUM_OF_PAGES ; i++ ){
            if(pages[i] != null) {
                if(pages[i].getProcess() == process){
                    pages[i].dereference();
                    pages[i].unModify();
                    pages[i] = null;
                }
            }
        }
    }
    public void resetReferenced() {
        for(Page p : pages) {
            if(p!=null)
                p.dereference();
        }
    }
    private void changePageNRU(Page page) {
        /*ALGORITHM NRU*/
        for(int j = 0; j <= 3; j++) {
            for(int i = 7 ; i < NUM_OF_PAGES ; i++) {
                int category = 2*(pages[i].isReferenced()?1:0) + (pages[i].isModifyied()?1:0);
                if(category == j) {
                    pages[i].dereference();
                    pages[i].setCharged(false);
                    if(pages[i].isModifyied()) {
                        //ESCRIBIR ARCHIVO
                        guardarPagina(pages[i]);
                    }
                    pages[i].unModify();
                    pages[i] = page;
                    return;
                }
            }
        }
    }

    private void guardarPagina(Page p) {
        File file = new File("paginas.txt");
        try {

            PrintWriter writer = new PrintWriter(new FileOutputStream(file,true));
            writer.println("[PID "+p.getProcess() + ", PAGE ID " + p.getPageId()+"]");
            writer.close();
        } catch (IOException e) {
            System.err.println("Error al abrir el archivo");
        }

    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0 ; i < NUM_OF_PAGES ; i++) {
            stringBuilder.append("[");
            if(pages[i] == null) {
                stringBuilder.append(" ");
            } else  {
                stringBuilder.append(pages[i].getProcess());
            }
            stringBuilder.append("]");
        }
        return stringBuilder.toString();
    }
}
