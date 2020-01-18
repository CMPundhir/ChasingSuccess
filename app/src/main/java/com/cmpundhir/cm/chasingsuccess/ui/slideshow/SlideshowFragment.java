package com.cmpundhir.cm.chasingsuccess.ui.slideshow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.cmpundhir.cm.chasingsuccess.utils.ImageUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SlideshowFragment extends Fragment {

    private static final String TAG = "AddNewCLient";
    private static final int PICK_FROM_GALLARY = 101;
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
    @BindView(R.id.prevImg)
    ImageView img;
    @BindView(R.id.upBtn)
    Button upBtn;
    Bitmap bitmap=null;
    Uri outPutfileUri;
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
            eAddr.setError("Please enter Address.");
            return null;
        }
        if(bitmap==null){
            Toast.makeText(getContext(), "Please select image", Toast.LENGTH_SHORT).show();
            onUploadClicked(null);
            return null;
        }
        Client client = new Client();
        client.setFirmName(firmName);
        client.setPointOfContact(poc);
        client.setContactNumber(mob);
        client.setEmail(email);
        client.setAddress(addr);
        client.setImg(ImageUtils.convert(bitmap));
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

    @OnClick(R.id.upBtn)
    public void onUploadClicked(View view){
//        Intent galleryIntent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        // Start the Intent
//        startActivityForResult(galleryIntent, PICK_FROM_GALLARY);

        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        startActivityForResult(chooserIntent, PICK_FROM_GALLARY);

        //selectImage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case PICK_FROM_GALLARY:
                if (resultCode == Activity.RESULT_OK) {
                    //pick image from gallery
                    Uri selectedImage = data.getData();
                    decodeUri(selectedImage);
                }
                break;
        }
    }

    public void decodeUri(Uri uri) {
        ParcelFileDescriptor parcelFD = null;
        try {
            parcelFD = getContext().getContentResolver().openFileDescriptor(uri, "r");
            FileDescriptor imageSource = parcelFD.getFileDescriptor();

            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeFileDescriptor(imageSource, null, o);

            // the new size we want to scale to
            final int REQUIRED_SIZE = 1024;

            // Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp < REQUIRED_SIZE && height_tmp < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }

            // decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            bitmap = BitmapFactory.decodeFileDescriptor(imageSource, null, o2);

            img.setImageBitmap(bitmap);

        } catch (FileNotFoundException e) {
            // handle errors
        } finally {
            if (parcelFD != null)
                try {
                    parcelFD.close();
                } catch (IOException e) {
                    // ignored
                }
        }
    }
}