package com.example.attendancetracker.UI;


import android.content.Context;
import android.content.DialogInterface;
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
import androidx.lifecycle.SavedStateVMFactory;
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

    SearchClassAdapter searchClassAdapter;

    DeleteRegisteredSessionDialog deleteRegisteredSessionDialog;


    DeleteRegisteredSessionDialog.OnDeleteSessionListener onDeleteSessionListener;


    @BindView(R.id.titleToolbar)
    TextView mTitleTextView;

    private LinearLayoutManager mLinearLayoutManager, searchClassLinearLayoutManager;

    private SessionAdapter mSessionAdapter;

    private List<AddClassSession> addClassSessionList, emptyList;

    private SessionViewModel sessionViewModel;

    AddClassSession mAddClassSession;

    private RegisterClassesListener mRegisterClassesListener;

    HandleFilterClassSessionDropdown handleFilterClassSessionDropdown;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public RegisteredClasses() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

//        sessionViewModel = SessionViewModel.getSessionViewModel();

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.
                inflate(R.layout.fragment_registered_classes, container, false);
        ButterKnife.bind(this, view);

        EditText editText = (EditText) searchRegisteredClasses.
                findViewById(androidx.appcompat.R.id.search_src_text);

        editText.setTextColor(getResources().getColor(R.color.colorAccent));

        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));

        editText.setHint("Search class session");

        addClassSessionList = new ArrayList<>();

        emptyList = new ArrayList<>();

        mLinearLayoutManager = new LinearLayoutManager(getContext());

        searchClassLinearLayoutManager = new LinearLayoutManager(getContext());

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


        filterListParentLayout.setTranslationY(getHeight());


        handleFilterClassSessionDropdown = new HandleFilterClassSessionDropdown(getContext(),
                filterListParentLayout, new AccelerateDecelerateInterpolator(),
                mRecyclerViewClassesList);


        if (registerClassesToolbar != null) {
            ((AppCompatActivity) (Objects.requireNonNull(getActivity()))).
                    setSupportActionBar(registerClassesToolbar);
            registerClassesToolbar.setTitle("");

            registerClassesToolbar.setNavigationOnClickListener((View v) -> {

                final NavController navController = Navigation.findNavController(view);
                navController.popBackStack();

            });
        }

        searchRegisteredClasses.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(getContext(), "query closed", Toast.LENGTH_SHORT).show();
                handleFilterClassSessionDropdown.performAnimation();
                mTitleTextView.setText("REGISTERED CLASSES");
                searchClassAdapter.UpdatedAdapter(emptyList);

                return false;
            }
        });

        searchRegisteredClasses.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerViewClassesList.setVisibility(View.GONE);
                handleFilterClassSessionDropdown.performAnimation();
                searchClassAdapter = new SearchClassAdapter(getContext(),
                        addClassSessionList);
                mSearchClassRecyclerView.setAdapter(searchClassAdapter);
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
            }
        });

        searchRegisteredClasses.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getContext(), "query submit", Toast.LENGTH_SHORT).show();
                editText.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getContext(), "query on text change", Toast.LENGTH_SHORT).show();

                if (!newText.isEmpty()) {
                    searchClassAdapter.filter(newText);
                }

                return false;
            }
        });


        RecyclerViewItemTouchHelper recyclerViewItemTouchHelper =
                new RecyclerViewItemTouchHelper(getContext()) {
                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                         int direction) {


                        int position = viewHolder.getAdapterPosition();

                        if (direction == ItemTouchHelper.LEFT) {
                            mAddClassSession = addClassSessionList.get(viewHolder.getAdapterPosition());
                            mSessionAdapter.removeItemFromList(viewHolder.getAdapterPosition());

                            DeleteRegisteredSessionDialog deleteRegisteredSessionDialog =
                                    new DeleteRegisteredSessionDialog();

                            deleteRegisteredSessionDialog.setTitleAndMessage("Delete Session",
                                    "Are you sure you want to delete this class Session?");

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

                                        }
                                    });

                            deleteRegisteredSessionDialog.show(Objects.requireNonNull(
                                    getFragmentManager()), "deleteSessionDialog");
                        }
                    }

                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(recyclerViewItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(mRecyclerViewClassesList);

        return view;
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sessionViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()),
                new SavedStateVMFactory(getActivity())).
                get(SessionViewModel.class);
        sessionViewModel.getAllClassSessionFromSessionRepository().
                observe(this, addClassSessionList -> {
                    mSessionAdapter.updateSessionAdapter(addClassSessionList);
                    this.addClassSessionList = addClassSessionList;
                });

        mSessionAdapter.setClassSessionDataListener(addClassSession -> {
            sessionViewModel.setAddClassSessionData(addClassSession);
            mRegisterClassesListener.goClassDetails(addClassSession);
        });

    }

    @Override
    public void onResume() {
        super.onResume();

    }


    //TODO : when you delete a registered class and that class
    // is up next it does reflect on home screen immediately
}
