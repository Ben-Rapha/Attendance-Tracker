package com.example.attendancetracker.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.R;
import com.example.attendancetracker.Util.MyUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SearchClassAdapter extends RecyclerView.Adapter<SearchClassAdapter.ViewHolder> {

    private Context context;

    private List<AddClassSession> addClassSessionList, filteredList;

    private OnClassnameClickListener onClassnameListener;





    public SearchClassAdapter(Context context, List<AddClassSession> addClassSessionList) {
        this.context = context;
        filteredList = new ArrayList<>();
        this.addClassSessionList = new ArrayList<>();
        this.addClassSessionList.addAll(addClassSessionList);
        this.filteredList.addAll(addClassSessionList);


    }


    @NonNull
    @Override
    public SearchClassAdapter.ViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.search_student_row, parent, false);
        return new SearchClassAdapter.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(
            @NonNull SearchClassAdapter.ViewHolder holder, int position) {

        AddClassSession addClassSession = filteredList.get(holder.getAdapterPosition());
        holder.classname.setText(addClassSession.getClassname());

    }

    public class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        @BindView(R.id.classNameSearchRow)
        TextView classname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClassnameListener.
                    OnClassnameClick(filteredList.get(getAdapterPosition()));
        }
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void UpdatedAdapter(List<AddClassSession> addClassSessionList) {
        filteredList = new ArrayList<>();
        if (this.addClassSessionList.size() == 0){
            MyUtil.loopDataIntoNewArray(addClassSessionList,this.addClassSessionList);
        }
        MyUtil.loopDataIntoNewArray(addClassSessionList,filteredList);
        notifyDataSetChanged();
    }

   public interface OnClassnameClickListener {
        void OnClassnameClick(AddClassSession addClassSession);
    }

    public void setOnClassnameListener(
            OnClassnameClickListener onClassnameListener) {
        this.onClassnameListener = onClassnameListener;
    }

   public void filter(String userInput){
        userInput = userInput.toLowerCase(Locale.getDefault());
        filteredList.clear();

        if (userInput.length() == 0) {
            filteredList.addAll(addClassSessionList);
        }
        else{
            for (AddClassSession addClassSession : addClassSessionList){
                String classname = addClassSession.getClassname().toLowerCase(Locale.getDefault());
                if (classname.contains(userInput)){
                    filteredList.add(addClassSession);
                }
            }
        }

        notifyDataSetChanged();

   }

}
