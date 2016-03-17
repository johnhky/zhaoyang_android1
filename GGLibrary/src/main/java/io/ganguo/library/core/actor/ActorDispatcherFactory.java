package io.ganguo.library.core.actor;

/**
 * 内部操作分发器工厂
 * <p/>
 * Created by Tony on 10/7/15.
 */
public class ActorDispatcherFactory {

    private static ActorDispatcher mThreads = null;
    private static ActorDispatcher mQueue = null;
    private static ActorDispatcher mUI = null;

    public static ActorDispatcher getThreads() {
        if (mThreads == null) {
            mThreads = new ThreadsActorDispatcher();
        }
        return mThreads;
    }

    public static ActorDispatcher getQueue() {
        if (mQueue == null) {
            mQueue = new QueueActorDispatcher();
        }
        return mQueue;
    }

    public static ActorDispatcher getUI() {
        if (mUI == null) {
            mUI = new UIActorDispatcher();
        }
        return mUI;
    }

}
