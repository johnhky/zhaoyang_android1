package com.doctor.sun.model;

import android.databinding.Observable;

import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.QuestionDTO;
import com.doctor.sun.entity.Description;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.FollowUpInfo;
import com.doctor.sun.entity.Options2;
import com.doctor.sun.entity.QuVisibilityManager;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.Reminder;
import com.doctor.sun.entity.Scales;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.entity.constans.QuestionsType;
import com.doctor.sun.entity.handler.PrescriptionHandler;
import com.doctor.sun.event.SaveAnswerSuccessEvent;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.immutables.Prescription;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.Function0;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vo.BaseItem;
import com.doctor.sun.vo.FurtherConsultationVM;
import com.doctor.sun.vo.ItemAddPrescription2;
import com.doctor.sun.vo.ItemAddReminder;
import com.doctor.sun.vo.ItemPickDate;
import com.doctor.sun.vo.ItemPickHospital;
import com.doctor.sun.vo.ItemPickImages;
import com.doctor.sun.vo.ItemPickTime;
import com.doctor.sun.vo.ItemTextInput;
import com.doctor.sun.vo.ItemTextInput2;
import com.google.common.base.Strings;

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
    public static final int DIVIDER_POSITION = 2;
    public static final int BUTTON_POSITION = 1;
    public static final int RANGE_ITEM_POSITION = 3;

    private QuestionModule api = Api.of(QuestionModule.class);
    private int firstItemPadding = 0;

    public QuestionsModel() {
        firstItemPadding = 0;
    }

    public QuestionsModel(int firstItemPositionPadding) {
        this.firstItemPadding = firstItemPositionPadding;
    }

    public void questions(String type, String id, String questionType, String templateType, final Function0<List<? extends SortedItem>> function0) {
        HashMap<String, String> params = new HashMap<>();
        if (!Strings.isNullOrEmpty(templateType)) {
            params.put("template_type", templateType);
        }
        Call<ApiDTO<QuestionDTO>> apiDTOCall = api.questions2(type, id, questionType, params);
        handleQuestions(questionType, function0, apiDTOCall);
    }

    private void handleQuestions(final String questionType, final Function0<List<? extends SortedItem>> function0, Call<ApiDTO<QuestionDTO>> apiDTOCall) {
        apiDTOCall.enqueue(new SimpleCallback<QuestionDTO>() {
            @Override
            protected void handleResponse(QuestionDTO response) {
                List<SortedItem> r = parseQuestions(response.questions, 0, 0);
                int questionSize = 0;
                if (response.questions != null) {
                    questionSize = response.questions.size();
                }

                if (questionSize == 0) {
                    // 进行中的订单，医生建议显示【待医生诊断】
                    BaseItem item = new BaseItem(R.layout.divider_1px);
                    item.setItemId(UUID.randomUUID().toString());
                    item.setPosition(r.size());
                    r.add(item);

                    Description description = new Description(R.layout.item_description, "嘱咐");
                    description.setItemId("description");
                    description.setPosition(r.size());
                    r.add(description);

                    ItemTextInput textInput = new ItemTextInput(R.layout.item_text_option_display, "");
                    textInput.setInput("待医生诊断");
                    textInput.setItemId("diagnosis");
                    textInput.setPosition(r.size());
                    r.add(textInput);
                }

                parseScales(response, r, questionSize);

                FollowUpInfo followUpInfo = response.followUpInfo;
                if (followUpInfo != null) {
                    followUpInfo.setPosition(-PADDING);
                    if (QuestionsType.DOCTOR_W_DOCTOR_QUESTIONS.equals(questionType)) {
                        followUpInfo.setItemLayoutId(R.layout.item_after_service_detail);
                    } else {
                        followUpInfo.setItemLayoutId(R.layout.p_item_after_service_detail);
                    }

                    r.add(followUpInfo);
                }
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

    public List<SortedItem> parseQuestions(List<Questions2> response, int indexAtQuestions, int indexToDisplay) {
        List<SortedItem> items = new ArrayList<SortedItem>();

        return parseQuestion(response, indexAtQuestions, indexToDisplay, items);
    }

    private List<SortedItem> parseQuestion(List<Questions2> questions, int indexAtQuestions, int indexToDisplay, List<SortedItem> acc) {
        if (questions == null || questions.isEmpty()) {
            return acc;
        }
        if (indexAtQuestions >= questions.size()) {
            return acc;
        } else {
            final Questions2 questions2 = questions.get(indexAtQuestions);
            questions2.questionIndex = indexToDisplay;
            int sortedVal = indexToDisplay + firstItemPadding;
            questions2.setPosition(sortedVal * PADDING);
            acc.add(questions2);

            switch (questions2.questionType) {
                case QuestionType.drug:
                    parseDrugs(acc, sortedVal, questions2);
                    break;
                case QuestionType.reminder:
                    parseReminder(acc, sortedVal, questions2);
                    break;
                case QuestionType.fill:
                    parseFill(acc, sortedVal, questions2);
                    break;
                case QuestionType.upImg:
                    parseUpImg(acc, sortedVal, questions2);
                    break;
                case QuestionType.sTime:
                    parsePickTime(acc, sortedVal, questions2);
                    break;
                case QuestionType.sDate:
                    parsePickDate(acc, sortedVal, questions2);
                    break;
                case QuestionType.asel:
                    parsePickHospital(acc, sortedVal, questions2);
                    break;
                case QuestionType.keepon:
                    parseFurtherConsultation(acc, sortedVal, questions2);
                    break;
                case QuestionType.rectangle:
                default:
                    parseOptions(acc, sortedVal, questions2);
                    break;
            }

            BaseItem divider = new BaseItem(R.layout.divider_1px_top13);
            int dividerPosition = positionIn(sortedVal, DIVIDER_POSITION);
            divider.setItemId("DIVIDER" + questions2.getKey());
            divider.setPosition(dividerPosition);
            acc.add(divider);

            if (questions2.questionsButton != null) {
                List<SortedItem> sortedItems = parseQuestion(questions2.questionsButton.questions, 0, indexToDisplay + 1, new ArrayList<SortedItem>());
                questions2.questionsButton.setDatas(sortedItems);
                questions2.questionsButton.setPosition(positionIn(sortedVal, BUTTON_POSITION));
                questions2.questionsButton.setItemId(questions2.getKey() + "QUESTION_BUTTON");


                acc.add(questions2.questionsButton);
                indexToDisplay += questionsSize(questions2.questionsButton.questions, 0, 0);
            }

            return parseQuestion(questions, indexAtQuestions + 1,
                    indexToDisplay + 1, acc);
        }
    }

    public int questionsSize(List<Questions2> questions, int i, int acc) {
        if (questions == null) {
            return 0;
        }
        if (i >= questions.size()) {
            return acc;
        }
        QuVisibilityManager questionsButton = questions.get(i).questionsButton;
        if (questionsButton != null) {
            int subQuestionsSize = questionsSize(questionsButton.questions, 0, 0);
            return questionsSize(questions, i + 1, acc + 1 + subQuestionsSize);
        } else {
            return questionsSize(questions, i + 1, acc + 1);
        }
    }

    private void parseOptions(List<SortedItem> items, int i, Questions2 questions2) {
        List<Options2> option = questions2.option;
        if (option == null || option.isEmpty()) return;

        for (int j = 0; j < option.size(); j++) {
            Options2 options2 = option.get(j);

            options2.questionId = questions2.getKey();
            options2.questionType = questions2.questionType;
            options2.questionContent = questions2.questionContent;

            int position = i * PADDING + j + 1;
            options2.setPosition(position);
            items.add(options2);
        }
    }

    private void parseFurtherConsultation(List<SortedItem> items, int i, Questions2 questions2) {
        FurtherConsultationVM vm = new FurtherConsultationVM();
        vm.questions2 = questions2;
        vm.setPosition(i * PADDING);
        vm.setQuestionId(questions2.getKey());
        vm.setQuestionContent(questions2.questionContent);
        if (questions2.option != null) {
            for (Options2 options2 : questions2.option) {
                switch (options2.optionType) {
                    case "A": {
                        if (options2.getSelected()) {
                            vm.setHasAnswer(true);
                            vm.setBtnOneChecked(true);
                            vm.setDate(options2.questionContent);
                        }
                        vm.setBtnOneEnabled(true);

                        break;
                    }
                    case "B": {
                        if (options2.getSelected()) {
                            vm.setHasAnswer(true);
                            vm.setBtnTwoChecked(true);
                            vm.setDate(options2.questionContent);
                        }
                        vm.setBtnTwoEnabled(true);

                        break;
                    }
                    case "C": {
                        if (options2.getSelected()) {
                            vm.setHasAnswer(true);
                            vm.setBtnThreeChecked(true);
                            Doctor doctor = new Doctor();
                            doctor.fromHashMap(options2.selectedOption);
                            vm.setDoctor(doctor);
                        }
                        vm.setBtnThreeEnabled(true);
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
        //默认给这个url
        String url = "tool/endemicAreaTrees";
        if (questions2.option != null) {
            for (Options2 options2 : questions2.option) {
                if (options2.optionType.equals("A")) {
                    url = options2.optionContent;
                }
            }
        }
        ItemPickHospital pickHospital = new ItemPickHospital(answerContent, url, lv1Id, lv2Id, lv3Id);
        pickHospital.setPosition(positionIn(i, RANGE_ITEM_POSITION));
        pickHospital.setItemId(questions2.getKey() + QuestionType.asel);
        items.add(pickHospital);
    }

    private int positionIn(int parentPosition, int beforeNextParent) {
        return (parentPosition + 1) * PADDING - beforeNextParent;
    }

    private void parsePickDate(List<SortedItem> items, int i, final Questions2 questions2) {
        ItemPickDate itemPickDate = new ItemPickDate(R.layout.item_pick_date3, "");
        itemPickDate.setPosition(positionIn(i, RANGE_ITEM_POSITION));
        itemPickDate.setItemId(questions2.getKey() + QuestionType.sDate);
        itemPickDate.setDate(questions2.fillContent);
        itemPickDate.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                questions2.answerCount = 1;
                questions2.notifyChange();
            }
        });
        questions2.answerCount = questions2.fillContent == null ? 0 : questions2.fillContent.length();
        itemPickDate.isAnswered = questions2.fillContent != null;
        items.add(itemPickDate);
    }

    private void parsePickTime(List<SortedItem> items, int i, final Questions2 questions2) {
        ItemPickTime itemPickTime = new ItemPickTime(R.layout.item_pick_question_time, "");
        itemPickTime.setPosition(positionIn(i, RANGE_ITEM_POSITION));
        itemPickTime.setItemId(questions2.getKey() + QuestionType.sTime);
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
        final ItemPickImages pickerItem = new ItemPickImages(R.layout.item_pick_image, "");
        if (questions2.fillContent != null && !questions2.fillContent.equals("")) {
            String[] split = questions2.fillContent.split(",");
            for (int j = 0; j < split.length; j++) {
                ItemPickImages item = new ItemPickImages(R.layout.item_view_image, split[j]);
                item.setPosition(i * PADDING + j + 1);
                item.setItemId(UUID.randomUUID().toString());
                item.setParentId(questions2.getKey());
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
        pickerItem.setPosition(positionIn(i, RANGE_ITEM_POSITION));
        pickerItem.setParentId(questions2.getKey());
        pickerItem.setItemId(questions2.getKey() + QuestionType.upImg);
        items.add(pickerItem);
    }

    private void parseFill(List<SortedItem> items, int i, final Questions2 questions2) {
        final ItemTextInput2 textInput = new ItemTextInput2(R.layout.item_text_input6, "", "");
        textInput.setMaxLength(questions2.extendType);
        textInput.setPosition(positionIn(i, RANGE_ITEM_POSITION));
        textInput.setItemId(questions2.getKey() + QuestionType.fill);
        textInput.setResult(questions2.fillContent);
        if (Strings.isNullOrEmpty(textInput.getResult())) {
            textInput.setResult("");
        }
        questions2.answerCount = textInput.getResult().length();
        textInput.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                questions2.answerCount = textInput.getResult().length();
                questions2.notifyChange();
            }
        });
        questions2.answerCount = questions2.fillContent == null ? 0 : questions2.fillContent.length();
        items.add(textInput);
    }

    private void parseReminder(List<SortedItem> items, int i, final Questions2 questions2) {
        final ItemAddReminder list = new ItemAddReminder();
        list.setPosition(positionIn(i, RANGE_ITEM_POSITION));
        list.setItemId(questions2.getKey() + QuestionType.reminder);
        List<Map<String, String>> arrayContent = questions2.arrayContent;
        if (arrayContent != null) {
            for (int j = 0; j < arrayContent.size(); j++) {
                Reminder reminder = Reminder.fromMap(arrayContent.get(j));
                ItemPickDate itemPickDate = list.createReminder();
                itemPickDate.setTitle(reminder.content);
                itemPickDate.setDate(reminder.time);
                list.registerItemChangedListener(itemPickDate);
                items.add(itemPickDate);
            }
            questions2.answerCount = arrayContent.size();
        }
        list.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                questions2.answerCount = list.itemSize();
                questions2.notifyChange();
            }
        });

        items.add(list);
    }

    private void parseDrugs(List<SortedItem> items, int i, final Questions2 questions2) {
        final ItemAddPrescription2 itemAddPrescription = new ItemAddPrescription2();
        List<Map<String, String>> arrayContent = questions2.arrayContent;

        if (arrayContent != null) {
            for (int j = 0; j < arrayContent.size(); j++) {
                Prescription prescription = PrescriptionHandler.fromHashMap(arrayContent.get(j));
                prescription.setPosition(i * PADDING + j + 1);
                prescription.setItemId(UUID.randomUUID().toString());
                itemAddPrescription.registerItemChangedListener(prescription);
                items.add(prescription);
            }
            questions2.answerCount = arrayContent.size();
        }

        int size = 0;
        List<Options2> option = questions2.option;
        if (option != null && !option.isEmpty()) {
            size = option.size();
            for (int j = 0; j < size; j++) {
                Options2 options2 = option.get(j);

                options2.questionId = questions2.getKey();
                options2.questionType = questions2.questionType;
                options2.questionContent = questions2.questionContent;

                options2.setPosition(positionIn(i, size - j + 2));
                items.add(options2);
            }
        }

        itemAddPrescription.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                questions2.answerCount = itemAddPrescription.itemSize();
                questions2.notifyChange();
            }
        });
        itemAddPrescription.setPosition(positionIn(i, size + RANGE_ITEM_POSITION));
        itemAddPrescription.setItemId(questions2.getKey() + QuestionType.drug);
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

    private Response<ApiDTO<String>> postAnswer(String appointmentId, String type, String string, String qType, int i) {
        try {
            return api.saveQuestions2(type, appointmentId, string, qType, i).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveAnswer(final String appointmentId, final String type, final String qType, final int endAppointment, final SortedListAdapter adapter) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String string = composeAnswer(adapter);
                Response<ApiDTO<String>> apiDTOResponse = postAnswer(appointmentId, type, string, qType, endAppointment);
                if (apiDTOResponse != null && apiDTOResponse.isSuccessful()) {
                    EventHub.post(new SaveAnswerSuccessEvent());
                }
            }
        }).start();

    }
}
