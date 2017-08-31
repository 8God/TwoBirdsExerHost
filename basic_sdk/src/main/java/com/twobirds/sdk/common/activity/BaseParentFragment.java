/**
 *
 * Copyright (c) 2015 Chutong Technologies All rights reserved.
 *
 */

/**
 * Version Control
 *
 * | version | date        | author         | description
 *   0.0.1     2015.11.30    TwoBirds         整理代码
 *
 */

package com.twobirds.sdk.common.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Fragment基类
 *
 * @author TwoBirds
 * @version 0.0.1
 * @since
 */
public abstract class BaseParentFragment extends Fragment {

    private Bundle backResult;

    private FragmentChangeListener mFragmentChangeListener;

    public void setBackResult(Bundle backResult) {
        this.backResult = backResult;
    }

    public Bundle getBackResult() {
        return backResult;
    }

    @Override
    final public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    final public void onResume() {
        super.onResume();

    }

    @Override
    final public void onStop() {
        super.onStop();

    }

    /**
     * 获取控件实例
     *
     * @param layoutView View容器
     * @param viewId  控件id
     * @since 0.0.1
     */
    protected <T extends View> T findView(View layoutView, int viewId) {
        return (T) layoutView.findViewById(viewId);
    }

    /**
     * 重定向Fragment
     *
     * @param toFragment 目标fragment
     * @see #redirect(Fragment, boolean)
     * @since 0.0.1
     */
    protected void redirect(final Fragment toFragment) {
        redirect(toFragment, true);
    }

    protected void redirect(final Fragment toFragment, final boolean isBackStackSupported) {
        IFragmentRedirecter fragmentRedirector = (IFragmentRedirecter) getActivity();
        fragmentRedirector.redirect(toFragment, isBackStackSupported);
    }

    protected void popBack(Bundle backFragmentArgs) {
        IFragmentRedirecter fragmentRedirector = (IFragmentRedirecter) getActivity();
        if (fragmentRedirector != null) {
            fragmentRedirector.popBack(backFragmentArgs);
        }
    }

    protected void updateMessageNO(int messageNO) {

    }

    public void onSoftInputKeyDown(int keyCode) {

    }

    public void onBackPressed() {

    }

    /**
     * 返回true,执行fragment的onKeyDown事件，阻断Activity的onKeyDown事件
     * 
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    protected void showToast(final CharSequence text) {
        if (null != text && 0 != text.toString().trim().length()) {
            Toast toast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
            //            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } // if (null != text && 0 != text.toString().trim().length())
    }

    public void setOnFragmentChangeListener(FragmentChangeListener mFragmentChangeListener) {
        this.mFragmentChangeListener = mFragmentChangeListener;
    }

    public void performFragmentChange(BaseParentFragment to) {
        if (null != mFragmentChangeListener) {
            mFragmentChangeListener.changeFragment(to);
        }
    }

    public void performFragmentChange(BaseParentFragment from, BaseParentFragment to) {
        if (null != mFragmentChangeListener) {
            mFragmentChangeListener.changeFragment(from, to);
        }
    }

    /**
     * 重定向Framgnet监听接口
     */
    public interface FragmentChangeListener {
        public void changeFragment(BaseParentFragment to);

        public void changeFragment(BaseParentFragment from, BaseParentFragment to);
    }

}
