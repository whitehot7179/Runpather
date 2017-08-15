package io.github.ck7179.runpather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        Button btn_FacebookLogin = (Button) findViewById(R.id.button_FacebookLogin);
        btn_FacebookLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                nextpage();
            }
        });
    }
    public void nextpage(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
