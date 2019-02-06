package com.example.likaiapply.eatergo;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForumNewRecordFragment extends Fragment {

    EditText TitleEText;
    EditText ContentEText;
    Button BackButton;
    Button AddPhotoButton;
    Button PublishButton;
    String currentContent;
    FragmentManager fragmentManager;

    public ForumNewRecordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ForumNewView= inflater.inflate(R.layout.fragment_forum_new_record, container, false);
        TitleEText=(EditText) ForumNewView.findViewById(R.id.NewRecordTitle);
        ContentEText=(EditText) ForumNewView.findViewById(R.id.NewRecordContent);
        BackButton=(Button) ForumNewView.findViewById(R.id.ForumNewBackButton);
        AddPhotoButton=(Button) ForumNewView.findViewById(R.id.ForumNewAddPhotoButton);
        PublishButton=(Button) ForumNewView.findViewById(R.id.ForumNewPublishButton);
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
                TitleEText.setText("");
                ContentEText.setText("");
            }
        });
        final ForumNewListener listener=(ForumNewListener)getActivity();

        AddPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentContent=ContentEText.getText().toString();
                listener.addPhoto();

            }
        });

        PublishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=listener.getCurrentUser();
                if(user.length()==0){
                    dispMessage("Error",new String[]{"Please Sign In First!"});
                    return;
                }
                listener.publish(TitleEText.getText().toString(),ContentEText.getText().toString());

                Fragment ForumHomeFrg=fragmentManager.findFragmentByTag("forumHome");
                Fragment ForumDeatilFrg=fragmentManager.findFragmentByTag("forumDetail");
                Fragment ForumNewFrg=fragmentManager.findFragmentByTag("forumNew");

                fragmentManager.beginTransaction()
                        .hide(ForumDeatilFrg).hide(ForumNewFrg)
                        .show(ForumHomeFrg)
                        .commit();
                TitleEText.setText("");
                ContentEText.setText("");

            }
        });

        return ForumNewView;
    }

    public void refresh(String picturePath,String path){

        Bitmap bitmap= BitmapFactory.decodeFile(picturePath);

        Editable eb = ContentEText.getEditableText(); //get current cursor location
        int startPosition = ContentEText.getSelectionStart();
        String imageUrlTag="<img src='"+path+".jpg'\\>";
        SpannableString ss = new SpannableString( imageUrlTag);

        BitmapDrawable drawable=new BitmapDrawable(bitmap);
        ss.setSpan(new ImageSpan(drawable,ImageSpan.ALIGN_BASELINE), 0 , ss.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        drawable.setBounds(2 , 0 , drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        eb.insert(startPosition, ss);

    }

    public void dispMessage(String title,String[] s){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setItems(s, null)
                .setNegativeButton("OK", null).show();
    }

    public interface ForumNewListener{
        void addPhoto();
        void publish(String title,String content);
        String getCurrentUser();
    }

}
