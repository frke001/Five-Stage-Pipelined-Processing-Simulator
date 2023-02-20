import java.util.LinkedList;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        Register rax = new Register("RAX");
        Register rbx = new Register("RBX");
        Register rcx = new Register("RCX");
        Register rbi = new Register("RBI");
        Register rci = new Register("RCI");
        Register rai = new Register("RAI");
        LinkedList<Instruction> instructions = new LinkedList<>();
        instructions.addLast(new BinaryInstruction(1,"SUB",rax,rbx,rcx));
        instructions.addLast(new BinaryInstruction(2,"MUL",rcx,rci,rai));
        instructions.addLast(new UnaryInstruction(1,"LOAD",rax,rci));
        instructions.addLast(new BinaryInstruction(2,"MUL",rax,rci,rai));
        instructions.addLast(new UnaryInstruction(1,"LOAD",rax,rci));
        String option = "";
        boolean forwardingStatus = false;
        while(!"1".equals(option) && !"2".equals(option)) {
            System.out.println("Forwarding data between flow stages:");
            System.out.println("Yes[1]");
            System.out.println("NO[2]");
            option = scanner.nextLine();
            if("1".equals(option))
                forwardingStatus = true;
        }

        FiveStagePipeline fiveStagePipeline = new FiveStagePipeline(forwardingStatus,instructions);
        fiveStagePipeline.setInstructions(instructions);
        fiveStagePipeline.pipeline();
    }


}