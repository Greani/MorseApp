package at.fhooe.mc.android.morseapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class ButtonFragment extends android.app.Fragment {

    public ButtonFragment() {
        // Required empty public constructor
    }

    public View onCreateView(LayoutInflater _inflater, ViewGroup _container,
                             Bundle _savedInstanceState) {
        // Inflate the layout for this fragment
        View v = _inflater.inflate(R.layout.fragment_button, _container, false);

        return v;
    }
}
