package com.cmpundhir.cm.chasingsuccess.ui.gallery;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cmpundhir.cm.chasingsuccess.R;
import com.cmpundhir.cm.chasingsuccess.pojos.clients.Client;
import com.cmpundhir.cm.chasingsuccess.utils.EndPoints;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GalleryFragment extends Fragment {
    private static final String TAG = "GalleryFragment";
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    Context context;
    FirestoreRecyclerAdapter adapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        ButterKnife.bind(this,root);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        getData();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        //getClients();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void getData(){
        Query query = FirebaseFirestore.getInstance().collection(EndPoints.clientPath);
        final FirestoreRecyclerOptions<Client> options = new FirestoreRecyclerOptions.Builder<Client>()
                .setQuery(query, Client.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Client, ClientHolder>(options) {
            @Override
            public void onBindViewHolder(ClientHolder holder, int position, Client model) {
                // Bind the Chat object to the ChatHolder
                // ...
                holder.txt.setText(options.getSnapshots().get(position).getFirmName());
            }

            @Override
            public ClientHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_client, group, false);

                return new ClientHolder(view);
            }
            @Override
            public void onDataChanged() {
                // Called each time there is a new query snapshot. You may want to use this method
                // to hide a loading spinner or check for the "no documents" state and update your UI.
                // ...
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                // Called when there is an error getting a query snapshot. You may want to update
                // your UI to display an error message to the user.
                // ...
            }
        };
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
    public void getClients(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(EndPoints.clientPath).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(DocumentSnapshot d : queryDocumentSnapshots.getDocuments()){
                    Log.d(TAG, "DocumentSnapshot added with ID: " + d.toString());
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


}