package com.bibi.Baidu.acitivity;

import android.os.Bundle;

import com.bibi.Baidu.R;

import org.xutils.view.annotation.ContentView;
import org.xutils.x;

/**
 * Created by bibinet on 2016/10/28.
 */
@ContentView(R.layout.activity_regist)
public class RegistActivity extends BaseActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
    }
}
