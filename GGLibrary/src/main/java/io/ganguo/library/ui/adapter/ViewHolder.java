package io.ganguo.library.ui.adapter;

import android.view.View;

import java.util.HashMap;
import java.util.Map;

import io.ganguo.library.R;

/**
 * ViewHolder - Item ConvertView
 * <p/>
 * Created by Tony on 4/2/15.
 */
@SuppressWarnings("unchecked")
public class ViewHolder {
    public static final String POSITION = "position";
    public static final String ITEM = "item";

    private View mConvertView;
    private Map<Integer, View> viewMap = new HashMap<>();
    private Map<Object, Object> dataMap = new HashMap<>();

    public ViewHolder(View convertView) {
        mConvertView = convertView;
        mConvertView.setTag(R.string.tag_view_holder, this);
    }

    public <V extends View> V findViewById(int id) {
        if (!viewMap.containsKey(id)) {
            viewMap.put(id, mConvertView.findViewById(id));
        }
        return (V) viewMap.get(id);
    }

    public View getView() {
        return mConvertView;
    }

    public int getPosition() {
        return getData(POSITION);
    }

    public <V> V getItem() {
        return getData(ITEM);
    }

    public <K, V> void putData(K key, V value) {
        dataMap.put(key, value);
    }

    public <K, V> V getData(K key) {
        return (V) dataMap.get(key);
    }
}
