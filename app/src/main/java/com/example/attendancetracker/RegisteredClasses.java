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


public class RegisteredClasses extends Fragment {

    Toolbar registerClassesToolbar;

    MainMenuListeners mMainMenuListeners;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public RegisteredClasses() {
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
       View view  = inflater.
               inflate(R.layout.fragment_registered_classes, container, false);

       registerClassesToolbar = view.findViewById(R.id.registerClassesToolbar);

       if (registerClassesToolbar != null){
           ((AppCompatActivity) (Objects.requireNonNull(getActivity()))).
                   setSupportActionBar(registerClassesToolbar);
           registerClassesToolbar.setTitle("");

           registerClassesToolbar.setNavigationOnClickListener((View v) ->{
               mMainMenuListeners.goToHome();
           });
       }

       return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if( context instanceof MainMenuListeners){
            mMainMenuListeners = (MainMenuListeners) context;
        } else{
            throw new RuntimeException("must implement MaimMenuListeners");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMainMenuListeners = null;
    }
}
