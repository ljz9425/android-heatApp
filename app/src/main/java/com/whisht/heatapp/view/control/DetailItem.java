package com.whisht.heatapp.view.control;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.whisht.heatapp.R;

public class DetailItem extends LinearLayout {

    private TextView tv_title;
    private TextView tv_value;
    private ImageView iv_image;
    private ImageView iv_setup;
    private String title, value;
    private Drawable image;
    private boolean isSetup;
    private ViewDetailClick viewDetailClick;
    public DetailItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        if (null == attrs) { return; }
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ComDetailItem);
        View view = View.inflate(context, R.layout.com_detail_item, this);
        tv_title = view.findViewById(R.id.com_di_tv_title);
        tv_value = view.findViewById(R.id.com_di_dv_value);
        iv_image = view.findViewById(R.id.com_di_iv_image);
        iv_setup = view.findViewById(R.id.com_di_iv_setup);
        title = typedArray.getString(R.styleable.ComDetailItem_title);
        value = typedArray.getString(R.styleable.ComDetailItem_value);
        image = typedArray.getDrawable(R.styleable.ComDetailItem_image);
        isSetup = typedArray.getBoolean(R.styleable.ComDetailItem_showSetup, false);
        if (null != title) {
            tv_title.setText(title);
        }
        if (null != value) {
            tv_value.setText(value);
        }
        if (null != image) {
            iv_image.setImageDrawable(image);
        }
        iv_setup.setVisibility(isSetup ? VISIBLE : INVISIBLE);
        iv_setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != viewDetailClick) {
                    viewDetailClick.onSetupClick(v);
                }
            }
        });
    }

    public final void setValue(CharSequence value) {
        tv_value.setText(value);
    }

    public String getValue() {
        return tv_value.getText().toString();
    }

    public void setOnSetupClickListener(DetailItem.ViewDetailClick viewDetailClick) {
        this.viewDetailClick = viewDetailClick;
    }

    public interface ViewDetailClick {
        void onSetupClick(View v);
    }
}
