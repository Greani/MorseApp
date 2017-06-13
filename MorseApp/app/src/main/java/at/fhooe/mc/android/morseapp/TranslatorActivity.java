package at.fhooe.mc.android.morseapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class TranslatorActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_morsekey:
                    mTextMessage.setText(R.string.title_morsekey);
                    return true;
                case R.id.navigation_translator:
                    mTextMessage.setText(R.string.title_translator);
                    return true;
                case R.id.navigation_shortcuts:
                    mTextMessage.setText(R.string.title_shortcuts);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translator);

        TranslatorBackend.fillMap();
        TranslatorBackend.fillBackMap();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        TransFragment fragment = new TransFragment();
        fragmentTransaction.add(R.id.main_fragment_container, fragment);
        fragmentTransaction.commit();

    }

}
