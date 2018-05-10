package com.example.kayll.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Person_age_Activity extends AppCompatActivity {
    public static final String action="age_change";
    private EditText et;
    private Button bt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_age_);
        et=(EditText)findViewById(R.id.reage);
        bt=(Button)findViewById(R.id.save_age_button);
        bt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(action);
                intent.putExtra("age",et.getText().toString());
                sendBroadcast(intent);
                finish();
            }
        });
    }
}
