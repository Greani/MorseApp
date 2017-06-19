package at.fhooe.mc.android.morseapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class TranslatorActivity extends AppCompatActivity {

    private final static String TAG="Translator Activity";

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_morsekey: {
                    FragmentManager fmgr = getFragmentManager();
                    FragmentTransaction ft = fmgr.beginTransaction();
                    ft.replace(R.id.main_activity_fragment_container,new ButtonFragment());
                    ft.addToBackStack(null);
                    ft.commit();
                }
                return true;
                case R.id.navigation_translator: {
                    FragmentManager fmgr = getFragmentManager();
                    FragmentTransaction ft = fmgr.beginTransaction();
                    ft.replace(R.id.main_activity_fragment_container,new TransFragment());
                    ft.addToBackStack(null);
                    ft.commit();
                }
                return true;
                case R.id.navigation_shortcuts: {
                    FragmentManager fmgr = getFragmentManager();
                    FragmentTransaction ft = fmgr.beginTransaction();
                    ft.replace(R.id.main_activity_fragment_container, new ShortcutFragment());
                    ft.addToBackStack(null);
                    ft.commit();
                }
                return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        View view = navigation.findViewById(R.id.navigation_translator);
        view.performClick();
        TranslatorBackend.fillMap();
        TranslatorBackend.fillBackMap();
    }

    @Override
    protected void onResume() {
        super.onResume();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        View view = navigation.findViewById(R.id.navigation_translator);
        view.performClick();
        TranslatorBackend.fillMap();
        TranslatorBackend.fillBackMap();
    }
}
