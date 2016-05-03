package com.doctor.sun.util;

import android.app.Activity;
import android.databinding.BindingAdapter;
import android.graphics.Rect;
import android.view.View;

import com.doctor.sun.R;

import java.util.HashMap;

import uk.co.deanwild.materialshowcaseview.MaterialShowcaseSequence;
import uk.co.deanwild.materialshowcaseview.MaterialShowcaseView;
import uk.co.deanwild.materialshowcaseview.shape.RectangleShape;

/**
 * Created by rick on 3/5/2016.
 */
public class ShowCaseUtil {
    private static HashMap<String, MaterialShowcaseSequence> sequenceMap = new HashMap<>();
    private static HashMap<String, Integer> sizeMap = new HashMap<>();

    @BindingAdapter(value = {"android:showcase", "android:showcaseId", "android:showcaseSize"})
    public static void showCase(View view, String content, String id, int size) {
        Activity context = (Activity) view.getContext();
        Rect rect = new Rect();
        view.getDrawingRect(rect);
        MaterialShowcaseView.Builder builder = new MaterialShowcaseView.Builder(context);
        RectangleShape shape = new RectangleShape(rect);
        shape.setAdjustToTarget(true);
        MaterialShowcaseView build = builder
                .setTarget(view)
                .setContentText(content)
                .setDismissText("我知道了")
                .setShape(shape)
                .setMaskColour(context.getResources().getColor(R.color.dark_36_transparent))
                .build();

        MaterialShowcaseSequence materialShowcaseSequence = sequenceMap.get(id);
        if (materialShowcaseSequence == null) {
            materialShowcaseSequence = new MaterialShowcaseSequence((Activity) view.getContext(), id);
        }
        materialShowcaseSequence.addSequenceItem(build);
        Integer value = sizeMap.get(id);
        if (value == null) {
            value = 0;
        }
        sizeMap.put(id, value + 1);
        sequenceMap.put(id, materialShowcaseSequence);

        if (sizeMap.get(id) >= size) {
            materialShowcaseSequence.start();
            sequenceMap.remove(id);
        }
    }


}
