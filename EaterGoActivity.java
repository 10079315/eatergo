package com.example.likaiapply.eatergo;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.nsd.NsdManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.likaiapply.eatergo.ProfileFragment.ProfileListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class EaterGoActivity extends AppCompatActivity implements SignInFragment.SignInListener,
        RegisterFragment.RegisterListener,ProfileFragment.ProfileListener{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    FragmentManager fragmentManager;
    Fragment HomeFrg;
    Fragment ForumFrg;
    Fragment SignInFrg;
    Fragment RegisterFrg;
    Fragment ProfileFrg;
    Fragment CalorieFrg;

    boolean IssignIn=false;
    boolean IsRegisterFailed = false;
    String currentUser;
    UserInfo currentUserInfo=new UserInfo();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            fragmentManager=getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if(IssignIn) {

                        getCurrentUserInfo(currentUser);
                        fragmentManager.beginTransaction()
                                .hide(fragmentManager.findFragmentByTag("home"))
                                .hide(fragmentManager.findFragmentByTag("forum"))
                                .hide(fragmentManager.findFragmentByTag("signin"))
                                .hide(fragmentManager.findFragmentByTag("register"))
                                .show(fragmentManager.findFragmentByTag("profile"))
                                .hide(fragmentManager.findFragmentByTag("calorie"))
                                .commit();
                    }else{

                        fragmentManager.beginTransaction()
                                .show(fragmentManager.findFragmentByTag("home"))
                                .hide(fragmentManager.findFragmentByTag("forum"))
                                .hide(fragmentManager.findFragmentByTag("signin"))
                                .hide(fragmentManager.findFragmentByTag("register"))
                                .hide(fragmentManager.findFragmentByTag("profile"))
                                .hide(fragmentManager.findFragmentByTag("calorie"))
                                .commit();
                    }
                    return true;
                case R.id.navigation_forum:
                    fragmentManager.beginTransaction()
                            .show(fragmentManager.findFragmentByTag("forum"))
                            .hide(fragmentManager.findFragmentByTag("home"))
                            .hide(fragmentManager.findFragmentByTag("signin"))
                            .hide(fragmentManager.findFragmentByTag("register"))
                            .hide(fragmentManager.findFragmentByTag("profile"))
                            .hide(fragmentManager.findFragmentByTag("calorie"))
                            .commit();

                    return true;
                case R.id.navigation_calorie:
                    fragmentManager.beginTransaction()
                            .hide(fragmentManager.findFragmentByTag("forum"))
                            .hide(fragmentManager.findFragmentByTag("home"))
                            .hide(fragmentManager.findFragmentByTag("signin"))
                            .hide(fragmentManager.findFragmentByTag("register"))
                            .hide(fragmentManager.findFragmentByTag("profile"))
                            .show(fragmentManager.findFragmentByTag("calorie"))
                            .commit();
                    Toast.makeText(EaterGoActivity.this, "seach food calorie!",
                            Toast.LENGTH_LONG).show();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnectivityManager connMgr=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connMgr.getActiveNetworkInfo();
        if(networkInfo != null&& networkInfo.isConnected()){
            Log.i("rew","Success Connect to Internet");

        }else{
            Log.i("rew","Failure Connect to Internet");

        }
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                    currentUser=user.getDisplayName();
                    IssignIn=true;

                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                    IssignIn=false;
                }
                // ...
            }
        };

        fragmentManager=getSupportFragmentManager();
        if(savedInstanceState==null) {
            HomeFrg=new HomeFragment();
            ForumFrg=new ForumFragment();
            SignInFrg=new SignInFragment();
            RegisterFrg=new RegisterFragment();
            ProfileFrg=new ProfileFragment();
            CalorieFrg=new searchFood();

            fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction()
                    .add(R.id.fgcontainer,HomeFrg, "home")
                    .add(R.id.fgcontainer,ForumFrg, "forum")
                    .add(R.id.fgcontainer,SignInFrg,"signin")
                    .add(R.id.fgcontainer,RegisterFrg,"register")
                    .add(R.id.fgcontainer,ProfileFrg,"profile")
                    .add(R.id.fgcontainer,CalorieFrg,"calorie")
                    .hide(ForumFrg).hide(SignInFrg).hide(RegisterFrg).hide(ProfileFrg).hide(CalorieFrg)
                    .show(HomeFrg)
                    .commit();
        }else{
            HomeFrg=fragmentManager.findFragmentByTag("home");
            ForumFrg=fragmentManager.findFragmentByTag("forum");
            SignInFrg=fragmentManager.findFragmentByTag("signin");
            RegisterFrg=fragmentManager.findFragmentByTag("register");
            ProfileFrg=fragmentManager.findFragmentByTag("profile");
            CalorieFrg=fragmentManager.findFragmentByTag("calorie");

            fragmentManager.beginTransaction()
                    .hide(ForumFrg).hide(SignInFrg).hide(RegisterFrg).hide(ProfileFrg).hide(CalorieFrg)
                    .show(HomeFrg)
                    .commit();
        }

        setContentView(R.layout.activity_eater_go);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void registerUser(final UserInfo newUser) {

        if(checkNickName(newUser.NickName)){

            return;
        }

        final String email=newUser.Email;
        final String password=newUser.Password;
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("TAG-SignUp", "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(EaterGoActivity.this, "Auth Failed",
                                    Toast.LENGTH_SHORT).show();
                            dispMessage("Register",new String[]{"Register Failed"});
                        } else {

                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(newUser.NickName).build();
                            user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User profile updated" + newUser.NickName);
                                    }
                                }
                            });

                            dispMessage("Register",new String[]{"Register Successfully"});
                            initialUserInfo(newUser);
                            signInUser(email,password);
                        }

                    }
                });



    }


    @Override
    public void signInUser(final String email,final String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("TAG-SignIn", "signInWithEmail:failed", task.getException());
                            Toast.makeText(EaterGoActivity.this, "Auth Failed",
                                    Toast.LENGTH_SHORT).show();
                            dispMessage("Sign In",new String[]{"Sign In Failed"});
                            IssignIn=false;
                        }else{
                            Log.d("TAG-SignIn", "signInWithEmail:onComplete:" + task.isSuccessful());
                            dispMessage("Sign In",new String[]{"Sign In Successfully"});
                            fragmentManager.beginTransaction()
                                    .hide(fragmentManager.findFragmentByTag("signin"))
                                    .hide(fragmentManager.findFragmentByTag("forum"))
                                    .hide(fragmentManager.findFragmentByTag("home"))
                                    .hide(fragmentManager.findFragmentByTag("register"))
                                    .hide(fragmentManager.findFragmentByTag("calorie"))
                                    .show(fragmentManager.findFragmentByTag("profile"))
                                    .commit();

                            getCurrentUserInfo(currentUser);
                            IssignIn=true;
                        }
                    }
                });

    }

    @Override
    public void signOff(){
        dispMessage("Sign Off",new String[]{"Sign Off"});
        IssignIn=false;
        mAuth.signOut();
    }

    public void initialUserInfo(UserInfo newUser) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");

        long timeStamp = System.currentTimeMillis();
        String time = stampToDate(timeStamp);
        newUser.RegistDate=time;
        myRef.child(newUser.NickName).setValue(newUser); //add user information to database
    }

    public boolean checkNickName(final String nickname){

        IsRegisterFailed=false;
        //check if nick name already exist
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    String person= ds.getKey();
                    if(nickname.equals(person)){
                        IsRegisterFailed =true;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG-firedatabase", "Failed to read value.", error.toException());
            }
        });
        if(IsRegisterFailed) dispMessage("Register",new String[]{"Nick Name already exist!"});

        return IsRegisterFailed;
    }

    public void getCurrentUserInfo(final String nickname){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if(nickname.equals(ds.getKey())){
                        currentUserInfo=ds.getValue(UserInfo.class);
                        ProfileFragment PFrg=(ProfileFragment) fragmentManager.findFragmentByTag("profile");
                        PFrg.getUserData(currentUserInfo);

                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG-firedatabase", "Failed to read value.", error.toException());
            }
        });

    }




    public void dispMessage(String title,String[] s){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setItems(s, null)
                .setNegativeButton("OK", null).show();
    }


    public String stampToDate(long timeMillis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        signOff();
    }

}
