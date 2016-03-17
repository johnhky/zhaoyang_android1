package io.ganguo.opensdk;

import android.content.Context;

import cn.sharesdk.framework.PlatformActionListener;

/**
 * Created by Lynn on 2/22/16.
 */
public abstract class BaseStepBuilder {
    /**
     * commit 返回ShareStep, 代表参数设置完成
     */
    protected interface OptionalStep {
        ShareStep commit();
    }

    public interface ShareStep {
        void share();
    }

    protected static abstract class BaseStep implements ShareStep {
        protected Context context;
        protected PlatformActionListener listener;

        protected BaseStep(Context context, PlatformActionListener platformActionListener) {
            this.context = context;
            this.listener = platformActionListener;
        }

        protected BaseStep() {
        }

        public Context getContext() {
            return context;
        }

        public PlatformActionListener getListener() {
            return listener;
        }
    }
}
