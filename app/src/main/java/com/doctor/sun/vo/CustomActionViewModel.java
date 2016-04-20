package com.doctor.sun.vo;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.doctor.sun.R;
import com.doctor.sun.entity.Appointment;
import com.doctor.sun.ui.activity.doctor.ChattingActivity;
import com.doctor.sun.ui.adapter.SimpleAdapter;
import com.doctor.sun.ui.widget.PickImageDialog;
import com.doctor.sun.util.FileChooser;

/**
 * Created by rick on 13/4/2016.
 */
public class CustomActionViewModel {
    public static final int IMAGE_REQUEST_CODE = ChattingActivity.IMAGE_REQUEST_CODE;
    public static final int FILE_REQUEST_CODE = ChattingActivity.FILE_REQUEST_CODE;

    private Activity mActivity;
    private Appointment data;

    public CustomActionViewModel(Context context, Appointment data) {
        this.mActivity = (Activity) context;
        this.data = data;
    }

    @NonNull
    public SimpleAdapter getSimpleAdapter() {
        SimpleAdapter adapter = new SimpleAdapter(mActivity);

        adapter.add(new ClickMenu(R.layout.item_menu2, R.drawable.nim_message_plus_phone, "语音电话", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.getHandler().makePhoneCall(v);
            }
        }));
        adapter.add(new ClickMenu(R.layout.item_menu2, R.drawable.nim_message_plus_photo_selector, "相册", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.openGallery(mActivity, IMAGE_REQUEST_CODE);
            }
        }));
        adapter.add(new ClickMenu(R.layout.item_menu2, R.drawable.nim_message_plus_video_selector, "拍摄", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.openCamera(mActivity, IMAGE_REQUEST_CODE);
            }
        }));
        adapter.add(new ClickMenu(R.layout.item_menu2, R.drawable.message_plus_video_chat_selector, "视频聊天", new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }));
        adapter.add(new ClickMenu(R.layout.item_menu2, R.drawable.message_plus_file_selector, "文件传输", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser.showFileChooser((Activity) v.getContext());
            }
        }));

        adapter.onFinishLoadMore(true);
        return adapter;
    }

}
