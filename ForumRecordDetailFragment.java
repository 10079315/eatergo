package com.example.likaiapply.eatergo;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForumRecordDetailFragment extends Fragment {


    Button BackButton;
    Button ReplyButton;
    HtmlTextView RecordText;
    EditText ReplyEText;
    FragmentManager fragmentManager;
    RecordInfo currentRecord;
    public ForumRecordDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ForumDetailView=inflater.inflate(R.layout.fragment_forum_record_detail, container, false);
        BackButton=(Button) ForumDetailView.findViewById(R.id.ForumDeatilBackButton);
        ReplyButton=(Button) ForumDetailView.findViewById(R.id.ForumDetailReplyButton);
        RecordText=(HtmlTextView) ForumDetailView.findViewById(R.id.ForumDetailText);
        ReplyEText=(EditText) ForumDetailView.findViewById(R.id.ForumDetailReplyEText);
        fragmentManager=getActivity().getSupportFragmentManager();

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment ForumHomeFrg=fragmentManager.findFragmentByTag("forumHome");
                Fragment ForumDeatilFrg=fragmentManager.findFragmentByTag("forumDetail");
                Fragment ForumNewFrg=fragmentManager.findFragmentByTag("forumNew");
                ((ForumHomeFragment)ForumHomeFrg).refreshView();

                fragmentManager.beginTransaction()
                        .hide(ForumDeatilFrg).hide(ForumNewFrg)
                        .show(ForumHomeFrg)
                        .commit();
            }
        });

        ReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reply=ReplyEText.getText().toString();
                ForumDetailListener listener=(ForumDetailListener)getActivity();
                String user=listener.getReplyUser();
                if(user.length()==0){
                    dispMessage("Error",new String[]{"Please Sign In First!"});
                    return;
                }
                long timeMillis = System.currentTimeMillis();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(timeMillis);
                 String timeStamp=simpleDateFormat.format(date);

                String current=currentRecord.Reply;
                currentRecord.Reply=current+"<br>"+"-----------------"+"<br>"+user+":     "+timeStamp+"<br>"+"Reply: "+reply+"<br>";
                listener.replyRecord(currentRecord);

            }
        });

        return ForumDetailView;
    }
    public void getRecord(RecordInfo record){
        currentRecord=new RecordInfo();
        currentRecord=record;

        RecordText.setHtmlFromString(record.Detail+record.Reply,false);
        ReplyEText.setText(" ");
    }


    public void dispMessage(String title,String[] s){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setItems(s, null)
                .setNegativeButton("OK", null).show();
    }
    interface ForumDetailListener{
        void replyRecord(RecordInfo record);
        String getReplyUser();

    }

}
