package com.doctor.sun.model;

import android.databinding.Observable;
import android.util.Log;

import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.QuestionDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Options2;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.Reminder;
import com.doctor.sun.entity.Scales;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.event.SaveAnswerSuccessEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.ItemPickHospital;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.Function0;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.FurtherConsultationVM;
import com.doctor.sun.vo.ItemAddPrescription2;
import com.doctor.sun.vo.ItemAddReminder;
import com.doctor.sun.vo.ItemPickDate;
import com.doctor.sun.vo.ItemPickImage;
import com.doctor.sun.vo.ItemPickTime;
import com.doctor.sun.vo.ItemTextInput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import io.ganguo.library.core.event.EventHub;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rick on 28/7/2016.
 */

public class QuestionsModel {
    public static final String TAG = QuestionsModel.class.getSimpleName();
    public static final int PADDING = 1000;
    private QuestionModule api = Api.of(QuestionModule.class);


    public void questions(String type, String id, final Function0<List<? extends SortedItem>> function0) {
        Call<ApiDTO<QuestionDTO>> apiDTOCall = api.questions2(type, id);
        handleQuestions(function0, apiDTOCall);
    }

    private void handleQuestions(final Function0<List<? extends SortedItem>> function0, Call<ApiDTO<QuestionDTO>> apiDTOCall) {
        apiDTOCall.enqueue(new SimpleCallback<QuestionDTO>() {
            @Override
            protected void handleResponse(QuestionDTO response) {
                List<SortedItem> r = parseQuestions(response.questions);
                int questionSize = 0;
                if (response.questions != null) {
                    questionSize = response.questions.size();
                }

                parseScales(response, r, questionSize);
                function0.apply(r);
            }
        });
    }


    private void parseScales(QuestionDTO response, List<SortedItem> r, int questionSize) {
        if (response.scales != null && !response.scales.isEmpty()) {
            for (int i = 0; i < response.scales.size(); i++) {
                Scales scales = response.scales.get(i);

                scales.setSpan(12);
                scales.setPosition((questionSize + i) * PADDING);
                r.add(scales);
            }
        }
    }

    private List<SortedItem> parseQuestions(List<Questions2> response) {
        List<SortedItem> items = new ArrayList<SortedItem>();
        if (response == null || response.isEmpty()) {
            return items;
        }
        for (int i = 0; i < response.size(); i++) {
            final Questions2 questions2 = response.get(i);
            questions2.setPosition(i * PADDING);
            items.add(questions2);

            switch (questions2.questionType) {
                case QuestionType.drug:
                    parseDrugs(items, i, questions2);
                    break;
                case QuestionType.reminder:
                    parseReminder(items, i, questions2);
                    break;
                case QuestionType.fill:
                    parseFill(items, i, questions2);
                    break;
                case QuestionType.upImg:
                    parseUpImg(items, i, questions2);
                    break;
                case QuestionType.sTime:
                    parsePickTime(items, i, questions2);
                    break;
                case QuestionType.sDate:
                    parsePickDate(items, i, questions2);
                    break;
                case QuestionType.asel:
                    parsePickHospital(items, i, questions2);
                    break;
                case QuestionType.keepon:
                    parseFurtherConsultation(items, i, questions2);
                    break;
                case QuestionType.rectangle:
                default:
                    parseOptions(items, i, questions2);
                    break;
            }
            BaseItem divider = new BaseItem(R.layout.divider_1px_margint_13dp);
            int slop = getSlop(i, 1);
            divider.setItemId("DIVIDER" + slop);
            divider.setPosition(slop);
            items.add(divider);
        }
        return items;
    }

    private void parseOptions(List<SortedItem> items, int i, Questions2 questions2) {
        List<Options2> option = questions2.option;
        if (option == null || option.isEmpty()) return;

        for (int j = 0; j < option.size(); j++) {
            Options2 options2 = option.get(j);

            options2.questionId = questions2.questionId;
            options2.questionType = questions2.questionType;
            options2.questionContent = questions2.questionContent;

            options2.setPosition(i * PADDING + j + 1);
            items.add(options2);
        }
    }

