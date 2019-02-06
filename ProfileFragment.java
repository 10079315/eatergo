package com.example.likaiapply.eatergo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    TextView NickNameText;
    TextView EmailText;
    TextView CountryText;
    TextView StateText;
    TextView CityText;
    TextView BirthdateText;
    TextView GenderText;
    TextView RegisterDate;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View ProfileView= inflater.inflate(R.layout.fragment_profile, container, false);
        Button SignOffButton=(Button) ProfileView.findViewById(R.id.SignOffButton);
        NickNameText=(TextView) ProfileView.findViewById(R.id.NicknameValue);
        EmailText=(TextView) ProfileView.findViewById(R.id.EmailValue);
        CountryText=(TextView) ProfileView.findViewById(R.id.CountryValue);
        StateText=(TextView) ProfileView.findViewById(R.id.StateVlaue);
        CityText=(TextView) ProfileView.findViewById(R.id.CityValue);
        BirthdateText=(TextView) ProfileView.findViewById(R.id.BirthdateValue);
        GenderText=(TextView) ProfileView.findViewById(R.id.GenderValue);
        RegisterDate=(TextView) ProfileView.findViewById(R.id.RegisterdateValue);

        SignOffButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileListener listener=(ProfileListener)getActivity();
                listener.signOff();
            }
        });

        return ProfileView;
    }

    public void getUserData(UserInfo currentUser){

        NickNameText.setText(currentUser.NickName);
        EmailText.setText(currentUser.Email);
        CountryText.setText(currentUser.Country);
        StateText.setText(currentUser.State);
        CityText.setText(currentUser.City);
        BirthdateText.setText(currentUser.Birthdate);
        GenderText.setText(currentUser.Gender);
        RegisterDate.setText(currentUser.RegistDate);

    }

    public interface ProfileListener {
        void signOff();
    }

}
