package com.whisht.heatapp.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.whisht.heatapp.R;

public class LoadingDialog extends AlertDialog {
    ImageView ivLoading;
    public LoadingDialog(Context context) {
        super(context, R.style.dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        ivLoading = findViewById(R.id.iv_loading);
        Glide.with(getContext()).load(R.drawable.ico_loading).into(ivLoading);
    }

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.CENTER;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.dimAmount = 0.0f;
        getWindow().getDecorView().setPadding(0,0,0,0);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#50000000"));
        getWindow().setAttributes(layoutParams);
    }
}
