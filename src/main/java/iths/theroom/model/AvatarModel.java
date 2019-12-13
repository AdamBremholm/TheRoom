package iths.theroom.model;

public class AvatarModel {

    private int base;
    private int head;
    private int torso;
    private int legs;

    public AvatarModel() {
    }

    public AvatarModel(int base, int head, int torso, int legs) {
        this.base = base;
        this.head = head;
        this.torso = torso;
        this.legs = legs;
    }

    public int getBase() {
        return base;
    }

    public void setBase(int base) {
        this.base = base;
    }

    public int getHead() {
        return head;
    }

    public void setHead(int head) {
        this.head = head;
    }

    public int getTorso() {
        return torso;
    }

    public void setTorso(int torso) {
        this.torso = torso;
    }

    public int getLegs() {
        return legs;
    }

    public void setLegs(int legs) {
        this.legs = legs;
    }
}
