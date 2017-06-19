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
    private static boolean terminate=true;
    private static Vibrator v=null;

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

    //Only works with vibration yet
    public static void sendSignals(Context _context, String _morseSequence) {
        v=(Vibrator) _context.getSystemService(Context.VIBRATOR_SERVICE);
        terminate=!terminate;
        if(terminate){
            if(v!=null)
            v.cancel();
        }
        else {
            if (vibration) {
                sendVibration(_context, _morseSequence);
            }
            if (audio) {

                sendAudio(_context, _morseSequence);
            }
            if (light) {

                if (_context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
                    sendLight(_context, _morseSequence);
                else
                    Toast.makeText(_context, "This phone doesn't have a flash", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static long[] generateSequence(String _morseSequence) {
        ArrayList<Long> sequenceList = new ArrayList<>();
        sequenceList.add((long) 200);
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
                int offset=30;
                long[] aSequence = generateSequence(_morseSequence);
                for (int i = 1; i < aSequence.length; i++) {
                    if(terminate)
                        break;
                    if ((i % 2) == 1) {
                        sendTone((int) aSequence[i]);
                    } else {
                        try {
                            Thread.sleep((int) aSequence[i]-offset);
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
            android.hardware.Camera cam=null;
            public void run() {
                int offset=1;
                long[] lSequence = generateSequence(_morseSequence);
                Log.e(TAG,sequenceToString(lSequence));
                for (int i = 1; i < lSequence.length; i++) {
                    if(terminate)
                        break;
                    if ((i % 2) == 1) {
                        Log.e(TAG,"Send");
                        cam = android.hardware.Camera.open();
                        android.hardware.Camera.Parameters p = cam.getParameters();
                        p.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
                        cam.setParameters(p);
                        cam.startPreview();
                        try {
                            Thread.sleep(lSequence[i]-offset);
                        } catch (InterruptedException e) {
                            Log.e(TAG, "Thread sleep exception(sendLight())");
                        }
                        cam.stopPreview();
                        cam.release();
                    } else {
                            Log.e(TAG,"Pause");

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
