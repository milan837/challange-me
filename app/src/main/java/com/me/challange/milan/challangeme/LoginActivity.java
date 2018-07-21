package com.me.challange.milan.challangeme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    CallbackManager callbackManager;
    String facebookId;
    String facebookToken,name,gender,imageUrl;
    Firebase ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(getApplicationContext());
        ref=new Firebase("https://challange-me.firebaseio.com/");
        final Firebase childRef=ref.child("user_login");

        Typeface type=Typeface.createFromAsset(getAssets(),"font/regular.ttf");
        Button loginWithFbButton=(Button)findViewById(R.id.login_with_fb_button);
        final LoginButton loginButton=(LoginButton)findViewById(R.id.login_button);
        loginWithFbButton.setTypeface(type);

        callbackManager=CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(final LoginResult loginResult) {
                facebookId = loginResult.getAccessToken().getUserId().toString();
                facebookToken = loginResult.getAccessToken().getToken().toString();

                childRef.orderByChild("facebookId").equalTo(facebookId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            SharedPreferences sharedPreferences=getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("facebookId",facebookId);
                            editor.putString("facebookToken",facebookToken);
                            editor.commit();

                            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            finish();
                        } else {
                            userDetailsFromFacebook(loginResult);
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(), "Sorry,somthing went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Sorry,somthing went wrong", Toast.LENGTH_LONG).show();
            }
        });

        loginWithFbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.performClick();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void userDetailsFromFacebook(LoginResult loginResult){
        GraphRequest graphRequest=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.i("hson_date", String.valueOf(response));
                try {
                    name=response.getJSONObject().getString("name");
                    gender=response.getJSONObject().getString("gender");
                    imageUrl="https://graph.facebook.com/"+facebookId+"/picture?height=200&width=200&migration_overrides=%7Boctober_2012%3Atrue%7D";

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Intent intent=new Intent(LoginActivity.this,LoginDetailsActivity.class);
                intent.putExtra("facebookId",facebookId);
                intent.putExtra("facebookToken",facebookToken);
                intent.putExtra("name",name);
                intent.putExtra("gender",gender);
                intent.putExtra("imageUrl",imageUrl);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,name,email,gender,birthday");
        graphRequest.setParameters(parameters);
        graphRequest.executeAsync();
    }
}
