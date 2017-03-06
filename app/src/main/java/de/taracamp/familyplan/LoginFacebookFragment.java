package de.taracamp.familyplan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFacebookFragment extends Fragment {

    private static final String TAG = "LoginFacebookFragment";
    private static final String LOG_INFO = "LOG_INFO";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_INFO,TAG + ":onCreateView()");
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_facebook, container, false);
    }

}
