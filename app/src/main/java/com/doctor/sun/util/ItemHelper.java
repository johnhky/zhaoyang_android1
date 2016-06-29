package com.doctor.sun.util;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.widget.RecyclerView;

import com.doctor.sun.ui.adapter.core.BaseAdapter;

/**
 * Created by rick on 29/2/2016.
 * 作用:在recyclerView的item里面转换activity时候,不好调用onActivityResult来处理activity返回的结果,
 * 这个时候就可以用这种方法来处理.预先定义好了的callback有 insert remove change
 * 也可以重写handleMessage来自定义回调.
 * <p/>
 * 用法:
 * Intent i = new Intent(context, PMainActivity.class);
 * ItemHelper.initCallback(i,adapter, vh);
 * startActivity(i);
 * <p/>
 * 在MainActivity里面调用
 * ItemHelper.insertItem(position, object);
 * 将会在上一个activity的recyclerView里面添加一个item.
 */
public class ItemHelper extends Handler {
    public static final int ITEM_CHANGE = 1;
    public static final int ITEM_REMOVE = 2;
    public static final int ITEM_INSERT = 3;

    public static final String HANDLER = "HANDLER";

    private BaseAdapter mAdapter;
    private RecyclerView.ViewHolder vh;

    public ItemHelper(BaseAdapter mAdapter, RecyclerView.ViewHolder viewHolder) {
        this.mAdapter = mAdapter;
        this.vh = viewHolder;
    }

    @Override
    public void handleMessage(final Message msg) {
        if (vh.getAdapterPosition() == -1) {
            return;
        }
        switch (msg.what) {
            case ITEM_CHANGE: {
                mAdapter.set(vh.getAdapterPosition(), msg.obj);
                mAdapter.notifyItemChanged(vh.getAdapterPosition());
                unbindIntent();
                break;
            }
            case ITEM_REMOVE: {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.remove(vh.getAdapterPosition());
                        mAdapter.notifyItemRemoved(vh.getAdapterPosition());
                        unbindIntent();
                    }
                }, 500);
                break;
            }
            case ITEM_INSERT: {

                mAdapter.add(msg.arg1, msg.obj);
                mAdapter.notifyItemInserted(msg.arg1);
                unbindIntent();
                break;
            }
        }

    }

    private void unbindIntent() {
        mAdapter = null;
        vh = null;
    }

    private static Message createChangeMessage(Object object) {
        Message message = Message.obtain();
        message.what = ITEM_CHANGE;
        message.obj = object;
        return message;
    }

    private static Message createRemoveMessage() {
        Message message =  Message.obtain();
        message.what = ITEM_REMOVE;
        return message;
    }

    private static Message createInsertMessage(int position, Object object) {
        Message message = Message.obtain();
        message.what = ITEM_INSERT;
        message.arg1 = position;
        message.obj = object;
        return message;
    }


    public static Intent initCallback(Intent intent, BaseAdapter mAdapter, RecyclerView.ViewHolder viewHolder) {
        intent.putExtra(HANDLER, new Messenger(new ItemHelper(mAdapter, viewHolder)));
        return intent;
    }

    private static Messenger getHandler(Intent intent) {
        return intent.getParcelableExtra(HANDLER);
    }

    public static void insertItem(Intent intent, int position, Object object) {
        Messenger messenger = intent.getParcelableExtra(HANDLER);
        if (messenger == null) return;
        try {
            messenger.send(createInsertMessage(position, object));
        } catch (RemoteException ignored) {
        }
    }

    public static void changeItem(Intent intent, Object object) {
        Messenger messenger = intent.getParcelableExtra(HANDLER);
        if (messenger == null) return;
        try {
            messenger.send(createChangeMessage(object));
        } catch (RemoteException ignored) {
        }
    }

    public static void removeItem(Intent intent) {
        Messenger messenger = intent.getParcelableExtra(HANDLER);
        if (messenger == null) return;
        try {
            messenger.send(createRemoveMessage());
        } catch (RemoteException ignored) {
        }
    }
}
