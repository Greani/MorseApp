package at.fhooe.mc.android.morseapp;


import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TransFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "Translator Fragment";

    public TransFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_trans, container, false);

        Button b = (Button)view.findViewById(R.id.translator_button_translate);
        b.setOnClickListener(this);

        return view;


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.translator_button_translate:{
                EditText TextTextBox=(EditText)getActivity().findViewById(R.id.translator_textfield_text);
                TextView MorseTextBox=(TextView)getActivity().findViewById(R.id.translator_textfield_morse);
                MorseTextBox.setText(TranslatorBackend.translate(TextTextBox.getText().toString()));

            }break;
            default:{
                Log.e(TAG,"Not available button was pressed");
            }
        }

    }
}
