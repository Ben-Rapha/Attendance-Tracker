package com.example.attendancetracker.UI;

import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
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

        getWindow().setEnterTransition(new Fade(Fade.IN));

        sessionViewModel = ViewModelProviders.
                of(this, new SavedStateViewModelFactory(this))
                .get(SessionViewModel.class);

        sessionViewModel.setViewModelObject(sessionViewModel);

        historyViewModel = ViewModelProviders
                .of(this, new SavedStateViewModelFactory(this))
                .get(HistoryViewModel.class);

        navController = Navigation.
                findNavController(this, R.id.mainActivityNavigationHost);

       navOptions = new NavOptions.Builder().
                setEnterAnim(R.anim.fadein).
                setPopEnterAnim(R.anim.fadein).build();
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

//        finish();
//        MainActivity.this.overridePendingTransition(0,R.anim.fadeout);
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
    public void goToLoginActivity() {
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(
                MainActivity.this).toBundle());

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


