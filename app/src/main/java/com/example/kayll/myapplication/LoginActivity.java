package com.example.kayll.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    private AutoCompleteTextView email;
    private EditText password;
    private Button login;
    private TextView register;
    public static final String action="login_success";
    private GestureDetector mDetector;
    private final static int MIN_MOVE = 0;   //最小距离
    private MyGestureListener mgListener;
    private SharedHelper sh;
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_login_toolbar);
        toolbar.getBackground().setAlpha(0);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);//设置Navigation 图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        email=(AutoCompleteTextView)findViewById(R.id.email);
        password=(EditText)findViewById(R.id.password);
        login=(Button)findViewById(R.id.email_sign_in_button);
        register=(TextView) findViewById(R.id.email_Register_button);
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                new Thread() {
                    @Override
                    public void run() {
                        try {
                            acceptServer();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
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
    private void acceptServer() throws IOException, JSONException {
        String path="http://10.180.163.194:8080/AppServer/LoginCheck";

        URL url=new URL(path);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setReadTimeout(5000);// 设置超时的时间
        urlConnection.setConnectTimeout(5000);// 设置链接超时的时间
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setRequestMethod("POST");
        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        urlConnection.connect();
        DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
        JSONObject obj = new JSONObject();
        obj.put("name", email.getText().toString());
        obj.put("password",password.getText().toString());
        wr.writeBytes(obj.toString());
        wr.flush();
        wr.close();
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8")) ;
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
       //urlConnection.disconnect();
        JSONObject json = new JSONObject(sb.toString());
        String result=null;
        if (json != null) {
            result = json.getString("result");
        }
        if(result.equals("success")){
            Intent intent=new Intent(action);
            intent.putExtra("name",email.getText().toString());
            sendBroadcast(intent);
        }else{
            Intent intent=new Intent(LoginActivity.this,LoginActivity.class);
            startActivity(intent);
        }

    }

}

