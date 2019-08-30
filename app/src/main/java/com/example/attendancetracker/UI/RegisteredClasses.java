package com.example.attendancetracker.UI;


import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.attendancetracker.Adapters.HistoryAdapter;
import com.example.attendancetracker.Adapters.SearchClassAdapter;
import com.example.attendancetracker.Adapters.SessionAdapter;
import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.R;
import com.example.attendancetracker.RecyclerViewItemTouchHelper;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.example.attendancetracker.UI.Dialogs.DeleteHistoryDialog;
import com.example.attendancetracker.UI.Dialogs.DeleteRegisteredSessionDialog;
import com.example.attendancetracker.Util.LoggerUtils;
import com.example.attendancetracker.Util.MyUtil;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.navigation.Navigation.findNavController;


public class RegisteredClasses extends Fragment {

    private Toolbar registerClassesToolbar;


    private MainMenuListeners mMainMenuListeners;

    @BindView(R.id.registeredClassesRecyclerView)
    RecyclerView mRecyclerViewClassesList;

    @BindView(R.id.searchRegisteredClasses)
    SearchView searchRegisteredClasses;

    @BindView(R.id.searchRegisteredClassRecyclerView)
    RecyclerView mSearchClassRecyclerView;

    @BindView(R.id.filterListLayout)
    ConstraintLayout filterListParentLayout;

    private SearchClassAdapter searchClassAdapter;

    DeleteRegisteredSessionDialog deleteRegisteredSessionDialog;


    DeleteRegisteredSessionDialog.OnDeleteSessionListener onDeleteSessionListener;


    @BindView(R.id.titleToolbar)
    TextView mTitleTextView;

    View view;

    private SessionAdapter mSessionAdapter;

    private List<AddClassSession> addClassSessionList, emptyList,searchArrayList;

    private SessionViewModel sessionViewModel;

    private AddClassSession mAddClassSession;

    private RegisterClassesListener mRegisterClassesListener;

    private HandleFilterClassSessionDropdown handleFilterClassSessionDropdown;

    private boolean isCancelledCalled = false;


    public RegisteredClasses() { }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addClassSessionList = new ArrayList<>();

        emptyList = new ArrayList<>();

        searchArrayList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.
                inflate(R.layout.fragment_registered_classes, container, false);
        ButterKnife.bind(this, view);

        EditText editText = (EditText) searchRegisteredClasses.
                findViewById(androidx.appcompat.R.id.search_src_text);

