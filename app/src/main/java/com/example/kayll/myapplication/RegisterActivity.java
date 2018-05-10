package com.example.kayll.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {

    private EditText edit1;
    private EditText edit2;
    private AutoCompleteTextView email;
    private Button submit;
    private GestureDetector mDetector;
    private final static int MIN_MOVE = 0;   //最小距离
    private MyGestureListener mgListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_register_toolbar);
        toolbar.getBackground().setAlpha(0);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);//设置Navigation 图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        edit1=(EditText)findViewById(R.id.password1);
        edit2=(EditText)findViewById(R.id.password2);
        email=email=(AutoCompleteTextView)findViewById(R.id.email2);
        submit=(Button)findViewById(R.id.email_submit_button);
        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(edit1.getText().toString().equals(edit2.getText().toString())){

                }else{
                    Intent intent=new Intent(RegisterActivity.this,RegisterActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });

        mgListener = new MyGestureListener();
        mDetector = new GestureDetector(this, mgListener);
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mDetector.onTouchEvent(event);
    }
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
            if(e1.getX() - e2.getX() < MIN_MOVE) {
                finish();
            }
            return true;
        }
    }
}
