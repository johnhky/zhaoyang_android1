package com.doctor.sun.event;

import java.lang.reflect.Array;
import java.util.ArrayList;

import io.ganguo.library.core.event.Event;

/**
 * Created by lucas on 12/25/15.
 */
public class QuestionDefaultEvent implements Event {
    private final ArrayList<String> questionDefaultId;

    public QuestionDefaultEvent(ArrayList<String> questionDefaultId) {
        this.questionDefaultId = questionDefaultId;
    }

    public ArrayList<String> getQuestionDefaultId() {
        return questionDefaultId;
    }
}
