package com.doctor.sun.event;

import java.util.ArrayList;

import io.ganguo.library.core.event.Event;

/**
 * Created by lucas on 12/25/15.
 */
public class QuestionIdEvent implements Event{
    private final ArrayList<String> questionId;

    public ArrayList<String> getQuestionId() {
        return questionId;
    }

    public QuestionIdEvent(ArrayList<String> questionId) {
        this.questionId = questionId;
    }
}
