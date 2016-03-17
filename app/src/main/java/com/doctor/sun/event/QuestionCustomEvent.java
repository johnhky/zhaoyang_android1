package com.doctor.sun.event;

import java.util.ArrayList;

import io.ganguo.library.core.event.Event;

/**
 * Created by lucas on 12/25/15.
 */
public class QuestionCustomEvent implements Event{
    private final ArrayList<String> questionCustomId;

    public ArrayList<String> getQuestionCustomId() {
        return questionCustomId;
    }

    public QuestionCustomEvent(ArrayList<String> questionCustomId) {
        this.questionCustomId = questionCustomId;

    }
}
