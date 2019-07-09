package com.example.attendancetracker.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Model.History;
import com.example.attendancetracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryDetailAdapter extends
        RecyclerView.Adapter<HistoryDetailAdapter.ViewHolder> {

    private Context context;

    private History history;


    public HistoryDetailAdapter(Context context, History history){
        this.context = context;
        this.history = history;
    }


    @NonNull
    @Override
    public HistoryDetailAdapter.ViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.check_attendance_row,parent,false);
        return new HistoryDetailAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull HistoryDetailAdapter.ViewHolder holder, int position) {

        holder.studentName.setText(history.getClassSession().getStudents().
                get(holder.getAdapterPosition()).getStudentName());

        if (history.getClassSession().getStudents().
                get(holder.getAdapterPosition()).isPresent()){
            holder.imageButtonPresentOrAbsent.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.ic_done));
        } else{
            holder.imageButtonPresentOrAbsent.setImageDrawable(context.getResources()
                    .getDrawable(R.drawable.ic_close));
        }




    }

    public class  ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.studentName)
        TextView studentName;

        @BindView(R.id.presentOrAbsent)
        ImageButton imageButtonPresentOrAbsent;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }


    }

    @Override
    public int getItemCount() {
        if (history != null){
           return history.getClassSession().getStudents().size();

        }
        return 0 ;
    }

    public void updateHistoryAdapter(History history){
        this.history = history;
        notifyDataSetChanged();
    }
}
