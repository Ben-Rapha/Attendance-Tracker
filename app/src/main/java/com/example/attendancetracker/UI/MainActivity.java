package com.example.attendancetracker.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.SavedStateVMFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavOptions;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.HistoryRepository.HistoryViewModel;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.example.attendancetracker.UI.Dialogs.AddStudentDialog;

import static androidx.navigation.Navigation.findNavController;


public class MainActivity extends AppCompatActivity
        implements MainMenuListeners,RegisterClassesListener,
        AddStudentDialog.AddStudentClickListener {


    SessionViewModel sessionViewModel;

    HistoryViewModel historyViewModel;

    ConstraintLayout constraintLayout;
    LinearLayout menuListContainer;
    NavOptions navOptions,menuNavOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_host);

        sessionViewModel = ViewModelProviders.of(this,new SavedStateVMFactory(this))
                .get(SessionViewModel.class);

        historyViewModel = ViewModelProviders.of(this,new SavedStateVMFactory(this))
                .get(HistoryViewModel.class);

//        sessionViewModel.setViewModelObject(sessionViewModel);
        navOptions = new NavOptions.Builder()
                .setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.fade_in)
                .setPopExitAnim(R.anim.fade_out)
                .build();

        menuNavOptions = new NavOptions.Builder().
                setEnterAnim(R.anim.main_menu_slide_up)
                .setExitAnim(R.anim.main_menu_slide_down)
                .setPopEnterAnim(R.anim.main_menu_slide_up)
                .setPopExitAnim(R.anim.main_menu_slide_down)
                .build();
    }

    @Override
    public void goToHome() {
       findNavController(this,R.id.mainActivityNavigationHost)
               .navigate(R.id.home2,null,navOptions);
    }

    @Override
    public void goToSetting() {

        findNavController(this,R.id.mainActivityNavigationHost)
                .navigate(R.id.action_home2_to_settingActivity,null,navOptions);
    }

    @Override
    public void goToProfile() {
        findNavController(this,R.id.mainActivityNavigationHost)
                .navigate(R.id.userProfile,null,navOptions);
    }

    @Override
    public void goToHistory() {
        findNavController(this,R.id.mainActivityNavigationHost)
                .navigate(R.id.goTo_historyFragment,null,navOptions);
    }

    @Override
    public void goToClasses() {
        findNavController(this,R.id.mainActivityNavigationHost)
                .navigate(R.id.registeredClasses,null,navOptions);
    }

    @Override
    public void goToAddNewClass() {
//        findNavController(this,R.id.mainActivityNavigationHost)
//                .navigate(R.id.addNewClass,null,navOptions);

        findNavController(this,R.id.mainActivityNavigationHost).
                navigate(R.id.addClassActivity);

    }

    @Override
    public void goClassDetails(AddClassSession addClassSession) {
        findNavController(this,R.id.mainActivityNavigationHost).
                navigate(R.id.action_registeredClasses_to_detailClassFragment);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void addStudentButtonClicked() {




    }
}


