public class BinaryInstruction extends Instruction{
    private Register source1;
    private Register source2;
    //private Register destination;

    public BinaryInstruction(int maxCycles, String name, Register source1, Register source2, Register destination){
        super(maxCycles,name);
        this.source1 = source1;
        this.source2 = source2;
        this.destination = destination;
        operands.add(source1);
        operands.add(source2);
        operands.add(destination);
    }
    public Register getSource1(){
        return source1;
    }
    public Register getSource2(){
        return source2;
    }
    public Register getDestination(){
        return destination;
    }
}
