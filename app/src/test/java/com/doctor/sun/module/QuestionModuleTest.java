package com.doctor.sun.module;

import com.doctor.sun.ConfigImpl;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.AppointmentBuilder;
import com.doctor.sun.entity.Doctor;
import com.doctor.sun.entity.MedicalRecord;
import com.doctor.sun.entity.ReserveDate;
import com.doctor.sun.entity.Time;
import com.doctor.sun.entity.constans.AppointmentType;
import com.doctor.sun.http.ApiMock;
import com.doctor.sun.immutables.Appointment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static org.junit.Assert.fail;

/**
 * Created by rick on 5/9/2016.
 */
public class QuestionModuleTest {
    public static final String TAG = QuestionModuleTest.class.getSimpleName();
    @Mock
    QuestionModule api = ApiMock.of(QuestionModule.class);

    @Mock
    TimeModule timeModule = ApiMock.of(TimeModule.class);
    private int doctorId;
    private int medicalRecordId;
    private int duration;
    private int appointmentType;

    @Before
    public void setup() throws IOException {
        ConfigImpl.getInstance().setToken("41d354f5e505adf5e77c1b7aa787608a");
        doctorId = 293;
        medicalRecordId = 225;
        duration = 15;
        appointmentType = 1;
    }

    @Test
    public void makeNewAppointment() {
        final AppointmentBuilder builder = new AppointmentBuilder(ApiMock.of(ProfileModule.class), ApiMock.of(AppointmentModule.class), ApiMock.of(ToolModule.class));
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        MedicalRecord record = new MedicalRecord();
        record.setMedicalRecordId(medicalRecordId);
        builder.setRecord(record);
        builder.setDoctor(doctor);
        builder.setDuration(duration);
        builder.setType(appointmentType);

        try {
            int doctorId = builder.getDoctor().getId();
            Response<ApiDTO<List<ReserveDate>>> response = timeModule.getDateSchedule(doctorId, builder.getDuration()).execute();
            if (response != null && response.isSuccessful() && response.body() != null) {
                ReserveDate date = response.body().getData().get(0);
                Response<ApiDTO<List<Time>>> execute = timeModule.getDaySchedule(doctorId, date.getDate(), builder.getType(), String.valueOf(builder.getDuration())).execute();
                List<Time> data = execute.body().getData();
                for (int size = data.size(); size > 0; size--) {
                    Time time = data.get(size - 1);
                    if (time.getReserva() == 0) {
                        time.setDate(date.getDate());
                        builder.setTime(time);
                        break;
                    }
                }
                int type = builder.getType();

                HashMap<String, String> params = new HashMap<String, String>();
                if (type == AppointmentType.PREMIUM) {
                    params.put("takeTime", String.valueOf(builder.getDuration()));
                }

                final String medicalRecordId = String.valueOf(builder.getRecord().getMedicalRecordId());

                AppointmentModule appointmentModule = ApiMock.of(AppointmentModule.class);
                Call<ApiDTO<Appointment>> apiDTOCall = appointmentModule.orderAppointment(doctorId, builder.getTimestamp(), AppointmentType.PREMIUM, medicalRecordId, builder.getFinalCouponId(), builder.getSelectedTagIds(), params);

                try {
                    Response<ApiDTO<Appointment>> execute1 = apiDTOCall.execute();

                } catch (IOException e) {

                }
            }
        } catch (IOException e) {
            fail("获取时间失败");
        }
    }

    @Test
    public void templates2() throws Exception {


    }


}