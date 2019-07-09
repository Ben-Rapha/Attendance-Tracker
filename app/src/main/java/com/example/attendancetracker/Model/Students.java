package com.example.attendancetracker.Model;

public class Students {

    private String mStudentName,mStudentEmail;

    private boolean isPresent;

    public Students(String mStudentName , String mStudentEmail,boolean isPresent){
        this.mStudentName = mStudentName;
        this.mStudentEmail = mStudentEmail;
        this.isPresent = isPresent;
    }


    public String getStudentName() {
        return mStudentName;
    }

    public void setStudentName(String mStudentName) {
        this.mStudentName = mStudentName;
    }

    public String getStudentEmail() {
        return mStudentEmail;
    }

    public void setStudentEmail(String mStudentEmail) {
        this.mStudentEmail = mStudentEmail;
    }

    public boolean isPresent() {
        return isPresent;
    }

    public void setPresent(boolean present) {
        isPresent = present;
    }
}
