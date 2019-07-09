package com.example.attendancetracker.Adapters;

import android.content.Context;
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
import com.example.attendancetracker.Model.Students;
import com.example.attendancetracker.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.attendancetracker.R.drawable.ic_done;

public class CheckAttendanceAdapter extends
        RecyclerView.Adapter<CheckAttendanceAdapter.ViewHolder> {

    private Context context;

    private List<Students> studentsList;

    private AddClassSession addClassSession;


    private boolean isPresent = false;


    Students student;

    public CheckAttendanceAdapter(Context context, List<Students>
            studentsList) {
        this.context = context;

        this.studentsList = studentsList;
    }


    @NonNull
    @Override
    public CheckAttendanceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.check_attendance_row, parent, false);
        return new CheckAttendanceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckAttendanceAdapter.ViewHolder holder, int position) {

        student = studentsList.get(holder.getAdapterPosition());
        holder.studentName.setText(student.getStudentName());
        isPresent = student.isPresent();

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        @BindView(R.id.studentName)
        TextView studentName;

        @BindView(R.id.presentOrAbsent)
        ImageButton presentOrAbsentImageButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            presentOrAbsentImageButton.setOnClickListener(this);
        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            Students student = studentsList.get(getAdapterPosition());
            int studentIndexPositionInArrayList = getAdapterPosition();
            isPresent = student.isPresent();
            if (!isPresent) {
                presentOrAbsentImageButton.setImageDrawable(itemView.getResources()
                        .getDrawable(ic_done));
                student.setPresent(true);
                studentsList.remove(studentIndexPositionInArrayList);
                studentsList.add(studentIndexPositionInArrayList, student);

            } else {
                presentOrAbsentImageButton.setImageDrawable(itemView.getResources()
                        .getDrawable(R.drawable.ic_close));
                student.setPresent(false);
                studentsList.remove(studentIndexPositionInArrayList);
                studentsList.add(studentIndexPositionInArrayList, student);
            }


        }
    }

    @Override
    public int getItemCount() {
        return studentsList.size();
    }


    public void setStudentsList(List<Students> studentsList) {
        this.studentsList = studentsList;
        notifyDataSetChanged();
    }

    public void setAddClassSession(AddClassSession session) {
        this.addClassSession = session;
    }

    public List<Students> getHistoryList(){
       return studentsList;
    }


}
