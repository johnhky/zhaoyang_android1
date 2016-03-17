package io.ganguo.library.util;

import io.ganguo.library.ApplicationTest;

/**
 * Created by Tony on 10/16/15.
 */
public class TasksTest extends ApplicationTest {

    public void testTask() {
        Tasks.runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });
        Tasks.runOnQueue(new Runnable() {
            @Override
            public void run() {

            }
        });
        Tasks.runOnThreadPool(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

}
