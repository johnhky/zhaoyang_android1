package com.doctor.sun.emoji;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Xml;

import com.doctor.sun.AppContext;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class EmojiManager {
    public static final String ASSET_PREFIX = "file:///android_asset/";

    private static final String EMOT_DIR = "emoji/";

    // max cache size
    private static final int CACHE_MAX_SIZE = 1024;

    private static Pattern pattern;

    // default entries
    private static final List<Entry> defaultEntries = new ArrayList<Entry>();
    // text to entry
    private static final Map<String, Entry> text2entry = new HashMap<String, Entry>();
    // asset bitmap cache, key: asset path
    private static int PER_PAGE = 20;

    static {
        Context context = AppContext.me();

        load(context, EMOT_DIR + "emoji.xml");

        pattern = makePattern();
    }

    private static class Entry {
        String text;
        String assetPath;

        Entry(String text, String assetPath) {
            this.text = text;
            this.assetPath = assetPath;
        }
    }

    //
    // display
    //

    public static final int getDisplayCount() {
        return defaultEntries.size();
    }

    public static final int getPageCount() {
        int fullCount = EmojiManager.getDisplayCount() / PER_PAGE;
        int remainPage = EmojiManager.getDisplayCount() % PER_PAGE > 0 ? 1 : 0;
        return fullCount + remainPage;
    }

    public static final String getDisplayText(int index) {
        return index >= 0 && index < defaultEntries.size() ? defaultEntries
                .get(index).text : null;
    }

    public static final String getAssetPath(int index) {
        return index >= 0 && index < defaultEntries.size() ? defaultEntries
                .get(index).assetPath : null;
    }


    public static final Pattern getPattern() {
        return pattern;
    }

    //
    // internal
    //

    private static Pattern makePattern() {
        return Pattern.compile(patternOfDefault());
    }

    private static String patternOfDefault() {
        return "\\[[^\\[]{1,10}\\]";
    }

    private static final void load(Context context, String xmlPath) {
        new EntryLoader().load(context, xmlPath);
    }

    //
    // load emoticons from asset
    //
    private static class EntryLoader extends DefaultHandler {
        private String catalog = "";

        void load(Context context, String assetPath) {
            InputStream is = null;
            try {
                is = context.getAssets().open(assetPath);
                Xml.parse(is, Xml.Encoding.UTF_8, this);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            if (localName.equals("Catalog")) {
                catalog = attributes.getValue(uri, "Title");
            } else if (localName.equals("Emoticon")) {
                String tag = attributes.getValue(uri, "Tag");
                String fileName = attributes.getValue(uri, "File");
                Entry entry = new Entry(tag, ASSET_PREFIX + EMOT_DIR + catalog + "/" + fileName);

                text2entry.put(entry.text, entry);
                if (catalog.equals("default")) {
                    defaultEntries.add(entry);
                }
            }
        }
    }
}
