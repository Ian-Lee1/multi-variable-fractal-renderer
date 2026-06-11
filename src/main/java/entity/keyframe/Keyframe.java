package entity.keyframe;

import entity.KeyframeType;

public class Keyframe {
    private Keyframe last, next;
    private KeyframeType type;
    private double value;
    private final int frame;

    public Keyframe(Keyframe last, KeyframeType type, double value, int frame){
        this.last = last;
        this.type = type;
        this.value = value;
        this.next = null;
        this.frame = frame;
    }
    public Keyframe(Keyframe last, KeyframeType type, double value, int frame, Keyframe next){
        this.last = last;
        this.type = type;
        this.value = value;
        this.next = next;
        this.frame = frame;
    }

    public void setValue(double val){
        value = val;
    }

    public double getValue(){return value;}

    public KeyframeType getType(){return type;}

    public Keyframe getNext(){
        return next;
    }
    public Keyframe getLast(){
        return last;
    }

    public int getFrame(){return frame;}

    public void setLast(Keyframe last){
        this.last = last;
    }
    public void setNext(Keyframe next){
        this.next = next;
    }
    public void insertNext(Keyframe next){
        next.setNext(this.next);
        next.setLast(this);
        if (this.next != null)
            this.next.setLast(next);
        this.next = next;
    }

    public void insertLast(Keyframe last){
        last.setLast(this.last);
        last.setNext(this);
        if (this.last != null)
            this.last.setNext(last);
        this.last = last;
    }
}
