package com.doctor.sun.util;

import android.app.Activity;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.View;

import com.doctor.sun.BuildConfig;
import com.doctor.sun.R;

import java.util.HashMap;

import io.ganguo.library.Config;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.ShowcaseConfig;
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape;

/**
 * Created by rick on 3/5/2016.
 */
public class ShowCaseUtil {
    public static final String SHOWCASE_ID = "SHOWCASE_ID";

    public static final String[] keys = new String[]{"doctorMain", "doctorMe", "patientDetail", "patientInfo", "diagnosisResult", "patientMe", "main", "consulting"};

    private static HashMap<String, SparseArray<MaterialShowcaseView.Builder>> buildersMap = new HashMap<>();

    @BindingAdapter(value = {"android:showcase", "android:showcaseId", "android:showcaseSize", "android:showcasePosition"})
    public static void showCase(View view, String content, String id, int size, int position) {
        if (BuildConfig.DEV_MODE) {
            return;
        }
        if (isShow(id)) return;

        MaterialShowcaseView.Builder builder = newBuilder(view, content);


        SparseArray<MaterialShowcaseView.Builder> builders = buildersMap.get(id);
        if (builders == null) {
            builders = new SparseArray<>();
        }
        builders.put(position, builder);
        buildersMap.put(id, builders);

        if (builders.size() >= size) {
            MaterialShowcaseSequence showcaseSequence = getShowcaseSequence(view, builders);
//            showcaseSequence.singleUse(id);
            showcaseSequence.start();
            buildersMap.remove(id);
            setHaveShow(id);
        }
    }

    public static void setHaveShow(String id) {
        Config.putBoolean(SHOWCASE_ID + id, true);
    }

    public static boolean isShow(String id) {
        return Config.getBoolean(SHOWCASE_ID + id, false);
    }

    public static void reset() {
        for (String key : keys) {
            Config.remove(SHOWCASE_ID + key);
        }
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

    private static MaterialShowcaseView.Builder newBuilder(View view, String content) {
        Activity context = (Activity) view.getContext();

        Rect rect = new Rect();
        view.getDrawingRect(rect);
        RectangleShape shape = new RectangleShape(rect);
        shape.setAdjustToTarget(true);

        Resources resources = context.getResources();
        return new MaterialShowcaseView.Builder(context)
                .setTarget(view)
                .setContentText(content)
                .setDismissText("我知道了")
                .setDismissTextColor(resources.getColor(R.color.colorPrimaryDark))
                .setShape(shape)
                .setMaskColour(resources.getColor(R.color.dark_36_transparent));
    }
}
