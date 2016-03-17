package io.ganguo.library.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自动适应高度，会自动根据item数量自动计算高度，解决ListView放在ScrollView时无发显示出来。
 * <p/>
 * Created by Wilson on 14-6-22.
 */
public class AdjustListView extends ListView {

    public AdjustListView(Context context) {
        this(context, null);
    }

    public AdjustListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
