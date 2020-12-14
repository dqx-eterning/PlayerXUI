package com.example.playerxui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.playerxui.bean.User;
import com.xuexiang.xui.XUI;

import org.litepal.LitePal;
import org.litepal.tablemanager.Connector;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private String dbPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        XUI.initTheme(this);
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        TextView username = findViewById(R.id.et_username);
        TextView password = findViewById(R.id.et_password);
        Button login = findViewById(R.id.bt_go);

       /* Connector.getDatabase();
        User user = new User();
        user.setUsername("dangqx");
        user.setPassword("123456");
        user.save();*/

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = username.getText().toString();
                String pass = password.getText().toString();

                List<User> list = LitePal.select("password").where("username = ? ", name).find(User.class);
                for (User user1 : list) {
                    dbPassword = user1.getPassword();
                }
                //Log.d("用户名和密码", "onCreate: "+dbPassword);
                if (pass.equals(dbPassword)){
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "用户名或者密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}