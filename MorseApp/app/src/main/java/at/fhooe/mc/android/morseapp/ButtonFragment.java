package at.fhooe.mc.android.morseapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Timer;

/**
 *  public class ButtonFragment extends android.app.Fragment implements View.OnTouchListener,GestureDetector.OnGestureListener, View.OnClickListener
 *  The ButtonFragment is a Fragment which is responsible for the MorseKey Screen.
 *  It interacts with the GUI and runs on the Main GUI Thread as it implements an OnTouchListener, OnClickListener and GestureDetector.
 *
 *  The Fragment has two states:(1)shown in the Container of the TransActivity, and (2) not shown in the container.
 *
 *  The Fragment creates a View designed in the R.layout_fragment_button.xml
 *  In this View you can interact with three Buttons per Onclick and one per OnTouch/GestureDetector.
 *  It's Gesture Detector decides between Short Touch and Long Touch to determine a short or long morse char.
 */
public class ButtonFragment extends android.app.Fragment implements View.OnTouchListener,GestureDetector.OnGestureListener, View.OnClickListener {

    //Global Gesture Detector to be used in the whole class
    GestureDetector mGestureDetector =null;
    //Global TAG for error messages and exceptions
    public static final String TAG="Buttons Fragment";
    //Global float saves the system time of the last interaction
    private float lastTouchTime=0;

    public ButtonFragment() {
        // Required empty public constructor
    }

    /**
     * public View onCreateView(LayoutInflater _inflater, ViewGroup _container,Bundle _savedInstanceState)
     * @param _inflater
     * @param _container
     * @param _savedInstanceState
     * @return  Retruns the inflated View
     *
     * Creates the view when the ButtonFragment ist created
     * Sets the GestureDetector to the activity and sets the OntouchListener and the OnclickListener on the buttons
     */
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {
        // Inflate the layout for this fragment
        View v = _inflater.inflate(R.layout.fragment_button, _container, false);

        Button b=null;
        //Setting OnTouchListener for Morsekey
        b=(Button)v.findViewById(R.id.buttons_button_morse_here);
        b.setOnTouchListener(this);
        mGestureDetector=new GestureDetector(getActivity(),this);

        //Setting OnClickListener for reset,pause and clear
        b=(Button)v.findViewById(R.id.buttons_button_reset);
        b.setOnClickListener(this);
        b=(Button)v.findViewById(R.id.buttons_button_pause);
        b.setOnClickListener(this);
        b=(Button)v.findViewById(R.id.buttons_button_clear);
        b.setOnClickListener(this);

        return v;
    }
    /*
        Methods of the OntouchListener/Gesture Detector only the implemented on are commented
        All called by the Listener/Detector. No manual call wanted/allowed.
     */

    /**
     *
     * @param v         View->Touched Element
     * @param _event    Event which happens,needed by the GestureDetector
     * @return          Returns false
     */
    @Override
    public boolean onTouch(View v, MotionEvent _event) {
        mGestureDetector.onTouchEvent(_event);
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;

    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    /**
     *
     * @param e Event gathered by the GestureDetector
     * @return  returns always true
     *
     * A Single Tap means that the char '·' has to be added to the morse text
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        addChar('·');
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    /**
     *
     * @param e Event gathered by the Gesture Detector
     *
     * LongPress means that the char '−' has to be added to the morse text
     */
    @Override
    public void onLongPress(MotionEvent e) {
        addChar('−');
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    /**
     *
     * @param _ch   Method expects a char ('·' or '−' or ' ')
     *
     * If the char is ' ' then the method has to call lookAtTimer() to see how many ' ' has to be added to the textfield.
     * Else/Then it creates a StringBuilder, adds the already existing text and the nadds the char and displays the new Text.
     * The morse text is then translated into normal text and displayed in the R.id.buttons_text_text
     */
    private void addChar(char _ch){
        //If new morse char the time between the chars has to be converted into ' 's
        if(_ch!=' ') {
            lookAtTimer();
        }

        StringBuilder bob=new StringBuilder();
        //Appending existing text to StringBuilder
        EditText morseCode=(EditText)getActivity().findViewById(R.id.buttons_text_morsecode);
        bob.append(morseCode.getText().toString());
        //Appending new char
        bob.append(_ch);
        //Display morse text
        morseCode.setText(bob.toString());
        //Translate and display normal text
        EditText text=(EditText)getActivity().findViewById(R.id.buttons_text_text);
        text.setText(TranslatorBackend.translateBack(bob.toString()));
    }

    /**
     * private void lookAtTimer()
     * Method to see how many milliseconds have elapsed between the last interaction and the current interaction using the lastTouchTime field.
     * Then it adds, depending on the maxTimeBetween how many ' ' have to be inserted.
     * At the end the methods sets the lastTouchTime to the current time
     *
     * maxTimeBetween defines how many milliseconds have to elapse to set one ' '
     */
    private void lookAtTimer(){
        int maxTimeBetween=800;  //Milliseconds
        if(lastTouchTime!=0){
            //Calculating the timespan bewtween last interaction and current one
            float span=SystemClock.elapsedRealtime()-lastTouchTime;
            //Adding ' ' per maxTimeBetween Milliseconds
            for(int i=maxTimeBetween;span-i>0;){
                addChar(' ');
                i=i+maxTimeBetween;
            }
        }
        //Setting the lastTouchTime to the current System time
        lastTouchTime= SystemClock.elapsedRealtime();

    }

    /**
     * public void onClick(View _v)
     * @param _v    View->Element which has been cicked
     *
     * Method which is called by the View. Shouldn't be called manually
     * Decides which Button has been pressed and executes the corresponding code
     * R.id.buttons_button_clear: Delete the last character of the text and reset lastTouchTimer
     * R.id.buttons_button_pause: Resets the lastTouchTimer
     * R.id.buttons_button_reset: Deletes the whole text and resets lastTouchTimer
     */
    @Override
    public void onClick(View _v) {
        switch(_v.getId()){
            case R.id.buttons_button_clear:{
                lastTouchTime=0;
                //Get Text from Textfield
                EditText morseCode=(EditText)getActivity().findViewById(R.id.buttons_text_morsecode);
                String s1=morseCode.getText().toString();
                StringBuilder bob = new StringBuilder();
                //Append every char form text except char[length-1] if text.length()>0
                if(s1.length()>0) {
                    bob.append(s1.substring(0, s1.length() - 1));
                    morseCode.setText(bob.toString());
                    EditText text=(EditText)getActivity().findViewById(R.id.buttons_text_text);
                    text.setText(TranslatorBackend.translateBack(bob.toString()));
                }
                //Else display "" on both textfields
                else{
                    morseCode.setText("");
                    EditText text=(EditText)getActivity().findViewById(R.id.buttons_text_text);
                    text.setText("");
                }
            }break;
            case R.id.buttons_button_pause:{
                lastTouchTime=0;
            }break;
            case R.id.buttons_button_reset:{
                lastTouchTime=0;
                //Set "" on both
                EditText morseCode=(EditText)getActivity().findViewById(R.id.buttons_text_morsecode);
                morseCode.setText("");
                EditText text=(EditText)getActivity().findViewById(R.id.buttons_text_text);
                text.setText("");
            }break;
            default: {
            Log.e(TAG,"unexpected button encountered");
            }
        }


    }
}
