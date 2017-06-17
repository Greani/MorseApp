package at.fhooe.mc.android.morseapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Toast;

import java.security.Policy;
import java.util.ArrayList;

import static android.content.Context.CAMERA_SERVICE;

/**
 * Created by Dominik on 16.06.2017.
 */

public class OutputManager {
    private static final String TAG = "OutputManager.class";
    private static boolean vibration = false;
    private static boolean audio = false;
    private static boolean light = false;

    public static boolean getVibrationStatus() {
        return (vibration);
    }

    public static boolean getAudioStatus() {
        return (audio);
    }

    public static boolean getLightStatus() {
        return (light);
    }

    public static void setVibrationStatus(boolean _status) {
        vibration = _status;
    }

    public static void setAudioStatus(boolean _status) {
        audio = _status;
    }

    public static void setLightStatus(boolean _status) {
        light = _status;
    }

    public static void invertVibrationStatus() {
        vibration = !vibration;
    }

    public static void invertAudioStatus() {
        audio = !audio;
    }

    public static void invertLightStatus() {
        light = !light;
    }

    //Only works with vibration yet
    public static void sendSignals(Context mContext, String _morseSequence) {
        if (vibration)
            sendVibration(mContext, _morseSequence);
        if (audio)
            sendAudio(mContext, _morseSequence);
        if (light)
            if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
                sendLight(mContext, _morseSequence);
            else
                Toast.makeText(mContext, "This phone doesn't have a flash", Toast.LENGTH_SHORT).show();
    }

    private static long[] generateSequence(String _morseSequence) {
        ArrayList<Long> sequenceList = new ArrayList<>();
        sequenceList.add((long) 0);
        for (int i = 0; i < _morseSequence.length(); i++) {
            switch (_morseSequence.charAt(i)) {
                case '·': {
                    sequenceList.add((long) 100);
                    if ((i + 1 >= _morseSequence.length()) || _morseSequence.charAt(i + 1) != ' ')
                        sequenceList.add((long) 0);
                }
                break;
                case '−': {
                    sequenceList.add((long) 1000);
                    if ((i + 1 >= _morseSequence.length()) || _morseSequence.charAt(i + 1) != ' ')
                        sequenceList.add((long) 0);
                }
                break;
                case ' ': {
                    long pauseLength = 0;
                    int j;
                    for (j = 0; (((i + j) < _morseSequence.length()) && (_morseSequence.charAt(i + j) == ' ')); j++) {
                        pauseLength = pauseLength + 400;
                    }
                    sequenceList.add(pauseLength);
                    i = i + j - 1;
                }
                break;
                default:
                    Log.e(TAG, "Wrong morse char");
            }
        }
        long[] Sequence = new long[sequenceList.size()];

        for (int i = 0; i < sequenceList.size(); i++) {
            Sequence[i] = sequenceList.get(i);
        }
        return Sequence;
    }

    private static void sendVibration(Context _context, String _morseSequence) {
        Vibrator v = (Vibrator) _context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] vSequence = generateSequence(_morseSequence);
        v.vibrate(vSequence, -1);
    }

    //when called back to back then signal adds and doesnt start new
    private static void sendAudio(Context _context, final String _morseSequence) {
        new Thread(new Runnable() {
            public void run() {
                long[] aSequence = generateSequence(_morseSequence);
                for (int i = 1; i < aSequence.length; i++) {
                    if ((i % 2) == 1) {
                        sendTone((int) aSequence[i]);
                    } else {
                        try {
                            Thread.sleep((int) aSequence[i]);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "Thread sleep exception(sendAudio())");
                        }
                    }
                }
            }
        }).start();
    }

    //Need better implementation crashes when called 2 times
    private static void sendLight(final Context _context, final String _morseSequence) {
        new Thread(new Runnable() {
            public void run() {
                long[] fSequence = generateSequence(_morseSequence);
                Log.e(TAG,sequenceToString(fSequence));
                for (int i = 1; i < fSequence.length; i++) {
                    if ((i % 2) == 1) {
                        Log.e(TAG,"Send");
                        android.hardware.Camera cam = android.hardware.Camera.open();
                        android.hardware.Camera.Parameters p = cam.getParameters();
                        p.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
                        cam.setParameters(p);
                        cam.startPreview();
                        try {
                            Thread.sleep(fSequence[i]);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "Thread sleep exception(sendLight())");
                        }
                        cam.stopPreview();
                        cam.release();
                    } else {
                        try {
                            Log.e(TAG,"Pause");
                            Thread.sleep((int) fSequence[i]);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "Thread sleep exception(sendLight())");
                        }
                    }
                }
            }
        }).start();



    }
    //Sends one tone
    private static void sendTone(int _duration){
        int amStreamType = AudioManager.STREAM_MUSIC;
        int volume = 75;
        ToneGenerator toneGenerator = new ToneGenerator(amStreamType, volume);
        int toneType = ToneGenerator.TONE_DTMF_0;
        toneGenerator.startTone(toneType,_duration);
        try {
            Thread.sleep(_duration);
        } catch (InterruptedException e) {
            Log.e(TAG,"Thread sleep exception(sendSound())");
        }
        toneGenerator.release();

    }





    //Testmethode
    private static String sequenceToString(long[] _seq){
        StringBuilder sb=new StringBuilder();
        for(int i=1;i<_seq.length;i++){
            if(i%2==1)
                sb.append("Send: ");
            else
                sb.append("Pause");
            sb.append(_seq[i]+"   ");
        }
        return (sb.toString());
    }


}
