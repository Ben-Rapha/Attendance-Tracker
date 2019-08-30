package com.example.attendancetracker.UI;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancetracker.Adapters.StudentsAdapter;
import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Model.Students;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.example.attendancetracker.UI.Dialogs.AddStudentDialog;
import com.example.attendancetracker.UI.Dialogs.EditStudentDialog;
import com.example.attendancetracker.UI.Dialogs.SendStudentEmailDialog;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailClassFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {

    private MainMenuListeners mMainMenuListeners;

    StudentsAdapter.EditStudentListener editStudentListener;

    StudentsAdapter.SendStudentEmailListener sendStudentEmailListener;


    private SessionViewModel sessionViewModel;

    @BindView(R.id.sessionToolbar )
    Toolbar toolbar;

    @BindView(R.id.sessionDetailToolbarLayout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @BindView(R.id.className)
     TextView mClassname;

    @BindView(R.id.location)
    TextView mLocation;

    @BindView(R.id.time)
     TextView mTime;

    @BindView(R.id.date)
    TextView mDate;

    @BindView(R.id.sun)
    TextView sun;
    @BindView(R.id.mon)
     TextView mon;
    @BindView(R.id.tue)
     TextView tue;
    @BindView(R.id.wed)
     TextView wed;
    @BindView(R.id.thu)
    TextView thur;
    @BindView(R.id.fri)
     TextView fri;
    @BindView(R.id.sat)
    TextView sat;



    private Drawable background,colorTransparent;


    private AddClassSession addClassSession;


    private View view;



    @BindView(R.id.addNewStudent)
    FloatingActionButton mFab;


    private StudentsAdapter studentsAdapter;

    @BindView(R.id.studentRecyclerView)
    RecyclerView studentRecyclerView;


    public DetailClassFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        background = getResources().getDrawable(R.drawable.ic_my_circle);
        colorTransparent = getResources().getDrawable(R.drawable.ic_mycircle_white);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.fragment_detail_class, container, false);
        ButterKnife.bind(this,view);

        List<Students> studentsList = new ArrayList<>();

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        studentRecyclerView.setLayoutManager(mLinearLayoutManager);
        studentRecyclerView.setHasFixedSize(true);

        studentRecyclerView.addItemDecoration
                (new DividerItemDecoration(Objects.requireNonNull(getContext())
                        ,DividerItemDecoration.VERTICAL));

        studentsAdapter = new StudentsAdapter(getContext(), studentsList);

        studentRecyclerView.setAdapter(studentsAdapter);

        setRecyclerVIewOnScrollListener();

        if (toolbar != null){
            ((AppCompatActivity) (Objects.requireNonNull(getActivity()))).
                    setSupportActionBar(toolbar);
            toolbar.setTitle(null);
            toolbar.setTitleTextColor(getResources().getColor(R.color.copperGold));

            toolbar.setNavigationOnClickListener((View v) ->{
                final NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            });

            collapsingToolbarLayout.setTitle("Session Detail");
            collapsingToolbarLayout.setCollapsedTitleGravity(Gravity.NO_GRAVITY);
            collapsingToolbarLayout.setExpandedTitleColor(
                    getResources().getColor(R.color.light_cyan));
            collapsingToolbarLayout.setCollapsedTitleTextColor(
                    getResources().getColor(R.color.copperGold));
            collapsingToolbarLayout.setExpandedTitleMarginBottom(32);
            collapsingToolbarLayout.setExpandedTitleMarginTop(0);
            collapsingToolbarLayout.setExpandedTitleMarginStart(32);

        }

        setEditListener();
        setSendStudentEmailListener();
        return view;
    }

    private void setRecyclerVIewOnScrollListener() {
        studentRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when RecyclerView's scroll state changes.
             *
             * @param recyclerView The RecyclerView whose scroll state has changed.
             * @param newState     The updated scroll state. One of {@link #SCROLL_STATE_IDLE},
             *                     {@link #SCROLL_STATE_DRAGGING} or {@link #SCROLL_STATE_SETTLING}.
             */

            int SCROLL_STATE_DRAGGING,SCROLL_STATE_IDLE,SCROLL_STATE_SETTLING;
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    mFab.show();
                } else{
                    mFab.hide();
                }

            }

            /**
             * Callback method to be invoked when the RecyclerView has been scrolled. This will be
             * called after the scroll has completed.
             * <p>
             * This callback will also be called if visible item range changes after a layout
             * calculation. In that case, dx and dy will be 0.
             *
             * @param recyclerView The RecyclerView which scrolled.
             * @param dx           The amount of horizontal scroll.
             * @param dy           The amount of vertical scroll.
             */
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 || dy >0){
                    mFab.hide();
                }
            }
        });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sessionViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).
                get(SessionViewModel.class);
       sessionViewModel.getAddClassSessionData().observe(getViewLifecycleOwner(), this::setData);

        mFab.setOnClickListener((View v) ->{
            showPopUpMenu();
        });

    }

    private void setData(AddClassSession addClassSession) {
        if (addClassSession!=null){
            String mSpaceTime = addClassSession.getStartTimeString()+" - "+addClassSession.getEndTimeString() ;
            String mSpaceDate = addClassSession.getFormattedDateStart()+" - "+addClassSession.getFormattedDateEnd();
            mClassname.setText(addClassSession.getClassname());
            mLocation.setText(addClassSession.getLocation());
            mTime.setText(mSpaceTime);
            mDate.setText(mSpaceDate);
            sun.setBackground(addClassSession.getSunday()!=null?background:colorTransparent);
            mon.setBackground(addClassSession.getMonday()!=null?background:colorTransparent);
            tue.setBackground(addClassSession.getTuesday()!=null?background:colorTransparent);
            wed.setBackground(addClassSession.getWednesday()!=null?background:colorTransparent);
            thur.setBackground(addClassSession.getThursday()!=null?background:colorTransparent);
            fri.setBackground(addClassSession.getFriday()!=null?background:colorTransparent);
            sat.setBackground(addClassSession.getSaturday()!=null?background:colorTransparent);
            this.addClassSession = addClassSession;
            setStudentsAdapter(addClassSession);
            setAddClassSession(addClassSession);
        }
    }

    private void setStudentsAdapter(AddClassSession addClassSession){
        studentsAdapter.updateStudentList(addClassSession.getStudents());

    }

    private void setAddClassSession(AddClassSession session){
        studentsAdapter.setAddClassSession(session);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainMenuListeners){
            mMainMenuListeners = (MainMenuListeners) context;
        }
    }


    private void setEditListener(){
        studentsAdapter.setEditStudentListener(new StudentsAdapter.EditStudentListener() {
            @Override
            public void EditStudent(AddClassSession sessionClass, Students student, int studentIndexInArray) {
                EditStudentDialog editStudentDialog = new EditStudentDialog();
                editStudentDialog.setCancelable(false);
                    editStudentDialog.setStudent(sessionClass,student,studentIndexInArray);
                    editStudentDialog.show(Objects.requireNonNull(getFragmentManager()),
                            "editStudentDialog");
            }
        });
    }

    private void setSendStudentEmailListener(){
        studentsAdapter.setSendStudentEmailListener(student -> {
            SendStudentEmailDialog sendStudentEmailDialog = new SendStudentEmailDialog();
            sendStudentEmailDialog.setCancelable(false);
            sendStudentEmailDialog.setStudent(student);
            sendStudentEmailDialog.show(Objects.requireNonNull(getFragmentManager()),
                    "studentEmailDialog");
        });
    }

    private void showPopUpMenu(){
        PopupMenu popupMenu = new PopupMenu(
                Objects.requireNonNull(getActivity()),mFab);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.actions);
        popupMenu.show();


    }

    /**
     * This method will be invoked when a menu item is clicked if the item
     * itself did not already handle the event.
     *
     * @param item the menu item that was clicked
     * @return {@code true} if the event was handled, {@code false}
     * otherwise
     */
    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.addStudent:
                addStudentDialog();
                return true;

            case R.id.checkAttendance:
                if (addClassSession.getStudents()!= null){
                    if (addClassSession.getStudents().size() > 0){
                        findNavController(Objects.
                                requireNonNull(getActivity()),R.id.mainActivityNavigationHost)
                                .navigate(R.id.action_detailClassFragment_to_checkAttendance);
                    } else{
                        Snackbar.make(view,getString(R.string.at_least_one_student),
                                Snackbar.LENGTH_LONG).show();
                        return false;
                    }
                }

                return true;
                default:
                    return false;
        }
    }

    private void addStudentDialog(){
        AddStudentDialog addStudentDialog = new AddStudentDialog();
        addStudentDialog.setCancelable(false);
        addStudentDialog.setAddClassSession(addClassSession);
        addStudentDialog.show(Objects.requireNonNull(getFragmentManager()),
                "addStudentDialog");
    }
}
