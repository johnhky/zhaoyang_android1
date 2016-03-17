package io.ganguo.library.util.gson;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;

import io.ganguo.library.util.date.Date;
import io.ganguo.library.util.date.DateTime;
import io.ganguo.library.util.date.DateTimeZone;

import static com.google.gson.FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES;


/**
 * Gson 工具类
 * <p>
 * Created by tony on 8/21/14.
 */
public abstract class GsonUtils {

    private static Gson GSON = createGson(true);
    private static Gson GSON_NO_NULLS = createGson(false);

    /**
     * gson builder
     *
     * @param builder
     */
    public static void createGson(GsonBuilder builder) {
        GSON = builder.create();
        GSON_NO_NULLS = builder.create();
    }

    /**
     * Create the standard {@link Gson} configuration
     *
     * @return created gson, never null
     */
    public static final Gson createGson() {
        return createGson(true);
    }

    /**
     * Create the standard {@link Gson} configurationØ
     *
     * @param serializeNulls whether nulls should be serialized
     * @return created gson, never null
     */
    public static final Gson createGson(final boolean serializeNulls) {
        final GsonBuilder builder = new GsonBuilder();

        // date formatter
        builder.registerTypeAdapter(Date.class, new DateFormatter());
        builder.registerTypeAdapter(DateTime.class, new DateTimeFormatter());
        builder.registerTypeAdapter(DateTimeZone.class, new DateTimeZoneFormatter());

        // 命名规则
        // tokenAuth -> token_auth
        // token_auth -> tokenAuth
        builder.setFieldNamingPolicy(LOWER_CASE_WITH_UNDERSCORES);

        // 是否序列号带空的参数到gson中
        // { token:null }
        if (serializeNulls) {
            builder.serializeNulls();
        }
        return builder.create();
    }

    /**
     * Get reusable pre-configured {@link Gson} instance
     *
     * @return Gson instance
     */
    public static final Gson getGson() {
        return GSON;
    }

    /**
     * Get reusable pre-configured {@link Gson} instance
     *
     * @return Gson instance
     */
    public static final Gson getGson(final boolean serializeNulls) {
        return serializeNulls ? GSON : GSON_NO_NULLS;
    }

    /**
     * Convert object to json
     *
     * @return json string
     */
    public static final String toJson(final Object object) {
        return toJson(object, true);
    }

    /**
     * Convert object to json
     *
     * @return json string
     */
    public static final String toJson(final Object object, final boolean includeNulls) {
        return includeNulls ? GSON.toJson(object) : GSON_NO_NULLS.toJson(object);
    }

    /**
     * Convert string to given type
     *
     * @return instance of type
     */
    public static final <V> V fromJson(String json, Class<V> type) {
        return GSON.fromJson(json, type);
    }

    /**
     * Convert string to given type
     *
     * @return instance of type
     */
    public static final <V> V fromJson(String json, Type type) {
        return GSON.fromJson(json, type);
    }

    /**
     * Convert content of reader to given type
     *
     * @return instance of type
     */
    public static final <V> V fromJson(Reader reader, Class<V> type) {
        return GSON.fromJson(reader, type);
    }

    /**
     * Convert content of reader to given type
     *
     * @return instance of type
     */
    public static final <V> V fromJson(Reader reader, Type type) {
        return GSON.fromJson(reader, type);
    }

    public static final <V> V fromResource(Context context, int resource, Class<V> type) {
        try {
            return fromJson(parseResource(context, resource), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final <V> V fromResource(Context context, int resource, Type type) {
        try {
            return fromJson(parseResource(context, resource), type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String parseResource(Context context, int resource) throws IOException {
        InputStream is = context.getResources().openRawResource(resource);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            is.close();
        }

        return writer.toString();
    }
}