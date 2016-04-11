package com.doctor.sun.emoji;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import com.doctor.sun.ui.widget.EmoticonSpan;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmoticonManager {

    private static int PER_PAGE = 20;

    private static final ArrayList<Emoticon> defaultEntries = new ArrayList<>();

    static {
        for (String s : EmoticonRepo.keys) {
            Emoticon emoticon = new Emoticon();
            emoticon.setDrawableId(EmoticonRepo.emoticons.get(s));
            emoticon.setTag(s);
            defaultEntries.add(emoticon);
        }
        for (int i = 0; i < EmoticonRepo.emoticons.size(); i++) {
        }
    }

    //
    // display
    //

    public static final int getDisplayCount() {
        return defaultEntries.size();
    }

    public static final int getPageCount() {
        int fullCount = EmoticonManager.getDisplayCount() / PER_PAGE;
        int remainPage = EmoticonManager.getDisplayCount() % PER_PAGE > 0 ? 1 : 0;
        return fullCount + remainPage;
    }


    public static SpannableStringBuilder mapToEmoticon(Context context, String text, int emoticonSize, int textSize) {

        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        String rexgString = "\\[[^\\[]{1,10}\\]";
        Pattern pattern = Pattern.compile(rexgString);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            Integer resourceId = EmoticonRepo.emoticons.get(matcher.group());
            if (resourceId == null) continue;

            EmoticonSpan emoticonSpan = new EmoticonSpan(context, resourceId, emoticonSize, textSize);
            builder.setSpan(
                    emoticonSpan, matcher.start(), matcher
                            .end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    public static void insertEmoticon(Context context, TextView textView, String keyCode) {
        int textSize = textView.getLineHeight();
        Editable editable = textView.getEditableText();
        int start = textView.getSelectionStart();
        editable.insert(start, mapToEmoticon(context, keyCode, textSize, textSize));
    }

    public static List<Emoticon> getEmoticons(int startPosition, int endPosition) {
        return defaultEntries.subList(startPosition, endPosition);
    }

    @BindingAdapter("android:emoticon")
    public static void setEmoticonText(TextView view, String text) {
        int textSize = (int) (view.getLineHeight() * 1.3);
        view.setText(mapToEmoticon(view.getContext(), text, textSize, textSize));
    }
}
