package com.doctor.sun.model;

import com.doctor.sun.R;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.dto.QuestionDTO;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.Options2;
import com.doctor.sun.entity.Prescription;
import com.doctor.sun.entity.Questions2;
import com.doctor.sun.entity.constans.QuestionType;
import com.doctor.sun.http.Api;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.module.QuestionModule;
import com.doctor.sun.ui.activity.ItemPickHospital;
import com.doctor.sun.ui.adapter.ViewHolder.SortedItem;
import com.doctor.sun.ui.adapter.core.SortedListAdapter;
import com.doctor.sun.util.Function0;
import com.doctor.sun.util.JacksonUtils;
import com.doctor.sun.vo.FurtherConsultationVM;
import com.doctor.sun.vo.ItemAddPrescription2;
import com.doctor.sun.vo.ItemPickDate;
import com.doctor.sun.vo.ItemPickImage;
import com.doctor.sun.vo.ItemPickTime;
import com.doctor.sun.vo.ItemReminderList;
import com.doctor.sun.vo.ItemTextInput;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Response;

/**
 * Created by rick on 28/7/2016.
 */

public class QuestionsModel {
    public static final String TAG = QuestionsModel.class.getSimpleName();
    public static final int PADDING = 1000;
    private QuestionModule api = Api.of(QuestionModule.class);


    public void questions(int appointmentId, final Function0<List<? extends SortedItem>> function0) {
        api.questions2(appointmentId).enqueue(new SimpleCallback<QuestionDTO>() {
            @Override
            protected void handleResponse(QuestionDTO response) {
                function0.apply(parseQuestions(response.questions));
            }
        });
    }

    public List<SortedItem> parseQuestions(List<Questions2> response) {
        List<SortedItem> items = new ArrayList<SortedItem>();
        if (response == null || response.isEmpty()) {
            return items;
        }
        for (int i = 0; i < response.size(); i++) {
            Questions2 questions2 = response.get(i);
            questions2.position = i * PADDING;
            items.add(questions2);

            switch (questions2.baseQuestionType) {
                case QuestionType.drug:
                    ItemAddPrescription2 itemAddPrescription = new ItemAddPrescription2();
                    for (int j = 0; j < questions2.wtfContent.size(); j++) {
                        Prescription prescription = new Prescription();
                        prescription.position = i * PADDING + j + 1;
                        prescription.itemId = UUID.randomUUID().toString();
                        prescription.fromHashMap(questions2.wtfContent.get(j));
                        items.add(prescription);
                        itemAddPrescription.getPrescriptions().add(prescription);
                    }

                    itemAddPrescription.setPosition((i + 1) * PADDING - 1);
                    itemAddPrescription.setItemId(questions2.baseQuestionId + QuestionType.drug);
                    items.add(itemAddPrescription);
                    break;

                case QuestionType.reminder:
                    ItemReminderList list = new ItemReminderList();
                    list.setPosition((i + 1) * PADDING - 1);
                    list.setItemId(questions2.baseQuestionId + QuestionType.reminder);
                    items.add(list);
                    break;

                //TODO
                case QuestionType.fill:
                    ItemTextInput textInput = new ItemTextInput(R.layout.item_text_input6, "");
                    textInput.setPosition((i + 1) * PADDING - 1);
                    textInput.setItemId(questions2.baseQuestionId + QuestionType.fill);
                    items.add(textInput);
                    break;
                case QuestionType.upImg:

                    if (questions2.fillContent != null && !questions2.fillContent.equals("")) {
                        String[] split = questions2.fillContent.split(",");
                        for (int j = 0; j < split.length; j++) {
                            ItemPickImage item = new ItemPickImage(R.layout.item_pick_image, split[j]);
                            item.setPosition(i * PADDING + j + 1);
                            item.setItemId(UUID.randomUUID().toString());
                            items.add(item);
                        }
                    }
                    ItemPickImage itemPickImage = new ItemPickImage(R.layout.item_pick_image, "");
                    itemPickImage.setPosition((i + 1) * PADDING - 1);
                    itemPickImage.setItemId(questions2.baseQuestionId + QuestionType.upImg);
                    items.add(itemPickImage);
                    break;

                case QuestionType.sTime:
                    ItemPickTime itemPickTime = new ItemPickTime(R.layout.item_pick_question_time, "");
                    itemPickTime.setPosition((i + 1) * PADDING - 1);
                    itemPickTime.setItemId(questions2.baseQuestionId + QuestionType.sTime);
                    items.add(itemPickTime);
                    break;
                case QuestionType.sDate:
                    ItemPickDate itemPickDate = new ItemPickDate(R.layout.item_pick_date3, "");
                    itemPickDate.setPosition((i + 1) * PADDING - 1);
                    itemPickDate.setItemId(questions2.baseQuestionId + QuestionType.sDate);
                    items.add(itemPickDate);
                    break;

                case QuestionType.asel:
                    ItemPickHospital pickHospital = new ItemPickHospital(1, 1, 1);
                    pickHospital.setPosition((i + 1) * PADDING - 1);
                    pickHospital.setItemId(questions2.baseQuestionId + QuestionType.asel);
                    items.add(pickHospital);
                    break;

                case QuestionType.keepon:
                    FurtherConsultationVM vm = new FurtherConsultationVM();
                    vm.setPosition(i * PADDING);
                    vm.setQuestionId(questions2.baseQuestionId);
                    vm.setQuestionContent(questions2.baseQuestionContent);

                    if (questions2.option != null)
                        for (Options2 options2 : questions2.option) {
                            if (options2.getSelected()) {
                                vm.setHasAnswer(true);
                                switch (options2.optionType) {
                                    case "A": {
                                        vm.setBtnOneChecked(true);
//                                            vm.setDate(content.get(0).toString());

                                        break;
                                    }
                                    case "B": {
                                        vm.setBtnTwoChecked(true);
//                                            vm.setDate(content.get(0).toString());

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
                    break;
                case QuestionType.rectangle:
                default:
                    List<Options2> option = questions2.option;
                    if (option == null || option.isEmpty()) continue;

                    for (int j = 0; j < option.size(); j++) {
                        Options2 options2 = option.get(j);

                        options2.questionId = questions2.baseQuestionId;
                        options2.questionType = questions2.baseQuestionType;

                        options2.setPosition(i * PADDING + j + 1);
                        items.add(options2);
                    }
                    break;

            }
        }
        return items;
    }

    private String composeAnswer(SortedListAdapter adapter) {
        ArrayList<Object> result = new ArrayList<>();
        for (int i = 0; i < adapter.size(); i++) {
            SortedItem sortedItem = adapter.get(i);
            HashMap<String, Object> stringObjectHashMap = sortedItem.toJson(adapter);
            if (stringObjectHashMap != null) {
                result.add(stringObjectHashMap);
            }
        }
        return JacksonUtils.toJson(result);
    }

    private Response<ApiDTO<String>> postAnswer(int appointmentId, String string) {
        try {
            return api.saveQuestions2(appointmentId, string).execute();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void saveAnswer(final int appointmentId, final SortedListAdapter adapter) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                postAnswer(appointmentId, composeAnswer(adapter));
            }
        }).start();

    }
}
