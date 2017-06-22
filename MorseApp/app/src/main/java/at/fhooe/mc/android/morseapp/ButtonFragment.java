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


public class ButtonFragment extends android.app.Fragment implements View.OnTouchListener,GestureDetector.OnGestureListener, View.OnClickListener {

    GestureDetector mGestureDetector =null;
    public static final String TAG="Buttons Fragment";
    private float lastTouchTime=0;

    public ButtonFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {
        // Inflate the layout for this fragment
        View v = _inflater.inflate(R.layout.fragment_button, _container, false);
        Button b=null;
        b=(Button)v.findViewById(R.id.buttons_button_morse_here);
        b.setOnTouchListener(this);
        mGestureDetector=new GestureDetector(getActivity(),this);
        b=(Button)v.findViewById(R.id.buttons_button_reset);
        b.setOnClickListener(this);
        b=(Button)v.findViewById(R.id.buttons_button_pause);
        b.setOnClickListener(this);
        b=(Button)v.findViewById(R.id.buttons_button_clear);
        b.setOnClickListener(this);
        return v;
    }

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

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        addChar('·');
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        addChar('−');
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
    private void addChar(char _ch){
        if(_ch!=' ') {
            lookAtTimer();
        }
        StringBuilder bob=new StringBuilder();
        EditText morseCode=(EditText)getActivity().findViewById(R.id.buttons_text_morsecode);
        bob.append(morseCode.getText().toString());
        bob.append(_ch);
        morseCode.setText(bob.toString());
        EditText text=(EditText)getActivity().findViewById(R.id.buttons_text_text);
        text.setText(TranslatorBackend.translateBack(bob.toString()));
    }
    private void lookAtTimer(){
        int maxTimeBetween=800;  //Milliseconds
        Log.e(TAG,"aaaaaaa"+lastTouchTime);
        if(lastTouchTime!=0){
            Log.e(TAG,"bbbbbbb");
            float span=SystemClock.elapsedRealtime()-lastTouchTime;
            Log.e(TAG,"cccccc"+span);
            for(int i=maxTimeBetween;span-i>0;){
                Log.e(TAG,"dddd"+i+"    "+(span-i));
                addChar(' ');
                i=i+maxTimeBetween;
            }
        }
        lastTouchTime= SystemClock.elapsedRealtime();

    }

    @Override
    public void onClick(View _v) {
        switch(_v.getId()){
            case R.id.buttons_button_clear:{
                lastTouchTime=0;
                EditText morseCode=(EditText)getActivity().findViewById(R.id.buttons_text_morsecode);
                String s1=morseCode.getText().toString();
                StringBuilder bob = new StringBuilder();
                if(s1.length()>0) {
                    bob.append(s1.substring(0, s1.length() - 1));
                    morseCode.setText(bob.toString());
                    EditText text=(EditText)getActivity().findViewById(R.id.buttons_text_text);
                    text.setText(TranslatorBackend.translateBack(bob.toString()));
                }
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
