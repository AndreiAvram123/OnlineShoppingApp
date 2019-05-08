package com.example.onlineshoopingapp.view;


import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * SPECIAL DIVIDER
 * Used in order to set a custom distance between items
 */
public class CustomDivider extends RecyclerView.ItemDecoration {
    private int height;

    public CustomDivider(int height) {
        this.height = height;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = height;
    }
}