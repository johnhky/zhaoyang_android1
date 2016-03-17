package io.ganguo.library.core.event;

/**
 * Created by Tony on 10/7/15.
 */
public class ActionEvent {
    private Type type;
    private Object target;

    public ActionEvent(Type type, Object target) {
        this.type = type;
        this.target = target;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }

    public enum Type {
        OK, ERROR
    }
}
