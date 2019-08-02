package com.example.attendancetracker.UI;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attendancetracker.R;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AuthenticatePasswordFragment extends Fragment {


    @BindView(R.id.new_password_textEdit)
    TextInputEditText mPasswordEditText;

    @BindView(R.id.confirm_password_textEdit)
    TextInputEditText mConfirmPasswordEditText;

    @BindView(R.id.new_password_textInputLayout)
    TextInputLayout mPasswordInputLayout;

    @BindView(R.id.confirm_password_textInputLayout)
    TextInputLayout mConfirmPasswordInputLayout;

    private String passwordChosen;

    private boolean confirmPasswordPass;

    private String confirmPassword;



    @BindView(R.id.done_Button)
    MaterialButton mDoneButton;


    private SharedPreferences mPreference;

    public AuthenticatePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPreference = Objects.requireNonNull(getActivity())
                .getSharedPreferences(MyUtil.LOGIN_SHARED_PREF_FILE,
                        Context.MODE_PRIVATE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.authenticate_password_fragment,
                container, false);

        ButterKnife.bind(this, view);

        mPasswordEditText.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                if (s != null) {
                    boolean passwordTest = MyUtil.checkPasswordValidity(s.toString());
                    if (passwordTest) {
                        mPasswordInputLayout.setError("");
                        passwordChosen = s.toString();

                        if (passwordChosen.equals(confirmPassword)){
                            mConfirmPasswordInputLayout.setError(null);
                        }else{
                            mConfirmPasswordInputLayout.setError("Password is not the same");
                        }
                    }
                    else {
                        mPasswordInputLayout.
                                setError(getString(R.string.password_error));

                    }
                }
            }
        });

        mConfirmPasswordEditText.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s != null) {
                    boolean confirmPasswordTest = MyUtil.checkPasswordValidity(s.toString());
                    if (confirmPasswordTest) {
                        mConfirmPasswordInputLayout.setError("");
                        if (!(s.toString().equals(passwordChosen))) {
                            mConfirmPasswordInputLayout.setError("Password is not the same");
                        } else {
                            confirmPasswordPass = true;
                            confirmPassword = s.toString();
                        }
                    } else {
                        mConfirmPasswordInputLayout.
                                setError(getString(R.string.password_error));
                    }
                }

            }
        });


        mDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (confirmPasswordPass && (confirmPassword.equals(passwordChosen)) ) {

                    SharedPreferences.Editor editor = mPreference.edit();
                    editor.putString(MyUtil.LOGIN_PASSWORD_KEY,
                            passwordChosen);
                    editor.apply();

                    final NavController navController = Navigation.findNavController(view);
                    navController.navigate(R.id.main_destination);


                } else{
                    mConfirmPasswordInputLayout.setError("Password is not the same");
                }
            }
        });


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                final NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.main_destination);

            }
        };

        requireActivity().getOnBackPressedDispatcher().
                addCallback(getViewLifecycleOwner(), callback);

        return view;


    }

}