    private void parseFurtherConsultation(List<SortedItem> items, int i, Questions2 questions2) {
        FurtherConsultationVM vm = new FurtherConsultationVM();
        vm.questions2 = questions2;
        vm.setPosition(i * PADDING);
        vm.setQuestionId(questions2.questionId);
        vm.setQuestionContent(questions2.questionContent);
        if (questions2.option != null)
            for (Options2 options2 : questions2.option) {
                if (options2.getSelected()) {
                    vm.setHasAnswer(true);
                    switch (options2.optionType) {
                        case "A": {
                            vm.setBtnOneChecked(true);
                            vm.setDate(options2.questionContent);

                            break;
                        }
                        case "B": {
                            vm.setBtnTwoChecked(true);
                            vm.setDate(options2.questionContent);

                            break;
                        }
                        case "C": {
                            vm.setBtnThreeChecked(true);
                            Doctor doctor = new Doctor();
                            doctor.fromHashMap(options2.selectedOption);
                            vm.setDoctor(doctor);
                            break;
                        }

                    }
                }
            }

        items.add(vm);
    }

    private void parsePickHospital(List<SortedItem> items, int i, Questions2 questions2) {
        int lv1Id = 1;
        int lv2Id = 1;
        int lv3Id = 1;
        String[] answerContent = new String[3];
        try {
            if (questions2.arrayContent != null && questions2.arrayContent.size() >= 3) {
                lv1Id = Integer.parseInt(questions2.arrayContent.get(0).get("key"));
                lv2Id = Integer.parseInt(questions2.arrayContent.get(1).get("key"));
                lv3Id = Integer.parseInt(questions2.arrayContent.get(2).get("key"));
                answerContent[0] = questions2.arrayContent.get(0).get("val");
                answerContent[1] = questions2.arrayContent.get(1).get("val");
                answerContent[2] = questions2.arrayContent.get(2).get("val");
            }
        } catch (NumberFormatException ignored) {

        }
        String url = "tool/endemicAreaTrees";
        if (questions2.option != null) {
            for (Options2 options2 : questions2.option) {
                if (options2.optionType.equals("A")) {
                    url = options2.optionContent;
                }
            }
        }
        ItemPickHospital pickHospital = new ItemPickHospital(answerContent, url, lv1Id, lv2Id, lv3Id);
        pickHospital.setPosition(getSlop(i, 2));
        pickHospital.setItemId(questions2.questionId + QuestionType.asel);
        items.add(pickHospital);
    }

    private int getSlop(int i, int gapBetweenNextItem) {
        return (i + 1) * PADDING - gapBetweenNextItem;
    }

