/**
 *
 * Copyright (c) 2015 Chutong Technologies All rights reserved.
 *
 */

/**
 * Version Control
 *
 * | version | date        | author         | description
 *   0.0.1     2015.11.30    TwoBirds     整理代码
 *
 */

package com.twobirds.sdk.component.custom.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.twobirds.sdk.component.R;

/**
 * 基础对话框
 *
 * @author TwoBirds
 * @version 0.0.1
 */
public class BasicDialog extends Dialog {

    public BasicDialog(Context context, int theme) {
        super(context, theme);
    }

    public BasicDialog(Context context) {
        super(context);
    }

    public BasicDialog(Context context, int xLeftTop, int yLeftTop, int theme) {
        super(context, theme);

        WindowManager.LayoutParams windowManagerLayoutParams = getWindow().getAttributes();

        windowManagerLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowManagerLayoutParams.x = xLeftTop;
        windowManagerLayoutParams.y = yLeftTop;

        windowManagerLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        windowManagerLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     *
     * Helper class for creating a custom dialog
     */
    public static class Builder {

        private Context context;

        private String title;

        private String message;

        private String positiveButtonText;

        private String negativeButtonText;

        private int width;
        private int height;
        private int xLeftTop;
        private int yLeftTop;

        private BasicDialog dialog;

        private View contentView;

        private OnClickListener

        positiveButtonClickListener, negativeButtonClickListener;

        public Builder(Context context) {
            this(context, -1, -1, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        }

        public Builder(Context context, int width, int height) {
            this(context, -1, -1, width, height);
        }

        public Builder(Context context, int xLeftTop, int yLeftTop, int width, int height) {
            this.context = context;
            this.xLeftTop = xLeftTop;
            this.yLeftTop = yLeftTop;
            this.width = width;
            this.height = height;
        }

        /**
         *
         * Set the Dialog message from String
         *
         * @param message
         *
         * @return
         */
        public Builder setMessage(String message) {
            this.message = message;
            return this;

        }

        /**
         *
         * Set the Dialog message from resource
         *
         * @param message
         *
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;

        }

        /**
         *
         * Set the Dialog title from resource
         *
         * @param title
         *
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;

        }

        /**
         *
         * Set the Dialog title from String
         *
         * @param title
         *
         * @return
         */
        public Builder setTitle(String title) {
            this.title = title;
            return this;

        }

        /**
         *
         * Set a custom content view for the Dialog.
         *
         * If a message is set, the contentView is not
         *
         * added to the Dialog...
         *
         * @param v
         *
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;

        }

        /**
         *
         * Set the positive button resource and it's listener
         *
         * @param positiveButtonText
         *
         * @param listener
         *
         * @return
         */
        public Builder setPositiveButton(int positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = (String) context.getText(positiveButtonText);
            this.positiveButtonClickListener = listener;
            return this;

        }

        /**
         *
         * Set the positive button text and it's listener
         *
         * @param positiveButtonText
         *
         * @param listener
         *
         * @return
         */
        public Builder setPositiveButton(String positiveButtonText, OnClickListener listener) {
            this.positiveButtonText = positiveButtonText;
            this.positiveButtonClickListener = listener;
            return this;

        }

        /**
         *
         * Set the negative button resource and it's listener
         *
         * @param negativeButtonText
         *
         * @param listener
         *
         * @return
         */
        public Builder setNegativeButton(int negativeButtonText, OnClickListener listener) {
            this.negativeButtonText = (String) context.getText(negativeButtonText);
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         *
         * Set the negative button text and it's listener
         *
         * @param negativeButtonText
         *
         * @param listener
         *
         * @return
         */
        public Builder setNegativeButton(String negativeButtonText, OnClickListener listener) {

            this.negativeButtonText = negativeButtonText;
            this.negativeButtonClickListener = listener;
            return this;
        }

        /**
         *
         * Create the custom dialog
         */
        public BasicDialog create() {

            // instantiate the dialog with the custom Theme
            if (-1 == xLeftTop || -1 == yLeftTop) {
                dialog = new BasicDialog(context, R.style.basic_dialog_style);
            } else {
                dialog = new BasicDialog(context, xLeftTop, yLeftTop, R.style.basic_dialog_style);
            }

            // 解决Dialog圆角出现黑色棱角的问题
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.basic_dialog, null);
            dialog.addContentView(layout, new LayoutParams(this.width, this.height));

            // set the dialog title  
            if (title != null) {
                ((TextView) layout.findViewById(R.id.title)).setText(title);
            } else {
                // if the dialog title just set the visibility to GONE  
                layout.findViewById(R.id.header).setVisibility(View.GONE);
            }

            boolean hasPositiveButton = false;
            boolean hasNegativeButton = false;
            // set the confirm button  
            if (positiveButtonText != null) {
                ((TextView) layout.findViewById(R.id.positiveButton)).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.positiveButton)).setText(positiveButtonText);
                ((TextView) layout.findViewById(R.id.positiveButton)).setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        if (positiveButtonClickListener != null) {

                            positiveButtonClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                        }
                        //						dialog.cancel();

                    }

                });
                hasPositiveButton = true;
            } else {
                // if no confirm button just set the visibility to GONE  
                layout.findViewById(R.id.positiveButton).setVisibility(View.GONE);
                hasPositiveButton = false;
            }

            // set the cancel button  
            if (negativeButtonText != null) {
                ((TextView) layout.findViewById(R.id.negativeButton)).setVisibility(View.VISIBLE);
                ((TextView) layout.findViewById(R.id.negativeButton)).setText(negativeButtonText);
                ((TextView) layout.findViewById(R.id.negativeButton)).setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {

                        if (negativeButtonClickListener != null) {
                            negativeButtonClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                        }
                        //						dialog.cancel();
                    }
                });
                hasNegativeButton = true;
            } else {
                // if no confirm button just set the visibility to GONE  
                layout.findViewById(R.id.negativeButton).setVisibility(View.GONE);
                hasNegativeButton = false;
            }
//            if(hasNegativeButton && hasPositiveButton) {
//                layout.findViewById(R.id.btn_middle_line).setVisibility(View.VISIBLE);
//            } else {
//                layout.findViewById(R.id.btn_middle_line).setVisibility(View.GONE);
//            }
//            if(!hasNegativeButton && !hasPositiveButton) {
//                layout.findViewById(R.id.btn_top_line).setVisibility(View.GONE);
//            } else {
//                layout.findViewById(R.id.btn_top_line).setVisibility(View.VISIBLE);
//            }

            // set the content message  
            if (message != null) {
                final TextView message_tv = (TextView) layout.findViewById(R.id.message);
                message_tv.setMovementMethod(ScrollingMovementMethod.getInstance());
                message_tv.setText(message);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        int lineCount = message_tv.getLineCount();
                        if(lineCount > 1) {
                            message_tv.setGravity(Gravity.LEFT);
                        }
                    }
                }, 100);
            } else if (contentView != null) {
                // if no message set  
                // add the contentView to the dialog body  
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
            } else {
                // if no message and no contentView set
                // hide the content layout
                ((LinearLayout) layout.findViewById(R.id.content)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.content)).setVisibility(View.GONE);
            }

            return dialog;

        } // create()

    }

}