package com.example.attendancetracker;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

public class UserProfile extends Fragment {

    MainMenuListeners mainMenuListeners;
    Toolbar mProfileToolbar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public UserProfile() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.
                inflate(R.layout.fragment_user_profile, container, false);

        mProfileToolbar = view.findViewById(R.id.profileToolbar);

        if (mProfileToolbar != null){
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).
                    setSupportActionBar(mProfileToolbar);
            mProfileToolbar.setTitle(null);

            mProfileToolbar.setNavigationOnClickListener((View v)->{
                mainMenuListeners.goToHome();
            });
        }

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainMenuListeners){
            mainMenuListeners = (MainMenuListeners) context;
        } else {
            throw new RuntimeException("must implement MainMenuListeners");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainMenuListeners = null;
    }
}
