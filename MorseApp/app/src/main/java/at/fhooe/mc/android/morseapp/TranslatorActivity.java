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
/**
 * public class TranslatorActivity extends AppCompatActivity
 *
 * is the main and only activity in this app
 * this activity is so to say the backbone of the app
 * it only consist of the navigation and the fragment container
 *
 * navigation is done according to the R.id.navigation xml and is a bottom navigation bar
 * the navigation the switches between the content in the fragment container
 * the content can be morsekey screen,translator screen and shortcut screen (fragments)
 *
 */
public class TranslatorActivity extends AppCompatActivity {
    //Tag for error messages and exceptions
    private final static String TAG="Translator Activity";

    //Creates a new Navigation Listener which observes the navigation bar
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        /**
         * public boolean onNavigationItemSelected(@NonNull MenuItem _item)
         * @param _item  which item in the menu/navigation bar has been selected
         * @return true if there the right item has been found else returns false
         *
         * Differentiates between the three items
         * Changes the content in the fragment container according to the items:
         *      R.id.navigation_morsekey=ButtonFragment
         *      R.id.navigation_translator=TransFramgnet
         *      R.id.navigation_shortcuts=ShortcutFragmentFragment
         */
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem _item) {
            switch (_item.getItemId()) {
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

    /**
     * protected void onCreate(Bundle _savedInstanceState)
     * @param _savedInstanceState
     *
     * called automatically when the app is started shouldn't be called manually
     * creates view according to the R.layout.activity_translator xml(navigation and fragment container)
     * implements the navigation
     * perfomrm onclick to show the right itme in the navigation activated and activate it
     * initialize the hashmaps of the TranslatorBackedn
     *
     */
    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_translator);
        //create navigation
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //perform onclick to show fragment
        View view = navigation.findViewById(R.id.navigation_translator);
        view.performClick();
        //initialize hashmaps
        TranslatorBackend.fillMap();
        TranslatorBackend.fillBackMap();
    }

    /**
     * protected void onResume()
     *
     * method is automatically called when starting or resuming the app
     * shouldn't be called manually
     * implements naviagtion
     * activates the TransFragment
     * fills hashmaps
     */
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
