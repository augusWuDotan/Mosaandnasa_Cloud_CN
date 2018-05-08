package cn.mosaandnasa.com.mosaandnasacn.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import cn.mosaandnasa.com.mosaandnasacn.R;
import cn.mosaandnasa.com.mosaandnasacn.model.CustomAsyncTask;
import cn.mosaandnasa.com.mosaandnasacn.model.WebCallBack;

public class LoginActivity extends AppCompatActivity {

    /**
     * 台灣與大陸版本異動項目
     * <p>
     * LoginActivity,MainActivity
     * isTaiwan > true | false
     * <p>
     * AndroidManifest icon
     * ic_launcher | ic_launcher_cn
     * <p>
     * activity_main backgroung
     * splash | splash_ch
     */

    private String URL_DOMAIN_cn = "http://mosaandnasa.com.cn/Home/doLoginWithApp/"; //大陸
    private String URL_DOMAIN = "http://mosaandnasa.com/Home/doLoginWithApp/"; //台灣
    private boolean isTaiwan = true;

    private TextView tv1;
    private EditText etA, etP;
    private Button ivLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tv1 = (TextView) this.findViewById(R.id.tv1);
        ivLogin = (Button) this.findViewById(R.id.ivLogin);
        etA = (EditText) this.findViewById(R.id.etA);
        etP = (EditText) this.findViewById(R.id.etP);


        if (isTaiwan) {
            tv1.setText(R.string.app_name1);
            ivLogin.setText(R.string.Login1);
            etA.setHint(R.string.Account1);
            etP.setHint(R.string.Password1);
//            etA.setText("teacher");
//            etP.setText("123123");
        } else {
            tv1.setText(R.string.app_name);
            ivLogin.setText(R.string.Login);
            etA.setHint(R.string.Account);
            etP.setHint(R.string.Password);
//            etA.setText("EVA-G");
//            etP.setText("gxy1996");
        }
    }

    /**
     * 登入
     */
    public void mLogin(View view) {
        //
        CustomAsyncTask task = new CustomAsyncTask(this, "登入中..");
        task.callBack = new WebCallBack() {
            @Override
            public void callBack(String value) {
                Log.d("LOGIN", value);
//                Toast.makeText(LoginActivity.this, value, Toast.LENGTH_SHORT).show();
                /*
                {
                    "ResultMessage": "13gch0xityQ=",
                    "Status": true,
                    "DictionaryValue": null
                }
                 */
                try {
                    JSONObject object = new JSONObject(value);
                    if (object.getBoolean("Status")) {
                        startActivity(new Intent(LoginActivity.this, MainActivity.class)
                                .putExtra("URL", object.getString("ResultMessage")));
                    } else {
                        Toast.makeText(LoginActivity.this, object.getString("ResultMessage"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.d("LOGIN", e.getMessage());
                }
            }
        };
        task.execute("POST", isTaiwan ? URL_DOMAIN : URL_DOMAIN_cn, etA.getText().toString().trim(), etP.getText().toString().trim());
    }

}
