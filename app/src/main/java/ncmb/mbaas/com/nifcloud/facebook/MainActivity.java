package ncmb.mbaas.com.nifcloud.facebook;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.nifcloud.mbaas.core.NCMB;
import com.nifcloud.mbaas.core.NCMBException;
import com.nifcloud.mbaas.core.NCMBFacebookParameters;
import com.nifcloud.mbaas.core.NCMBUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    CallbackManager callbackManager;
    final String APP_KEY = "2bfb444423219ff54256bbe41ff270c5d8c3e81eaa3121c18603363e99b0b673";
    final String CLIENT_KEY = "2e0167555ae06b73a73a8b2ef1ea9614d566b17cb7c0d191da80797221088bf2";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //**************** APIキーの設定とSDKの初期化 **********************
        NCMB.initialize(this.getApplicationContext(), APP_KEY, CLIENT_KEY);

        // Facebook settings
        // add client token in AndroidManifest.xml before init facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        setSupportActionBar(toolbar);

        //AppEventsLogger.activateApp(this); replace with the following code
        AppEventsLogger.activateApp(getApplication());

        callbackManager = CallbackManager.Factory.create();

        // set login button permissions
        loginButton.setPermissions(Arrays.asList("public_profile", "email"));

        //register callback login
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                //Login to NIFCLOUD mobile backend
                NCMBFacebookParameters parameters = new NCMBFacebookParameters(
                        loginResult.getAccessToken().getUserId(),
                        loginResult.getAccessToken().getToken(),
                        loginResult.getAccessToken().getExpires()
                );

                try {

                    NCMBUser.loginWith(parameters);
                    Toast.makeText(getApplicationContext(), "Login to NIFCLOUD mbaas with Facebook account", Toast.LENGTH_LONG).show();

                } catch (NCMBException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("tag", "onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.d("tag", "onError:" + exception);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
