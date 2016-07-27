package com.doctor.sun.vo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.os.Build;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.sun.BR;
import com.doctor.sun.databinding.IncludeInputLayoutBinding;
import com.doctor.sun.event.HideInputEvent;
import com.doctor.sun.im.IMManager;
import com.doctor.sun.im.NimMsgInfo;
import com.doctor.sun.util.PermissionUtil;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;

import io.ganguo.library.core.event.EventHub;
import io.ganguo.library.util.Systems;
import io.ganguo.library.util.Tasks;

/**
 * Created by rick on 13/4/2016.
 */
public class InputLayoutViewModel extends BaseObservable {
    public static final int DELAY_MILLIS = 300;
    //软键盘类型
    public static final int TYPE_EMOTICON = 1;
    public static final int TYPE_CUSTOM_ACTION = 2;
    public static final int TYPE_AUDIO = 3;
    public static final int TYPE_EMPTY = 4;


    public static final String[] AUDIO_PERMISSIONS =
            new String[]{
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int AUDIO_PERMISSION_REQUEST = 30;
    private static EditText inputTextView;

    private NimMsgInfo data;
    private RecordAudioViewModel recordAudioViewModel;
    private IncludeInputLayoutBinding binding;
    private boolean recordMode = false;
    private int keyboardType = 0;
    private int keyboardHeight = 0;

    private String msg = "";

    public InputLayoutViewModel(final IncludeInputLayoutBinding binding, NimMsgInfo callback) {
        this.binding = binding;
        this.data = callback;
        recordAudioViewModel = new RecordAudioViewModel(binding.getRoot().getContext());
        binding.setAudioVM(recordAudioViewModel);
        setInputTextView(binding.inputText);
    }

    public static EditText getInputTextView() {
        return inputTextView;
    }

    public static void setInputTextView(EditText inputTextView) {
        InputLayoutViewModel.inputTextView = inputTextView;
    }

    public void sendMsg(final Context context) {
        if (msg == null || msg.equals("")) {
            Toast.makeText(context, "不能发送空消息", Toast.LENGTH_SHORT).show();
            return;
        }
        sendMessage(msg);
    }

    private void sendMessage(String msg) {
        if (data.getType() == SessionTypeEnum.Team) {
            IMManager.getInstance().sentTextMsg(data.getTeamId(), data.getType(), msg, data.enablePush());
        } else if (data.getType() == SessionTypeEnum.P2P) {
            IMManager.getInstance().sentTextMsg(data.getP2PId(), SessionTypeEnum.P2P, msg, data.enablePush());
        }
        setMsg("");
    }

    public TextView.OnEditorActionListener sendMessageAction() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendMessage(msg);
                }
                return true;
            }
        };
    }


    public void toggleMode(Context context, EditText inputText) {
        Activity activity = (Activity) context;
        if (!hasAudioPermission(activity)) return;

        setRecordMode(!isRecordMode());
        if (!isRecordMode()) {
            inputText.requestFocus();
            Systems.showKeyboard(activity.getWindow(), inputText);
        } else {
            EventHub.post(new HideInputEvent());
            Systems.hideKeyboard(context);
        }
    }

    private boolean hasAudioPermission(Activity context) {
        boolean hasSelfPermission = PermissionUtil.hasSelfPermission(context, AUDIO_PERMISSIONS);
        if (!hasSelfPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(AUDIO_PERMISSIONS, AUDIO_PERMISSION_REQUEST);
            }
        }
        return hasSelfPermission;
    }


    public boolean isRecordMode() {
        return recordMode;
    }

    public void setRecordMode(boolean recordMode) {
        this.recordMode = recordMode;
        notifyChange();
    }


    public void showEmoticon(Context context) {
        Systems.hideKeyboard(context);
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setKeyboardType(TYPE_EMOTICON);
                setRecordMode(false);
                notifyChange();
            }
        }, DELAY_MILLIS);
    }


    public void showCustomAction(Context context) {
        Systems.hideKeyboard(context);
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setRecordMode(false);
                setKeyboardType(TYPE_CUSTOM_ACTION);
            }
        }, DELAY_MILLIS);
    }

    public interface SendMessageCallback {
        void sendMessage(EditText editText);

        TextView.OnEditorActionListener sendMessageAction();
    }

    public int getKeyboardType() {
        return keyboardType;
    }

    public void setKeyboardType(int keyboardType) {
        this.keyboardType = keyboardType;
        binding.inputText.clearFocus();
        notifyChange();
    }

    public void onShowSoftInput() {
        this.keyboardType = TYPE_EMPTY;
        notifyChange();
    }

    public void onHideSoftInput() {
        this.keyboardType = 0;
        notifyChange();
    }

    public void setKeyboardHeight(int keyboardHeight) {
        this.keyboardHeight = keyboardHeight;
    }

    public int getKeyboardHeight() {
        return keyboardHeight;
    }

    @Bindable
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        notifyPropertyChanged(BR.msg);
    }
}
