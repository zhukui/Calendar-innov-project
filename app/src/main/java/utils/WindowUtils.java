package utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

import com.lihaizhou.mycalendar.R;

/**
 * 弹窗辅助类
 *
 * @ClassName WindowUtils
 *
 *
 */
public class WindowUtils  implements OnClickListener{
    private  View mView = null;
    private  WindowManager mWindowManager = null;
    private  Context mContext = null;
    private  Boolean isShown = false;
    private  TextView leftBtn,rightBtn,tv_title;
    private String mlefttxt="",mrighttxt="",mtitle="";
    private boolean isTouch=false;


    public  WindowCallBack mBack;
    public interface WindowCallBack
    {
        public void RightonClikck();
        public void LeftonClikck();
    }
    public  void setWindowCallBack( WindowCallBack back)
    {
        mBack=back;
    }

    /**设置按钮左右两边的选项名称
     * @param lefttxt
     * @param righttxt
     */
    public void setBtnText(String lefttxt,String righttxt)
    {
        this.mlefttxt=lefttxt;
        this.mrighttxt=righttxt;
    }
    /**点击内容区域外消失弹框
     * @param is
     */
    public void setOnTouch(boolean is)
    {
        this.isTouch=is;
    }
    public WindowUtils(Context context)
    {
        this.mContext=context;
    }
    /**
     * 显示弹出框
     *
     * @param context
     * @param view
     */
    public  void showPopupWindow(String title) {
        if (isShown) {
            // LogUtil.i(LOG_TAG, "return cause already shown");
            return;
        }
        this.mtitle=title;
        isShown = true;
        //  LogUtil.i(LOG_TAG, "showPopupWindow");

        // 获取应用的Context
        mContext = mContext.getApplicationContext();
        // 获取WindowManager
        mWindowManager = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);

        mView = setUpView();

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

        // 类型
        params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;

        // WindowManager.LayoutParams.TYPE_SYSTEM_ALERT

        // 设置flag

        int flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM;
        // | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 如果设置了WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE，弹出的View收不到Back键的事件
        params.flags = flags;
        // 不设置这个弹出框的透明遮罩显示为黑色
        params.format = PixelFormat.TRANSLUCENT;
        // FLAG_NOT_TOUCH_MODAL不阻塞事件传递到后面的窗口
        // 设置 FLAG_NOT_FOCUSABLE 悬浮窗口较小时，后面的应用图标由不可长按变为可长按
        // 不设置这个flag的话，home页的划屏会有问题

        params.width = LayoutParams.MATCH_PARENT;
        params.height = LayoutParams.MATCH_PARENT;

        params.gravity = Gravity.CENTER;

        mWindowManager.addView(mView, params);

        //    LogUtil.i(LOG_TAG, "add view");

    }

    /**
     * 隐藏弹出框
     */
    public  void hidePopupWindow() {
        //    LogUtil.i(LOG_TAG, "hide " + isShown + ", " + mView);
        if (isShown && null != mView) {
            //      LogUtil.i(LOG_TAG, "hidePopupWindow");
            mWindowManager.removeView(mView);
            isShown = false;
        }

    }

    private  View setUpView() {

        //  LogUtil.i(LOG_TAG, "setUp view");

        View view = LayoutInflater.from(mContext).inflate(R.layout.popupwindow,
                null);
        tv_title=(TextView)view.findViewById(R.id.title);
        rightBtn = (TextView) view.findViewById(R.id.rightBtn);
        leftBtn= (TextView) view.findViewById(R.id.leftBtn);
        rightBtn.setOnClickListener(this);
        leftBtn.setOnClickListener(this);
        if(!mtitle.isEmpty()){tv_title.setText(mtitle);}
        if(!mlefttxt.isEmpty()){leftBtn.setText(mlefttxt);}
        if(!mrighttxt.isEmpty()){rightBtn.setText(mrighttxt);}
        // 点击窗口外部区域可消除
        // 这点的实现主要将悬浮窗设置为全屏大小，外层有个透明背景，中间一部分视为内容区域
        // 所以点击内容区域外部视为点击悬浮窗外部

        if(isTouch)
        {
            final View popupWindowView = view.findViewById(R.id.popup_window);// 非透明的内容区域

            view.setOnTouchListener(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    //     LogUtil.i(LOG_TAG, "onTouch");
                    int x = (int) event.getX();
                    int y = (int) event.getY();
                    Rect rect = new Rect();
                    popupWindowView.getGlobalVisibleRect(rect);
                    if (!rect.contains(x, y)) {
                        hidePopupWindow();
                    }

                    //    LogUtil.i(LOG_TAG, "onTouch : " + x + ", " + y + ", rect: " + rect);
                    return false;
                }
            });
        }


        // 点击back键可消除
        view.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        hidePopupWindow();
                        return true;
                    default:
                        return false;
                }
            }
        });

        return view;

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(v.getId()==R.id.leftBtn)
        {
            if(mBack!=null)
            {
                mBack.LeftonClikck();
            }
        }
        if(v.getId()==R.id.rightBtn)
        {
            if(mBack!=null)
            {
                mBack.RightonClikck();
            }
        }
        hidePopupWindow();
    }

    public boolean getIsShown()
    {
        return isShown;
    }

}

