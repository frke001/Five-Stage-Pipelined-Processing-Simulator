public class UnaryInstruction extends Instruction{

    private Register source;
    //private Register destination;

    public UnaryInstruction(int maxCycles, String name, Register source, Register destination){
        super(maxCycles,name);
        this.source = source;
        this.destination = destination;
        operands.add(source);
        operands.add(destination);
    }
    public Register getSource(){
        return source;
    }
    public Register getDestination(){
        return destination;
    }
}
