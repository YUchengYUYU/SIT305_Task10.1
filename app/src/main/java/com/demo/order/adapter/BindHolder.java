package com.demo.order.adapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public class BindHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {
    private VB vb;
    public BindHolder(VB vb) {
        super(vb.getRoot());
        this.vb = vb;
    }

    public VB getVb() {
        return vb;
    }
}
