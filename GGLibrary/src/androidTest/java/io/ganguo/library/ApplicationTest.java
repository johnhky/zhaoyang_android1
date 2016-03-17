package io.ganguo.library;

import android.test.ApplicationTestCase;

import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<BaseApp> {

    protected Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    public ApplicationTest() {
        super(BaseApp.class);
    }
}