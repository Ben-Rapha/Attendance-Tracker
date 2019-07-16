package com.example.attendancetracker.UI;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Model.History;
import com.example.attendancetracker.R;
import com.example.attendancetracker.RecyclerViewItemTouchHelper;
import com.example.attendancetracker.Repository.HistoryRepository.HistoryViewModel;
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


public class HistoryFragment extends Fragment {

    @BindView(R.id.historyRecyclerView)
    RecyclerView historyRecyclerView;

    private LinearLayoutManager mLinearLayoutManager, mSearchLinearManger;

    private HistoryAdapter mHistoryAdapter;

    @BindView(R.id.historyToolbar)
    Toolbar mHistoryToolbar;

    private MainMenuListeners mainMenuListeners;

    private List<History> historyList;

    private HistoryViewModel historyViewModel;

    private History history, history1;

    @BindView(R.id.filterListHistoryLayout)
    ConstraintLayout constraintLayout;

    @BindView(R.id.searchHistoryClassRecyclerView)
    RecyclerView filterHistoryRecyclerView;


    @BindView(R.id.searchHistoryClasses)
    SearchView searchView;

    @BindView(R.id.historyTitleTextView)
    TextView mTitleTextView;

    SearchClassAdapter searchClassAdapter;

    HandleFilterClassSessionDropdown handleFilterClassSessionDropdown;


    List<AddClassSession> emptyList, addClassSessionList;


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

        emptyList = new ArrayList<>();
        addClassSessionList = new ArrayList<>();

        mHistoryAdapter = new HistoryAdapter(getContext(), historyList);

        mLinearLayoutManager = new LinearLayoutManager(getContext());

        mSearchLinearManger = new LinearLayoutManager(getContext());

        historyRecyclerView.setLayoutManager(mLinearLayoutManager);

        filterHistoryRecyclerView.setLayoutManager(mSearchLinearManger);

        EditText editText = (EditText) searchView.
                findViewById(androidx.appcompat.R.id.search_src_text);

        editText.setTextColor(getResources().getColor(R.color.colorAccent));

        editText.setHintTextColor(getResources().getColor(R.color.colorAccent));

        editText.setHint("Search class history");


        historyRecyclerView.addItemDecoration
                (new DividerItemDecoration(Objects.requireNonNull(getContext())
                        , DividerItemDecoration.VERTICAL));

        filterHistoryRecyclerView.addItemDecoration
                (new DividerItemDecoration(Objects.requireNonNull(getContext())
                        , DividerItemDecoration.VERTICAL));


        historyRecyclerView.setAdapter(mHistoryAdapter);

        constraintLayout.setTranslationY(getHeight());

        handleFilterClassSessionDropdown = new HandleFilterClassSessionDropdown(getContext(),
                constraintLayout, new AccelerateDecelerateInterpolator(),
                historyRecyclerView);

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Toast.makeText(getContext(), "query closed", Toast.LENGTH_SHORT).show();
                handleFilterClassSessionDropdown.performAnimation();
                mTitleTextView.setText("HISTORY CLASSES");
                searchClassAdapter.UpdatedAdapter(emptyList);
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                historyRecyclerView.setVisibility(View.GONE);
                handleFilterClassSessionDropdown.performAnimation();
                searchClassAdapter = new SearchClassAdapter(getContext(),
                        addClassSessionList);
                filterHistoryRecyclerView.setAdapter(searchClassAdapter);
                mTitleTextView.setText(null);

                searchClassAdapter.setOnClassnameListener(addClassSession -> {

                    for (History history : historyList) {
                        if (history.getClassSession().getClassname().
                                equals(addClassSession.getClassname())) {
                        history1 = new History(history.getClassSession(),
                                history.getTimeTaken(), history.getDateTaken());}
                    }

                    historyViewModel.setHistoryData(history1);
                    searchView.onActionViewCollapsed();
                    Objects.requireNonNull(getActivity()).
                            getWindow().setSoftInputMode
                            (WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    findNavController(Objects.requireNonNull(getActivity()),
                            R.id.mainActivityNavigationHost)
                            .navigate(R.id.historyDetailFragment);
                    editText.clearFocus();
                });
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
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



        if (mHistoryToolbar != null) {
            ((AppCompatActivity) Objects.requireNonNull(getActivity())).
                    setSupportActionBar(mHistoryToolbar);
            mHistoryToolbar.setTitle(null);

            mHistoryToolbar.setNavigationOnClickListener((View v) -> {
                final NavController navController = Navigation.findNavController(view);
                navController.popBackStack();
            });
        }


        mHistoryAdapter.setOnHistoryDataClickListener(
                new HistoryAdapter.OnHistoryDataClickListener() {
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

                        int position = viewHolder.getAdapterPosition();

                        if (direction == ItemTouchHelper.LEFT) {
                            history = historyList.get(viewHolder.getAdapterPosition());
                            mHistoryAdapter.removeItemFromList(viewHolder.getAdapterPosition());

                            DeleteRegisteredSessionDialog deleteRegisteredSessionDialog =
                                    new DeleteRegisteredSessionDialog();

                            deleteRegisteredSessionDialog.setTitleAndMessage("Delete History",
                                    "Are you sure you want to delete this class History?");

                            deleteRegisteredSessionDialog.setOnDeleteSessionListener(
                                    new DeleteRegisteredSessionDialog.OnDeleteSessionListener() {
                                @Override
                                public void deleteSession() {
                                    historyViewModel.deleteHistory(
                                            history.getClassSession());
                                }

                                @Override
                                public void restoreSession() {
                                    mHistoryAdapter.insertItemInList(position, history);
                                }
                            });
                            deleteRegisteredSessionDialog.show(Objects.requireNonNull(
                                    getFragmentManager()), "deleteHistoryDialog");

                        }


                    }

                };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(recyclerViewItemTouchHelper);
        itemTouchHelper.attachToRecyclerView(historyRecyclerView);
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

                    for (History history : historyList) {
                        addClassSessionList.add(history.getClassSession());
                    }

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
