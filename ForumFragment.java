package com.example.likaiapply.eatergo;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForumFragment extends Fragment {

    String nickname;

    public ForumFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View ForumView= inflater.inflate(R.layout.fragment_forum, container, false);
        ListView forumList=(ListView) ForumView.findViewById(R.id.ForumList);

        String[] themeCatgory=new String[]{"Restaurant Review","Healthy Life Style","Secret Recipes"};
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, themeCatgory);
        forumList.setAdapter(adapter);

        final AdapterView.OnItemClickListener mItemClick=new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemName=parent.getItemAtPosition(position).toString();//get clicked item
                Intent go=new Intent(getActivity(),ForumActivity.class);
                switch (itemName){
                    case "Restaurant Review":
                        go.putExtra("nickname",nickname);
                        go.putExtra("theme","Restaurant Review");
                        startActivity(go);
                        break;
                    case "Healthy Life Style":
                        go.putExtra("nickname",nickname);
                        go.putExtra("theme","Healthy Life Style");
                        startActivity(go);
                        break;
                    case "Secret Recipes":
                        go.putExtra("nickname",nickname);
                        go.putExtra("theme","Secret Recipes");
                        startActivity(go);
                        break;
                    default:
                        break;

                }

            }
        };
        forumList.setOnItemClickListener(mItemClick);

        return ForumView;
    }

}
