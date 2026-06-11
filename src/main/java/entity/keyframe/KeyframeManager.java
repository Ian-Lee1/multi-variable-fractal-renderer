package entity.keyframe;

import entity.KeyframeTuple;
import entity.KeyframeType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class KeyframeManager {
    private HashMap<KeyframeType, Keyframe> keyframeChannels = new HashMap<>();
    private int frameCounter = 0;
    public KeyframeManager(){

    }

    public ArrayList<KeyframeTuple> getValues(){
        // calculate and return the values of all keyframe types stored in keyframeChannels at the current frame.
        ArrayList<KeyframeTuple> data = new ArrayList<>();
                for (Map.Entry<KeyframeType, Keyframe> entry : keyframeChannels.entrySet()) {
            //calculate and add to data.
        }
        return data;
    }

    private record Range(Keyframe start, Keyframe end){}
    private Range getRange(KeyframeType type){
        //get the range made up of a starting and end keyframe that includes frameCounter's current value.
        //if no two keyframes includes it, return the closest keyframe as both the start and end.

        //This should also update keyframeChannels to store the most relevant keyframe based on frameCounter's value.
        Keyframe moment = keyframeChannels.get(type);
        do {
            if (frameCounter == moment.getFrame()) {
                keyframeChannels.put(type, moment);
                return new Range(moment, moment.getNext());
            }
            else if (frameCounter < moment.getFrame()){
                if (moment.getLast() == null) {
                    keyframeChannels.put(type, moment);
                    return new Range(moment, null);
                }
                else
                    moment = moment.getLast();
            }
            else { // frameCounter > moment.getFrame()
            if (moment.getNext() == null || frameCounter < moment.getNext().getFrame()){
                keyframeChannels.put(type, moment);
                return new Range(moment, moment.getNext());
            }
                moment = moment.getNext();
            }
        } while(true);
    }


}
