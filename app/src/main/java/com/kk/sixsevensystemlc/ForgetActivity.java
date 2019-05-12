package com.kk.sixsevensystemlc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;

public class ForgetActivity extends AppCompatActivity {

    private TextView mEmailText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_activity);
        mEmailText = (TextView) findViewById(R.id.email);

        Button mForgetButton = (Button)findViewById(R.id.forget_button);
        mForgetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forget();
            }
        });
    }

    protected void forget(){

        if (!validemail()) {
            onForgetFailed();
            return;
        }

        String email = mEmailText.getText().toString();

        AVUser.requestPasswordResetInBackground(email, new RequestPasswordResetCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    onForgetSuccess();
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    public void onForgetSuccess(){
        Toast.makeText(getBaseContext(), "Retrieve Succeed", Toast.LENGTH_LONG).show();
        finish();
    }
    public void onForgetFailed() {
        Toast.makeText(getBaseContext(), "Retrieve failed", Toast.LENGTH_LONG).show();
    }

    public boolean validemail() {
        boolean valid = true;

        String email = mEmailText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailText.setError("enter a valid email address");
            valid = false;
        } else {
            mEmailText.setError(null);
        }

        return valid;
    }
}
