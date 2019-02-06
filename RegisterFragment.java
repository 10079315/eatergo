package com.example.likaiapply.eatergo;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    EditText NickNameEText;
    EditText EmailEText;
    EditText PasswordEText;
    Spinner CountrySpinner;
    Spinner StateSpinner;
    EditText CityEText;
    EditText BirthdateEText;
    Spinner GenderSpinner;
    Button RegisterButton;

    String countryName;
    String stateName;
    String gender;


    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View RegisterView= inflater.inflate(R.layout.fragment_register, container, false);
        NickNameEText=(EditText) RegisterView.findViewById(R.id.NicknameInput);
        EmailEText=(EditText)RegisterView.findViewById(R.id.EmailInput);
        PasswordEText=(EditText) RegisterView.findViewById(R.id.PasswordInput);
        CountrySpinner=(Spinner)RegisterView.findViewById(R.id.Countryspinner);
        StateSpinner=(Spinner)RegisterView.findViewById(R.id.StateSpinner);
        CityEText=(EditText) RegisterView.findViewById(R.id.CityInput);
        BirthdateEText=(EditText) RegisterView.findViewById(R.id.BirthDateInput);
        GenderSpinner=(Spinner) RegisterView.findViewById(R.id.GenderSpinner);
        RegisterButton=(Button) RegisterView.findViewById(R.id.Register);

        CountrySpinner.setOnItemSelectedListener(this);
        StateSpinner.setOnItemSelectedListener(this);
        GenderSpinner.setOnItemSelectedListener(this);


        //get country data from assets
        List<String> countrydata=new ArrayList<String>();
        try {
            String data="";
            InputStream File = getActivity().getAssets().open("countries");
            BufferedReader in = new BufferedReader( new InputStreamReader(File));
            while((data=in.readLine())!=null)
                countrydata.add(data);
        } catch (IOException e) {
            Log.e("rew", "read Error", e);
        }

        //set Country data to spinner
        ArrayAdapter<String> countryadapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, countrydata);
        countryadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        CountrySpinner.setAdapter(countryadapter);

        //set Gender data
        String[] genderdata=new String[]{"Male","Female"};
        ArrayAdapter<String> genderadapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, genderdata);
        genderadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GenderSpinner.setAdapter(genderadapter);

        RegisterButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String email=EmailEText.getText().toString();
                String password=PasswordEText.getText().toString();

                String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
                Pattern regex = Pattern.compile(check);
                Matcher matcher = regex.matcher(email);
                boolean isLegalEmail = matcher.matches();

                if(password.length()<6) {
                    String errorInfo="Password length must at least 6 letters!";
                    showAlert(errorInfo);
                } else if(!isLegalEmail){
                    showAlert("Illegal Email address!");
                } else {
                    UserInfo newUser=new UserInfo();
                    if(NickNameEText.getText().toString().length()==0)newUser.NickName=" ";
                    else newUser.NickName=NickNameEText.getText().toString();
                    newUser.Email=EmailEText.getText().toString();
                    newUser.Password=PasswordEText.getText().toString();
                    newUser.Country=countryName;
                    newUser.State=stateName;
                    newUser.Gender=gender;

                    if(CityEText.getText().toString().length()==0)newUser.City=" ";
                    else newUser.City=CityEText.getText().toString();
                    if(BirthdateEText.getText().toString().length()==0)newUser.Birthdate=" ";
                    else newUser.Birthdate=BirthdateEText.getText().toString();

                    RegisterListener listener = (RegisterListener) getActivity();
                    listener.registerUser(newUser);
                }

            }
        });

        return RegisterView;
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()){
            case R.id.Countryspinner:
                countryName=parent.getItemAtPosition(position).toString();

                List<String> statedata=new ArrayList<String>();
                try {
                    String state_content="";
                    InputStream File = getActivity().getAssets().open(countryName);
                    BufferedReader in = new BufferedReader( new InputStreamReader(File));
                    while((state_content=in.readLine())!=null)
                        statedata.add(state_content);

                } catch (IOException e) {
                    Log.e("rew", "read Error", e);
                }
                ArrayAdapter<String> stateadapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, statedata);
                stateadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                StateSpinner.setAdapter(stateadapter);

                break;
            case R.id.StateSpinner:
                stateName=parent.getItemAtPosition(position).toString();
                break;
            case R.id.GenderSpinner:
                gender=parent.getItemAtPosition(position).toString();
                break;
            default:
                break;

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public interface RegisterListener {
        void registerUser(UserInfo newUser);
    }

}
