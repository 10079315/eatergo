package com.example.likaiapply.eatergo;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ForumActivity extends AppCompatActivity implements ForumNewRecordFragment.ForumNewListener,
        ForumHomeFragment.ForumHomeListener,ForumRecordDetailFragment.ForumDetailListener{

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private StorageReference mStorageRef;

    FragmentManager fragmentManager;
    Fragment ForumNewFrg;
    Fragment ForumHomeFrg;
    Fragment ForumDeatilFrg;
    String nickname;
    String theme;
    String picturePath;
    String Currenttitle;
    boolean IsSignIn=false;
    RecordInfo detail;
    RecordInfo recordDetail;
    ArrayList<String> resultTitle=new ArrayList<>();

    String currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager=getSupportFragmentManager();
        if(savedInstanceState==null) {
            ForumDeatilFrg=new ForumRecordDetailFragment();
            ForumHomeFrg=new ForumHomeFragment();
            ForumNewFrg=new ForumNewRecordFragment();

            fragmentManager.popBackStackImmediate();
            fragmentManager.beginTransaction()
                    .add(R.id.forumFrgcontainer,ForumDeatilFrg, "forumDetail")
                    .add(R.id.forumFrgcontainer,ForumHomeFrg, "forumHome")
                    .add(R.id.forumFrgcontainer,ForumNewFrg, "forumNew")
                    .hide(ForumDeatilFrg).hide(ForumNewFrg)
                    .show(ForumHomeFrg)
                    .commit();
        }else{
            ForumHomeFrg=fragmentManager.findFragmentByTag("forumHome");
            ForumDeatilFrg=fragmentManager.findFragmentByTag("forumDetail");
            ForumNewFrg=fragmentManager.findFragmentByTag("forumNew");

            fragmentManager.beginTransaction()
                    .hide(ForumDeatilFrg).hide(ForumNewFrg)
                    .show(ForumHomeFrg)
                    .commit();
        }

        setContentView(R.layout.activity_forum);



        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d("TAG", "onAuthStateChanged:signed_in:" + user.getUid());
                    currentUser=user.getDisplayName();
                    IsSignIn=true;
                    System.out.println("currentUser:  "+currentUser);

                } else {
                    // User is signed out
                    Log.d("TAG", "onAuthStateChanged:signed_out");
                    IsSignIn=false;
                    currentUser="";
                    System.out.println("currentUser:  "+"No User!");
                }
                // ...
            }
        };
        mStorageRef = FirebaseStorage.getInstance().getReference();

        Bundle passData=getIntent().getExtras();
        nickname=passData.getString("nickname","");
        theme=passData.getString("theme","");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==RESULT_OK) {
            try {
                Uri selectedImage = data.getData(); //get the Uri
                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor =getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);  //get photo directory
                cursor.close();

                uploadImage(picturePath);


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadImage(final String picturePath){
        long imageID = System.currentTimeMillis();

        Uri file = Uri.fromFile(new File(picturePath));
        StorageReference riversRef = mStorageRef.child("images/"+imageID);
        UploadTask uploadTask = riversRef.putFile(file);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                @SuppressWarnings("VisibleForTests")Uri downloadUrl = taskSnapshot.getDownloadUrl();
                ForumNewRecordFragment ForumNewFrg=(ForumNewRecordFragment) fragmentManager.findFragmentByTag("forumNew");
                ForumNewFrg.refresh(picturePath,downloadUrl.toString());
            }
        });

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
    public void addPhoto() {
        Intent intent=new Intent();
        intent.setAction(Intent.ACTION_PICK);//Pick an item from the data
        intent.setType("image/*");
        startActivityForResult(intent, 1);

    }

    @Override
    public void publish(String title, String content) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(theme);
        long imageID = System.currentTimeMillis();
        String date=stampToDate(imageID);

        RecordInfo newRecord=new RecordInfo();
        newRecord.Detail=content;
        newRecord.Theme=theme;
        newRecord.Title=title;
        newRecord.UserName=currentUser;
        newRecord.PublishDate=date;
        newRecord.Reply=" ";
        myRef.child(newRecord.Title).setValue(newRecord);
        getRecordTitle();

    }

    @Override
    public String getCurrentUser() {
        return currentUser;
    }

    @Override
    public void getRecordTitle() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(theme);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                resultTitle.clear();

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    RecordInfo record=ds.getValue(RecordInfo.class);
                    resultTitle.add(ds.getKey()+"\n"+"<"+record.UserName+">"+record.PublishDate);
                }

                try{
                    ((ForumHomeFragment)ForumHomeFrg).refreshData(resultTitle);
                }catch (NullPointerException e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG-firedatabase", "Failed to read value.", error.toException());
            }
        });

    }

    @Override
    public void getRecordDetail(final String RecordTitle) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(theme);
        detail=new RecordInfo();
        Currenttitle=RecordTitle;

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if(ds.getKey().equals(RecordTitle)){
                        detail=ds.getValue(RecordInfo.class);

                        ((ForumRecordDetailFragment) ForumDeatilFrg).getRecord(detail);
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

    public String stampToDate(long timeMillis){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    @Override
    public void replyRecord(RecordInfo reply) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(theme);
        myRef.child(reply.Title).setValue(reply);
        getRecordTitle();

    }

    @Override
    public String getReplyUser() {
        return currentUser;
    }
}
