package de.taracamp.familyplan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class LoginFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "LoginFragment";
    private static final String LOG_INFO = "LOG_INFO";

    private View rootView;

    private Button buttonEmail;
    private Button buttonFacebook;
    private Button buttonGoogle;
    private Button buttonGuest;

    private void init(){

        /* Email Login */

        buttonEmail = (Button)rootView.findViewById(R.id.btn_Emaillogin);
        buttonEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_INFO,"Button Email wurde aktiviert.");
                replaceFragment(new LoginEmailFragment());
            }
        });

        /*  Facebook Login*/

        buttonFacebook = (Button)rootView.findViewById(R.id.btn_Facebooklogin);
        buttonFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_INFO,"Button Facebook wurde aktiviert.");
                replaceFragment(new LoginFacebookFragment());
            }
        });

        /*  Google Login */

        buttonGoogle = (Button)rootView.findViewById(R.id.btn_Googlelogin);
        buttonGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_INFO,"Button Google wurde aktiviert.");
                replaceFragment(new LoginGoogleFragment());
            }
        });

        /* Gast Login */

        buttonGuest = (Button)rootView.findViewById(R.id.btn_Guestlogin);
        buttonGuest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOG_INFO,"Button Gast wurde aktiviert.");
                //// TODO: 06.03.2017 Gast implementierung
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login,container,false);
        Log.d(LOG_INFO,TAG + ":onCreateView()");

        init();

        return rootView;
    }

    /*
    * Ersetzt das aktive Fragment mit einem anderen
    * */
    private void replaceFragment(Fragment fragment){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,fragment)
                .addToBackStack(null)
                .commit();
    }

}
