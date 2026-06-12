package entity.keyframe;

import bezier.BezierSolver;
import entity.Complex;
import entity.ComplexBasic;
import entity.KeyframeTuple;
import entity.KeyframeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KeyframeManager {
    private HashMap<HashKey, Keyframe> keyframeChannels = new HashMap<>();

    private int frameCounter = 0;

    public KeyframeManager(){

    }

    public void setFrame(int f){
        this.frameCounter = f;
    }
    public void insert(KeyframeType type, int index, double value){
        HashKey key = new HashKey(type, index);
        if (keyframeChannels.containsKey(key)) {
            Range range = getRange(new HashKey(type, index));
            if (range.start.getFrame() == frameCounter)
                range.start.setValue(value);
            else if (range.start.getFrame() < frameCounter){
                Keyframe moment = new Keyframe(null, type, value, frameCounter);
                range.start.insertNext(moment);
            }
            else { //range.start.getFrame() > frameCounter
                Keyframe moment = new Keyframe(null, type, value, frameCounter);
                range.start.insertLast(moment);
            }
        }
        else{
            Keyframe moment = new Keyframe(null, type, value, frameCounter);
            keyframeChannels.put(key, moment);
        }



    }

    public void remove(KeyframeType type, int index){
        HashKey key = new HashKey(type, index);
        if (keyframeChannels.containsKey(key)) {
            Range range = getRange(new HashKey(type, index));
            if (range.start.getFrame() == frameCounter) {
                Keyframe newMoment = range.start.remove();
                if (newMoment != null)
                    keyframeChannels.put(key, newMoment);
                else
                    keyframeChannels.remove(key);
            }
        }
    }

    public ArrayList<KeyframeTuple> getValues(){
        // calculate and return the values of all keyframe types stored in keyframeChannels at the current frame.
        ArrayList<KeyframeTuple> data = new ArrayList<>();
                for (Map.Entry<HashKey, Keyframe> entry : keyframeChannels.entrySet()) {
                    Range range = getRange(entry.getKey());
                    double value;
                    if (range.end == null)
                        value = range.start.getValue();
                    else {
                        Keyframe lastKey = range.start.getLast();
                        Keyframe nextKey = range.end.getNext();
                        Complex prev, next;

                        Complex start = new ComplexBasic(range.start.getFrame(), range.start.getValue());
                        Complex end = new ComplexBasic(range.end.getFrame(), range.end.getValue());
                        if (lastKey != null)
                            prev = new ComplexBasic(lastKey.getFrame(), lastKey.getValue());
                        else prev = null;
                        if (nextKey != null)
                            next = new ComplexBasic(nextKey.getFrame(), nextKey.getValue());
                        else next = null;
                        Complex point = BezierSolver.getPoint(prev, start, end, next, (frameCounter - range.start.getFrame())/(0.0 + range.end.getFrame() - range.start.getFrame()) );
                        value = point.getI();
                    }
                    data.add(new KeyframeTuple(entry.getKey().type(), entry.getKey().index, value));
        }
        return data;
    }

    private record Range(Keyframe start, Keyframe end){}
    private record HashKey(KeyframeType type, int index){}

    private Range getRange(HashKey key){
        //get the range made up of a starting and end keyframe that includes frameCounter's current value.
        //if no two keyframes includes it, return the closest keyframe as both the start and end.

        //This should also update keyframeChannels to store the most relevant keyframe based on frameCounter's value.
        Keyframe moment = keyframeChannels.get(key);
        do {
            if (frameCounter == moment.getFrame()) {
                keyframeChannels.put(key, moment);
                return new Range(moment, moment.getNext());
            }
            else if (frameCounter < moment.getFrame()){
                if (moment.getLast() == null) {
                    keyframeChannels.put(key, moment);
                    return new Range(moment, null);
                }
                else
                    moment = moment.getLast();
            }
            else { // frameCounter > moment.getFrame()
            if (moment.getNext() == null || frameCounter < moment.getNext().getFrame()){
                keyframeChannels.put(key, moment);
                return new Range(moment, moment.getNext());
            }
                moment = moment.getNext();
            }
        } while(true);
    }


}
