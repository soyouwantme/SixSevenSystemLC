package com.kk.sixsevensystemlc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.LogInCallback;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity {
    private static final int REQUEST_SIGNUP = 0;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private TextView mSignUp;
    private TextView mForget;
    private int role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //检测用户是否已经登录，如果已经登录，直接跳转到用户主界面，否则什么也不做
        List<String> isLogged = isLogged();
        Log.e("logjkc",isLogged.get(0)+"");
        if(isLogged.get(0) != "null" ){
            switch (isLogged.get(1)){
                case "0":
                    startActivity(new Intent(LoginActivity.this, SupplierActivity.class));
                    break;
                case "1":
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    break;
                case "2":
                    startActivity(new Intent(LoginActivity.this, ConsumerActivity.class));
                    break;
                default:
                    break;
            }
            Toast.makeText(getApplicationContext(),"欢迎回来 "+isLogged.get(0),Toast.LENGTH_SHORT).show();
            finish();//关闭当前登录界面，否则在主界面按后退键还会回到登录界面
        }
        //以下是正常的登录相关代码
        //AVUser.logOut();
        mEmailView = findViewById(R.id.email);
        mEmailView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_NEXT){

                }
                return false;
            }
        });
        mPasswordView = findViewById(R.id.password);
        //在我们编辑完之后点击软键盘上的回车键才会触发
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {

                    return false;
                }
                return false;
            }
        });

        final Button mSignInButton = findViewById(R.id.email_sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignInButton.setEnabled(false);
                attemptLogin();
            }
        });

        mSignUp = findViewById(R.id.signup_text);
        mSignUp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                /**startActivityForResult( )
                 可以一次性完成这项任务，当程序执行到这段代码的时候，假若从T1Activity跳转到下一个
                 Text2Activity，而当这个Text2Activity调用了finish()方法以后，程序会自动跳转回
                 T1Activity，并调用前一个T1Activity中的onActivityResult( )方法。*/
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });

        mForget = findViewById(R.id.forget_text);
        mForget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,ForgetActivity.class));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_SIGNUP){
            if(requestCode ==RESULT_OK){

            }
        }
    }

    public void attemptLogin(){

        if (!validate()) {
            onLoginFailed();
            return;
        }

        final String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        AVQuery<AVObject> query = new AVQuery<>("role");
        query.whereEqualTo("email",email);
        query.getFirstInBackground(new GetCallback<AVObject>() {
            @Override
            public void done(AVObject object, AVException e) {
                Log.d("rolejkc",object.get("role")+" 666");
                role = Integer.parseInt(object.get("role")+"");
            }
        });


        AVUser.logInInBackground(email, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                LoginActivity.this.finish();
                 switch (role){
                    case 0:
                        startActivity(new Intent(LoginActivity.this, SupplierActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(LoginActivity.this, ConsumerActivity.class));
                        break;
                    default:
                        break;
                }
                //SharedPreferences 保存数据的实现代码
                SharedPreferences sharedPreferences =
                        getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                //如果不能找到Editor接口。尝试使用 SharedPreferences.Editor
                editor.putString("email", email);
                //我将用户信息保存到其中，你也可以保存登录状态
                editor.putString("role",role+"");
                editor.commit();
            }
        });

    }

    private List<String> isLogged() {
        String user_id ;
        String role_id ;
        List<String> info = new ArrayList<>();
        SharedPreferences sharedPreferences =
                getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = sharedPreferences.getString("email", "null");
        role_id = sharedPreferences.getString("role","null");
        info.add(user_id);
        info.add(role_id);
        return info;
    }

    public void onLoginSucceed(){

    }

    public void onLoginFailed(){

    }

    public boolean validate() {
        boolean valid = true;

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailView.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailView.setError(null);
        }

        if (password.isEmpty() || password.length() < 6 || password.length() > 10) {
            mPasswordView.setError("between 6 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordView.setError(null);
        }

        return valid;
    }
}

