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
        assertTrue(Strings.isEmpty(null, ""));
        assertTrue(Strings.isNotEmpty("a"));
    }

    public void testEquals() throws Exception {
        assertTrue(Strings.isEquals("a", "a"));
        assertFalse(Strings.isEquals("a", "b"));
        assertTrue(Strings.isEqualsIgnoreCase("a", "A"));
        assertFalse(Strings.isEqualsIgnoreCase("a", "b"));
    }

    public void testFormat() throws Exception {
        String pattern = "{0} is {1}";
        String result = Strings.format(pattern, "apple", "fruit");

        assertEquals(result, "apple is fruit");
    }

    public void testEmail() {
        String text = "tony@ganguo.io";
        String text2 = "tony@ganguo";

        assertTrue(Strings.isEmail(text));
        assertFalse(Strings.isEmail(text2));
    }

    public void testMobile() {
        String text = "13878561400";
        String text2 = "1387856140";

        assertTrue(Strings.isMobile(text));
        assertFalse(Strings.isMobile(text2));
    }

    public void testDeviceId() {
        logger.i("testDeviceId: " + Strings.randomUUID());
    }
}
