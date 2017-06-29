package at.fhooe.mc.android.morseapp;


import android.app.Fragment;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;


/**
 * public class TransFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener
 *
 * A fragment responsible for displaying the main translator screen
 * it works with the OutputManager class and the TranslatorBackend class.
 * it interacts with the GUI per OnClickListener and OnCheckedChangeListener
 *
 * The fragment has two states:(1)shown in the Container of the TransActivity, and (2) not shown in the container.
 *
 * The fragment creates a view designed in the R.layout_fragment_button.xml
 * this fragment is the main screen and isn shown after starting the app
 * the fragment has 2 modes:(1)text to morse (2)morse to text
 * the layout consist of the status switches, two textfields, 3 visible and 4 not visible buttons(visible in mode(2))
 *
 */
public class TransFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    //TAG for error messages and exceptions
    public static final String TAG = "Translator Fragment";
    //flag which when true app has to wait for termination of the Threads in the OutputManager
    public boolean waitClick=false;

    public TransFragment() {
        // Required empty public constructor
    }

    /**
     * public View onCreateView(LayoutInflater _inflater, ViewGroup _container,Bundle _savedInstanceState)
     * @param _inflater
     * @param _container
     * @param _savedInstanceState
     * @return  returns the inflated View
     *
     * method is automatically called when the fragment is created and shouldn't be called manually
     * creates the view according to the layout file
     * sets the onClickListener to the buttons
     * sets the OnCheckedChangedListener to the switches
     */
    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {

        View view=_inflater.inflate(R.layout.fragment_trans, _container, false);

        Button b=null;
        b = (Button)view.findViewById(R.id.translator_button_translate);
        b.setOnClickListener(this);
        b = (Button)view.findViewById(R.id.translator_button_switch);
        b.setOnClickListener(this);
        b = (Button)view.findViewById(R.id.translator_button_send);
        b.setOnClickListener(this);

        Switch sw=null;
        sw=(Switch)view.findViewById(R.id.translator_switch_audio);
        sw.setOnCheckedChangeListener(this);
        sw=(Switch)view.findViewById(R.id.translator_switch_light);
        sw.setOnCheckedChangeListener(this);
        sw=(Switch)view.findViewById(R.id.translator_switch_vibration);
        sw.setOnCheckedChangeListener(this);
        return view;
    }
    /**
     * public void onResume()
     *
     * is automatically called when fragment is resumed or created
     * shouldn't be called manually
     * sets the status of the switches according to the values in the OutputManager
     * sets the mode of the translator according to the mode variable
     * if mode=(2)(morse to text) then an onclick is performed to show the hidden buttons
     */
    @Override
    public void onResume() {
        super.onResume();

        View view=getView();
        Switch sw=null;
        sw=(Switch)view.findViewById(R.id.translator_switch_audio);
        sw.setChecked(OutputManager.getAudioStatus());
        sw=(Switch)view.findViewById(R.id.translator_switch_light);
        sw.setChecked(OutputManager.getLightStatus());
        sw=(Switch)view.findViewById(R.id.translator_switch_vibration);
        sw.setChecked(OutputManager.getVibrationStatus());

        if(TranslatorBackend.getMode()!=true)
        {
            Button b=(Button)getActivity().findViewById(R.id.translator_button_switch);
            TranslatorBackend.invertMode();
            b.performClick();
        }
    }

    /**
     * public void onClick(View view)
     * @param view      element which has been clicked
     *
     * Differentiate between the buttons:
     *          R.id.translator_button_translate: calls extractInsertTranslate which translates and displays morse and text
     *          R.id.translator_button_switch: switches between the modes and hides or shows the buttons
     *          R.id.translator_button_send: translates and send signals also changes the buttons text
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.translator_button_translate:{
                extractInsertTranslate();
            }break;
            case R.id.translator_button_switch:{
                //get the elements
                Button b1=(Button)getActivity().findViewById(R.id.translator_button_dash);
                Button b2=(Button)getActivity().findViewById(R.id.translator_button_dot);
                Button b3=(Button)getActivity().findViewById(R.id.translator_button_pause);
                Button b4=(Button)getActivity().findViewById(R.id.translator_button_delete);
                EditText TextTextBox = (EditText) getActivity().findViewById(R.id.translator_textfield_text);
                TextView MorseTextBox = (TextView) getActivity().findViewById(R.id.translator_textfield_morse);
                //current Mode = text to morse then
                if(TranslatorBackend.getMode()) {
                    //show buttons
                    b1.setVisibility(View.VISIBLE);
                    b2.setVisibility(View.VISIBLE);
                    b3.setVisibility(View.VISIBLE);
                    b4.setVisibility(View.VISIBLE);
                    //set OnClickListener
                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            appendString("−");
                        }
                    });
                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            appendString("·");
                        }
                    });
                    b3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            appendString(" ");
                        }
                    });
                    b4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteOne();
                        }
                    });
                    //make textfield not writeable
                    TextTextBox.setTag(TextTextBox.getKeyListener());
                    TextTextBox.setKeyListener(null);
                    //Change text and hint
                    TextTextBox.setHint(R.string.translator_textfield_text_hint_mode_2);
                    MorseTextBox.setText(R.string.translator_textfield_morse_hint_mode_2);
                }
                //current mode=morse to text
                else
                {
                    //Hide buttons
                    b1.setVisibility(View.GONE);
                    b2.setVisibility(View.INVISIBLE);
                    b3.setVisibility(View.INVISIBLE);
                    b4.setVisibility(View.INVISIBLE);
                    //deactivate OnClickListener of these buttons
                    b1.setOnClickListener(null);
                    b2.setOnClickListener(null);
                    b3.setOnClickListener(null);
                    b4.setOnClickListener(null);
                    //Change hint and text
                    TextTextBox.setKeyListener((KeyListener)TextTextBox.getTag());
                    TextTextBox.setHint(R.string.translator_textfield_text_hint_mode_1);
                    MorseTextBox.setText(R.string.translator_textfield_morse_hint_mode_1);
                }
                //invert mode to new current mode
                TranslatorBackend.invertMode();
            }break;
            case R.id.translator_button_send:{
                //translate
                extractInsertTranslate();
                Button b=(Button)getActivity().findViewById(R.id.translator_button_send);
                //if buttons shows send then send
                if(b.getText()==getResources().getString(R.string.translator_button_out_send_text)) {
                    //change text to stop
                    b.setText(R.string.translator_button_out_pause_text);
                    TextView MorseTextBox = (TextView) getActivity().findViewById(R.id.translator_textfield_morse);
                    //call sendSignals to send the signals
                    OutputManager.sendSignals(getActivity(),MorseTextBox.getText().toString());
                    //waitclick=false cause no need for waiting of termination
                    waitClick=false;
                }
                else {
                    //wenn button text=Stop
                    if(!waitClick){
                        //force termination of all Threads
                        OutputManager.forceTermination(getActivity());
                        //waitClick true cause you have to wait for termination of all threads
                        waitClick=true;
                    }

                }


            }break;
            default:{
                Log.e(TAG,"Unexpected button was pressed");
            }
        }

    }
    /**
     * public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked)
     * @param _buttonView   switch which has been changed
     * @param _isChecked    if switch is checked or not
     *
     * sets the OutputManager statuses according to the _isChecked parameter
     */
    @Override
    public void onCheckedChanged(CompoundButton _buttonView, boolean _isChecked) {
        switch (_buttonView.getId()) {
            case R.id.translator_switch_audio:{
                OutputManager.setAudioStatus(_isChecked);
            }break;
            case R.id.translator_switch_light:{
                OutputManager.setLightStatus(_isChecked);
            }break;
            case R.id.translator_switch_vibration:{
                OutputManager.setVibrationStatus(_isChecked);
            }break;
            default:Log.e(TAG,"Unexpected Switch was pressed");
        }
    }

    /**
     * private void appendString(String s)
     * @param _s String that should be appended either "−" or "·" or " "
     *
     * Used in mode (2)morse to text
     * Called by the buttons R.id.translator_button_dash, R.id.translator_button_dot and R.id.translator_button_pause
     * method for the additional buttons to interact with the textfield
     * appends the string to the morse textfield and translates it to text
     */
    private void appendString(String _s){
        //get current text in the morse textfield
        TextView MorseTextBox = (TextView) getActivity().findViewById(R.id.translator_textfield_morse);
        String s1=MorseTextBox.getText().toString();
        StringBuilder sb = new StringBuilder();
        //if textfield isn't empty and showing the hint
        if(!s1.equalsIgnoreCase(getResources().getString(R.string.translator_textfield_morse_hint_mode_2)))
            sb.append(s1);
        //Append new char/String
        sb.append(_s);
        //Set text to morse textfield
        MorseTextBox.setText(sb.toString());
    }

    /**
     * private void deleteOne()
     *
     * method is called by the R.id.translator_button_delete
     * method to delete one char from the morse textfield
     */
    private void deleteOne(){
        //get current text
        TextView MorseTextBox = (TextView) getActivity().findViewById(R.id.translator_textfield_morse);
        String s1=MorseTextBox.getText().toString();
        StringBuilder sb = new StringBuilder();
        //if not showing the hint
        if(!s1.equalsIgnoreCase(getResources().getString(R.string.translator_textfield_morse_hint_mode_2))) {
            if(s1.length()>1) {
                sb.append(s1.substring(0, s1.length() - 1));
                //set the new morse text
                MorseTextBox.setText(sb.toString());
            }
            else {
                MorseTextBox.setText(getResources().getString(R.string.translator_textfield_morse_hint_mode_2));
            }
        }

    }

    /**
     * public void extractInsertTranslate()
     *
     * method to translate and display the texts
     * differentiates between two modes
     *      mode=true:extract text, translate it to morse and display morse
     *      mode=true:extract morse, translate it to text and display text
     */
    public void extractInsertTranslate(){
        if(TranslatorBackend.getMode()) {
            EditText TextTextBox = (EditText) getActivity().findViewById(R.id.translator_textfield_text);
            TextView MorseTextBox = (TextView) getActivity().findViewById(R.id.translator_textfield_morse);
            MorseTextBox.setText(TranslatorBackend.translate(TextTextBox.getText().toString()));
        }
        else
        {
            EditText TextTextBox = (EditText) getActivity().findViewById(R.id.translator_textfield_text);
            TextView MorseTextBox = (TextView) getActivity().findViewById(R.id.translator_textfield_morse);
            TextTextBox.setText(TranslatorBackend.translateBack(MorseTextBox.getText().toString()));
        }
    }
}

