package io.ganguo.library.core.actor;

import io.ganguo.library.util.Tasks;

/**
 * UI线程的内部分发器，每次只运行一个实例，队列式运行
 * <p/>
 * Created by Tony on 10/6/15.
 */
public class UIActorDispatcher implements ActorDispatcher {

    /**
     * 把一个event放入事件队列的末尾等待处理
     *
     * @param actor 事件Actor
     */
    @Override
    public void pushActor(final Actor actor) {
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                actor.execute();
            }
        });
    }

}
