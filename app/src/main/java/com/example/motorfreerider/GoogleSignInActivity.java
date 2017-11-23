package com.example.motorfreerider;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

/**
 * Demonstrate Firebase Authentication using a Google ID Token.
 */
public class GoogleSignInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

        private LinearLayout Prof_Section;
        private Button SignOut;
        private SignInButton SignIn;
        private TextView Name ,Email,ID;
        private ImageView Prof_Pic;

        private GoogleApiClient googleApiClient;
        private static final int REQ_CODE = 9001;
        @Override
        protected  void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            SignOut = (Button)findViewById(R.id.sign_out_button);
            //ID = (TextView)findViewById(R.id.ID);
            SignIn = (SignInButton)findViewById(R.id.sign_in_button);
            SignIn.setOnClickListener(this);
            SignOut.setOnClickListener(this);
            GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
            googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();
        }

        @Override
        public  void onClick (View v){
            switch(v.getId())
            {
                case R.id.sign_in_button:
                    signIn();
                    break;
                case R.id.sign_out_button:
                    signOut();
                    break;
            }
        }

        @Override
        public  void onConnectionFailed(@NonNull ConnectionResult connectionResult){

        }
        private void signIn() {
            Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
            startActivityForResult(intent,REQ_CODE);

        }
        private void signOut() {
            Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                @Override
                public void onResult(@NonNull Status status) {
                    updateUI(false,null);
                }
            });
        }
        private void handleResult(GoogleSignInResult result){
            GoogleSignInAccount account = result.getSignInAccount();
            if(result.isSuccess()){
                String name = account.getDisplayName();
                String email = account.getEmail();
                String img_url = account.getPhotoUrl().toString();
                String idToken =account.getIdToken();


                updateUI(true,account);
            }
            else{
                updateUI(false,account);
            }
        }
        private void updateUI(boolean isLogin,GoogleSignInAccount account){
            if(isLogin){
                String idToken = account.getId();
            //    ID.setText(getString(R.string.id_fmt,idToken));
                findViewById(R.id.sign_in_button).setVisibility(View.GONE);
                findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            }
            else{
             //   findViewById(R.id.ID).setVisibility(View.GONE);
                findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
                findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
            }
        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQ_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}