    private void parsePickDate(List<SortedItem> items, int i, final Questions2 questions2) {
        ItemPickDate itemPickDate = new ItemPickDate(R.layout.item_pick_date3, "");
        itemPickDate.setPosition(getSlop(i, 2));
        itemPickDate.setItemId(questions2.questionId + QuestionType.sDate);
        itemPickDate.setDate(questions2.fillContent);
        itemPickDate.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                questions2.answerCount = 1;
                questions2.notifyChange();
            }
        });
        items.add(itemPickDate);
    }

    private void parsePickTime(List<SortedItem> items, int i, final Questions2 questions2) {
        ItemPickTime itemPickTime = new ItemPickTime(R.layout.item_pick_question_time, "");
        itemPickTime.setPosition(getSlop(i, 2));
        itemPickTime.setItemId(questions2.questionId + QuestionType.sTime);
        itemPickTime.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                questions2.answerCount = 1;
                questions2.notifyChange();
            }
        });
        items.add(itemPickTime);
    }

    private void parseUpImg(List<SortedItem> items, int i, final Questions2 questions2) {
        final ItemPickImage pickerItem = new ItemPickImage(R.layout.item_pick_image, "");
        if (questions2.fillContent != null && !questions2.fillContent.equals("")) {
            String[] split = questions2.fillContent.split(",");
            for (int j = 0; j < split.length; j++) {
                ItemPickImage item = new ItemPickImage(R.layout.item_view_image, split[j]);
                item.setPosition(i * PADDING + j + 1);
                item.setItemId(UUID.randomUUID().toString());
                pickerItem.registerItemChangedListener(item);
                items.add(item);
            }
            pickerItem.setItemSize(split.length);
        }


        pickerItem.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                questions2.answerCount = pickerItem.getItemSize();
                questions2.notifyChange();
            }
        });
        if (questions2.extendType > 0) {
            pickerItem.setItemSizeConstrain(questions2.extendType);
        }
        pickerItem.setPosition(getSlop(i, 2));
        pickerItem.setItemId(questions2.questionId + QuestionType.upImg);
        items.add(pickerItem);
    }

    private void parseFill(List<SortedItem> items, int i, final Questions2 questions2) {
        final ItemTextInput textInput = new ItemTextInput(R.layout.item_text_input6, "");
        textInput.setPosition(getSlop(i, 2));
        textInput.setItemId(questions2.questionId + QuestionType.fill);
        textInput.setInput(questions2.fillContent);
        questions2.answerCount = textInput.getInput().length();
        textInput.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                questions2.answerCount = textInput.getInput().length();
                questions2.notifyChange();
            }
        });
        items.add(textInput);
    }

    private void parseReminder(List<SortedItem> items, int i, final Questions2 questions2) {
        final ItemAddReminder list = new ItemAddReminder();
        for (int j = 0; j < questions2.arrayContent.size(); j++) {
            Reminder reminder = Reminder.fromMap(questions2.arrayContent.get(j));
            ItemPickDate itemPickDate = new ItemPickDate(R.layout.item_reminder2, reminder.content);
            itemPickDate.setItemId(UUID.randomUUID().toString());
            itemPickDate.setPosition(i * PADDING + j + i);
            itemPickDate.setSubPosition(j);
            itemPickDate.setDate(reminder.time);
            list.registerItemChangedListener(itemPickDate);
            items.add(itemPickDate);
        }
        list.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                questions2.answerCount = list.itemSize();
                questions2.notifyChange();
            }
        });

        list.setPosition(getSlop(i, 2));
        list.setItemId(questions2.questionId + QuestionType.reminder);
        items.add(list);
    }

    private void parseDrugs(List<SortedItem> items, int i, final Questions2 questions2) {
        final ItemAddPrescription2 itemAddPrescription = new ItemAddPrescription2();
        List<Map<String, String>> arrayContent = questions2.arrayContent;

        if (arrayContent != null) {
            for (int j = 0; j < arrayContent.size(); j++) {
                Prescription prescription = new Prescription();
                prescription.position = i * PADDING + j + 1;
                prescription.itemId = UUID.randomUUID().toString();
                prescription.fromHashMap(arrayContent.get(j));
                itemAddPrescription.registerItemChangedListener(prescription);
                items.add(prescription);
            }
        }

        itemAddPrescription.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                questions2.answerCount = itemAddPrescription.itemSize();
                questions2.notifyChange();
            }
        });
        itemAddPrescription.setPosition(getSlop(i, 2));
        itemAddPrescription.setItemId(questions2.questionId + QuestionType.drug);
        items.add(itemAddPrescription);
    }


    private String composeAnswer(SortedListAdapter adapter) {
        ArrayList<Object> result = new ArrayList<>();
        for (int i = 0; i < adapter.size(); i++) {
            SortedItem sortedItem = adapter.get(i);
            HashMap<String, Object> stringObjectHashMap = sortedItem.toJson(adapter);
            if (stringObjectHashMap != null) {
                result.add(stringObjectHashMap);
            }
            if (sortedItem instanceof Questions2) {
                Questions2 questions2 = (Questions2) sortedItem;
                questions2.refill = 0;
            }
        }
        return JacksonUtils.toJson(result);
    }

    private Response<ApiDTO<String>> postAnswer(String appointmentId, String type, String string) {
        try {
            return api.saveQuestions2(type, appointmentId, string).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveAnswer(final String appointmentId, final String type, final SortedListAdapter adapter) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String string = composeAnswer(adapter);
                Response<ApiDTO<String>> apiDTOResponse = postAnswer(appointmentId, type, string);
                if (apiDTOResponse != null && apiDTOResponse.isSuccessful()) {
                    EventHub.post(new SaveAnswerSuccessEvent());
                }
            }
        }).start();

    }
}
