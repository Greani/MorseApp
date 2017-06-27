package at.fhooe.mc.android.morseapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.security.Policy;
import java.util.ArrayList;


import static android.content.Context.CAMERA_SERVICE;

/**
 * public class OutputManager
 *
 * Responsible for sending the morse code through different mediums.
 * Handles the switches of the fragments for the mediums
 * Has two nested classes for Threads
 *
 * Only static methods and variables cause the Manager is so to say a "background task".
 * Operates in the background of both fragments which need the output.
 * And as there is only one instance for the camera(flash) and only one speaker its easier to handle them in a static context
 */

public class OutputManager {
    //Tag for error messages and exceptions
    private static final String TAG = "OutputManager.class";
    //The states of the three switches
    private static boolean vibration = false;
    private static boolean audio = false;
    private static boolean light = false;
    //Indicator for the threads if they need to be terminated
    private static boolean terminate=false;
    //Indicator if camera is working and not released
    private static boolean lightOn=false;
    //Vibrator for handling the VIbrations
    private static Vibrator v=null;
    //Handler to have acces to the GUI from the two Threads
    private static Handler mHandler=new Handler();

    /**
     * static boolean getVibrationStatus()
     * @return  Returns if the switch is/should be set(returns boolean vibration).
     */
    public static boolean getVibrationStatus() {
        return (vibration);
    }

    /**
     * public static boolean getAudioStatus()
     * @return  Returns if the switch is/should be set(returns boolean audio)
     */

    public static boolean getAudioStatus() {
        return (audio);
    }

    /**
     * public static boolean getLightStatus()
     * @return  Returns if the switch is/should be set(returns boolean light)
     */
    public static boolean getLightStatus() {
        return (light);
    }

    /**
     * public static void setVibrationStatus(boolean _status)
     * @param _status   Status in which the variable vibration should be set
     * Sets the status of vibration(Most of the time through the switches)
     */

    public static void setVibrationStatus(boolean _status) {
        vibration = _status;
    }

    /**
     * public static void setAudioStatus(boolean _status)
     * @param _status   Status in which the variable audio should be set
     * Sets the status of audio(Most of the time through the switches)
     */

    public static void setAudioStatus(boolean _status) {
        audio = _status;
    }

    /**
     * public static void setLightStatus(boolean _status)
     * @param _status   Status in which the variable light should be set
     * Sets the status of light(Most of the time through the switches)
     */

    public static void setLightStatus(boolean _status) {
        light = _status;
    }

    /**
     * public static void sendSignals(final Activity _act, String _morseSequence)
     * @param _act  The activity/fragment from where the method is called.
     * @param _morseSequence    String which carries the morse code
     *
     * Method which is called to send the Signal. Is the only method which is called to send the Signals
     * Looks at the indicator variables(vibration,audio,light) and then calls the corresponding method to send.
     * So to say it works more like a manager.
     */

    public static void sendSignals(final Activity _act, String _morseSequence) {
            //convert morse code into a sequence of milliseconds
            //sequence consist: First value=delay of output and then switching between signal and pause
            long[] sequence=generateSequence(_morseSequence);
            //calling the methods according to the variables
            if (vibration) {
                sendVibration(_act, sequence);
            }
            if (audio) {
                sendAudio(_act,sequence);
            }
            if (light) {
                //Prove if camera flash is available on the smartphone
                //Else display a Toast message
                if (_act.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    lightOn=true;
                    sendLight(_act, sequence);
                }
                else{
                    Toast.makeText(_act, "This phone doesn't have a flash", Toast.LENGTH_SHORT).show();
                }
            }
            //If only vibration is set, then there is no delay to close the camera/sound.
            //So you can resend it immediatly
            if(!audio&&!light)
            {
                //Reset the buttons Text to "Send"
                Button b=(Button)_act.findViewById(R.id.translator_button_send);
                if(b!=null) {
                    b.setText(R.string.translator_button_out_send_text);
                }
                    ShortcutFragment.setState(1);
            }

    }

    /**
     * public static void forceTermination(Activity _act)
     * @param _act activity/fragment where the method is called
     *
     * The method is called when the process of sending the signals should be terminated
     * Sets terminate to true to signal the termination to the different threads
     * Discards the vibration service
     */

