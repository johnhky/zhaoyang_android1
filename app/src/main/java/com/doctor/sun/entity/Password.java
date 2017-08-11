package com.doctor.sun.entity;

/**
 * Created by heky on 17/6/26.
 */

public class Password {
    public boolean is_set_passwd;

    public boolean is_set_password() {
        return is_set_passwd;
    }

    public void setIs_set_password(boolean is_set_password) {
        this.is_set_passwd = is_set_password;
    }


    @Override
    public String toString() {
        return "password: "+is_set_passwd;
    }
}
