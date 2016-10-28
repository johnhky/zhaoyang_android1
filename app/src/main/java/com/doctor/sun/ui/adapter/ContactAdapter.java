package com.doctor.sun.ui.adapter;

import android.content.Context;
import android.databinding.ViewDataBinding;
import android.util.SparseIntArray;
import android.widget.SectionIndexer;

import com.doctor.sun.R;
import com.doctor.sun.entity.Description;
import com.doctor.sun.ui.widget.SideSelector;
import com.doctor.sun.vo.LayoutId;

import java.util.List;

import io.ganguo.library.util.Tasks;

/**
 * Created by rick on 25/12/2015.
 */
public class ContactAdapter extends SimpleAdapter<LayoutId, ViewDataBinding> implements SectionIndexer {
    private int lastItemSize = 0;
    private SparseIntArray positions;

    public ContactAdapter() {
        super();
    }


    public void addIndex() {
        String[] clone = SideSelector.ALPHABET.clone();
        for (int i = clone.length - 1; i >= 0; i--) {
            Description object = new Description(R.layout.item_description, String.valueOf(clone[i]));
            object.setIndexPosition(i);
            object.setEnabled(false);
            this.add(object);
        }
    }

    @Override
    public Object[] getSections() {
        String[] chars = new String[SideSelector.ALPHABET.length];
        for (int i = 0; i < SideSelector.ALPHABET.length; i++) {
            chars[i] = String.valueOf(SideSelector.ALPHABET[i]);
        }

        return chars;
    }

    @Override
    public int getPositionForSection(int i) {
        updatePosition();

        if (positions == null) {
            return (int) (size() * ((float) i / (float) getSections().length));
        } else {
            return positions.get(i);
        }
    }

    private boolean needInit() {
        boolean result = lastItemSize != size();
        lastItemSize = size();
        return result;
    }

    private void onUpdatePosition() {
        //开新线程更新[a-z]位置
        new Thread(new Runnable() {
            @Override
            public void run() {
                SparseIntArray intArray = new SparseIntArray();
                List<LayoutId> data = getData();

                int lastDividerPosition = 0;
                Description lastDivider = null;
                for (int i = 0; i < data.size(); i++) {
                    LayoutId layoutId = data.get(i);
                    if (layoutId.getItemLayoutId() == R.layout.item_description) {
                        if (i - lastDividerPosition > 1) {
                            if (lastDivider != null)
                                lastDivider.setEnabled(true);
                        }
                        intArray.put(intArray.size(), i);

                        lastDividerPosition = i;
                        lastDivider = (Description) layoutId;
                    }
                }
                positions = intArray;
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        notifyDataSetChanged();
                    }
                });
            }
        }).start();
    }

    public void updatePosition() {
        if (needInit()) {
            onUpdatePosition();
        }
    }

    public void resetState() {
        lastItemSize = 0;
    }

    @Override
    public int getSectionForPosition(int position) {
        try {
            Description description = (Description) get(position);
            if (description.isEnabled()) {
                return description.getIndexPosition();
            } else {
                return getSectionForPosition(position + 1);
            }
        } catch (Exception e) {
            return -1;
        }
    }

    public void clear() {
        super.clear();
        resetState();
        addIndex();
    }
}
