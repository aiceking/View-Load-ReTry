package com.aiceking.viewloadretrylibrary.viewmodel;

import android.view.ViewGroup;

public class ContentViewModel {
    private int index;

    public ContentViewModel(int index, ViewGroup viewGroup) {
        this.index = index;
        this.viewGroup = viewGroup;
    }

    private ViewGroup viewGroup;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public ViewGroup getViewGroup() {
        return viewGroup;
    }

    public void setViewGroup(ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
    }
}
