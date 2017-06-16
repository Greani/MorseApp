package at.fhooe.mc.android.morseapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.os.Vibrator;
import android.util.Log;

import java.security.Policy;
import java.util.ArrayList;

/**
 * Created by Dominik on 16.06.2017.
 */

public class OutputManager {
    private static final String TAG="OutputManager.class";
    private static boolean vibration=false;
    private static boolean audio=false;
    private static boolean light=false;

    public static boolean getVibrationStatus(){return(vibration);}
    public static boolean getAudioStatus(){return (audio);}
    public static boolean getLightStatus(){return (light);}
    public static void setVibrationStatus(boolean _status){
        vibration=_status;
    }
    public static void setAudioStatus(boolean _status){
        audio=_status;
    }
    public static void setLightStatus(boolean _status){
        light=_status;
    }
    public static void invertVibrationStatus(){vibration=!vibration;}
    public static void invertAudioStatus(){audio=!audio;}
    public static void invertLightStatus(){light=!light;}

    //Only works with vibration yet
    public static void sendSignals(Context mContext,String _morseSequence){
        if(vibration)
            sendVibration(mContext,_morseSequence);
        if(audio)
            sendAudio();
        if(light)
            if(mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH));
            sendLight();

    }
    private static void sendVibration(Context _context,String _morseSequence){

        ArrayList<Long> sequenceList=new ArrayList<>();
        sequenceList.add((long)0);
        for(int i=0;i<_morseSequence.length();i++){
            switch (_morseSequence.charAt(i)){
                case '·':{
                    sequenceList.add((long)100);
                    if((i+1>=_morseSequence.length())||_morseSequence.charAt(i+1)!=' ')
                        sequenceList.add((long)0);
                }break;
                case '−':{
                    sequenceList.add((long)1000);
                    if((i+1>=_morseSequence.length())||_morseSequence.charAt(i+1)!=' ')
                        sequenceList.add((long)0);
                }break;
                case ' ':{
                    long pauseLength=0;
                    int j;
                    for(j=0;(((i+j)<_morseSequence.length())&&(_morseSequence.charAt(i+j)==' '));j++)
                    {
                        pauseLength=pauseLength+400;
                    }
                    sequenceList.add(pauseLength);
                    i=i+j-1;
                }break;
                default:
                    Log.e(TAG,"Wrong morse char");
            }
        }

        Vibrator v =(Vibrator)_context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] vSequence = new long[sequenceList.size()];

        for(int i=0;i<sequenceList.size();i++){
            vSequence[i]=sequenceList.get(i);
        }
        v.vibrate(vSequence,-1);


    }

    //Both not implemented
    private static void sendAudio(){}
    private static void sendLight(){}


}
