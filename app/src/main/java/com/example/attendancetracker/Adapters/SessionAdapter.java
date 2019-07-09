package com.example.attendancetracker.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SessionAdapter extends
        RecyclerView.Adapter<SessionAdapter.ViewHolder> {
   private Context context;
   private List<AddClassSession> addClassSessionList;
   private Drawable background,colorTransparent;

   private ClassSessionDataListener mClassSessionDataListener;


   public interface ClassSessionDataListener{

       void getClassSession(AddClassSession addClassSession);
   }



    public SessionAdapter(Context context, List<AddClassSession> addClassSessionList){
        this.context = context;
        this.addClassSessionList = addClassSessionList;


    }

    @NonNull
    @Override
    public SessionAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.session_row,viewGroup,false);
        return new SessionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull SessionAdapter.ViewHolder holder, int position) {

        final AddClassSession addClassSession = addClassSessionList
                .get(holder.getAdapterPosition());
        String mSpaceTime = addClassSession.getStartTimeString()+" - "+addClassSession.getEndTimeString() ;
        holder.mClassname.setText(addClassSession.getClassname());
        holder.mLocation.setText(addClassSession.getLocation());
        holder.mTime.setText(mSpaceTime);

//        Log.v("timeReach", addClassSession.getClassname()+" : " +addClassSession.getStartTimeString() +"-"+addClassSession.getEndTimeString());

        holder.sun.setBackground(addClassSession.getSunday()!=null?background:colorTransparent);
        holder.mon.setBackground(addClassSession.getMonday()!=null?background:colorTransparent);
        holder.tue.setBackground(addClassSession.getTuesday()!=null?background:colorTransparent);
        holder.wed.setBackground(addClassSession.getWednesday()!=null?background:colorTransparent);
        holder.thur.setBackground(addClassSession.getThursday()!=null?background:colorTransparent);
        holder.fri.setBackground(addClassSession.getFriday()!=null?background:colorTransparent);
        holder.sat.setBackground(addClassSession.getSaturday()!=null?background:colorTransparent);



    }

    @Override
    public int getItemCount() {
        return addClassSessionList.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.className)
        TextView mClassname;
        @BindView(R.id.classRoomName)
        TextView mLocation;
        @BindView(R.id.classDuration)
        TextView mTime;
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




        public ViewHolder(@NonNull View dataView)  {
            super(dataView);
            ButterKnife.bind(this,dataView);
            background = dataView.getResources().getDrawable(R.drawable.ic_my_circle);
            colorTransparent = dataView.getResources().getDrawable(R.drawable.ic_mycircle_white);
            dataView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           mClassSessionDataListener.
                   getClassSession(addClassSessionList.get(getAdapterPosition()));

        }
    }

    public void updateSessionAdapter(List<AddClassSession> addClassSessionList){
        this.addClassSessionList = addClassSessionList;
        notifyDataSetChanged();
    }

    public List<AddClassSession> getAddClassSessionList() {
        return addClassSessionList;
    }


    public void setClassSessionDataListener(ClassSessionDataListener classSessionDataListener){
       mClassSessionDataListener = classSessionDataListener;
    }
}
