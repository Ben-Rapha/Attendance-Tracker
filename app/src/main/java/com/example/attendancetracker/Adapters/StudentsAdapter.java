package com.example.attendancetracker.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancetracker.AddClassSession;
import com.example.attendancetracker.Model.Students;
import com.example.attendancetracker.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StudentsAdapter extends RecyclerView.Adapter<StudentsAdapter.ViewHolder> {

        private Context context;
        private List<Students> students;

        private  AddClassSession addClassSession;

        private SendStudentEmailListener sendStudentEmailListener;

        private EditStudentListener editStudentListener;


        public StudentsAdapter(Context context,List<Students> students){
            this.context = context;
            this.students = students;
        }

    @NonNull
    @Override
    public StudentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.student_row,viewGroup,false);
        return new StudentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentsAdapter.ViewHolder holder, int position) {
       final Students student = students.get(holder.getAdapterPosition());
       holder.studentNameTextView.setText(student.getStudentName());
       holder.studentEmailTextView.setText(student.getStudentEmail());




    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.studentName)
        TextView studentNameTextView;

        @BindView(R.id.studentEmail)
        TextView studentEmailTextView;

        @BindView(R.id.sendEmail)
        ImageView sendEmailImageView;

        @BindView(R.id.more)
        ImageButton moreButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            sendEmailImageView.setOnClickListener(this);
            moreButton.setOnClickListener(this::moreButtonClicked);


        }

        /**
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {

            sendStudentEmailListener.sendStudentEmail(students.get(getAdapterPosition()));
        }

        private void moreButtonClicked(View v){
//            editStudentListener.EditStudent(students.get(getAdapterPosition()));
            if (addClassSession != null){
                editStudentListener.EditStudent(addClassSession,
                        students.get(getAdapterPosition()),
                        getAdapterPosition());
            }

        }
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return students.size();
    }

    public void updateStudentList(List<Students> studentsList){
        this.students = studentsList;
        notifyDataSetChanged();

    }

    public void setAddClassSession(AddClassSession session){
        this.addClassSession = session;
        notifyDataSetChanged();
    }

    public void setSendStudentEmailListener(SendStudentEmailListener listener){
        this.sendStudentEmailListener = listener;

    }

    public void setEditStudentListener(EditStudentListener listener){
        this.editStudentListener = listener;
    }

    public interface SendStudentEmailListener{

        void sendStudentEmail(Students students);
    }

   public  interface EditStudentListener{
        void EditStudent(AddClassSession sessionClass, Students student,int studentIndexInArray);
    }
}
