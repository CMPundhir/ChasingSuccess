package com.cmpundhir.cm.chasingsuccess.ui.gallery;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cmpundhir.cm.chasingsuccess.R;

public class ClientHolder extends RecyclerView.ViewHolder {
    TextView tFirmNAme,tPOC,tCont,tEmail,tAddr;
    ImageView imageView;
    public ClientHolder(@NonNull View itemView) {
        super(itemView);
        tFirmNAme = itemView.findViewById(R.id.tFirmName);
        tPOC = itemView.findViewById(R.id.tPOC);
        tCont = itemView.findViewById(R.id.tCOnt);
        tEmail = itemView.findViewById(R.id.tEMail);
        tAddr = itemView.findViewById(R.id.tAddr);
        imageView = itemView.findViewById(R.id.img);
    }
}
