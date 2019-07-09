package com.example.attendancetracker.UI;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attendancetracker.R;

import java.util.Objects;


public class UserSettings extends PreferenceFragmentCompat {

    private MainMenuListeners mainMenuListeners;






    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.user_preference,rootKey);

    }


//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//       View view =  inflater.
//               inflate(R.layout.fragment_user_settings, container, false);
//       mSettingToolbar = view.findViewById(R.id.settingToolbar);
//
//       if (mSettingToolbar != null){
//
//           ((AppCompatActivity) Objects.requireNonNull(getActivity()))
//                   .setSupportActionBar(mSettingToolbar);
//
//           mSettingToolbar.setNavigationIcon(R.drawable.openedmenusvgcoppergold);
//           mSettingToolbar.setTitle(null);
//
//           mSettingToolbar.setNavigationOnClickListener((View v) -> {
//               mainMenuListeners.goToHome();
//           });
//       }
//       return view;
//    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof MainMenuListeners){
            mainMenuListeners = (MainMenuListeners) context;

        }else {
            throw new RuntimeException("must" +
                    " implement MainMenuListeners");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainMenuListeners = null;
    }
}
