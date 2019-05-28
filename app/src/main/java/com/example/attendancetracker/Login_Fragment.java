package com.example.attendancetracker;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

public class Login_Fragment extends Fragment {

   private onButtonClickListener onButtonClickListener;


  private MaterialButton mSignInButton;



    public Login_Fragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View view =  inflater.inflate(R.layout.login_fragment,container,false);

       mSignInButton = view.findViewById(R.id.signInButton);

       mSignInButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               onButtonClickListener.goToDestination();
           }
       });

       return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof onButtonClickListener){
            onButtonClickListener = (Login_Fragment.onButtonClickListener) context;
        } else{
            throw new RuntimeException("must implement onButtonClickListener");
        }
    }

    public interface onButtonClickListener{
        void goToDestination();
    }




}
