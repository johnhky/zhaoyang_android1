package com.doctor.sun.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.databinding.IncludeSkipShowcaseBinding;
import com.doctor.sun.event.ShowCaseFinishedEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.ProfileModule;

import java.util.List;

import io.ganguo.library.BaseApp;
import io.ganguo.library.core.event.EventHub;
import uk.co.deanwild.materialshowcaseview.IShowcaseListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;

/**
 * Created by rick on 3/5/2016.
 */
public class ShowCaseUtil {
    public static final String TAG = ShowCaseUtil.class.getSimpleName();
    public static final String SHOWCASE_ID = "SHOWCASE_ID";

    private static SparseArray<BuilderHolder> buildersMap2 = new SparseArray<>();


    @BindingAdapter(requireAll = false,
            value = {"android:showcase"
                    , "android:previous"
                    , "android:current"
                    , "android:next"
                    , "android:isRect"})
    public static void showCase2(final View view, String content, final int previous, final int current, final int next, boolean isRect) {
        boolean showThisItemNow = previous == -1;
        if (showThisItemNow) {
            if (isShow(current)) {
                showNext(next);
            } else {
                showThis(view, content, current, next, isRect);
            }
        } else {
            buildersMap2.put(current, new BuilderHolder(view, content, isRect, next));
        }

    }

    private static void showThis(View view, String content, final int current, final int next, boolean isRect) {
        final MaterialShowcaseView.Builder builder = newBuilder(view, content, isRect);
        builder.setListener(new IShowcaseListener() {
            @Override
            public void onShowcaseDisplayed(final MaterialShowcaseView materialShowcaseView) {
                LayoutInflater from = LayoutInflater.from(materialShowcaseView.getContext());
                IncludeSkipShowcaseBinding inflate = DataBindingUtil.inflate(from, R.layout.include_skip_showcase, materialShowcaseView, true);
                inflate.setData("跳过帮助教程");
                inflate.dismissShowcase.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        materialShowcaseView.onClick(v);
                    }
                });
            }

            @Override
            public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                showNext(next);
                saveToServer(current);
            }
        });
        builder.show();
    }

    private static void showNext(int next) {
        BuilderHolder builderHolder = buildersMap2.get(next);
        if (builderHolder != null) {
            showCase2(builderHolder.getView(), builderHolder.getContent(), -1, next, builderHolder.getNext(), builderHolder.isRect);
        } else {
            //最后一页
            buildersMap2.clear();
        }
    }

    public static void show(MaterialShowcaseView.Builder builder1) {
        try {
            builder1.show();
        } catch (Exception e) {

        }
    }

    public static void setHaveShow(int id) {
        putBoolean(SHOWCASE_ID + id, true);
        EventHub.post(new ShowCaseFinishedEvent(id));
    }

    public static void setHaveShow(String id) {
        putBoolean(SHOWCASE_ID + id, true);
        EventHub.post(new ShowCaseFinishedEvent(id));
    }

    public static void saveToServer(final int id) {
        ProfileModule api = Api.of(ProfileModule.class);
        api.setIsShow(id).enqueue(new SimpleCallback<String>() {
            @Override
            protected void handleResponse(String response) {
                setHaveShow(id);
            }
        });
    }

    public static void restoreState() {
        ProfileModule api = Api.of(ProfileModule.class);
        api.tutorialRecord().enqueue(new SimpleCallback<List<String>>() {
            @Override
            protected void handleResponse(List<String> response) {
                for (String s : response) {
                    setHaveShow(s);
                }
            }
        });
    }

    public static boolean isShow(int id) {
        return getBoolean(SHOWCASE_ID + id, false);
    }

    public static void reset() {
        getSharedPreferences().edit().clear().apply();
    }

    private static MaterialShowcaseView.Builder newBuilder(View view, String content, boolean isRect) {
        Activity context = (Activity) view.getContext();

        Resources resources = context.getResources();
        MaterialShowcaseView.Builder builder = new MaterialShowcaseView.Builder(context)
                .setTarget(view)
                .setContentText(content)
                .setTargetTouchable(true)
                .setDismissOnTargetTouch(true)
                .setDismissOnTouch(false)
                .setDismissText("我知道了")
                .setDismissTextColor(resources.getColor(R.color.colorPrimaryDark))
                .setMaskColour(resources.getColor(R.color.dark_36_transparent));
        if (isRect) {
            builder.withRectangleShape(true);
        } else {
            builder.withCircleShape();
        }
        return builder;
    }

    public static SharedPreferences getSharedPreferences() {
        BaseApp me = AppContext.me();
        return me.getSharedPreferences("SHOW_CASE", Context.MODE_PRIVATE);
    }

    public static boolean getBoolean(String key, boolean def) {
        return getSharedPreferences().getBoolean(key, def);
    }

    public static void putBoolean(String key, boolean value) {
        SharedPreferences sharedPref = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static void remove(String... keys) {
        SharedPreferences sharedPref = getSharedPreferences();
        SharedPreferences.Editor editor = sharedPref.edit();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.apply();
    }

    private static class BuilderHolder {
        private final int next;
        private View view;
        private String content;
        private boolean isRect;

        public BuilderHolder(View view, String content, boolean isRect, int next) {
            this.view = view;
            this.content = content;
            this.isRect = isRect;
            this.next = next;
        }

        public View getView() {
            return view;
        }

        public String getContent() {
            return content;
        }

        public boolean isRect() {
            return isRect;
        }

        public int getNext() {
            return next;
        }
    }
}
