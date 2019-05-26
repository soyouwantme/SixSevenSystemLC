package com.kk.sixsevensystemlc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.SignUpCallback;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    private TextView mEmailText;
    private EditText mPasswordText;
    private EditText mRePasswordText;
    private RadioGroup mRadioGroup;
    private Button mSignUpButton;
    private int role;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mEmailText = (TextView)findViewById(R.id.email);
        mPasswordText = (EditText)findViewById(R.id.password);
        mRePasswordText = (EditText)findViewById(R.id.re_password);

        mSignUpButton = (Button) findViewById(R.id.sign_up_button);
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        mRadioGroup = findViewById(R.id.radio_group);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        //RadioButton radbtn = findViewById(checkedId);
                        Log.d("signin",checkedId+"");
                        role = checkedId - 2131230788;
                        /*
                        switch (checkedId){
                            case 0:
                                role = 0;
                                break;
                            case 1:
                                role = 1;
                                break;
                            case 2:
                                role = 2;
                                break;
                            default:
                                break;
                        }*/
                    }
                });

        TextView mBackLogin = (TextView) findViewById(R.id.back_login);
        mBackLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    public void signup(){
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        mSignUpButton.setEnabled(false);

        final String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        AVUser user = new AVUser();// 新建 AVUser 对象实例
        user.setUsername(email);// 设置用户名
        user.setPassword(password);// 设置密码
        user.setEmail(email);// 设置邮箱
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(),"注册成功",Toast.LENGTH_SHORT).show();
                    // 注册成功，把用户对象赋值给当前用户 AVUser.getCurrentUser()
                    switch (role){
                        case 0:
                            startActivity(new Intent(SignupActivity.this, SupplierActivity.class));
                            break;
                        case 1:
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            break;
                        case 2:
                            startActivity(new Intent(SignupActivity.this, ConsumerActivity.class));
                            break;
                        default:
                            break;
                    }
                    //创建role表项
                    AVObject r = new AVObject("role");
                    r.put("email",email);
                    r.put("role",role+"");
                    r.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(AVException e) {
                            if(e!=null){
                                Log.d("error",e.toString());
                            }
                        }
                    });
                    SignupActivity.this.finish();
                } else {
                    // 失败的原因可能有多种，常见的是用户名已经存在。
                    Toast.makeText(getApplicationContext(),"用户名已存在",Toast.LENGTH_SHORT).show();
                    mSignUpButton.setEnabled(true);
                }
            }
        });
    }

    public void onSignupSuccess() {
        mSignUpButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        mSignUpButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = mEmailText.getText().toString();
        String password = mPasswordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            mPasswordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            mPasswordText.setError(null);
        }

        return valid;
    }
}
