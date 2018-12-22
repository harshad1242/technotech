package com.technotech.technotechapplication.Common;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.technotech.technotechapplication.DashboardActivity.DashboardActivity;
import com.technotech.technotechapplication.R;
import com.technotech.technotechapplication.util.Utils;


public class LoginActivity extends AppCompatActivity {

    EditText edt_emailid,edt_password;
    TextView txt_forgot,txt_Signup;
    Button btn_login;
    Utils util;
    Context context;
    SharedPreferences common_mypref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        common_mypref = getApplicationContext().getSharedPreferences(
                "user", 0);
        context=this;
        util=new Utils(context);
        edt_emailid=(EditText)findViewById(R.id.edt_emailid);
        edt_password=(EditText)findViewById(R.id.edt_password);
        txt_forgot=(TextView)findViewById(R.id.txt_forgot);
        txt_Signup=(TextView)findViewById(R.id.txt_Signup);
        btn_login=(Button)findViewById(R.id.btn_login);
        txt_Signup.setOnClickListener(mClickListener);
        txt_forgot.setOnClickListener(mClickListener);
        btn_login.setOnClickListener(mClickListener);
    }

    final View.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(final View v)
        {
            switch (v.getId()) {
                case R.id.txt_forgot:
                    util.ButtonClickEffect(v);
                    Intent intent_forgot = new Intent(LoginActivity.this, ForgotpasswordActivity.class);
                    startActivity(intent_forgot);
                    break;
                case R.id.txt_Signup:
                    util.ButtonClickEffect(v);
                    Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    break;

                case R.id.btn_login :
                    Intent intent_login = new Intent(LoginActivity.this,DashboardActivity.class);
                    startActivity(intent_login);
                    break;

            }
        }
    };
}
