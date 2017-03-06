package de.taracamp.familyplan;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class LoginGoogleFragment extends Fragment {

    private static final String TAG = "LoginGoogleFragment";
    private static final String LOG_INFO = "LOG_INFO";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_INFO,TAG + ":onCreateView()");
        return inflater.inflate(R.layout.fragment_login_google, container, false);
    }

}
