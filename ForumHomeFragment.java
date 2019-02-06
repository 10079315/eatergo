package com.example.likaiapply.eatergo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForumHomeFragment extends Fragment {

    FragmentManager fragmentManager;
    ListView ForumSubList;
    Button BackButton;
    Button NewButton;
    public ForumHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ForumHomeView= inflater.inflate(R.layout.fragment_forum_home, container, false);
        ForumSubList=(ListView) ForumHomeView.findViewById(R.id.ForumSubList);
        BackButton=(Button) ForumHomeView.findViewById(R.id.ForumHomeBackButton);
        NewButton=(Button) ForumHomeView.findViewById(R.id.ForumHomeNewButton);
        fragmentManager=getActivity().getSupportFragmentManager();

        final ForumHomeListener listener=(ForumHomeListener)getActivity();
        listener.getRecordTitle();

        final AdapterView.OnItemClickListener mItemClick=new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemName=parent.getItemAtPosition(position).toString();//get clicked item
                String[] str=new String[2];
                        str=itemName.split("\\n");

                if(str.length>0) listener.getRecordDetail(str[0]);

                Fragment ForumHomeFrg=fragmentManager.findFragmentByTag("forumHome");
                Fragment ForumDeatilFrg=fragmentManager.findFragmentByTag("forumDetail");
                Fragment ForumNewFrg=fragmentManager.findFragmentByTag("forumNew");

                fragmentManager.beginTransaction()
                        .show(ForumDeatilFrg).hide(ForumNewFrg)
                        .hide(ForumHomeFrg)
                        .commit();


            }
        };
        ForumSubList.setOnItemClickListener(mItemClick);



        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        NewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment ForumHomeFrg=fragmentManager.findFragmentByTag("forumHome");
                Fragment ForumDeatilFrg=fragmentManager.findFragmentByTag("forumDetail");
                Fragment ForumNewFrg=fragmentManager.findFragmentByTag("forumNew");

                fragmentManager.beginTransaction()
                        .hide(ForumDeatilFrg).show(ForumNewFrg)
                        .hide(ForumHomeFrg)
                        .commit();
            }
        });

        return ForumHomeView;
    }

    public void refreshData(ArrayList<String> data){
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, data);
        ForumSubList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //refreshView();
    }
    public void refreshView(){
        ForumSubList.invalidateViews();
    }


    public interface ForumHomeListener{
        void getRecordTitle();
        void getRecordDetail(String RecordTitle);
    }

}