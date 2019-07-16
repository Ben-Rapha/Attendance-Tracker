package com.example.attendancetracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public abstract class RecyclerViewItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    final ColorDrawable background = new ColorDrawable(Color.RED);

    private Context context;

    private Drawable deleteIcon;

    public RecyclerViewItemTouchHelper(int dragDirs, int swipeDirs, Context context) {
        super(dragDirs, swipeDirs);

        this.context = context;

    }

    public RecyclerViewItemTouchHelper(Context context) {
        super(0, ItemTouchHelper.LEFT);
        this.context = context;
        deleteIcon = context.getDrawable(R.drawable.ic_delete);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView,
                          @NonNull RecyclerView.ViewHolder viewHolder,
                          @NonNull RecyclerView.ViewHolder target) {
        return false;
    }


    @Override
    public void onChildDraw(@NonNull Canvas c,
                            @NonNull RecyclerView recyclerView,
                            @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState,
                            boolean isCurrentlyActive) {

        super.onChildDraw(c, recyclerView, viewHolder,
                dX, dY, actionState, isCurrentlyActive);

        background.setBounds((int) (viewHolder.itemView.getRight() + dX), viewHolder.itemView.getTop(),
                (int) (viewHolder.itemView.getRight()), viewHolder.itemView.getBottom());

        background.draw(c);

        int left = viewHolder.itemView.getRight() - deleteIcon.getIntrinsicWidth() * 3;

        int right = viewHolder.itemView.getRight() - deleteIcon.getIntrinsicWidth();

        int top = (viewHolder.itemView.getTop() + viewHolder.itemView.getBottom()) / 2 - deleteIcon.getIntrinsicHeight();

        int bottom = (viewHolder.itemView.getTop() + viewHolder.itemView.getBottom()) / 2 + deleteIcon.getIntrinsicHeight();

        deleteIcon.setBounds(left, top, right, bottom);

        deleteIcon.draw(c);

    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;

    }
}
