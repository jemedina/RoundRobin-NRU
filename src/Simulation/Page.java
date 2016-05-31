package Simulation;

public class Page {
    private boolean referenced, modifyied, charged;
    public static int pageCounter = 0;
    private int pageId;
    private int processId;
    private int virtualStart,virtualEnd;
    public Page(int virtualStart, int virtualEnd,int id) {
        this.virtualStart = virtualStart;
        this.virtualEnd = virtualEnd;
        referenced = false;
        this.processId = id;
        pageId = pageCounter++;
    }
    public void unLoad() {
        charged = false;
    }
    public int getPageId() {
        return pageId;
    }

    public int getProcess() {
        return processId;
    }

    public boolean isCharged() {
        return charged;
    }

    public void setCharged(boolean charged) {
        this.charged = charged;
    }

    public int getVirtualStart() {
        return virtualStart;
    }
    public int getVirtualEnd() {
        return virtualEnd;
    }
    public boolean isReferenced() {return referenced;}

    public boolean containsDirection(int direction) {
        if(direction >= virtualStart && direction <= virtualEnd) {
            return true;
        }
        return false;
    }

    public void unModify() {
        modifyied = false;
    }

    public void reference(PrincipalMemory principalMemory) {
        referenced = true;
        modifyied = (Math.random()*10 > 5);
        if(!this.isCharged()) {
            principalMemory.loadPage(this);
            this.setCharged(true);
        }
    }
    public void recalculateModify() {
        modifyied = (Math.random()*10 > 5);
    }

    public boolean isModifyied() {
        return modifyied;
    }

    public void dereference() {
        referenced = false;
    }
}