    public static void forceTermination(Activity _act){
        terminate=true;
        v=(Vibrator) _act.getSystemService(Context.VIBRATOR_SERVICE);
        //if there is a vibrator then cancel the vibration process
            if(v!=null)
                v.cancel();

    }

    /**
     * private static long[] generateSequence(String _morseSequence)
     * @param _morseSequence    String of the morse code
     * @return  returns a sequence of milliseconds
     *
     * method converts a morse code into a sequence of milliseconds
     * the sequence consist of delay of the signal and then alternating between signal in ms and pause in ms
     * a '·' is 100 ms signal
     * a '−' is 1000 ms signal
     * a ' ' is 400 ms Pause
     * if there are more then one ' ' then the pause ms is added into one float value
     */
    private static long[] generateSequence(String _morseSequence) {
        //ArrayList to temporary store the sequence
        ArrayList<Long> sequenceList = new ArrayList<>();
        //Add the delay
        sequenceList.add((long) 200);

        for (int i = 0; i < _morseSequence.length(); i++) {
            switch (_morseSequence.charAt(i)) {
                case '·': {
                    sequenceList.add((long) 100);
                    //if the next char is also a signal then the pause between is 0ms
                    if ((i + 1 >= _morseSequence.length()) || _morseSequence.charAt(i + 1) != ' ')
                        sequenceList.add((long) 0);
                }
                break;
                case '−': {
                    sequenceList.add((long) 1000);
                    //if the next char is also a signal then the pause between is 0ms
                    if ((i + 1 >= _morseSequence.length()) || _morseSequence.charAt(i + 1) != ' ')
                        sequenceList.add((long) 0);
                }
                break;
                case ' ': {
                    //add the pause and if there is more then one ' ' then add them together
                    long pauseLength = 0;
                    int j;
                    for (j = 0; (((i + j) < _morseSequence.length()) && (_morseSequence.charAt(i + j) == ' ')); j++) {
                        pauseLength = pauseLength + 400;
                    }
                    sequenceList.add(pauseLength);
                    //Jump to the next char after a longer pause
                    i = i + j - 1;
                }
                break;
                default:
                    Log.e(TAG, "Wrong morse char");
            }
        }
        //Convert the ArrayList into a float Array
        long[] Sequence = new long[sequenceList.size()];
        for (int i = 0; i < sequenceList.size(); i++) {
            Sequence[i] = sequenceList.get(i);
        }
        return Sequence;
    }

    /**
     * private static void sendVibration(Activity _act, long[] _Sequence)
     * @param _act  Passing through the activity form sendSignals()
     * @param _Sequence Sequence of milliseconds generated with generateSequence()
     *
     * Method gets the vibration service from the sequence and uses the vibrate() method to send the sequence
     */
    private static void sendVibration(Activity _act, long[] _Sequence) {
        Vibrator v = (Vibrator) _act.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(_Sequence, -1);
    }

    /**
     * private static void sendAudio(Activity _act ,long[] _sequence)
     * @param _act  passing through activity from sendSignals()
     * @param _sequence sequence of milliseconds generated with generateSequence()
     *
     * Method starts a new AudioThread passing activity and sequence
     */
    private static void sendAudio(Activity _act ,long[] _sequence) {
        Thread tAudio=new AudioThread(_act,_sequence);
        tAudio.start();
    }

    /**
     * private static void sendLight(Activity _act, long[] _sequence)
     * @param _act  passing through activity from sendSignals()
     * @param _sequence sequence of milliseconds generated with generateSequence()
     *
     * Method starts and new LightThread passing activity and sequence
     */
    private static void sendLight(Activity _act, long[] _sequence) {
        Thread lThread = new LightThread(_act,_sequence);
        lThread.start();
    }

