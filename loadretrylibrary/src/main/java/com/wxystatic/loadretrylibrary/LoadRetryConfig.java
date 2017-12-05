package com.wxystatic.loadretrylibrary;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;

/**
 * Created by static on 2017/12/5/005.
 */

public class LoadRetryConfig {
    private  @ColorRes int background;
    private  @ColorRes int btnNormal;
    private  @ColorRes int btnPressed;
    private  @ColorRes int btnTextColor;
    private  @ColorRes int errorTextColor;
    private  @DrawableRes int gif;
    private int toolBarHeight;
    private  String btnText;
    private  String errorText;

    public int getToolBarHeight() {
        return toolBarHeight;
    }

    public void setToolBarHeight(int toolBarHeight) {
        this.toolBarHeight = toolBarHeight;
    }
    public Float getBtnRadius() {
        return btnRadius;
    }

    public void setBtnRadius(Float btnRadius) {
        this.btnRadius = btnRadius;
    }

    private Float btnRadius;

    public int getBackground() {
        return background;
    }

    public void setBackground(int background) {
        this.background = background;
    }

    public int getBtnNormal() {
        return btnNormal;
    }

    public void setBtnNormal(int btnNormal) {
        this.btnNormal = btnNormal;
    }

    public int getBtnPressed() {
        return btnPressed;
    }

    public void setBtnPressed(int btnPressed) {
        this.btnPressed = btnPressed;
    }

    public int getBtnTextColor() {
        return btnTextColor;
    }

    public void setBtnTextColor(int btnTextColor) {
        this.btnTextColor = btnTextColor;
    }

    public int getErrorTextColor() {
        return errorTextColor;
    }

    public void setErrorTextColor(int errorTextColor) {
        this.errorTextColor = errorTextColor;
    }

    public int getGif() {
        return gif;
    }

    public void setGif(int gif) {
        this.gif = gif;
    }

    public String getBtnText() {
        return btnText;
    }

    public void setBtnText(String btnText) {
        this.btnText = btnText;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }


}
