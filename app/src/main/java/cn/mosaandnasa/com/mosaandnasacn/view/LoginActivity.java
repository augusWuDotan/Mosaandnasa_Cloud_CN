package cn.mosaandnasa.com.mosaandnasacn.view;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Map;

import cn.mosaandnasa.com.mosaandnasacn.R;
import cn.mosaandnasa.com.mosaandnasacn.model.CustomAsyncTask;
import cn.mosaandnasa.com.mosaandnasacn.model.WebCallBack;

public class LoginActivity extends AppCompatActivity {
    private String URL_DOMAIN = "";
    private TextView tv1;
    private EditText etA, etP;
    private Button ivLogin;
    private Switch save;//切換是否紀錄

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        URL_DOMAIN = getString(R.string.domain) + "/Home/doLoginWithApp/";
        tv1 = (TextView) this.findViewById(R.id.tv1);
        ivLogin = (Button) this.findViewById(R.id.ivLogin);
        etA = (EditText) this.findViewById(R.id.etA);
        etP = (EditText) this.findViewById(R.id.etP);
        save = (Switch) this.findViewById(R.id.save);

        tv1.setText(R.string.app_name);
        ivLogin.setText(R.string.Login);
        etA.setHint(R.string.Account);
        etP.setHint(R.string.Password);
        save.setText(R.string.save);
        //etA.setText("teacher");
        //etP.setText("2016mosa06");
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
                        //
                        saveAccount(etA.getText().toString().trim(), etP.getText().toString().trim(), save.isChecked());//儲存帳密
                        //
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
        task.execute("POST", URL_DOMAIN, etA.getText().toString().trim(), etP.getText().toString().trim());
    }

    /**
     * 紀錄帳密
     */
    private void saveAccount(String account, String password, boolean isSave) {
        SharedPreferences preferences = getSharedPreferences("Mosaandnasa_Teacher", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("ACCOUNT", account);
        editor.putString("PASSWORD", password);
        editor.putBoolean("SAVE", isSave);
        editor.commit();
    }

    /**
     * 取得帳密
     *
     * @return
     */
    private Map<String, ?> getAccount() {
        Log.d("LOGIN", "UserAccount getAccount");
        SharedPreferences preferences = getSharedPreferences("Mosaandnasa_Teacher", Activity.MODE_PRIVATE);
        return preferences.getAll();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Map<String, ?> UserAccount = getAccount();
        if (UserAccount == null) {

        } else {
            boolean isSave = UserAccount.get("SAVE") == null ? false : (Boolean) UserAccount.get("SAVE");
            if (isSave) {
                etA.setText((String) UserAccount.get("ACCOUNT"));
                etP.setText((String) UserAccount.get("PASSWORD"));
                save.setChecked(true);
            } else {
                etA.setText("");
                etP.setText("");
                save.setChecked(false);
            }
        }
    }
}
