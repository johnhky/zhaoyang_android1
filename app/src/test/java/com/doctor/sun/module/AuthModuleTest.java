package com.doctor.sun.module;

import com.doctor.sun.ConfigImpl;
import com.doctor.sun.dto.ApiDTO;
import com.doctor.sun.entity.Token;
import com.doctor.sun.http.ApiMock;
import com.doctor.sun.http.callback.SimpleCallback;
import com.doctor.sun.util.MD5;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.fail;
import static org.junit.Assert.assertNull;

/**
 * Created by rick on 21/6/2016.
 */
public class AuthModuleTest {
    public static final String TAG = AuthModuleTest.class.getSimpleName();

    @Mock
    private AuthModule api = ApiMock.of(AuthModule.class);

    private Token token;

    @Before
    public void setup() throws IOException {
        String password = MD5.getMessageDigest("a123456".getBytes());
        Call<ApiDTO<Token>> register = api.login("13922331300", password);
        Response<ApiDTO<Token>> response = register.execute();
        if (response.isSuccessful()) {
            assertNotNull(response.body().getData());
            ConfigImpl.getInstance().setToken(response.body().getData().getToken());
        } else {
            assertNull(response.message());
        }
    }

    @Test
    public void testRegister() throws Exception {
        String password = MD5.getMessageDigest("a123456".getBytes());
        Call<ApiDTO<Token>> register = api.register(AuthModule.DOCTOR_TYPE, "1392233333", "123456", password);
        register.enqueue(new SimpleCallback<Token>() {
            @Override
            protected void handleResponse(Token response) {
                token = response;
                assert response != null;
            }
        });
    }

    @Test
    public void testSendCaptcha() throws Exception {

    }

    @Test
    public void testLogin() throws Exception {
        String password = MD5.getMessageDigest("a123456".getBytes());
        Call<ApiDTO<Token>> register = api.login("13922331300", password);
        Response<ApiDTO<Token>> response = register.execute();
        if (response.isSuccessful()) {
            assertNotNull(response.body().getData().getAccount());
            ConfigImpl.getInstance().setToken(response.body().getData().getToken());
        } else {
            fail();
        }
    }

    @Test
    public void testReset() throws Exception {

    }

    @Test
    public void testLogout() throws Exception {
        Call<ApiDTO<String>> logout = api.logout(ConfigImpl.getInstance().getToken());
        Response<ApiDTO<String>> execute = logout.execute();
        if (execute.isSuccessful()) {
            assertEquals(execute.body().getMessage(),"");
        } else {
            fail();
        }
    }
}