package iths.theroom.model;

public class AvatarModel {

    private String base;
    private String head;
    private String torso;
    private String legs;

    public AvatarModel() {
    }

    public AvatarModel(String base, String head, String torso, String legs) {
        this.base = base;
        this.head = head;
        this.torso = torso;
        this.legs = legs;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getTorso() {
        return torso;
    }

    public void setTorso(String torso) {
        this.torso = torso;
    }

    public String getLegs() {
        return legs;
    }

    public void setLegs(String legs) {
        this.legs = legs;
    }
}
