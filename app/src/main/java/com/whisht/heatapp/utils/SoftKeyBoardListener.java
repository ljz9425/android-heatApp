package com.whisht.heatapp.utils;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.RequiresApi;

/**
 * @ClassName:
 * @Description:(这里用一句话描述这个类的作用)
 * @author: LQY
 * @time: 2020-03-21 8:46
 */
public class SoftKeyBoardListener {
    private Activity activity;
    private OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener;
    private int screenHeight;
    // 空白高度 = 屏幕高度 - 当前 Activity 的可见区域的高度
    // 当 blankHeight 不为 0 即为软键盘高度。
    private int blankHeight = 0;
    private int vtoBottom;
    private int mesureResId;
    private int moveResId;
    private View v;
    private int diff = 0;
    private int diffResId = 0;
    private boolean init = false;

    /**
     *
     * @param activity
     * @param mesureResId 需要判断的控件
     * @param moveResId 需要移动的控件
     */
    public SoftKeyBoardListener(Activity activity,int mesureResId,int moveResId) {
        this(activity,mesureResId,moveResId,0);
    }

    /**
     *
     * @param activity
     * @param mesureResId 需要判断的控件
     * @param moveResId 需要移动的控件
     * @param diffResId 缩小距离的资源，用于获取高度，缩小的时候直接隐藏
     */
    public SoftKeyBoardListener(Activity activity,int mesureResId,int moveResId,int diffResId){
        this.activity = activity;
        if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        this.mesureResId = mesureResId;
        this.moveResId = moveResId;
        this.diffResId = diffResId;
    }

    public void onCreate(){
        v = activity.findViewById(mesureResId);
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                //不可以直接获取控件位置，放在这个里面获取；
                if(diffResId != 0)
                    diff = activity.findViewById(diffResId).getHeight();
                int[] viewLocation = new int[2];
                v.getLocationOnScreen(viewLocation); //获取该控价在屏幕中的位置（左上角的点）
                screenHeight = CommonUtils.getViewVisableSize(activity.findViewById(moveResId))[1];
                vtoBottom = screenHeight - viewLocation[1] - v.getHeight() - 20; //屏幕高度-控件距离顶部高度-控件高度
                init = true;
            }
        },100);
        View content = activity.findViewById(android.R.id.content);
        content.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public void onResume(){
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                //不可以直接获取控件位置，放在这个里面获取；
                if(diffResId != 0)
                    diff = activity.findViewById(diffResId).getHeight();
                int[] viewLocation = new int[2];
                v.getLocationOnScreen(viewLocation); //获取该控价在屏幕中的位置（左上角的点）
                screenHeight = CommonUtils.getViewVisableSize(activity.findViewById(moveResId))[1];
                vtoBottom = screenHeight - viewLocation[1] - v.getHeight() - 20; //屏幕高度-控件距离顶部高度-控件高度
                init = true;
            }
        },100);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void onDestory() {
        View content = activity.findViewById(android.R.id.content);
        content.getViewTreeObserver().removeOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    private ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {

        @Override
        public void onGlobalLayout() {
            if(!init)
                return;
//            Rect rect = new Rect();
//            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
//            int newBlankheight = screenHeight - rect.bottom;
            int newBlankheight = screenHeight - CommonUtils.getViewVisableSize(activity.findViewById(moveResId))[1];

            if (newBlankheight != blankHeight) {
                if(newBlankheight==0){
                    // keyboard close
                    if(diff == 0) {
                        if(vtoBottom < blankHeight)
                            //只上移
                            gotoDown(vtoBottom - blankHeight);
                    }
                    else if(vtoBottom + diff < blankHeight){
                        //上移+缩小距离
                        gotoDown(vtoBottom - blankHeight + diff);
                        if (onKeyBoardStatusChangeListener != null)
                            onKeyBoardStatusChangeListener.OnKeyBoardClose(diff);
                    }else if(vtoBottom < blankHeight){
                        //只缩小距离
                        if (onKeyBoardStatusChangeListener != null)
                            onKeyBoardStatusChangeListener.OnKeyBoardClose(diff);
                    }
                }else{
                    // keyboard pop
                    if(diff == 0) {
                        if(vtoBottom < newBlankheight)
                            //只上移
                            gotoUp(vtoBottom - newBlankheight);
                    }
                    else if(vtoBottom + diff < newBlankheight){
                        //上移+缩小距离
                        gotoUp(vtoBottom - newBlankheight + diff);
                        if (onKeyBoardStatusChangeListener != null)
                            onKeyBoardStatusChangeListener.OnKeyBoardPop(diff);
                    }else if(vtoBottom < newBlankheight){
                        //只缩小距离
                        if (onKeyBoardStatusChangeListener != null)
                            onKeyBoardStatusChangeListener.OnKeyBoardPop(diff);
                    }
                }
            }
            blankHeight = newBlankheight;
        }
    };
    public void gotoUp(int offset){
//        System.out.println("goto up:"+offset);
        activity.findViewById(moveResId).setTranslationY(offset);
//        ObjectAnimator animatorUp = null;
//        if (animatorUp == null) { //如果每次弹出的键盘高度不一致，就不要这个判断，每次都新创建动画（密码键盘可能和普通键盘高度不一致）
//            animatorUp = ObjectAnimator.ofFloat(activity.findViewById(moveResId), "translationY", 0, offset);
//            animatorUp.setDuration(360);
//            animatorUp.setInterpolator(new AccelerateDecelerateInterpolator());
//        }
//        animatorUp.start();
    }
    private void gotoDown(int offset){
//        System.out.println("goto down:"+offset);
        activity.findViewById(moveResId).setTranslationY(0);
//        ObjectAnimator animatorUp = null;
//        if (animatorUp == null) { //如果每次弹出的键盘高度不一致，就不要这个判断，每次都新创建动画（密码键盘可能和普通键盘高度不一致）
//            animatorUp = ObjectAnimator.ofFloat(activity.findViewById(moveResId), "translationY", offset,0);
//            animatorUp.setDuration(360);
//            animatorUp.setInterpolator(new AccelerateDecelerateInterpolator());
//        }
//        animatorUp.start();
    }
    public void setOnKeyBoardStatusChangeListener(
            OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener) {
        this.onKeyBoardStatusChangeListener = onKeyBoardStatusChangeListener;
    }

    public interface OnKeyBoardStatusChangeListener {
        /**
         * 缩小距离用
         * @param offset
         */
        void OnKeyBoardPop(int offset);

        /**
         * 缩小距离用
         * @param offset
         */
        void OnKeyBoardClose(int offset);
    }
}
