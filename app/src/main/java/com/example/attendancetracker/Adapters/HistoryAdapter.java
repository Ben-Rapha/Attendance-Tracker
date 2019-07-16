package com.example.attendancetracker.Adapters;

import android.content.Context;
import android.media.MediaRouter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancetracker.Model.History;
import com.example.attendancetracker.Model.Students;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Util.MyUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {


    private Context context;
    private List<History> historyList;

    private OnHistoryDataClickListener onHistoryDataClickListener;


    public HistoryAdapter(Context context, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public HistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_row, parent, false);
        return new HistoryAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.ViewHolder holder,
                                 int position) {

        History history = historyList.get(holder.getAdapterPosition());

        holder.className.setText(history.getClassSession().getClassname());

        holder.classRoomName.setText(history.getClassSession().getLocation());

        String mSpaceTime = history.getClassSession().getStartTimeString() + " - "
                + history.getClassSession().getEndTimeString();

        holder.classDuration.setText(mSpaceTime);

        holder.timeTaken.setText(MyUtil.getFormattedTime(history.getTimeTaken()));

        holder.dateTaken.setText(MyUtil.getFormattedDate(history.getDateTaken()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        @BindView(R.id.className)
        TextView className;

        @BindView(R.id.classRoomName)
        TextView classRoomName;

        @BindView(R.id.classDuration)
        TextView classDuration;

        @BindView(R.id.timeTakenTime)
        TextView timeTaken;

        @BindView(R.id.dateTakenDate)
        TextView dateTaken;

        @BindView(R.id.deleteHistory)
        ImageButton imageButtonDeleteHistory;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            imageButtonDeleteHistory.setOnClickListener(this::onImageButtonClick);
        }


        @Override
        public void onClick(View v) {
            onHistoryDataClickListener.
                    sendHistoryData(historyList.get(getAdapterPosition()));

        }

       private void onImageButtonClick(View v){
            onHistoryDataClickListener.
                    sendHistoryData(historyList.get(getAdapterPosition()));
        }
    }


    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void upDateHistoryAdapterList(List<History> historyList) {
        this.historyList = historyList;
        notifyDataSetChanged();
    }

    public interface OnHistoryDataClickListener{

        void sendHistoryData(History history);

        void onImageButtonClickListener(History history);
    }

    public void setOnHistoryDataClickListener (OnHistoryDataClickListener onHistoryDataClickListener){

        this.onHistoryDataClickListener = onHistoryDataClickListener;
    }

    public void removeItemFromList(int position){

        historyList.remove(position);

        notifyItemRemoved(position);

    }

    public void insertItemInList(int position,History history){
        historyList.add(position,history);
        notifyItemInserted(position);
    }
}
