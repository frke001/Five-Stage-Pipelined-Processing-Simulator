public class Register {

    private boolean taken;
    private String name;

    public Register(String name){
        super();
        this.name = name;
        taken = false;
    }
    public boolean isTaken(){
        return taken;
    }
    public void setTaken(boolean taken){
        this.taken = taken;
    }
    public String getName(){
        return name;
    }
    @Override
    public boolean equals(Object obj){
        Register reg = (Register) obj;
        if(reg != null){
            if(this.name.equals(reg.getName()))
                return true;
        }
        return false;
    }
}
