package com.cmpundhir.cm.chasingsuccess.utils;

import com.google.firebase.auth.FirebaseAuth;

public class EndPoints {
    public static String clientPath = "users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/clients";
}
