package io.ganguo.library.util;

import io.ganguo.library.ApplicationTest;
import io.ganguo.library.util.log.Logger;
import io.ganguo.library.util.log.LoggerFactory;

/**
 * Created by Tony on 9/30/15.
 */
public class StringsText extends ApplicationTest {

    private Logger logger = LoggerFactory.getLogger(StringsText.class);

    public void testEmpty() throws Exception {
        assertTrue(StringsUtils.isEmpty(null, ""));
        assertTrue(StringsUtils.isNotEmpty("a"));
    }

    public void testEquals() throws Exception {
        assertTrue(StringsUtils.isEquals("a", "a"));
        assertFalse(StringsUtils.isEquals("a", "b"));
        assertTrue(StringsUtils.isEqualsIgnoreCase("a", "A"));
        assertFalse(StringsUtils.isEqualsIgnoreCase("a", "b"));
    }

    public void testFormat() throws Exception {
        String pattern = "{0} is {1}";
        String result = StringsUtils.format(pattern, "apple", "fruit");

        assertEquals(result, "apple is fruit");
    }

    public void testEmail() {
        String text = "tony@ganguo.io";
        String text2 = "tony@ganguo";

        assertTrue(StringsUtils.isEmail(text));
        assertFalse(StringsUtils.isEmail(text2));
    }

    public void testMobile() {
        String text = "13878561400";
        String text2 = "1387856140";

        assertTrue(StringsUtils.isMobile(text));
        assertFalse(StringsUtils.isMobile(text2));
    }

    public void testDeviceId() {
        logger.i("testDeviceId: " + StringsUtils.randomUUID());
    }
}
