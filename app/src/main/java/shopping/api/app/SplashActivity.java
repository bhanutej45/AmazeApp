package shopping.api.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    PreferenceManager preferenceManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        preferenceManager = new PreferenceManager(this);


        changeStatusBarColor(R.color.black);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    if (preferenceManager.getBoolean("login")){
                        startActivity(new Intent(SplashActivity.this,MainActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }else{
                        startActivity(new Intent(SplashActivity.this,LoginActivity.class)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    }
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                    startActivity(new Intent(SplashActivity.this,LoginActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                    finish();
                }
            }
        },1000);

    }

    private void changeStatusBarColor(int colorResId) {
        getWindow().setStatusBarColor(ContextCompat.getColor(this, colorResId));
    }

}