package com.example.attendancetracker.UI;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.SavedStateVMFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.HistoryRepository.HistoryViewModel;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.example.attendancetracker.UI.Dialogs.AddStudentDialog;

import static androidx.navigation.Navigation.findNavController;


public class MainActivity extends AppCompatActivity
        implements MainMenuListeners, RegisterClassesListener,
        AddStudentDialog.AddStudentClickListener {


    SessionViewModel sessionViewModel;

    HistoryViewModel historyViewModel;

    ConstraintLayout constraintLayout;

    LinearLayout menuListContainer;

    NavOptions navOptions, menuNavOptions;

    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_host);

        sessionViewModel = ViewModelProviders.
                of(this, new SavedStateVMFactory(this))
                .get(SessionViewModel.class);

        sessionViewModel.setViewModelObject(sessionViewModel);

        historyViewModel = ViewModelProviders
                .of(this, new SavedStateVMFactory(this))
                .get(HistoryViewModel.class);

        navController = Navigation.
                findNavController(this, R.id.mainActivityNavigationHost);


        navOptions = new NavOptions.Builder().
                setEnterAnim(android.R.anim.slide_in_left)
                .setPopExitAnim(android.R.anim.slide_out_right)
                .build();
    }

    @Override
    public void goToHome() {

        navController.
                navigate(R.id.home2, null, navOptions);
    }

    @Override
    public void goToSetting() {
        navController.
                navigate(R.id.action_home2_to_settingActivity, null, navOptions);
    }

    @Override
    public void goToProfile() {
        navController.
                navigate(R.id.userProfile, null, navOptions);
    }

    @Override
    public void goToHistory() {
        navController.
                navigate(R.id.goTo_historyFragment, null, navOptions);
    }

    @Override
    public void goToClasses() {

        navController.
                navigate(R.id.registeredClasses, null, navOptions);
    }

    @Override
    public void goToAddNewClass() {
//        navController.navigate(R.id.addClassActivity, null, navOptions);
        navController.navigate(R.id.addNewClassSessionFragment,
                null, navOptions);
    }

    @Override
    public void goClassDetails(AddClassSession addClassSession) {
        navController.navigate(R.id.action_registeredClasses_to_detailClassFragment,
                null, navOptions);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void addStudentButtonClicked() {

    }

    private void setNavOptions(@IdRes int destination) {
        navOptions = new NavOptions.Builder().
                setPopUpTo(destination, true).build();
    }
}


