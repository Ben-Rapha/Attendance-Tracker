package com.example.attendancetracker.UI;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.SavedStateVMFactory;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.attendancetracker.Adapters.HistoryAdapter;
import com.example.attendancetracker.Model.History;
import com.example.attendancetracker.R;
import com.example.attendancetracker.RecyclerViewItemTouchHelper;
import com.example.attendancetracker.Repository.HistoryRepository.HistoryViewModel;
import com.example.attendancetracker.Repository.SessionClassRepository.SessionViewModel;
import com.example.attendancetracker.UI.Dialogs.DeleteHistoryDialog;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static androidx.navigation.Navigation.findNavController;


public class HistoryFragment extends Fragment {

    @BindView(R.id.historyRecyclerView)
    RecyclerView historyRecyclerView;

    private LinearLayoutManager mLinearLayoutManager;

    private HistoryAdapter mHistoryAdapter;

    @BindView(R.id.historyToolbar)
    Toolbar mHistoryToolbar;

    private MainMenuListeners mainMenuListeners;

    private List<History> historyList;

    private HistoryViewModel historyViewModel;

    private History history;


    public HistoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);

        historyList = new ArrayList<>();

        mHistoryAdapter = new HistoryAdapter(getContext(), historyList);

        mLinearLayoutManager = new LinearLayoutManager(getContext());

        historyRecyclerView.setLayoutManager(mLinearLayoutManager);

        historyRecyclerView.addItemDecoration
                (new DividerItemDecoration(Objects.requireNonNull(getContext())
                        , DividerItemDecoration.VERTICAL));

        historyRecyclerView.setAdapter(mHistoryAdapter);


        if (mHistoryToolbar != null) {
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).
                    setSupportActionBar(mHistoryToolbar);
            mHistoryToolbar.setTitle(null);

            mHistoryToolbar.setNavigationOnClickListener((View v) -> {
                final NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            });
        }


        mHistoryAdapter.setOnHistoryDataClickListener(new HistoryAdapter.OnHistoryDataClickListener() {
            @Override
            public void sendHistoryData(History history) {
                historyViewModel.setHistoryData(history);
                findNavController(Objects.requireNonNull(getActivity()),
                        R.id.mainActivityNavigationHost)
                        .navigate(R.id.historyDetailFragment);
            }

            @Override
            public void onImageButtonClickListener(History history) {

                DeleteHistoryDialog deleteHistoryDialog = new DeleteHistoryDialog();
                deleteHistoryDialog.setHistory(history);
                deleteHistoryDialog.show(Objects.requireNonNull(
                        getFragmentManager()), "deleteHistoryDialog");


            }
        });

        RecyclerViewItemTouchHelper recyclerViewItemTouchHelper =
                new RecyclerViewItemTouchHelper(getContext()) {
                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
                                         int direction) {

                        if (direction == ItemTouchHelper.LEFT) {

                            int position = viewHolder.getAdapterPosition();
                            history = historyList.get(position);
                            mHistoryAdapter.removeItemFromList(position);


                            Snackbar snackbar = Snackbar.make(view, "Are you sure you want to delete this class history?"
                                    , Snackbar.LENGTH_INDEFINITE).setAction("UNDO", v -> {
                                mHistoryAdapter.insertItemInList(position, history);

                            });
                            snackbar.setTextColor(getResources().getColor(R.color.light_cyan));
                            snackbar.setActionTextColor(getResources().getColor(R.color.colorAccent));

                            snackbar.show();

                        }


                    }

                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(recyclerViewItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(historyRecyclerView);
        return view;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MainMenuListeners) {
            mainMenuListeners = (MainMenuListeners) context;
        } else {
            throw new RuntimeException("must implement MainMenuListener");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        historyViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()),
                new SavedStateVMFactory(getActivity())).
                get(HistoryViewModel.class);


        historyViewModel.getHistoryListLiveData().observe(getViewLifecycleOwner(),
                historyList -> {
                    mHistoryAdapter.upDateHistoryAdapterList(historyList);
                    this.historyList = historyList;

                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mainMenuListeners = null;
    }

    @Override
    public void onPause() {
        super.onPause();


    }
}
