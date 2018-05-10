package com.example.kayll.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Face_Register_Activity extends AppCompatActivity {
    private JSONObject jsonObject;
    private AutoCompleteTextView tv1,tv2,tv3,tv4;
    private ImageView img2;
    private Button bt;
    private Context context = null;
    private PopupWindow popupWindow;
    private ImageView img_show;
    private File currentImageFile =null;
    private Uri imageUri;
    private static final  int REQUEST_CAMERA_CAPTURE=1;
    private static final  int REQUEST_IMAGE_CAPTURE=2;
    public static final int CROP_PHOTO = 3;
    private int flag=0;
    private GestureDetector mDetector;
    private final static int MIN_MOVE = 0;   //最小距离
    private MyGestureListener mgListener;
    private String tel=null;
    private Bitmap face=null,id_card=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_face__register_);

        Object data=SharedHelper.get(context,"loginName","");
        tel=data.toString();

        Toolbar toolbar = (Toolbar) findViewById(R.id.activity_face_register_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);//设置Navigation 图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv1=(AutoCompleteTextView)findViewById(R.id.msg_name);
        tv2=(AutoCompleteTextView)findViewById(R.id.msg_sex);
        tv3=(AutoCompleteTextView)findViewById(R.id.msg_id);
        tv4=(AutoCompleteTextView)findViewById(R.id.msg);
        img_show=(ImageView)findViewById(R.id.face1);
        img2=(ImageView)findViewById(R.id.face2);
        bt=(Button)findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener(){
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
        img_show.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                flag=1;
                initPopupWindow();
            }
        });
        img2.setOnClickListener(new View.OnClickListener(){
                @Override
            public void onClick(View v) {
                 flag=2;
                initPopupWindow();
            }
        });

        //实例化SimpleOnGestureListener与GestureDetector对象
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


    class popupDismissListener implements PopupWindow.OnDismissListener{

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }

    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    protected void initPopupWindow(){
        View popupWindowView = getLayoutInflater().inflate(R.layout.buttom_dialog, null);
        //内容，高度，宽度
        popupWindow = new PopupWindow(popupWindowView, WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        //动画效果
        popupWindow.setAnimationStyle(R.style.DialogAnimation);
        //菜单背景色
        ColorDrawable dw = new ColorDrawable(0xffffffff);
        popupWindow.setBackgroundDrawable(dw);
        //宽度
        //popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
        //高度
        //popupWindow.setHeight(LayoutParams.FILL_PARENT);
        //显示位置

        popupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_face__register_, null), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);

        //设置背景半透明
        backgroundAlpha(0.5f);
        //关闭事件
        popupWindow.setOnDismissListener(new popupDismissListener());

        popupWindowView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /*if( popupWindow!=null && popupWindow.isShowing()){
                    popupWindow.dismiss();
                    popupWindow=null;
                }*/
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                return false;
            }
        });

        Button open = (Button)popupWindowView.findViewById(R.id.btn_open_camera);
        Button save = (Button)popupWindowView.findViewById(R.id.btn_choose_img);
        Button close = (Button)popupWindowView.findViewById(R.id.btn_cancel);

        open.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                takePhoto();
                popupWindow.dismiss();
            }

        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                choosePhoto();
                popupWindow.dismiss();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQUEST_CAMERA_CAPTURE&&resultCode==RESULT_OK){
            Intent intent = new Intent("com.android.camera.action.CROP");
            intent.setDataAndType(imageUri, "image/*");
            intent.putExtra("scale", true);
            intent.putExtra("aspectX", 1);
            intent.putExtra("aspectY", 1);
            intent.putExtra("outputX", 300);
            intent.putExtra("outputY", 300);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(currentImageFile));
            startActivityForResult(intent, CROP_PHOTO);            // 启动裁剪程序

        }else if(requestCode==CROP_PHOTO&&resultCode==RESULT_OK){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                        .openInputStream(Uri.fromFile(currentImageFile)));
                if(flag==1) {
                    id_card = bitmap;
                    img_show.setImageBitmap(bitmap);
                }
                else if(flag==2){
                    face=bitmap;
                    img2.setImageBitmap(bitmap);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if(requestCode==REQUEST_IMAGE_CAPTURE&&resultCode==RESULT_OK){
            try {
                if(data != null) {
                    Uri uri = data.getData();
                    imageUri = uri;
                }
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver()
                        .openInputStream(imageUri));
                if(flag==1) {
                    id_card = bitmap;
                    img_show.setImageBitmap(bitmap);
                }
                else if(flag==2) {
                    face = bitmap;
                    img2.setImageBitmap(bitmap);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    private void takePhoto(){
        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

         if(hasSdcard()) {
             File dir = new File(Environment.getExternalStorageDirectory(), "pictures");
             if (!dir.exists()) {
                 dir.mkdirs();
             }
             currentImageFile = new File(dir, System.currentTimeMillis() + ".jpg");
             if (!currentImageFile.exists()) {
                 try {
                     currentImageFile.createNewFile();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }

             Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
             if(currentapiVersion<24){
                 imageUri=Uri.fromFile(currentImageFile);
                 it.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
             }else{
                 ContentValues contentValues = new ContentValues(1);
                 contentValues.put(MediaStore.Images.Media.DATA, currentImageFile.getAbsolutePath());
                 if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                         != PackageManager.PERMISSION_GRANTED) {
                     //申请WRITE_EXTERNAL_STORAGE权限
                     Toast.makeText(this,"请开启存储权限",Toast.LENGTH_SHORT).show();
                     return;
                 }
                 imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                 it.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
             }

             startActivityForResult(it,REQUEST_CAMERA_CAPTURE);
         }
    }
    private void choosePhoto(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        }
    }
    private boolean hasSdcard(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return true;
        }else{
            return false;
        }
    }
    private void acceptServer() throws IOException, JSONException{
        String path="http://10.180.163.194:8080/AppServer/Register_for_user";

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

        ByteArrayOutputStream baos = new ByteArrayOutputStream();//将Bitmap转成Byte[]
        face.compress(Bitmap.CompressFormat.PNG, 50, baos);//压缩
        String facep = Base64.encodeToString(baos.toByteArray(),Base64.DEFAULT);//加密转换成String

        ByteArrayOutputStream baos2 = new ByteArrayOutputStream();//将Bitmap转成Byte[]
        id_card.compress(Bitmap.CompressFormat.PNG, 50, baos2);//压缩
        String id_cardp = Base64.encodeToString(baos2.toByteArray(),Base64.DEFAULT);//加密转换成String

        obj.put("tel", tel);
        obj.put("name", tv1.getText().toString());
        obj.put("sex",tv2.getText().toString());
        obj.put("id",tv3.getText().toString());
        obj.put("addr",tv4.getText().toString());
        obj.put("idcard",id_cardp);
        obj.put("face",facep);
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
            Toast.makeText(context,"认证信息已上传" ,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"信息上传失败" ,Toast.LENGTH_SHORT).show();
        }
    }

}