        editText.setTextColor(getResources().getColor(R.color.colorAccent));

        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));

        editText.setHint(getString(R.string.search_sessions));


        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());

        LinearLayoutManager searchClassLinearLayoutManager = new LinearLayoutManager(getContext());

        mRecyclerViewClassesList.setLayoutManager(mLinearLayoutManager);

        mSearchClassRecyclerView.setLayoutManager(searchClassLinearLayoutManager);

        mSearchClassRecyclerView.addItemDecoration
                (new DividerItemDecoration(Objects.requireNonNull(getContext())
                        , DividerItemDecoration.VERTICAL));

        mRecyclerViewClassesList.addItemDecoration
                (new DividerItemDecoration(Objects.requireNonNull(getContext())
                        , DividerItemDecoration.VERTICAL));

        mSessionAdapter = new SessionAdapter(getContext(), addClassSessionList);


        registerClassesToolbar = view.findViewById(R.id.registerClassesToolbar);

        mRecyclerViewClassesList.setAdapter(mSessionAdapter);
        searchClassAdapter = new SearchClassAdapter(getContext(),
                addClassSessionList);

        mSearchClassRecyclerView.setAdapter(searchClassAdapter);


        filterListParentLayout.setTranslationY(getHeight());


        handleFilterClassSessionDropdown = new HandleFilterClassSessionDropdown(getContext(),
                filterListParentLayout, new AccelerateDecelerateInterpolator(),
                mRecyclerViewClassesList);

                setupOnCloseDropDownListener();

        setUpOnSearchClickListener(editText);

        setUpSearchRegisteredClassesListener(editText);

        setUpRecyclerViewTouchHelper();



        return view;
    }



    private void setupOnCloseDropDownListener() {
        searchRegisteredClasses.setOnCloseListener(() -> {
            handleFilterClassSessionDropdown.performAnimation();
            mTitleTextView.setText(getString(R.string.registered_class));
            searchClassAdapter.UpdatedAdapter(emptyList);
            return false;
        });
    }

    private void setUpOnSearchClickListener(EditText editText) {
        searchRegisteredClasses.setOnSearchClickListener(v -> {
            mRecyclerViewClassesList.setVisibility(View.GONE);
            searchClassAdapter.UpdatedAdapter(addClassSessionList);
            handleFilterClassSessionDropdown.performAnimation();
            mTitleTextView.setText(null);
            searchClassAdapter.setOnClassnameListener(addClassSession -> {
                sessionViewModel.setAddClassSessionData(addClassSession);
                searchRegisteredClasses.onActionViewCollapsed();
                Objects.requireNonNull(getActivity()).
                        getWindow().setSoftInputMode
                        (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                mRegisterClassesListener.goClassDetails(addClassSession);
                editText.clearFocus();
            });
        });
    }

    private void setUpSearchRegisteredClassesListener(EditText editText) {
        searchRegisteredClasses.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                editText.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!newText.isEmpty()) {
                    searchClassAdapter.filter(newText);
                } else{
                        searchClassAdapter.UpdatedAdapter(searchArrayList);
                }
                return false;
            }
        });
    }


    /**
     * deletes, cancel or dismiss a session in the recycler view adapter
     */
    private void setUpRecyclerViewTouchHelper() {
        RecyclerViewItemTouchHelper recyclerViewItemTouchHelper =
                new RecyclerViewItemTouchHelper(Objects.requireNonNull(getContext())) {
                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                         int direction) {
                        int position = viewHolder.getAdapterPosition();

                        if (direction == ItemTouchHelper.LEFT) {
                            mAddClassSession = addClassSessionList.get(viewHolder.getAdapterPosition());
                            mSessionAdapter.removeItemFromList(viewHolder.getAdapterPosition());

                            DeleteRegisteredSessionDialog deleteRegisteredSessionDialog =
                                    new DeleteRegisteredSessionDialog();

                            deleteRegisteredSessionDialog.setCancelable(false);

                            deleteRegisteredSessionDialog.setTitleAndMessage(getString(
                                    R.string.delete_session_title),
                                    getString(R.string.delete_sesssion_verification));

                            deleteRegisteredSessionDialog.setClassSession(mAddClassSession);

                            deleteRegisteredSessionDialog.setOnDeleteSessionListener(
                                    new DeleteRegisteredSessionDialog.OnDeleteSessionListener() {
                                        @Override
                                        public void deleteSession() {
                                            sessionViewModel.deleteClassSessionFromDatabase(
                                                    mAddClassSession.getClassname());
                                        }

                                        @Override
                                        public void restoreSession() {
                                            mSessionAdapter.insertItemInList(position, mAddClassSession);
                                            isCancelledCalled = true;
                                        }

                                        @Override
                                        public void onDialogDismiss() {
                                            if (!isCancelledCalled) {
                                                mSessionAdapter.insertItemInList(position,
                                                        mAddClassSession);
                                            } else {
                                                isCancelledCalled = false;
                                            }
                                        }
                                    });

                            deleteRegisteredSessionDialog.show(Objects.requireNonNull(
                                    getFragmentManager()), "deleteSessionDialog");


                        }
                    }
                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(recyclerViewItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(mRecyclerViewClassesList);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).
                get(SessionViewModel.class);

        sessionViewModel.getAllClassSessionFromSessionRepository().
                observe(this, addClassSessionList -> {
                    mSessionAdapter.updateSessionAdapter(addClassSessionList);
                    this.addClassSessionList = addClassSessionList;
                    MyUtil.loopDataIntoNewArray(addClassSessionList,searchArrayList);
                });

        mSessionAdapter.setClassSessionDataListener(new SessionAdapter.ClassSessionDataListener() {
            @Override
            public void getClassSession(AddClassSession addClassSession) {
                sessionViewModel.setAddClassSessionData(addClassSession);
                mRegisterClassesListener.goClassDetails(addClassSession);
            }

            @Override
            public void editClassSession(AddClassSession addClassSession) {
                final NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.
                        action_registeredClasses_to_editClassSession2);
                sessionViewModel.setAddClassSessionData(addClassSession);
            }
        });

    }


    @Override
    public void onResume() {
        super.onResume();
        if (registerClassesToolbar != null) {
            ((AppCompatActivity) (Objects.requireNonNull(getActivity()))).
                    setSupportActionBar(registerClassesToolbar);
            registerClassesToolbar.setTitle(null);

            registerClassesToolbar.setNavigationOnClickListener((View v) -> {
                final NavController navController = Navigation.findNavController(v);
                navController.popBackStack();

            });
        }
    }

    private int getHeight() {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainMenuListeners) {
            mMainMenuListeners = (MainMenuListeners) context;
            mRegisterClassesListener = (RegisterClassesListener) context;
        } else {
            throw new RuntimeException("must implement MaimMenuListeners");
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        mMainMenuListeners = null;
    }
}
