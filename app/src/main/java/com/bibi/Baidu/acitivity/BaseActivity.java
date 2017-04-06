package com.bibi.Baidu.acitivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.bibi.Baidu.R;

import org.xutils.view.annotation.ContentView;

/**
 * Created by bibinet on 2016/10/28.
 */
public class BaseActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        overridePendingTransition(R.anim.tr_entry,R.anim.tr_void);
    }
    public  void finshActivity(){
        super.finish();
        overridePendingTransition(R.anim.tr_void,R.anim.tr_exit);

    }
    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && event.getRepeatCount() == 0) {
                super.finish();
                overridePendingTransition(R.anim.tr_void, R.anim.tr_exit);
            }
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
        overridePendingTransition(R.anim.tr_entry,R.anim.tr_void);
    }


}
