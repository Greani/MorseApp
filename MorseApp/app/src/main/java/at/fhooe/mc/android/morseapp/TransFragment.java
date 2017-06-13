package at.fhooe.mc.android.morseapp;


import android.app.Fragment;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;


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
        b = (Button)view.findViewById(R.id.translator_button_switch);
        b.setOnClickListener(this);

        return view;


    }
    private void setString(String s){
        TextView MorseTextBox = (TextView) getActivity().findViewById(R.id.translator_textfield_morse);
        String s1=MorseTextBox.getText().toString();
        StringBuilder sb = new StringBuilder();
        if(!s1.equalsIgnoreCase(getResources().getString(R.string.translator_textfield_morse_hint_mode_2)))
        sb.append(s1);
        sb.append(s);
        MorseTextBox.setText(sb.toString());
    }
    private void deleteOne(){
        TextView MorseTextBox = (TextView) getActivity().findViewById(R.id.translator_textfield_morse);
        String s1=MorseTextBox.getText().toString();
        StringBuilder sb = new StringBuilder();
        if(!s1.equalsIgnoreCase(getResources().getString(R.string.translator_textfield_morse_hint_mode_2))) {
            if(s1.length()>0)
            sb.append(s1.substring(0,s1.length()-1));
        }
            MorseTextBox.setText(sb.toString());
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.translator_button_translate:{
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
            }break;
            case R.id.translator_button_switch:{
                Button b1=(Button)getActivity().findViewById(R.id.translator_button_dash);
                Button b2=(Button)getActivity().findViewById(R.id.translator_button_dot);
                Button b3=(Button)getActivity().findViewById(R.id.translator_button_pause);
                Button b4=(Button)getActivity().findViewById(R.id.translator_button_delete);
                EditText TextTextBox = (EditText) getActivity().findViewById(R.id.translator_textfield_text);
                TextView MorseTextBox = (TextView) getActivity().findViewById(R.id.translator_textfield_morse);
                if(TranslatorBackend.getMode()) {
                    b1.setVisibility(View.VISIBLE);
                    b2.setVisibility(View.VISIBLE);
                    b3.setVisibility(View.VISIBLE);
                    b4.setVisibility(View.VISIBLE);
                    String s1 =TextTextBox.getText().toString();

                    b1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setString("−");
                        }
                    });
                    b2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setString("·");
                        }
                    });
                    b3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            setString(" ");
                        }
                    });
                    b4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            deleteOne();
                        }
                    });

                    TextTextBox.setTag(TextTextBox.getKeyListener());
                    TextTextBox.setKeyListener(null);
                    TextTextBox.setHint(R.string.translator_textfield_text_hint_mode_2);
                    MorseTextBox.setText(R.string.translator_textfield_morse_hint_mode_2);
                }
                else
                {
                    b1.setVisibility(View.INVISIBLE);
                    b2.setVisibility(View.INVISIBLE);
                    b3.setVisibility(View.INVISIBLE);
                    b4.setVisibility(View.INVISIBLE);
                    b1.setOnClickListener(null);
                    b2.setOnClickListener(null);
                    b3.setOnClickListener(null);
                    b4.setOnClickListener(null);
                    TextTextBox.setKeyListener((KeyListener)TextTextBox.getTag());
                    TextTextBox.setHint(R.string.translator_textfield_text_hint_mode_1);
                    MorseTextBox.setText(R.string.translator_textfield_morse_hint_mode_1);
                }
                TranslatorBackend.setMode();
            }break;
            default:{
                Log.e(TAG,"Not available button was pressed");
            }
        }

    }
}

