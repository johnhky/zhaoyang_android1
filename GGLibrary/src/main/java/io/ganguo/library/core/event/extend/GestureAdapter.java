package io.ganguo.library.core.event.extend;

import android.view.GestureDetector;
import android.view.MotionEvent;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * 手势适配器
 * <p/>
 * Created by zhihui_chen on 14-9-29.
 */
public class GestureAdapter implements GestureDetector.OnGestureListener {
    public static final Logger LOG = LoggerFactory.getLogger(GestureAdapter.class);

    /**
     * 按下
     *
     * @param e
     * @return
     */
    @Override
    public boolean onDown(MotionEvent e) {
        LOG.d("onDown");
        return false;
    }

    /**
     * 按下
     *
     * @param e
     */
    @Override
    public void onShowPress(MotionEvent e) {
        LOG.d("onShowPress");
    }

    /**
     * 单击
     *
     * @param e
     * @return
     */
    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        LOG.d("onSingleTapUp");
        return false;
    }

    /**
     * 滑动
     *
     * @param e1
     * @param e2
     * @param distanceX
     * @param distanceY
     * @return
     */
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    /**
     * 长按
     *
     * @param e
     */
    @Override
    public void onLongPress(MotionEvent e) {
        LOG.d("onLongPress");
    }

    /**
     * 手势滑动
     *
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float up = e1.getY() - e2.getY();
        float bottom = e2.getY() - e1.getY();
        float left = e1.getX() - e2.getX();
        float right = e2.getX() - e1.getX();

        LOG.d("onFling up: " + up + " bottom: " + bottom + " left: " + left + " right: " + right);

        if (up > bottom && up > left && up > right) {
            // 向上手势
            return onFlingUp(e1, e2, velocityX, velocityY);
        } else if (bottom > up && bottom > left && bottom > right) {
            // 向下手势
            return onFlingBottom(e1, e2, velocityX, velocityY);
        } else if (left > up && left > bottom && left > right) {
            // 向左手势
            return onFlingLeft(e1, e2, velocityX, velocityY);
        } else if (right > up && right > bottom && right > left) {
            // 向右手势
            return onFlingRight(e1, e2, velocityX, velocityY);
        }

        return false;
    }

    /**
     * 向左手势
     *
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    public boolean onFlingLeft(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        LOG.d("onFlingLeft");
        return false;
    }

    /**
     * 向右手势
     *
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    public boolean onFlingRight(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        LOG.d("onFlingRight");
        return false;
    }

    /**
     * 向上手势
     *
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    public boolean onFlingUp(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        LOG.d("onFlingUp");
        return false;
    }

    /**
     * 向下手势
     *
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    public boolean onFlingBottom(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        LOG.d("onFlingBottom");
        return false;
    }
}
