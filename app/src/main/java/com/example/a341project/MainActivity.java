package com.example.a341project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    Button Register;
    Button Login;
    EditText Username, Password;

    boolean allCheck = false;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        //database reference goes here
        databaseReference = firebaseDatabase.getReference("");


        Register = findViewById(R.id.register);
        Login = findViewById(R.id.login);
        Username = findViewById(R.id.new_username);
        Password = findViewById(R.id.new_password);

        Register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                    Intent infoIntent = new Intent(MainActivity.this, Register.class);
                    startActivity(infoIntent);
            }
        });

        //link to home screen here.
        Login.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                allCheck = CheckAllFields();
                if (allCheck) {
                    /*
                    String infoSend;
                    infoSend=Username.getText();
                    Intent infoIntent = new Intent(MainActivity.this, MainActivity2.class);
                    infoIntent.putExtra("key", infoSend);
                    startActivity(infoIntent);

                     */
                }
            }
        });
    }

    private boolean CheckAllFields() {

        int duration = Toast.LENGTH_SHORT;
        String toastText="";

        boolean finalCheck=true;

        if (Username.getText().length() == 0) {
            toastText = ("Please enter a username.");
            finalCheck = false;
        }

        else if (Password.getText().length() < 0) {
            toastText = "Please enter a password.";
            finalCheck = false;
        }

        if (!finalCheck) {
            Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
            toast.show();
        }

        return finalCheck;
    }
}