package com.cmpundhir.cm.chasingsuccess.ui.gallery;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmpundhir.cm.chasingsuccess.R;

public class ClientHolder extends RecyclerView.ViewHolder {
    TextView txt;
    public ClientHolder(@NonNull View itemView) {
        super(itemView);
        txt = itemView.findViewById(R.id.txt);
    }
}
