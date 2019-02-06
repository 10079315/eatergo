package com.example.likaiapply.eatergo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    FragmentManager fragmentManager;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View HomeView=inflater.inflate(R.layout.fragment_home, container, false);
        Button RegisterButton=(Button)HomeView.findViewById(R.id.RegisterButton);
        Button SiginButton=(Button)HomeView.findViewById(R.id.SignInButton);
        ImageView eatergoIcon=(ImageView)HomeView.findViewById(R.id.EaterGoIcon);



        fragmentManager=getActivity().getSupportFragmentManager();

        RegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .show(fragmentManager.findFragmentByTag("register"))
                        .hide(fragmentManager.findFragmentByTag("forum"))
                        .hide(fragmentManager.findFragmentByTag("signin"))
                        .hide(fragmentManager.findFragmentByTag("home"))
                        .hide(fragmentManager.findFragmentByTag("profile"))
                        .commit();
            }
        });
        SiginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                fragmentManager.beginTransaction()
                        .show(fragmentManager.findFragmentByTag("signin"))
                        .hide(fragmentManager.findFragmentByTag("forum"))
                        .hide(fragmentManager.findFragmentByTag("home"))
                        .hide(fragmentManager.findFragmentByTag("register"))
                        .hide(fragmentManager.findFragmentByTag("profile"))
                        .commit();
            }
        });


        return HomeView;
    }

}
