package com.doctor.sun.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;

import com.doctor.sun.AppContext;
import com.doctor.sun.R;
import com.doctor.sun.databinding.IncludeSkipShowcaseBinding;
import com.doctor.sun.event.ShowCaseFinishedEvent;

import java.util.HashMap;

import io.ganguo.library.BaseApp;
import io.ganguo.library.core.event.EventHub;
import uk.co.deanwild.materialshowcaseview.IShowcaseListener;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;

/**
 * Created by rick on 3/5/2016.
 */
public class ShowCaseUtil {
    public static final String SHOWCASE_ID = "SHOWCASE_ID";

    public static final String[] keys = new String[]{"doctorMain",
            "doctorMe", "patientDetail",
            "patientInfo", "diagnosisResult",
            "patientMe", "main", "consulting"};

    private static HashMap<String, SparseArray<MaterialShowcaseView.Builder>> buildersMap = new HashMap<>();
    private static SparseArray<MaterialShowcaseView.Builder> builders;

    @BindingAdapter(requireAll = false,
            value = {"android:showcase"
                    , "android:showcaseId"
                    , "android:showcaseSize"
                    , "android:showcasePosition"
                    , "android:isRect"})
    public static void showCase(View view, String content, final String id, int size, int position, boolean isRect) {
//        if (BuildConfig.DEV_MODE) {
//            return;
//        }
        if (isShow(id)) return;

        final MaterialShowcaseView.Builder builder = newBuilder(view, content, isRect);


        builders = buildersMap.get(id);
        if (builders == null) {
            builders = new SparseArray<>();
        }
        builders.put(position, builder);
        buildersMap.put(id, builders);

        if (builders.size() >= size) {
            for (int i = 0; i < builders.size(); i++) {
                final MaterialShowcaseView.Builder currentBuilder = builders.get(i);
                final int nextPosition = i + 1;
                currentBuilder.setListener(new IShowcaseListener() {
                    @Override
                    public void onShowcaseDisplayed(final MaterialShowcaseView materialShowcaseView) {
                        LayoutInflater from = LayoutInflater.from(materialShowcaseView.getContext());
                        IncludeSkipShowcaseBinding inflate = DataBindingUtil.inflate(from, R.layout.include_skip_showcase, materialShowcaseView, true);
                        inflate.setData("跳过帮助教程");
                        inflate.dismissShowcase.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (builders != null) {
                                    builders.clear();
                                }
                                materialShowcaseView.onClick(v);
                            }
                        });
                    }

                    @Override
                    public void onShowcaseDismissed(MaterialShowcaseView materialShowcaseView) {
                        if (builders == null) {
                            setHaveShow(id);
                        }
                        final MaterialShowcaseView.Builder nextBuilder = builders.get(nextPosition);
                        if (nextBuilder != null) {
                           show(nextBuilder);
                        } else {
                            builders.clear();
                            buildersMap.remove(id);
                            setHaveShow(id);
                        }
                    }
                });
            }
            MaterialShowcaseView.Builder firstBuilder = builders.get(0);
            if (firstBuilder != null) {
                show(firstBuilder);
            }
        }
    }

    public static void show(MaterialShowcaseView.Builder builder1) {
        try {
            builder1.show();
        } catch (Exception e) {

        }
    }

    public static void setHaveShow(String id) {
        putBoolean(SHOWCASE_ID + id, true);
        EventHub.post(new ShowCaseFinishedEvent(id));
    }

    public static boolean isShow(String id) {
        return getBoolean(SHOWCASE_ID + id, false);
    }

    public static void reset() {
        getSharedPreferences().edit().clear().apply();
    }

    @NonNull
    private static MaterialShowcaseSequence getShowcaseSequence(View view, SparseArray<MaterialShowcaseView.Builder> builders) {
        MaterialShowcaseSequence showcaseSequence = new MaterialShowcaseSequence((Activity) view.getContext());
        ShowcaseConfig config = new ShowcaseConfig();
        config.setDelay(500);
        showcaseSequence.setConfig(config);
        for (int i = 0; i < builders.size(); i++) {
            MaterialShowcaseView.Builder b = builders.get(i);
            if (b != null) {
                showcaseSequence.addSequenceItem(b.build());
            }
        }
        return showcaseSequence;
    }

    private static MaterialShowcaseView.Builder newBuilder(View view, String content, boolean isRect) {
        Activity context = (Activity) view.getContext();

        Resources resources = context.getResources();
        MaterialShowcaseView.Builder builder = new MaterialShowcaseView.Builder(context)
                .setTarget(view)
                .setContentText(content)
                .setDismissText("我知道了")
                .setTargetTouchable(true)
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
}
