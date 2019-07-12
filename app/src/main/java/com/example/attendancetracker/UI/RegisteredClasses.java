package com.example.attendancetracker.UI;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateVMFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.attendancetracker.Adapters.HistoryAdapter;
import com.example.attendancetracker.Adapters.SessionAdapter;
import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import static androidx.navigation.Navigation.findNavController;


public class RegisteredClasses extends Fragment {

    private Toolbar registerClassesToolbar;

    MainMenuListeners mMainMenuListeners;

    @BindView(R.id.registeredClassesRecyclerView)
    RecyclerView mRecyclerViewClassesList;

    @BindView(R.id.searchRegisteredClasses)
    SearchView searchRegisteredClasses;

    @BindView(R.id.titleToolbar)
    TextView mTitleTextView;

    LinearLayoutManager mLinearLayoutManager;

    SessionAdapter mSessionAdapter;

    List<AddClassSession> addClassSessionList;

    SessionViewModel sessionViewModel;

    AddClassSession mAddClassSession;

    RegisterClassesListener mRegisterClassesListener;






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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view  = inflater.
               inflate(R.layout.fragment_registered_classes, container, false);
       ButterKnife.bind(this,view);

       addClassSessionList = new ArrayList<>();

       mLinearLayoutManager = new LinearLayoutManager(getContext());

       mRecyclerViewClassesList.setLayoutManager(mLinearLayoutManager);

       mRecyclerViewClassesList.addItemDecoration
               (new DividerItemDecoration(Objects.requireNonNull(getContext())
                       ,DividerItemDecoration.VERTICAL));

       mSessionAdapter = new SessionAdapter(getContext(),addClassSessionList);



       registerClassesToolbar = view.findViewById(R.id.registerClassesToolbar);

       mRecyclerViewClassesList.setAdapter(mSessionAdapter);

       if (registerClassesToolbar != null){
           ((AppCompatActivity) (Objects.requireNonNull(getActivity()))).
                   setSupportActionBar(registerClassesToolbar);
           registerClassesToolbar.setTitle("");

           registerClassesToolbar.setNavigationOnClickListener((View v) ->{

               final NavController navController = Navigation.findNavController(view);
               navController.popBackStack();

           });
       }

       searchRegisteredClasses.setOnFocusChangeListener((v, hasFocus) -> {

           if (hasFocus){
               mTitleTextView.setText("");
           } else{

               mTitleTextView.setText
                       (getString(R.string.registered_classes));
           }
       });

       return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if( context instanceof MainMenuListeners){
            mMainMenuListeners = (MainMenuListeners) context;
            mRegisterClassesListener = (RegisterClassesListener) context;
        } else{
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

        sessionViewModel =ViewModelProviders.of(Objects.requireNonNull(getActivity()),
                new SavedStateVMFactory(getActivity())).
                get(SessionViewModel.class);
        sessionViewModel.getAllClassSessionFromSessionRepository().
                observe(this, addClassSessionList -> {
                    mSessionAdapter.updateSessionAdapter(addClassSessionList);
                });

        mSessionAdapter.setClassSessionDataListener(addClassSession -> {
            sessionViewModel.setAddClassSessionData(addClassSession);
            mRegisterClassesListener.goClassDetails(addClassSession);
        });

    }
}
