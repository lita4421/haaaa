package com.example.huaweiacc;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

public class MainActivity extends AppCompatActivity {

    Button sign_in;
    AccountAuthParams authParams;
    AccountAuthService service;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sign_in = findViewById(R.id.sign_in);

         sign_in.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                         .setAuthorizationCode().createParams();
                 service = AccountAuthManager.getService(MainActivity.this, authParams);
                 startActivityForResult(service.getSignInIntent(), 8888);
             }
         });
         
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Process the authorization result to obtain the authorization code from AuthAccount.
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 8888) {
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                // The sign-in is successful, and the user's ID information and authorization code are obtained.
                AuthAccount authAccount = authAccountTask.getResult();
                Log.i("main", "serverAuthCode:" + authAccount.getAuthorizationCode());
                Log.d("main","getDisplayName:"+authAccount.getDisplayName());

                //authAccountTask.getResult()
                Log.d("main","getResult:"+authAccountTask.getResult());
            } else {
                // The sign-in failed.
                Log.e("main", "sign in failed:" + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
    }
}