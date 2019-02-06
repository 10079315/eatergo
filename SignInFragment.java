package com.example.likaiapply.eatergo;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    public SignInFragment() {
        // Required empty public constructor
    }
    EditText UserNameEText;
    EditText PasswordEText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View SignInView=inflater.inflate(R.layout.fragment_sign_in, container, false);
        Button SignInButton=(Button)SignInView.findViewById(R.id.SignInButton);
        UserNameEText=(EditText)SignInView.findViewById(R.id.UserNameInput);
        PasswordEText=(EditText)SignInView.findViewById(R.id.PassWordInput);


        SignInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                String email=UserNameEText.getText().toString();
                String password=PasswordEText.getText().toString();
                String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                boolean isLegalEmail = matcher.matches();

                if(password.length()<6) {
                    String errorInfo="Wrong Password!";
                    showAlert(errorInfo);
                } else if(!isLegalEmail){
                    showAlert("Illegal Email address!");
                } else {
                    SignInListener listener = (SignInListener) getActivity();
                    listener.signInUser(email,password);
                }

            }
        });



        return SignInView;
    }

    public void showAlert(String error){

        AlertDialog alert=new AlertDialog.Builder(getActivity()).create();
        alert.setTitle("Alert");
        alert.setMessage(error);
        alert.setButton(AlertDialog.BUTTON_NEUTRAL, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    public interface SignInListener {
        void signInUser(String email,String password);
    }

}
