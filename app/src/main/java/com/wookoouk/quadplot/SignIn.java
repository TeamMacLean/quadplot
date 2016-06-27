//package com.wookoouk.quadplot;
//
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//public class SignIn extends Fragment {
//    private FirebaseAuth mAuth;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mAuth = FirebaseAuth.getInstance();
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        return inflater.inflate(R.layout.sign_in, container, false);
//    }
//
//    public void doIt() {
//        mAuth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d("AUTH", "signInWithEmail:onComplete:" + task.isSuccessful());
//
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Log.w("TAG", "signInWithEmail", task.getException());
//                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        } else {
//                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                            if (user != null) {
//                                // Name, email address, and profile photo Url
//                                String name = user.getDisplayName();
//                                String email = user.getEmail();
//                                Uri photoUrl = user.getPhotoUrl();
//
//                                // The user's ID, unique to the Firebase project. Do NOT use this value to
//                                // authenticate with your backend server, if you have one. Use
//                                // FirebaseUser.getToken() instead.
//                                String uid = user.getUid();
//                            }
//                        }
//
//                        // ...
//                    }
//                });
//    }
//}