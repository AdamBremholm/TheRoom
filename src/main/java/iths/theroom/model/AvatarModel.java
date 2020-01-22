package iths.theroom.model;

public class AvatarModel {

    private int base;
    private int head;
    private int torso;
    private int legs;
    private String uuid;

    public AvatarModel() {
    }

    public AvatarModel(int base, int head, int torso, int legs, String uuid) {
        this.base = base;
        this.head = head;
        this.torso = torso;
        this.legs = legs;
        this.uuid = uuid;
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

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
