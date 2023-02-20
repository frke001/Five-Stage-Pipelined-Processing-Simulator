import java.util.ArrayList;

public class Instruction {

    private int maxCycles;
    private int currentCycle = 1;
    private String name;
    private static int num = 0;
    private int serialNumber;
    private int cycle;
    public Register destination;

    public ArrayList<Register> operands = new ArrayList<>();

    public Instruction(int maxCycles, String name){
        this.maxCycles = maxCycles;
        this.name = name;
        this.serialNumber = num++;
    }

    public boolean isExecutable(){
        for (var el : operands) {
            if(el.isTaken())
                return false;
        }
        return true;
    }
    public int getMaxCycles(){
        return maxCycles;
    }
    public int getCurrentCycle(){
        return currentCycle;
    }
    public void setCurrentCycle(int currentCycle){
        this.currentCycle = currentCycle;
    }
    public String getName(){
        return name;
    }
    public int getSerialNumber(){
        return serialNumber;
    }
}
