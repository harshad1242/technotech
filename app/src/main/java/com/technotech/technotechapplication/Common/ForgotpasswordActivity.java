package com.technotech.technotechapplication.Common;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.technotech.technotechapplication.R;
import com.technotech.technotechapplication.util.Utils;

public class ForgotpasswordActivity extends AppCompatActivity {

    TextInputLayout txttitle_email;
    Button btn_forgot;
    LinearLayout ly_main, ly_maintemp;
    EditText edt_email;
    Context context;
    Utils util;
    SharedPreferences common_mypref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        ly_main = (LinearLayout) findViewById(R.id.ly_main);
        edt_email = (EditText) findViewById(R.id.edt_emailid);
        btn_forgot = (Button) findViewById(R.id.btn_forgot);
        txttitle_email = (TextInputLayout) findViewById(R.id.txttitle_email);
        btn_forgot.setOnClickListener(mClickListener);
    }

    final View.OnClickListener mClickListener = new View.OnClickListener() {
        public void onClick(final View v) {
            switch (v.getId()) {
                case R.id.btn_forgot:
                    util.ButtonClickEffect(v);
                    break;
            }
        }

    };

    public boolean validation() {
        // /Function is used for to perform the validation....
        // /Return 1.flag true or false...
        boolean flag = false;
        if (edt_email.getText().toString().trim().length()==0) {
            // cd.showAlert(flag, getResources().getString(R.string.emailvalidation));
            util.customToast(getResources().getString(R.string.uservalidation));
            // edt_email.requestFocus();
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(edt_email.getText().toString().trim()).matches()) {
            util.customToast(getResources().getString(R.string.validemail));
            //edt_email.requestFocus();
//			 utils.showKeyboard(edt_password);
        }
        else {
            flag = true;

        }
        return flag;

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
