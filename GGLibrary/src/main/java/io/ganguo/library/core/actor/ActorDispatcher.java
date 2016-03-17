package io.ganguo.library.core.actor;

/**
 * 内部操作分发器接口
 * <p/>
 * Created by Tony on 10/6/15.
 */
public interface ActorDispatcher {

    /**
     * 把一个event放入事件队列的末尾等待处理
     * 线程安全的
     *
     * @param actor 事件Actor
     */
    void pushActor(Actor actor);

}
