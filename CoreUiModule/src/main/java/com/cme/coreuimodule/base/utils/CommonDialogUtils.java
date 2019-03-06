package com.cme.coreuimodule.base.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cme.corelib.utils.UiUtil;
import com.cme.coreuimodule.base.dialog.CustomDialog;
import com.common.coreuimodule.R;

/**
 * Created by klx on 2017/9/5.
 * 通用弹窗
 */

public class CommonDialogUtils {
    public static void showSetPermissionDialog(final Activity activity, String title) {
        new AlertDialog.Builder(activity)
                .setCancelable(true)
                .setTitle(title)
                .setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(activity.getString(R.string.goSetting), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = UiUtil.getAppDetailSettingIntent(activity);
                        activity.startActivity(intent);
                    }
                }).show();
    }

    public static void showSetPermissionDialog(final Activity activity, String title,
                                               final DialogInterface.OnClickListener cancelClick,
                                               final DialogInterface.OnClickListener confirmClick) {
        new AlertDialog.Builder(activity)
                .setCancelable(true)
                .setTitle(title)
                .setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (cancelClick != null) {
                            cancelClick.onClick(dialog, which);
                        }
                    }
                })
                .setPositiveButton(activity.getString(R.string.goSetting), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (confirmClick != null) {
                            confirmClick.onClick(dialog, which);
                            return;
                        }
                        Intent intent = UiUtil.getAppDetailSettingIntent(activity);
                        activity.startActivity(intent);
                    }
                }).show();
    }

    public static AlertDialog showTitleDialog(final Activity activity, String title,
                                              final DialogInterface.OnClickListener cancelClick,
                                              final DialogInterface.OnClickListener confirmClick) {
        return new AlertDialog.Builder(activity)
                .setCancelable(true)
                .setTitle(title)
                .setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (cancelClick != null) {
                            cancelClick.onClick(dialog, which);
                        }
                    }
                })
                .setPositiveButton(activity.getString(R.string.goSetting), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = UiUtil.getAppDetailSettingIntent(activity);
                        activity.startActivity(intent);
                        if (confirmClick != null) {
                            confirmClick.onClick(dialog, which);
                        }
                    }
                }).show();
    }

    public static void setDialogSize(Activity context, float widthProportion, float heightProportion,
                                     View rootView) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenHeigh = dm.heightPixels;
        int screenWidth = dm.widthPixels;

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) rootView.getLayoutParams();
        if (heightProportion >= 0) {
            layoutParams.height = (int) (screenHeigh * heightProportion);// 高度设置为屏幕高度比
        }
        layoutParams.width = (int) (screenWidth * widthProportion);// 宽度设置为屏幕的宽度比
        rootView.setLayoutParams(layoutParams);
    }

    public static AlertDialog createDialog(Context context, View rootView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.default_dialog_style);
        builder.setView(rootView);
        builder.setCancelable(true);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setGravity(Gravity.CENTER);
        alertDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        return alertDialog;
    }

    /**
     * @param first            第一个显示的文本
     * @param second           第二个显示的文本
     * @param context
     * @param oneClickListener 第一个点击的监听
     * @param twoClickListener 第二个点击的监听
     */
    public static void showBottomChooseDialog(int first, int second, Activity context,
                                              final View.OnClickListener oneClickListener, final View.OnClickListener twoClickListener) {
        View pview = LayoutInflater.from(context).inflate(
                R.layout.layout_choose_photo_select, null);
        TextView tv_pick_from_take = (TextView) pview
                .findViewById(R.id.tv_pick_from_take);
        TextView tv_pick_from_dicm = (TextView) pview
                .findViewById(R.id.tv_pick_from_dicm);
        TextView tv_show_cancel = (TextView) pview
                .findViewById(R.id.tv_show_cancel);

        tv_pick_from_take.setText(first);
        if (-1 != second) {
            tv_pick_from_dicm.setVisibility(View.VISIBLE);
            tv_pick_from_dicm.setText(second);
        } else {
            tv_pick_from_dicm.setVisibility(View.GONE);
        }

        final CustomDialog builder = new CustomDialog(context, R.style.my_dialog)
                .create(pview, true, 1f, 0.25f, 1);
        builder.show();

        tv_show_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        tv_pick_from_take.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (oneClickListener != null) {
                    oneClickListener.onClick(v);
                }
                builder.dismiss();
            }
        });
        tv_pick_from_dicm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (twoClickListener != null) {
                    twoClickListener.onClick(v);
                }
                builder.dismiss();
            }
        });
        tv_show_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
    }

    /**
     * @param first            第一个显示的文本
     * @param second           第二个显示的文本
     * @param context
     * @param oneClickListener 第一个点击的监听
     * @param twoClickListener 第二个点击的监听
     */
    public static void showBottomChooseDialog(String first, String second, Activity context, final View.OnClickListener oneClickListener, final View.OnClickListener twoClickListener) {
        View pview = LayoutInflater.from(context).inflate(
                R.layout.layout_choose_photo_select, null);
        TextView tv_pick_from_take = (TextView) pview
                .findViewById(R.id.tv_pick_from_take);
        TextView tv_pick_from_dicm = (TextView) pview
                .findViewById(R.id.tv_pick_from_dicm);
        TextView tv_show_cancel = (TextView) pview
                .findViewById(R.id.tv_show_cancel);

        tv_pick_from_take.setText(first);
        if (!TextUtils.isEmpty(second)) {
            tv_pick_from_dicm.setVisibility(View.VISIBLE);
            tv_pick_from_dicm.setText(second);
        } else {
            tv_pick_from_dicm.setVisibility(View.GONE);
        }

        final CustomDialog builder = new CustomDialog(context, R.style.my_dialog)
                .create(pview, true, 1f, 0.25f, 1);
        builder.show();

        tv_show_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        tv_pick_from_take.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (oneClickListener != null) {
                    oneClickListener.onClick(v);
                }
                builder.dismiss();
            }
        });
        tv_pick_from_dicm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (twoClickListener != null) {
                    twoClickListener.onClick(v);
                }
                builder.dismiss();
            }
        });
        tv_show_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
    }

    public static void showBottomChooseDialog(String first, Activity context, final View.OnClickListener oneClickListener) {
        View pview = LayoutInflater.from(context).inflate(
                R.layout.dialog_one, null);
        TextView tv_pick_from_take = (TextView) pview
                .findViewById(R.id.tv_pick_from_take);

        TextView tv_show_cancel = (TextView) pview
                .findViewById(R.id.tv_show_cancel);

        tv_pick_from_take.setText(first);

        final CustomDialog builder = new CustomDialog(context, R.style.my_dialog)
                .create(pview, true, 1f, 0.2f, 1);
        builder.show();

        tv_show_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        tv_pick_from_take.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (oneClickListener != null) {
                    oneClickListener.onClick(v);
                }
                builder.dismiss();
            }
        });
        tv_show_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
    }

    public static void showBottomChooseDialog(Activity context, final View.OnClickListener oneClickListener, final View.OnClickListener twoClickListener, final View.OnClickListener thirdClickListener) {
        View pview = LayoutInflater.from(context).inflate(
                R.layout.layout_more_select, null);
        TextView tv_send_friend = (TextView) pview
                .findViewById(R.id.tv_send_friend);
        TextView tv_favorites = (TextView) pview
                .findViewById(R.id.tv_favorites);
        TextView tv_backup_pic = (TextView) pview
                .findViewById(R.id.tv_backup_pic);

        tv_send_friend.setText(R.string.send_friend);
        tv_favorites.setText(R.string.favorites);
        tv_backup_pic.setText(R.string.backup_pic);

        final CustomDialog builder = new CustomDialog(context, R.style.my_dialog)
                .create(pview, true, 1f, 0.25f, 1);
        builder.show();


        tv_send_friend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (oneClickListener != null) {
                    oneClickListener.onClick(v);
                }
                builder.dismiss();
            }
        });
        tv_favorites.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (twoClickListener != null) {
                    twoClickListener.onClick(v);
                }
                builder.dismiss();
            }
        });
        tv_backup_pic.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (thirdClickListener != null) {
                    thirdClickListener.onClick(v);
                }
                builder.dismiss();
                builder.dismiss();
            }
        });
    }

    /**
     * @param first            第一个显示的文本
     * @param second           第二个显示的文本
     * @param context
     * @param oneClickListener 第一个点击的监听
     * @param twoClickListener 第二个点击的监听
     */
    public static void showBottomChooseDialogCenter(String first, String second, boolean isPremit, Activity context, final View.OnClickListener oneClickListener, final View.OnClickListener twoClickListener) {
        View pview = LayoutInflater.from(context).inflate(
                R.layout.layout_choose_third_select, null);
        TextView tv_one = (TextView) pview
                .findViewById(R.id.tv_one);
        TextView tv_two = (TextView) pview
                .findViewById(R.id.tv_two);

        TextView tv_show_cancel = (TextView) pview
                .findViewById(R.id.tv_show_cancel);
        tv_one.setText(first);
        tv_two.setText(second);

        float heightPercent;
        if (isPremit) {
            tv_two.setVisibility(View.VISIBLE);
            heightPercent = 0.24f;
        } else {
            tv_two.setVisibility(View.GONE);
            heightPercent = 0.16f;
        }
        final CustomDialog builder = new CustomDialog(context, R.style.my_dialog)
                .create(pview, true, 1f, heightPercent, 1);
        builder.show();

        tv_show_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });

        tv_one.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (oneClickListener != null) {
                    oneClickListener.onClick(v);
                }
                builder.dismiss();
            }
        });
        tv_two.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (twoClickListener != null) {
                    twoClickListener.onClick(v);
                }
                builder.dismiss();
            }
        });

        tv_show_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
    }

    /**
     * 选择图片的弹框
     *
     * @param context
     * @param onTackFromCamera 拍照
     * @param onPickFromDic    从相册中选择
     */
    public static void showChoosePicDialog(Activity context, final View.OnClickListener onTackFromCamera, final View.OnClickListener onPickFromDic) {
        showBottomChooseDialog(R.string.pic_tack_from_album, R.string.pic_tack_camera, context, onPickFromDic, onTackFromCamera);
    }

    /**
     * 显示加载进度
     *
     * @param message
     */
    public static AlertDialog getProgressDialog(Activity context, String message) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.layout_progressing, null);
        TextView tv_message = (TextView) rootView.findViewById(R.id.tv_message);
        tv_message.setText(message);
        AlertDialog alertDialog = createDialog(context, rootView);
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    /**
     * 显示确认弹窗
     *
     * @param activity
     * @param message        提示消息
     * @param onConfirmClick 确认点击监听
     */
    public static void showConfirmDialog(Activity activity, String leftText, String rightText, String message, final View.OnClickListener onConfirmClick) {
        showConfirmDialog(activity, leftText, rightText, message, null, onConfirmClick);
    }

    /**
     * 显示确认弹窗
     *
     * @param activity
     * @param message        提示消息
     * @param onConfirmClick 确认点击监听
     */
    public static void showConfirmDialog(Activity activity, String leftText, String rightText, String message, final View.OnClickListener onCancelClick, final View.OnClickListener onConfirmClick) {
        final CustomDialog dialog = new CustomDialog(activity, R.style.Dialog);
        View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_confirm, null);
        dialog.addContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tv_message = (TextView) layout.findViewById(R.id.tv_message);
        TextView tv_cancel = (TextView) layout.findViewById(R.id.tv_cancel);
        TextView tv_confirm = (TextView) layout.findViewById(R.id.tv_confirm);

        tv_message.setText(message);
        tv_cancel.setText(leftText);
        tv_confirm.setText(rightText);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onCancelClick != null) {
                    onCancelClick.onClick(v);
                }
            }
        });

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onConfirmClick != null) {
                    onConfirmClick.onClick(v);
                }
            }
        });
        dialog.show();
    }

    /**
     * 显示确认弹窗
     *
     * @param activity
     * @param message        提示消息
     * @param onConfirmClick 确认点击监听
     */
    public static void showConfirmDialog(Activity activity, String message, final View.OnClickListener onConfirmClick) {
        showConfirmDialog(activity, activity.getString(R.string.cancel), activity.getString(R.string.confirm), message, onConfirmClick);
    }

        /**
     * 显示确认弹窗
     *
     * @param activity
     * @param message        提示消息
     * @param onConfirmClick 确认点击监听
     */
    public static void showConfirmDialog(Activity activity, String message,final View.OnClickListener onCancelClick, final View.OnClickListener onConfirmClick) {
        showConfirmDialog(activity, activity.getString(R.string.cancel), activity.getString(R.string.confirm), message, onCancelClick,onConfirmClick);
    }

    /**
     * 显示确认弹窗
     *
     * @param activity
     * @param message        提示消息
     * @param onConfirmClick 确认点击监听
     */
    public static void showOnlyConfirmDialog(Activity activity, String message, final View.OnClickListener onConfirmClick) {
        final CustomDialog dialog = new CustomDialog(activity, R.style.Dialog);
        View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_only_confirm, null);
        dialog.addContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tv_message = (TextView) layout.findViewById(R.id.tv_message);
        TextView tv_confirm = (TextView) layout.findViewById(R.id.tv_confirm);

        tv_message.setText(message);

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onConfirmClick != null) {
                    onConfirmClick.onClick(v);
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 强制下线
     *
     * @param activity
     * @param message        提示消息
     * @param onConfirmClick 确认点击监听
     */
    public static void loginExit(Activity activity, String message, final View.OnClickListener onConfirmClick) {
        final CustomDialog dialog = new CustomDialog(activity, R.style.Dialog);
        View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_only_confirm, null);
        dialog.addContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tv_message = (TextView) layout.findViewById(R.id.tv_message);
        tv_message.setTextSize(16);
        TextView tv_confirm = (TextView) layout.findViewById(R.id.tv_confirm);

        tv_message.setText(message);

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onConfirmClick != null) {
                    onConfirmClick.onClick(v);
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }


    public static void call(Activity activity, String message, final View.OnClickListener onConfirmClick) {
        final CustomDialog dialog = new CustomDialog(activity, R.style.Dialog);
        View layout = LayoutInflater.from(activity).inflate(R.layout.dialog_layout_only_confirm, null);
        dialog.addContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TextView tv_message = (TextView) layout.findViewById(R.id.tv_message);
        tv_message.setTextSize(16);
        TextView tv_confirm = (TextView) layout.findViewById(R.id.tv_confirm);

        tv_message.setText(message);

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onConfirmClick != null) {
                    onConfirmClick.onClick(v);
                }
            }
        });
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

}
