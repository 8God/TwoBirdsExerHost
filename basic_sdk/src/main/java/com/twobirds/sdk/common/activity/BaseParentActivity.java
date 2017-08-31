package com.twobirds.sdk.common.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Stack;

/**
 * | version | date        | author         | description
 *   0.0.1     2015.11.30    TwoBirds         整理代码
 *   0.0.2     2015.12.23    TwoBirds         添加getContext()、findView()方法
 *
 * Activity基类
 *
 * @author TwoBirds
 * @version 0.0.2
 * @since
 *
 */
public abstract class BaseParentActivity extends ActionBarActivity implements IFragmentRedirecter, IMessageNOUpdater {

    public static final String BACK_ACTIVITY_CLASS_NAME_KEY = "BACK_ACTIVITY_CLASS_NAME_KEY";

    private Stack<String> fragmentNameStack;

    private static final int DEFAULT_FRAGMENT_VIEW_CONTAINER_ID = 0;

    private String backActivityClassName = null;

    /**
     * 获得回退Activity的类名
     *
     * @return Activity的类名
     * @see #backActivityClassName
     * @since 0.0.1
     */
    public String getBackActivityClassName() {
        return backActivityClassName;
    }

    protected int getFragmentViewContainerId() {
        return DEFAULT_FRAGMENT_VIEW_CONTAINER_ID;
    }

    @Override
    public void updateTabMessageNO(int messageNO) {

    }

    /**
     * 重定向Fragment
     *
     * @param fragment 需要跳转的目标fragment
     * @param isBackStackSupported 是否支持回退
     * @see #changeFragment(Fragment, boolean)
     * @since 0.0.1
     */
    @Override
    public void redirect(Fragment fragment, boolean isBackStackSupported) {
        changeFragment(fragment, isBackStackSupported);
    }

    @Override
    public void onBackPressed() {
        popBack(null);
    }

    @Override
    public void popBack(Bundle backFragmentArgs) {

        boolean isLastFragment = true;

        if (null != fragmentNameStack && fragmentNameStack.size() >= 2) {
            fragmentNameStack.pop();
            if (null != backFragmentArgs) {
                BaseParentFragment backFragment = (BaseParentFragment) getSupportFragmentManager().findFragmentByTag(fragmentNameStack.peek());
                Bundle backResult = backFragment.getBackResult();
                if (null == backResult) {
                    backResult = backFragmentArgs;
                } else {
                    backResult.putAll(backFragmentArgs);
                }
                backFragment.setBackResult(backResult);
            }

            getSupportFragmentManager().popBackStack();

            isLastFragment = false;
        } // if (null != fragmentNameStack && fragmentNameStack.size() >= 2)

        if (isLastFragment) {
            finish();
        }
    }

    protected void changeFragment(final Fragment fragment) {
        changeFragment(fragment, true);
    }

    protected void changeFragment(final Fragment fragment, final boolean isBackStackSupported) {

        final int fragmentViewContainerId = getFragmentViewContainerId();
        if (fragmentViewContainerId != DEFAULT_FRAGMENT_VIEW_CONTAINER_ID) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(fragmentViewContainerId, fragment, fragment.getClass().getName());
            if (isBackStackSupported) {
                if (null == fragmentNameStack) {
                    fragmentNameStack = new Stack<String>();
                }

                if (fragmentNameStack.isEmpty() || !fragment.getClass().getName().equals(fragmentNameStack.peek())) {
                    fragmentNameStack.push(fragment.getClass().getName());
                    fragmentTransaction.addToBackStack(null);
                }
            }
            fragmentTransaction.commit();
        } // if (fragmentViewContainerId != DEFAULT_FRAGMENT_VIEW_CONTAINER_ID)

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if (null != intent) {
            backActivityClassName = intent.getStringExtra(BACK_ACTIVITY_CLASS_NAME_KEY);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

        if (null != intent) {
            backActivityClassName = intent.getStringExtra(BACK_ACTIVITY_CLASS_NAME_KEY);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        //        MyApplication.getInstance(BaseActivity.this).setRunningForeground();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

        //        MyApplication.getInstance(BaseActivity.this).setRunningBackground();
    }

    protected void showToast(final CharSequence text) {
        if (null != text && 0 != text.toString().trim().length()) {
            Toast toast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
            //            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } // if (null != text && 0 != text.toString().trim().length())
    }

    protected void showLongToast(final CharSequence text) {
        if (null != text && 0 != text.toString().trim().length()) {
            Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
            //            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        } // if (null != text && 0 != text.toString().trim().length())
    }

    @Override
    public void finish() {
        if (null != backActivityClassName) {
            Class backActivityClass = null;
            try {
                backActivityClass = Class.forName(backActivityClassName);
            } catch (ClassNotFoundException e) {
            }
            if (null != backActivityClass) {
                Intent openBackActivityIntent = new Intent(this, backActivityClass);
                openBackActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(openBackActivityIntent);
                //                overridePendingTransition(R.anim.anim_left_in, R.anim.anim_right_out);
            } // if (null != backActivityClass)
        } else {
            try {

            } catch (UnsupportedOperationException e) {
                Log.e("BaseActivity", "throws UnsupportedOperationException");
            }
            super.finish();
        }
    }

    protected Context getContext() {
        return this;
    }

    protected <T extends View> T findView(int viewId) {
        return (T) findViewById(viewId);
    }

    protected <T extends View> T findView(View layout, int viewId) {
        return (T) layout.findViewById(viewId);
    }

}
