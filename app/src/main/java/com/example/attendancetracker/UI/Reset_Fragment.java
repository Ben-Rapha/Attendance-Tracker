package com.example.attendancetracker.UI;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attendancetracker.R;
import com.google.android.material.button.MaterialButton;

public class Reset_Fragment extends Fragment {

    private resetListener mResetListener;

    MaterialButton mResetPassword;

    public Reset_Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.reset_fragment, container, false);
        mResetPassword = view.findViewById(R.id.reset_passcode_Button);

        mResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mResetListener.goToAuthenticatePassword();
            }
        });
        return view;
    }


    public interface resetListener {
        void goToAuthenticatePassword();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof resetListener) {
            mResetListener = (resetListener) context;
        } else {
            throw new RuntimeException("must implement resetListener");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mResetListener = null;
    }
}
