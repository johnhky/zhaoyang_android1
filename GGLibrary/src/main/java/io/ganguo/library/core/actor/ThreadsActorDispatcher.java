package io.ganguo.library.core.actor;

import io.ganguo.library.util.Tasks;

/**
 * 多线程的内部分发器，可以同时使用多个实例里
 * <p/>
 * Created by Tony on 10/6/15.
 */
public class ThreadsActorDispatcher implements ActorDispatcher {

    /**
     * 把一个event放入事件队列的末尾等待处理
     *
     * @param actor 事件Actor
     */
    @Override
    public void pushActor(final Actor actor) {
        Tasks.runOnThreadPool(new Runnable() {
            @Override
            public void run() {
                actor.execute();
            }
        });
    }

}
