package io.ganguo.library;

import java.io.Serializable;

/**
 * Created by Wilson on 10/10/15.
 */
public class TestBean implements Serializable {
    private int id;
    private String name;
    private boolean isReady;
    private boolean ok;

    public TestBean() {
    }

    public TestBean(int id, String name, boolean isReady, boolean ok) {
        this.id = id;
        this.name = name;
        this.isReady = isReady;
        this.ok = ok;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isReady=" + isReady +
                ", ok=" + ok +
                '}';
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean isReady() {
        return isReady;
    }

    public void setIsReady(boolean isReady) {
        this.isReady = isReady;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}