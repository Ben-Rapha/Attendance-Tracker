package com.example.attendancetracker.UI;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateVMFactory;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.HandleMenuDropDownListener;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment {

    @BindView(R.id.backDrop_foreground)
    public ConstraintLayout constraintLayout;

    @BindView(R.id.menuListLinearLayout)
    public LinearLayout menuListContainer;

    @BindView(R.id.todayTextView)
    public TextView mTodayTextView;

    @BindView(R.id.classesTextView)
    public TextView mClassesTextView;

    @BindView(R.id.profileTextView)
    public TextView mProfileTextView;

    @BindView(R.id.historyTextView)
    public TextView mHistoryTextVIew;

    @BindView(R.id.settingTextView)
    public TextView mSettingTextView;

    @BindView(R.id.addNewClassFab)
    public FloatingActionButton mAddNewClassFloatingActionButton;

    private MainMenuListeners mMainMenuListeners;

    @BindView(R.id.mainActivityToolbar)
    Toolbar toolbar;


    HandleMenuDropDownListener.BackdropListener backdropListener;

    public HomeFragment() {
        // Required empty public constructor
    }




    private LiveData<List<AddClassSession>> addClassSessionLiveData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this,view);

        mTodayTextView.setVisibility(View.GONE);
        mClassesTextView.setVisibility(View.GONE);
        mHistoryTextVIew.setVisibility(View.GONE);
        mSettingTextView.setVisibility(View.GONE);
        mProfileTextView.setVisibility(View.GONE);

        if (toolbar != null) {
            toolbar.setTitle("");
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).
                    setSupportActionBar(toolbar);

            toolbar.setNavigationOnClickListener(new HandleMenuDropDownListener(
                    getContext(), constraintLayout,
                    Objects.requireNonNull(getContext()).getDrawable(R.drawable.ic_openedmenusvg),
                    getContext().getDrawable(R.drawable.ic_closed_menu),
                    new AccelerateDecelerateInterpolator(),
                    new OvershootInterpolator(), menuListContainer, mAddNewClassFloatingActionButton));
        }

        mClassesTextView.setOnClickListener((View v) -> {
            enableActiveButton(mClassesTextView);
            mMainMenuListeners.goToClasses();
        });

        mHistoryTextVIew.setOnClickListener((View v) -> {
            enableActiveButton(mHistoryTextVIew);
            mMainMenuListeners.goToHistory();
        });

        mSettingTextView.setOnClickListener((View v) -> {
            enableActiveButton(mSettingTextView);
            mMainMenuListeners.goToSetting();
        });

        mProfileTextView.setOnClickListener((View v) -> {
            enableActiveButton(mProfileTextView);
            mMainMenuListeners.goToProfile();
        });

        mAddNewClassFloatingActionButton.setOnClickListener((View v) -> {
            mMainMenuListeners.goToAddNewClass();
        });

        HandleMenuDropDownListener.setBackdropListener(
                new HandleMenuDropDownListener.BackdropListener() {
            @Override
            public void backDropDown(boolean isDown) {
                if (isDown){

                    mTodayTextView.setVisibility(View.VISIBLE);
                    mClassesTextView.setVisibility(View.VISIBLE);
                    mHistoryTextVIew.setVisibility(View.VISIBLE);
                    mSettingTextView.setVisibility(View.VISIBLE);
                    mProfileTextView.setVisibility(View.VISIBLE);

                } else{

                    mTodayTextView.setVisibility(View.GONE);
                    mClassesTextView.setVisibility(View.GONE);
                    mHistoryTextVIew.setVisibility(View.GONE);
                    mSettingTextView.setVisibility(View.GONE);
                    mProfileTextView.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainMenuListeners) {
            mMainMenuListeners = (MainMenuListeners) context;
        }
    }

    private void enableActiveButton(TextView backgroundChange ){
            backgroundChange.setBackground(getResources()
                .getDrawable(R.drawable.menu_text_background_shape));
            mTodayTextView.setBackground(null);

    }


}
