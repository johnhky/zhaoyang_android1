package com.doctor.sun.model;

import com.doctor.sun.R;
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
import com.doctor.sun.util.Function0;
import com.doctor.sun.vo.FurtherConsultationVM;
import com.doctor.sun.vo.ItemAddPrescription2;
import com.doctor.sun.vo.ItemPickDate;
import com.doctor.sun.vo.ItemPickTime;
import com.doctor.sun.vo.ItemReminderList;
import com.doctor.sun.vo.ItemTextInput;
import com.doctor.sun.vo.PickImageViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by rick on 28/7/2016.
 */

public class QuestionsModel {
    public static final int PADDING = 1000;
    private QuestionModule api = Api.of(QuestionModule.class);


    public void questions(String appointmentId, final Function0<List<? extends SortedItem>> function0) {
        api.questions2(appointmentId).enqueue(new SimpleCallback<List<Questions2>>() {
            @Override
            protected void handleResponse(List<Questions2> response) {
                parseQuestions(response);
                function0.apply(parseQuestions(response));
            }
        });
    }

    public List<SortedItem> parseQuestions(List<Questions2> response) {
        List<SortedItem> items = new ArrayList<SortedItem>();
        for (int i = 0; i < response.size(); i++) {
            Questions2 questions2 = response.get(i);
            questions2.position = i * PADDING;
            items.add(questions2);

            switch (questions2.baseQuestionType) {
                case QuestionType.drug:
                    for (int j = 0; j < questions2.wtfContent.size(); j++) {
                        Prescription prescription = new Prescription();
                        prescription.position = i * PADDING + j + 1;
                        prescription.itemId = UUID.randomUUID().toString();
                        prescription.fromHashMap(questions2.wtfContent.get(j));
                        items.add(prescription);
                    }

                    ItemAddPrescription2 itemAddPrescription = new ItemAddPrescription2();
                    itemAddPrescription.setPosition((i + 1) * PADDING - 1);
                    itemAddPrescription.setItemId(questions2.baseQuestionId + QuestionType.drug);
                    items.add(itemAddPrescription);
                    break;

                case QuestionType.reminder:
                    ItemReminderList list = new ItemReminderList();
                    list.setPosition((i + 1) * PADDING - 1);
                    list.setItemId(questions2.baseQuestionId + "reminder");
                    items.add(list);
                    break;

                //TODO
                case QuestionType.fill:
                    ItemTextInput textInput = new ItemTextInput(R.layout.item_text_input6, "");
                    textInput.setPosition((i + 1) * PADDING - 1);
                    items.add(textInput);
                    break;
                case QuestionType.upImg:

                    if (questions2.fillContent != null && !questions2.fillContent.equals("")) {
                        String[] split = questions2.fillContent.split(",");
                        for (int j = 0; j < split.length; j++) {
                            PickImageViewModel item = new PickImageViewModel(R.layout.item_pick_image, split[j]);
                            item.setPosition(i * PADDING + j + 1);
                            item.setItemId(UUID.randomUUID().toString());
                            items.add(item);
                        }
                    }
                    PickImageViewModel itemPickImage = new PickImageViewModel(R.layout.item_pick_image, "");
                    itemPickImage.setPosition((i + 1) * PADDING - 1);
                    itemPickImage.setItemId(questions2.baseQuestionId + "pick_image");
                    items.add(itemPickImage);
                    break;

                case QuestionType.sTime:
                    ItemPickTime itemPickTime = new ItemPickTime(R.layout.item_pick_question_time, "");
                    itemPickTime.setPosition((i + 1) * PADDING - 1);
                    itemPickTime.setItemId(questions2.baseQuestionId + "pick_time");
                    items.add(itemPickTime);
                    break;
                case QuestionType.sDate:
                    ItemPickDate itemPickDate = new ItemPickDate(R.layout.item_pick_date3, "");
                    itemPickDate.setPosition((i + 1) * PADDING - 1);
                    itemPickDate.setItemId(questions2.baseQuestionId + "pick_date");
                    items.add(itemPickDate);
                    break;

                case QuestionType.asel:
                    ItemPickHospital pickHospital = new ItemPickHospital(1, 1, 1);
                    pickHospital.setPosition((i + 1) * PADDING - 1);
                    pickHospital.itemId = questions2.baseQuestionId + "hospital";
                    items.add(pickHospital);
                    break;

                case QuestionType.keepon:
                    FurtherConsultationVM vm = new FurtherConsultationVM();
                    vm.setPosition(i * PADDING);
                    vm.setQuestionId(questions2.baseQuestionId);
                    vm.setQuestionContent(questions2.baseQuestionContent);

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

                        options2.position = i * PADDING + j + 1;
                        items.add(options2);
                    }
                    break;

            }
        }
        return items;
    }
}
