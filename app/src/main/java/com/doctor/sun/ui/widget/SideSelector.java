package com.doctor.sun.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.doctor.sun.R;


/**
 * Created by Jorstin on 2015/3/21.
 */
public class SideSelector extends View {
    public static final String[] ALPHABET = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z"};
    int choose = -1;
    Paint paint = new Paint();
    boolean showBkg = false;
    private PopupWindow mPopupWindow;
    private TextView mPopupText;
    private Handler handler = new Handler();
    private RecyclerView listView;
    private SectionIndexer indexer;

    private int itemHeight = 0;

    public SideSelector(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public SideSelector(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SideSelector(Context context) {
        this(context, null);
        itemHeight = context.getResources().getDimensionPixelSize(R.dimen.dp_500);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (showBkg) {
            canvas.drawColor(Color.parseColor("#AAAAAA"));
        }

        int height = getHeight();
        int width = getWidth();
        int singleHeight = height / ALPHABET.length;
        for (int i = 0; i < ALPHABET.length; i++) {
            paint.setColor(Color.parseColor("#FF5C5C5C"));
            paint.setTextSize(getResources().getDimensionPixelSize(R.dimen.font_12));//设置字体的大小
            paint.setFakeBoldText(true);
            paint.setAntiAlias(true);
            if (i == choose) {
                paint.setColor(Color.parseColor("#FF000000"));
            }
            float xPos = width / 2 - paint.measureText(ALPHABET[i]) / 2;
            float yPos = singleHeight * i + singleHeight;
            canvas.drawText(ALPHABET[i], xPos, yPos, paint);
            paint.reset();
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = choose;
        final int c = (int) (y / getHeight() * ALPHABET.length);

        LinearLayoutManager layoutManager = (LinearLayoutManager) listView.getLayoutManager();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                showBkg = true;
                if (oldChoose != c) {
                    if (c >= 0 && c < ALPHABET.length) { //让第一个字母响应点击事件
                        showPopup(c);
                        int positionForSection = indexer.getPositionForSection(c);
                        layoutManager.scrollToPositionWithOffset(positionForSection, -itemHeight);
                        choose = c;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != c) {
                    if (c >= 0 && c < ALPHABET.length) { //让第一个字母响应点击事件
                        showPopup(c);
                        int positionForSection = indexer.getPositionForSection(c);
                        layoutManager.scrollToPositionWithOffset(positionForSection, -itemHeight);
                        choose = c;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                showBkg = false;
                choose = -1;
                dismissPopup();
                invalidate();
                break;
        }
        return true;
    }

    private void showPopup(int item) {
        if (mPopupWindow == null) {

            handler.removeCallbacks(dismissRunnable);
            mPopupText = new TextView(getContext());
            mPopupText.setBackgroundColor(Color.GRAY);
            mPopupText.setTextColor(Color.WHITE);
            mPopupText.setTextSize(getResources().getDimensionPixelSize(R.dimen.font_20));
            mPopupText.setGravity(Gravity.CENTER_HORIZONTAL
                    | Gravity.CENTER_VERTICAL);

            int height = getResources().getDimensionPixelSize(R.dimen.dp_80);

            mPopupWindow = new PopupWindow(mPopupText, height, height);
        }

        /*String text = "";
        if (item == 26) {
            text = "#";
        } else {
            text = Character.toString((char) ('A' + item));
        }*/
        String text = ALPHABET[item];
        mPopupText.setText(text);
        if (mPopupWindow.isShowing()) {
            mPopupWindow.update();
        } else {
            mPopupWindow.showAtLocation(getRootView(),
                    Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL, 0, 0);
        }
    }

    private void dismissPopup() {
        handler.postDelayed(dismissRunnable, 1500);
    }

    public void removeDis() {
        handler.removeCallbacks(dismissRunnable);
    }

    Runnable dismissRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (mPopupWindow != null && mPopupWindow.isShowing()) {
                mPopupWindow.dismiss();
            }
        }
    };

    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    public void setListView(RecyclerView listView) {
        this.listView = listView;
        indexer = (SectionIndexer) listView.getAdapter();
    }

    @Deprecated
    public void setTextSize(float textSize) {

    }
}