package com.example.a341project;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    Button Register;
    EditText Username, Email, Birthday, Password, PasswordV, City, Province, ID;
    RadioGroup IdType;
    boolean allCheck = false;

    private int Year, Month, Day;
    CharSequence IdTypeVal;
    CharSequence ProvinceVal;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        //database reference goes here
        databaseReference = firebaseDatabase.getReference("");


        Register = findViewById(R.id.new_register);
        Username = findViewById(R.id.new_username);
        Email = findViewById(R.id.email);
        Birthday = findViewById(R.id.dob);
        Password = findViewById(R.id.new_password);
        PasswordV = findViewById(R.id.verify_password);
        City = findViewById(R.id.city);
        ID = findViewById(R.id.IDno);

        Spinner Province = (Spinner) findViewById(R.id.province);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.provinces,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Province.setAdapter(adapter);

        IdType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton = findViewById(checkedId);
                IdTypeVal=radioButton.getText();

            }
        });

        Province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ProvinceVal = Province.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                Year = c.get(Calendar.YEAR);
                Month = c.get(Calendar.MONTH);
                Day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Birthday.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                    }
                }, Year, Month, Day);
                datePickerDialog.show();
            }
        });




        Register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                allCheck = CheckAllFields();
                if (allCheck) {
                    String infoSend;
                    infoSend=Username.getText()+","+Email.getText()+","+Password.getText()+","+Birthday.getText()+","
                            +City.getText()+","+ProvinceVal+","+IdTypeVal+","+ID.getText()+"\n";



                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            databaseReference.setValue(infoSend);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });


                    finish();
                }
            }
        });



    }

    private boolean CheckAllFields() {
        String regexEmail = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+$";
        Pattern patternEmail = Pattern.compile(regexEmail);
        Matcher matcherEmail = patternEmail.matcher(Email.getText());

        String regexPass = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,}$";
        Pattern patternPass = Pattern.compile(regexPass);
        Matcher matcherPass = patternPass.matcher(Password.getText());

        String regexUsername = "^[a-zA-Z0-9._]+$";
        Pattern patternUsername = Pattern.compile(regexUsername);
        Matcher matcherUsername = patternUsername.matcher(Username.getText());
        String regexID = "";
        if(IdTypeVal.equals("Passport")) {
            regexID = "^[A-Za-z][A-Za-z]\\d\\d\\d\\d\\d\\d$";
        }else{
            //tests according to BCID, different depending on province.
            regexID = "^\\d\\d\\d\\d\\d\\d\\d\\d$";
        }

        Pattern patternID = Pattern.compile(regexID);
        Matcher matcherID = patternID.matcher(ID.getText());


        int duration = Toast.LENGTH_SHORT;
        String toastText="";

        boolean finalCheck=true;

        if (Username.getText().length() == 0) {
            toastText=("Please enter a username.");
            finalCheck= false;
        } else if (!matcherUsername.matches()){
            toastText=("Please enter a valid username");
            finalCheck= false;
        }

        else if (Email.getText().length() == 0) {
            toastText=("Please enter your email.");
            finalCheck= false;
        } else if (!matcherEmail.matches()){
            toastText=("Please enter a valid email");
            finalCheck= false;
        }

        else if (Password.getText().length() < 8) {
            toastText="Password must be minimum 8 characters";
            finalCheck= false;
        } else if(!matcherPass.matches()){
            toastText="Password should contain 1 numeric digit, 1 uppercase letter, and\n" +
                    "1 special character)";
            finalCheck= false;
        }

        else if (PasswordV.getText().length() < 8) {
            toastText="Please re-enter password.";
            finalCheck= false;
        } else if(!PasswordV.getText().equals(Password.getText())){
            toastText="Passwords do not match.";
            finalCheck= false;
        }

        else if (Birthday.getText().length() == 0) {
            toastText=("Please enter your date of birth.");
            finalCheck= false;
        }

        else if (City.getText().length() == 0) {
            toastText="Please enter city.";
            finalCheck= false;

        }
        else if (IdType == null) {
            toastText=("Please select the type of ID for verification.");
            finalCheck= false;
        }

        else if (Province == null) {
            toastText=("Please select your province.");
            finalCheck= false;
        }

        else if (ID.getText().length() == 0) {
            toastText="Please enter an ID number.";
            finalCheck= false;
        } else if(!matcherID.matches()){
            toastText="ID number is not valid.";
            finalCheck= false;
        }



        if (!finalCheck) {
            Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
            toast.show();
        }

        return finalCheck;
    }

}