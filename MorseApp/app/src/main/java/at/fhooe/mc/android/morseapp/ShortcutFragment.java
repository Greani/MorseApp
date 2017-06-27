package at.fhooe.mc.android.morseapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;


/**
 * public class ShortcutFragment extends android.app.Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener
 *
 * A fragment responsible for displaying the shortcut screen
 * it works with the OutputManager class and the TranslatorBackend class.
 * it interacts with the GUI per OnClickListener and OnCheckedChangeListener
 *
 * The Fragment has two states:(1)shown in the Container of the TransActivity, and (2) not shown in the container.
 *
 * The fragment creates a view designed in the R.layout_fragment_button.xml
 * the layout consists of several buttons and switches
 * every button sends the signal according to it's text
 */
public class ShortcutFragment extends android.app.Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    //TAG for error messages and exceptions
    public static final String TAG="Shortcut Fragment";
    //state which indicates if something is being sent or stopped
    private static int state=1;
    public ShortcutFragment() {
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
     * sets the onClickListener to the button
     * sets the OnCheckedChangedListener to the switches
     */
    @Override
    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {
        // Inflate the layout for this fragment
        View view=_inflater.inflate(R.layout.fragment_shortcut, _container, false);

        //setting the listeners to the buttons
        Button b=null;
        b=(Button)view.findViewById(R.id.shortcut_button_help);
        b.setOnClickListener(this);
        b=(Button)view.findViewById(R.id.shortcut_button_yes);
        b.setOnClickListener(this);
        b=(Button)view.findViewById(R.id.shortcut_button_no);
        b.setOnClickListener(this);
        b=(Button)view.findViewById(R.id.shortcut_button_maybe);
        b.setOnClickListener(this);
        b=(Button)view.findViewById(R.id.shortcut_button_bye);
        b.setOnClickListener(this);
        b=(Button)view.findViewById(R.id.shortcut_button_hello);
        b.setOnClickListener(this);
        b=(Button)view.findViewById(R.id.shortcut_button_sos);
        b.setOnClickListener(this);

        //setting listeners to the switches
        Switch sw=null;
        sw=(Switch)view.findViewById(R.id.shortcut_switch_audio);
        sw.setOnCheckedChangeListener(this);
        sw=(Switch)view.findViewById(R.id.shortcut_switch_light);
        sw.setOnCheckedChangeListener(this);
        sw=(Switch)view.findViewById(R.id.shortcut_switch_vibration);
        sw.setOnCheckedChangeListener(this);

        return(view);
    }

    /**
     * public void onResume()
     *
     * is automatically called when fragment is resumed or created
     * shouldn't be called manually
     * sets the status of the switches according to the values in the OutputManager
     */
    @Override
    public void onResume() {
        super.onResume();
        View view=getView();
        Switch sw=null;
        //setting the setChecked values
        sw=(Switch)view.findViewById(R.id.shortcut_switch_audio);
        sw.setChecked(OutputManager.getAudioStatus());
        sw=(Switch)view.findViewById(R.id.shortcut_switch_light);
        sw.setChecked(OutputManager.getLightStatus());
        sw=(Switch)view.findViewById(R.id.shortcut_switch_vibration);
        sw.setChecked(OutputManager.getVibrationStatus());
    }

    /**
     * public void onClick(View _v)
     * @param _v view-> which button has been pressed
     *
     * method which is automatically called by the OnClickListener shouldn't be called manually
     * no differentiation per siwtch of the buttons cause it only depends on the text of a button
     * executes code according to the states:
     * state1: no Signal is being sent -> get Text of the Button, translate and send it
     * state2: a Signal is being sent -> force Termination of the Signal
     * state3: waiting for termination being done -> nothing
     */
    @Override
    public void onClick(View _v) {
        if(state==1) {
            state=2;
            Button b = (Button) getActivity().findViewById(_v.getId());
            OutputManager.sendSignals(getActivity(), TranslatorBackend.translate(b.getText().toString()));

        } else if (state == 2) {
            state=3;
            OutputManager.forceTermination(getActivity());
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
            case R.id.shortcut_switch_audio:{
                OutputManager.setAudioStatus(_isChecked);
            }break;
            case R.id.shortcut_switch_light:{
                OutputManager.setLightStatus(_isChecked);
            }break;
            case R.id.shortcut_switch_vibration:{
                OutputManager.setVibrationStatus(_isChecked);
            }break;
            default:
                Log.e(TAG,"Unexpected Switch was pressed");
        }
    }

    /**
     *
     * @param _state value which state should be
     *
     *  method exist so that the handler in the OutputManager can change the state
     */
    public static void setState(int _state){
        state=_state;
    }
}
