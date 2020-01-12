package com.cmpundhir.cm.chasingsuccess.ui.slideshow;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ComputableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.cmpundhir.cm.chasingsuccess.R;
import com.cmpundhir.cm.chasingsuccess.pojos.clients.Client;
import com.cmpundhir.cm.chasingsuccess.utils.EndPoints;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SlideshowFragment extends Fragment {

    private static final String TAG = "AddNewCLient";
    @BindView(R.id.eFirmName)
    TextInputEditText eFirmName;
    @BindView(R.id.ePOC)
    TextInputEditText ePOC;
    @BindView(R.id.eContNum)
    TextInputEditText eContNum;
    @BindView(R.id.eEmail)
    TextInputEditText eEmail;
    @BindView(R.id.eAddr)
    TextInputEditText eAddr;
    @BindView(R.id.proceedBtn)
    Button proceedBtn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    FirebaseFirestore db;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        ButterKnife.bind(this,root);
        db = FirebaseFirestore.getInstance();
        return root;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    public Client readInputs(){
        String firmName,poc,mob,email,addr;
        firmName = eFirmName.getText().toString();
        poc = ePOC.getText().toString();
        mob = eContNum.getText().toString();
        email = eEmail.getText().toString();
        addr = eAddr.getText().toString();
        if(TextUtils.isEmpty(firmName)){
            eFirmName.setError("Please enter Firm Name.");
            return null;
        }
        if(TextUtils.isEmpty(poc)){
            ePOC.setError("Please enter poc.");
            return null;
        }
        if(TextUtils.isEmpty(mob)){
            eContNum.setError("Please enter contact number.");
            return null;
        }
        if(TextUtils.isEmpty(email)){
            eEmail.setError("Please enter email.");
            return null;
        }
        if(TextUtils.isEmpty(addr)){
            eFirmName.setError("Please enter Address.");
            return null;
        }
        Client client = new Client();
        client.setFirmName(firmName);
        client.setPointOfContact(poc);
        client.setContactNumber(mob);
        client.setEmail(email);
        client.setAddress(addr);
        return client;
    }


    @OnClick(R.id.proceedBtn)
    public void addNewClient(View view){
        Client client = readInputs();
        if(client==null)return;
        progressBar.setVisibility(View.VISIBLE);
        proceedBtn.setEnabled(false);
        db.collection(EndPoints.clientPath).add(client).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG,"success");
                progressBar.setVisibility(View.GONE);
                proceedBtn.setEnabled(true);
                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG,"failure : "+e.getMessage());
                progressBar.setVisibility(View.GONE);
                proceedBtn.setEnabled(true);
            }
        });
    }
}