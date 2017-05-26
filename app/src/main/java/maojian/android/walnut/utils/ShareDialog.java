package maojian.android.walnut.utils;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.*;
import android.widget.TextView;
import maojian.android.walnut.R;

/**
 * @author hezuzhi
 * @Description: ()
 * @date 2016/6/20  12:19.
 * @version: 1.0
 */
public class ShareDialog {
    private Dialog dialog;
    private Context mContext;
    private Display display;
    private TextView txt_cancel;
    private confirmListener mylistener;
    private View line;
    private TextView txt_report;
    public boolean reportV;

    public ShareDialog(Context context) {
        this.mContext = context;
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public ShareDialog builder() {
        // 获取Dialog布局
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.share_dialog, null);

        // 设置Dialog最小宽度为屏幕宽度
        view.setMinimumWidth((int) (display.getWidth() * 0.95));

        // 获取自定义Dialog布局中的控件
        line = (View) view.findViewById(R.id.lineview);
        txt_report = (TextView) view.findViewById(R.id.txt_report);

        if (reportV) {
            txt_report.setText("Delete");
            txt_report.setVisibility(View.VISIBLE);
            line.setVisibility(View.VISIBLE);
        }else {
            txt_report.setVisibility(View.GONE);
            line.setVisibility(View.GONE);
        }
        txt_cancel = (TextView) view.findViewById(R.id.txt_cancel);
        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        view.findViewById(R.id.rl_weixin_fri).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (mylistener != null) {
                    mylistener.onClick(0);
                }
            }
        });
        view.findViewById(R.id.rl_weixin_py).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (mylistener != null) {
                    mylistener.onClick(1);
                }
            }
        });

        view.findViewById(R.id.rl_weibo0).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (mylistener != null) {
                    mylistener.onClick(2);
                }
            }
        });
        view.findViewById(R.id.rl_weibo).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (mylistener != null) {
                    mylistener.onClick(3);
                }
            }
        });

        view.findViewById(R.id.txt_report).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (mylistener != null) {
                    mylistener.onClick(4);
                }
            }
        });

        // 定义Dialog布局和参数
        dialog = new Dialog(mContext, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER | Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);

        return this;
    }

    public ShareDialog setCancelable(boolean cancel) {
        dialog.setCancelable(cancel);
        return this;
    }

    public ShareDialog setCanceledOnTouchOutside(boolean cancel) {
        dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void show() {
        dialog.show();
    }

    public void setConfim(confirmListener listener) {
        this.mylistener = listener;
    }

    public void setRepotrGone() {

        reportV = true;
    }

    public interface confirmListener {
        void onClick(int item);
    }

}