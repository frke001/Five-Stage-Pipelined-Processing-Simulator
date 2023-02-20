import java.util.ArrayList;
import java.util.LinkedList;

public class FiveStagePipeline {

    private boolean operandForwarding;
    private int numberOfInstruction;
    public LinkedList<Instruction> instructions = new LinkedList<>();
    private LinkedList<Instruction> fetchedInstructions = new LinkedList<>();
    private LinkedList<Instruction> decodedInstructions = new LinkedList<>();
    private LinkedList<Instruction> executedInstructions = new LinkedList<>();
    private LinkedList<Instruction> memoryAccessedInstructions = new LinkedList<>();
    private LinkedList<Instruction> finishedInstructions = new LinkedList<>();

    private ArrayList<ArrayList<String>> table = new ArrayList<>();
    private static int currCycle = 1;
    public FiveStagePipeline(boolean operandForwarding, LinkedList<Instruction> instructions){
        super();
        this.operandForwarding = operandForwarding;
        this.instructions = instructions;
        this.numberOfInstruction = instructions.size();
        int i = 0;
        for(var el : instructions){
            table.add(el.getSerialNumber(),new ArrayList<>());
            table.get(el.getSerialNumber()).add(el.getName()+"-"+el.getSerialNumber());
        }
    }
    public void fetch(){
        Instruction inst = null;
        if(!instructions.isEmpty()) {
            inst = instructions.pollFirst();
            fetchedInstructions.addLast(inst);
            System.out.println(inst.getName() + " " + inst.getSerialNumber() + " Fetch");
            table.get(inst.getSerialNumber()).add(currCycle-1,"IF");
        }
    }
    public void decode(){
        //fetch();
        Instruction inst = null;
        if(!fetchedInstructions.isEmpty()) {
            inst = fetchedInstructions.pollFirst();
            if (inst.isExecutable()) {
                System.out.println(inst.getName() + " " + inst.getSerialNumber() + " Decode");
                table.get(inst.getSerialNumber()).add(currCycle-1,"ID");
                for (var el : inst.operands){
                    el.setTaken(true);
                }
                decodedInstructions.addLast(inst);
            }else{
                checkHazard(inst);
                System.out.println(inst.getName() + " " + inst.getSerialNumber() + " is waiting for data");
                fetchedInstructions.addFirst(inst);
            }
        }
        fetch();
    }
    private void checkHazard(Instruction inst) {
        Instruction ins1 = executedInstructions.peekFirst();
        if(ins1 != null) {
            if (inst instanceof BinaryInstruction) {
                var tmp1 = (BinaryInstruction) inst;
                if (tmp1.getSource1().equals(ins1.destination) || tmp1.getSource2().equals(ins1.destination))
                    System.out.println("RAW Hazard detected!");

                /*
                i1. *R2 <- R5 + R3 /dest
                i2. R4 <- *R2 + R3 /source
                 */
            } else {
                var tmp1 = (UnaryInstruction) inst;
                if (tmp1.getSource().equals(ins1.destination))
                    System.out.println("RAW Hazard detected!");
            }
        }
    }
    public void execute(){
        //decode();
        Instruction inst = null;
        if(!decodedInstructions.isEmpty()) {
            inst = decodedInstructions.peekFirst();
            System.out.println(inst.getName() + " " + inst.getSerialNumber() + " Execute");
            table.get(inst.getSerialNumber()).add(currCycle-1,"EX");
            if (inst.getMaxCycles() != inst.getCurrentCycle()) {
                inst.setCurrentCycle(inst.getCurrentCycle()+1);
            }else {
                decodedInstructions.pollFirst();
                inst.setCurrentCycle(0);
                executedInstructions.addLast(inst);
            }
        }
        decode();
        if(operandForwarding && inst != null && inst.getCurrentCycle()==0){
            for (var el : inst.operands){
                el.setTaken(false);
            }
            Instruction temp = fetchedInstructions.peekFirst();
            if(temp != null)
                System.out.println(inst.getName() + " " + inst.getSerialNumber() + " Execute forwards operand to " + temp.getName() + " " + temp.getSerialNumber() + " Decode");
        }
    }
    public void memoryAccess(){
        //execute();
        Instruction inst = null;
        if(!executedInstructions.isEmpty()) {
            inst = executedInstructions.peekFirst();
            System.out.println(inst.getName() + " " + inst.getSerialNumber() + " Memory Access");
            table.get(inst.getSerialNumber()).add(currCycle-1,"MEM");
            if (inst instanceof UnaryInstruction) {
                if(inst.getCurrentCycle() < 2){
                    inst.setCurrentCycle(inst.getCurrentCycle()+1);
                }else{
                    executedInstructions.pollFirst();
                    memoryAccessedInstructions.addLast(inst);
                }
            }else {
                executedInstructions.pollFirst();
                memoryAccessedInstructions.addLast(inst);
            }
        }
        execute();
    }
    public void writeBack(){
        //memoryAccess();
        Instruction inst = null;
        if(!memoryAccessedInstructions.isEmpty()) {
            inst = memoryAccessedInstructions.pollFirst();
            System.out.println(inst.getName() + " " + inst.getSerialNumber() + " Write Back");
            table.get(inst.getSerialNumber()).add(currCycle-1,"WB");
            finishedInstructions.addLast(inst);
        }
        memoryAccess();
        if(!operandForwarding && inst != null) {
            for (var el : inst.operands)
                el.setTaken(false);
            Instruction temp = fetchedInstructions.peekFirst();
            if(temp != null)
                System.out.println(inst.getName() + " " + inst.getSerialNumber() + " WriteBack forwards operand to " + temp.getName() + " " + temp.getSerialNumber() + " Decode");
        };
    }
    
    public void pipeline(){
        addEmpty();
        System.out.println("Ciklus " + currCycle++ + ": ");
        fetch();
        addEmpty();
        System.out.println("Ciklus " + currCycle++ + ": ");
        decode();
        addEmpty();
        System.out.println("Ciklus " + currCycle++ + ": ");
        execute();
        addEmpty();
        System.out.println("Ciklus " + currCycle++ + ": ");
        memoryAccess();
        addEmpty();
        System.out.println("Ciklus " + currCycle++ + ": ");
        writeBack();
        while(finishedInstructions.size() != numberOfInstruction){
            addEmpty();
            System.out.println("Ciklus " + currCycle++ + ": ");
            writeBack();
        }
        System.out.println();
        System.out.print(String.format("%-10s","Cycle"));
        for(int i=1; i < this.currCycle ;i++)
            System.out.print(String.format("%-5s", i));
        System.out.println();
        writeTable();
    }
    public void writeTable(){
        for(var arr : table){
            for(int i=0;i<arr.size();i++) {
                if(i==0)
                    System.out.print(String.format("%-10s",arr.get(i)));
                else
                    System.out.print(String.format("%-5s", arr.get(i)));
            }
            System.out.println();
        }
    }
    private void addEmpty()
    {
        for(var arr : table)
            arr.add(currCycle,"");
    }
    public void setInstructions(LinkedList<Instruction> instructions){
        this.instructions = instructions;
        this.numberOfInstruction = instructions.size();
    }
}
