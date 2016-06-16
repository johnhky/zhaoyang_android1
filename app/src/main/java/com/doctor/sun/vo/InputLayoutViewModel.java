package com.doctor.sun.vo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.adapters.TextViewBindingAdapter;
import android.os.Build;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.doctor.sun.databinding.IncludeInputLayoutBinding;
import com.doctor.sun.event.HideInputEvent;
import com.doctor.sun.im.IMManager;
import com.doctor.sun.im.NIMConnectionState;
import com.doctor.sun.im.NimTeamId;
import com.doctor.sun.util.PermissionUtil;
import com.netease.nimlib.sdk.RequestCallback;
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

    private NimTeamId data;
    private RecordAudioViewModel recordAudioViewModel;
    private IncludeInputLayoutBinding binding;
    private boolean recordMode = false;
    private int keyboardType = 0;
    private int keyboardHeight = 0;

    public InputLayoutViewModel(final IncludeInputLayoutBinding binding, NimTeamId callback) {
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

    public TextViewBindingAdapter.AfterTextChanged afterInputChanged() {
        return new TextViewBindingAdapter.AfterTextChanged() {
            @Override
            public void afterTextChanged(Editable s) {
                binding.setIsEditing(s.length() > 0);
                binding.executePendingBindings();
            }
        };
    }

    public View.OnClickListener onSendClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (inputTextView.getText().toString().equals("")) {
                    Toast.makeText(inputTextView.getContext(), "不能发送空消息", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (NIMConnectionState.getInstance().isLogin()) {
                    sendMessage();
                } else {
                    NIMConnectionState.getInstance().setCallback(new RequestCallback() {
                        @Override
                        public void onSuccess(Object o) {
                            onClick(v);
                        }

                        @Override
                        public void onFailed(int i) {
                            Toast.makeText(inputTextView.getContext(), "正在连接IM服务器,聊天功能关闭", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            Toast.makeText(inputTextView.getContext(), "正在连接IM服务器,聊天功能关闭", Toast.LENGTH_SHORT).show();
                        }
                    });
                    IMManager.getInstance().login();
                }
            }
        };
    }

    private void sendMessage() {
        if (data.getType() == SessionTypeEnum.Team) {
            IMManager.getInstance().sentTextMsg(data.getTeamId(), SessionTypeEnum.Team, inputTextView.getText().toString());
            inputTextView.setText("");
        } else if (data.getType() == SessionTypeEnum.P2P) {
            IMManager.getInstance().sentTextMsg(data.getP2PId(), SessionTypeEnum.P2P, inputTextView.getText().toString());
            inputTextView.setText("");
        }
    }

    public TextView.OnEditorActionListener sendMessageAction() {
        return new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    sendMessage();
                }
                return true;
            }
        };
    }

    public View.OnClickListener onAudioBtnClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity context = (Activity) binding.getRoot().getContext();

                if (!hasAudioPermission(context)) return;

                setRecordMode(!isRecordMode());
                binding.setData(InputLayoutViewModel.this);
                binding.setIsEditing(false);
                if (!isRecordMode()) {
                    binding.inputText.requestFocus();
                    Systems.showKeyboard(context.getWindow(), binding.inputText);
                } else {
                    EventHub.post(new HideInputEvent());
                    Systems.hideKeyboard(context);
                }
            }
        };
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

    @NonNull
    public View.OnClickListener showEmoticonClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = binding.getRoot().getContext();
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
        };
    }

    @NonNull
    public View.OnClickListener showCustomActionClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = binding.getRoot().getContext();
                Systems.hideKeyboard(context);
                Tasks.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setRecordMode(false);
                        setKeyboardType(TYPE_CUSTOM_ACTION);
                    }
                }, DELAY_MILLIS);
            }
        };
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
}
