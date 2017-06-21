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
 * A simple {@link Fragment} subclass.
 */
public class ShortcutFragment extends android.app.Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    public static final String TAG="Shortcut Fragment";

    public ShortcutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_shortcut, container, false);

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

        Switch sw=null;
        sw=(Switch)view.findViewById(R.id.shortcut_switch_audio);
        sw.setOnCheckedChangeListener(this);
        sw=(Switch)view.findViewById(R.id.shortcut_switch_light);
        sw.setOnCheckedChangeListener(this);
        sw=(Switch)view.findViewById(R.id.shortcut_switch_vibration);
        sw.setOnCheckedChangeListener(this);

        return(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        View view=getView();
        Switch sw=null;
        sw=(Switch)view.findViewById(R.id.shortcut_switch_audio);
        sw.setChecked(OutputManager.getAudioStatus());
        sw=(Switch)view.findViewById(R.id.shortcut_switch_light);
        sw.setChecked(OutputManager.getLightStatus());
        sw=(Switch)view.findViewById(R.id.shortcut_switch_vibration);
        sw.setChecked(OutputManager.getVibrationStatus());
    }

    @Override
    public void onClick(View _v) {
        Button b=(Button)getActivity().findViewById(_v.getId());
        OutputManager.sendSignals(getActivity(),TranslatorBackend.translate(b.getText().toString()));

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.shortcut_switch_audio:{
                OutputManager.setAudioStatus(isChecked);
            }break;
            case R.id.shortcut_switch_light:{
                OutputManager.setLightStatus(isChecked);
            }break;
            case R.id.shortcut_switch_vibration:{
                OutputManager.setVibrationStatus(isChecked);
            }break;
            default:
                Log.e(TAG,"Unexpected Switch was pressed");
        }
    }
}
