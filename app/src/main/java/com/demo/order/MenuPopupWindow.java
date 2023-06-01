package com.demo.order;

import android.content.res.Resources;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.demo.order.databinding.PopupwindowMenuBinding;

public class MenuPopupWindow extends PopupWindow {
    public MenuPopupWindow(View view) {
        super(view, ViewGroup.LayoutParams.MATCH_PARENT, 0, true);
        setHeight(Resources.getSystem().getDisplayMetrics().heightPixels - dp2px(50));
    }

    private int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().getDisplayMetrics());
    }
}
