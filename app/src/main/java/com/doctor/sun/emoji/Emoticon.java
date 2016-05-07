package com.doctor.sun.emoji;

import android.app.Activity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.sun.R;
import com.doctor.sun.im.NimTeamId;
import com.doctor.sun.im.NIMConnectionState;
import com.doctor.sun.ui.adapter.ViewHolder.LayoutId;
import com.doctor.sun.vo.InputLayoutViewModel;

/**
 * Created by rick on 7/4/2016.
 * 表情
 */
public class Emoticon implements LayoutId {
    public final int itemLayoutId;
    private String id;
    private String tag;
    private String assetPath;
    private int drawableId;

    public Emoticon(int itemLayoutId) {
        this.itemLayoutId = itemLayoutId;
    }

    public Emoticon() {
        this.itemLayoutId = R.layout.item_emoji;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAssetPath() {
        return assetPath;
    }

    public void setAssetPath(String assetPath) {
        this.assetPath = assetPath;
    }

    public void setDrawableId(int drawableId) {
        this.drawableId = drawableId;
    }

    public int getDrawableId() {
        return drawableId;
    }

    @Override
    public int getItemLayoutId() {
        return itemLayoutId;
    }

    @Override
    public String toString() {
        return "Emoticon{" +
                "id='" + id + '\'' +
                ", tag='" + tag + '\'' +
                ", assetPath='" + assetPath + '\'' +
                '}';
    }

    public void onSelect(View view) {
        Activity mActivity = (Activity) view.getContext();
        TextView textView = getTextView(mActivity);
        if (textView == null){
            return;
        }

        EmoticonManager.insertEmoticon(mActivity, textView, tag);
    }

    private TextView getTextView(Activity mActivity) {
        View focusCurrent = mActivity.getWindow().getCurrentFocus();
        TextView textView;
        if (focusCurrent == null || !(focusCurrent instanceof EditText)) {
            textView = InputLayoutViewModel.getInputTextView();
        } else {
            textView = (TextView) focusCurrent;
        }
        return textView;
    }

    public void onDelete(View view) {
        Activity mActivity = (Activity) view.getContext();
        TextView textView = getTextView(mActivity);
        if (textView == null){
            return;
        }

        textView.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
    }

    public void sendSticker(View view) {
        NimTeamId id = (NimTeamId) view.getContext();
        if (NIMConnectionState.getInstance().isLogin()) {
            com.doctor.sun.im.Messenger.getInstance().sentSticker(id.getTeamId(), id.getType(), this);
        } else {
            Toast.makeText(view.getContext(), "正在连接IM服务器,聊天功能关闭", Toast.LENGTH_SHORT).show();
            com.doctor.sun.im.Messenger.getInstance().login();
        }
    }
}
