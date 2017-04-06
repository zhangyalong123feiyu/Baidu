package com.bibi.Baidu.acitivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.bibi.Baidu.Appliction.Constant;
import com.bibi.Baidu.Appliction.MyTextWatcher;
import com.bibi.Baidu.Bean.LoginResult;
import com.bibi.Baidu.R;
import com.bibi.Baidu.http.httpconstant;
import com.bibi.Baidu.utils.SharedPresUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by bibinet on 2016/10/28.
 */

public class LoginActivity extends BaseActivity {
    @ViewInject(R.id.image_left)
    private ImageView image_left;
  /*  @ViewInject(R.id.image_right)
    private ImageView image_right;*/
    @ViewInject(R.id.location)
    private Button location;
    @ViewInject(R.id.regist)
    private Button regist;
    @ViewInject(R.id.headpic)
    private ImageView headpic;
    @ViewInject(R.id.count)
    private EditText editcount;
    @ViewInject(R.id.password)
    private EditText editpassword;
    @ViewInject(R.id.login)
    private Button login;
    @ViewInject(R.id.isremerpassword)
    private CheckBox isremberpassword;
    private SharedPresUtils shpreference;
    private ProgressDialog dialog;
    private edtextwatcher edwatcher=new edtextwatcher();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        x.view().inject(this);
        initdialog();
        ImageOptions options=new ImageOptions.Builder().setFailureDrawableId(R.mipmap.login_head).setUseMemCache(true).setCircular(true).build();
        x.image().bind(headpic,"http://b.hiphotos.baidu.com/image/pic/item/d009b3de9c82d15825ffd75c840a19d8bd3e42da.jpg",options);
        setlistioner();
        dologin();
    }

    private void initdialog() {
        dialog=new ProgressDialog(this);
        dialog.setTitle("登录");
        dialog.setCanceledOnTouchOutside(false);
        dialog.setMessage("正在登录。。。");
        dialog.show();
    }

    private void dologin() {
        editcount.addTextChangedListener(edwatcher);
        editpassword.addTextChangedListener(edwatcher);
        final String acount=editcount.getText().toString().trim();
        final String password=editpassword.getText().toString().trim();
        final boolean isremember = isremberpassword.isChecked();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loging(acount,password,isremember);
            }
        });
    }
    private void loging(final String acount, final String password, final boolean isremember) {
        String url= httpconstant.baseurl+"/user/to/user/login";
        RequestParams params=new RequestParams();
        params.addBodyParameter("mobile",acount);
        params.addBodyParameter("password",password);
        params.addBodyParameter("version", LoginActivity.this.getString(R.string.appversion));
        params.setMaxRetryCount(1);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String s) {
                dialog.dismiss();
                Gson gson=new Gson();
                LoginResult info = gson.fromJson(s, LoginResult.class);
                int flage = info.getFlag();

                switch (flage) {
                		case 10000:
                           Constant.userInfo =info.userinfo;
                            Constant.userInfo.account=acount;
                            if(isremember){
                            shpreference.saveUserInfo(true,true,acount,password);
                            }else{
                                shpreference.saveUserInfo(false,true,acount,password);
                            }
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finshActivity();
                			break;

                		default:
                			break;
                		}

            }

            @Override
            public void onError(Throwable throwable, boolean b) {

            }

            @Override
            public void onCancelled(CancelledException e) {

            }

            @Override
            public void onFinished() {

            }
        });

    }
    private void setlistioner() {
    regist.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(LoginActivity.this,RegistActivity.class));
        }
    });
    }
 class edtextwatcher extends MyTextWatcher{
     @Override
     public void afterTextChanged(Editable s) {
          String acount=editcount.getText().toString().trim();
          String password=editpassword.getText().toString().trim();
         if (acount.length()>0&&password.length()>0){
             login.setEnabled(true);
         }else {
             login.setEnabled(false);
         }
     }
 }

}
