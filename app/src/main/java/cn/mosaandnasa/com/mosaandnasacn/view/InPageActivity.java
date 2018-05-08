package cn.mosaandnasa.com.mosaandnasacn.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import cn.mosaandnasa.com.mosaandnasacn.R;

public class InPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_page);
    }

    public void goLogin(View view){
        //進入登入
        startActivity(new Intent(this,LoginActivity.class));
        finish();
    }
}
