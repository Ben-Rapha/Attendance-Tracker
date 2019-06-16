package com.example.attendancetracker;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;


public class HomeFragment extends Fragment {

    ConstraintLayout constraintLayout;
    LinearLayout menuListContainer;

    private TextView mHomeTextView,mClassesTextView,
            mProfileTextView,mHistoryTextVIew,
            mSettingTextView;

    FloatingActionButton mAddNewClassFloatingActionButton;

    private  MainMenuListeners mMainMenuListeners;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        mHomeTextView = view.findViewById(R.id.todayTextView);
        Toolbar toolbar = view.findViewById(R.id.mainActivityToolbar);
        constraintLayout = view.findViewById( R.id.backDrop_foreground);
        menuListContainer = view.findViewById(R.id.menuListLinearLayout);
        mClassesTextView = view.findViewById(R.id.classesTextView);
        mHistoryTextVIew = view.findViewById(R.id.historyTextView);
        mSettingTextView = view.findViewById(R.id.settingTextView);
        mProfileTextView = view.findViewById(R.id.profileTextView);
        mAddNewClassFloatingActionButton = view.findViewById(R.id.addNewClassFab);


        if (toolbar != null){
            toolbar.setTitle("");
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).
                    setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new HandleMenuDropDownListener(
                    getContext(), constraintLayout,
                    Objects.requireNonNull(getContext()).getDrawable(R.drawable.ic_openedmenusvg),
                    getContext().getDrawable(R.drawable.ic_closed_menu),
                    new AccelerateDecelerateInterpolator(),
                    new OvershootInterpolator(), menuListContainer,mAddNewClassFloatingActionButton));
        }

//        mHomeTextView.setOnClickListener((View v) -> {
//                mMainMenuListeners.goToHome();
//        });

        mClassesTextView.setOnClickListener((View v) -> {
                mMainMenuListeners.goToClasses();
        });

        mHistoryTextVIew.setOnClickListener((View v)->{
            mMainMenuListeners.goToHistory();
        });

        mSettingTextView.setOnClickListener((View v)->{
            mMainMenuListeners.goToSetting();
        });

        mProfileTextView.setOnClickListener((View v)->{
                mMainMenuListeners.goToProfile();
        });

        mAddNewClassFloatingActionButton.setOnClickListener((View v) ->{
            mMainMenuListeners.goToAddNewClass();
        });
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainMenuListeners ) {
            mMainMenuListeners = (MainMenuListeners) context;
        }
    }


}