    /**
     * static class AudioThread extends Thread
     *
     * thread class to activate the a tone and pause the tone.
     * the thread is in a loop as long as the sequence isn't over.
     * the loop can be interrupted if the terminate flag is set.
     *
     * thread activates and deactivates the tone according to the sequence.
     * the tone is generated beforehand and then started and stopped
     * if the terminate flag is set then the loop is broken up
     * then per handler the button text is reset and state of the ShortcutFragment is reset.
     */
    static class AudioThread extends Thread{
        //Sequence
        private long[] aSequence;
        //TransActivity
        private Activity mainActivity;
        //Constructor passing the activity and the sequence
        public AudioThread(Activity _act,long[] _sequence){
            aSequence=_sequence;
            mainActivity=_act;
        }

        /**
         * public void run()
         * method automatically started when the thread gets started
         * sends the tone periodically according to the sequence
         * when the thread should be terminated the loop ends and if the flash isn't used the button will be reset
         */
        @Override
        public void run() {
            super.run();
            //Time to shorten the time cause the tone generating takes some time too
            int offset=30;
            for (int i = 1; i < aSequence.length; i++) {
                //if terminate break up the loop
                if(terminate)
                    break;
                //Look if it's a signal or a pause
                if ((i % 2) == 1) {
                    //call mehtod with the duration
                    sendTone((int) aSequence[i]);
                } else {
                    //if it's a pause then let the thread sleep
                    try {
                        Thread.sleep((int) aSequence[i]-offset);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "Thread sleep exception(sendAudio())");
                    }
                }
            }
            //if light isn't used then reset the button and state
            if(!lightOn) {
                terminate=false;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Button b = (Button) mainActivity.findViewById(R.id.translator_button_send);
                        if(b!=null){
                            b.setText(R.string.translator_button_out_send_text);
                        }
                        ShortcutFragment.setState(1);
                    }
                });

            }
        }

        /**
         * private static void sendTone(int _duration)
         * @param _duration duration of the time
         *
         * method generates a new tone, starts the tone and then lets the Thread sleep for _duration
         */
        private static void sendTone(int _duration){
            int amStreamType = AudioManager.STREAM_MUSIC;
            int volume = 75;
            //Generating tone
            ToneGenerator toneGenerator = new ToneGenerator(amStreamType, volume);
            //setting tone type
            int toneType = ToneGenerator.TONE_DTMF_0;
            //starting tone
            toneGenerator.startTone(toneType,_duration);
            //Thread sleep _duration
            try {
                Thread.sleep(_duration);
            } catch (InterruptedException e) {
                Log.e(TAG,"Thread sleep exception(sendSound())");
            }
            //release and stop the tone
            toneGenerator.release();

        }
    }
    /**
     * static class LightThread extends Thread
     *
     * thread class to activate the flash and pause the flash.
     * the thread is in a loop as long as the sequence isn't over.
     * the loop can be interrupted if the terminate flag is set.
     *
     * thread activates and deactivates the flash according to the sequence.
     * to activate the flash the camera has to be opened and the flash has to be set
     * then the preview has to be started.
     * to deactivate the flash the preview has to be stopped
     * it's important to terminate the thread and to release the camera else major problems can encounter
     * when ending the thread, per handler the button text is reset and state of the ShortcutFragment is reset.
     */
    static class LightThread extends Thread{
        private long[] lSequence;
        private Activity mainActivity;
        android.hardware.Camera cam=null;
        //Constructor setting sequence and activity
        public LightThread(Activity _act,long[] _sequence){
            lSequence=_sequence;
            mainActivity=_act;
        }

        /**
         * public void run()
         * Opens the camera, sets the flash and starts preview
         * then thread sleeps for the wanted time and then stop preview and release camera.
         */
        @Override
        public void run() {
            super.run();
            int offset=1;
            for (int i = 1; i < lSequence.length; i++) {
                if(terminate)
                    break;
                if ((i % 2) == 1) {
                    //open camera
                    cam = android.hardware.Camera.open();
                    //set Parameters
                    android.hardware.Camera.Parameters p = cam.getParameters();
                    p.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
                    //give parameters to camera
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
                }
            }
            //Reset the button text and state in the fragments
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Button b = (Button) mainActivity.findViewById(R.id.translator_button_send);
                        if(b!=null){
                            b.setText(R.string.translator_button_out_send_text);
                        }
                        ShortcutFragment.setState(1);

                    }
                });
            //reset terminate and lighOn
            terminate=false;
            lightOn=false;

        }
    }


}
