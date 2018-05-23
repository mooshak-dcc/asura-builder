package pt.up.fc.dcc.asura.builder.base.utils;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Utilities to read/write JSON
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class Json {
    private final static Logger LOGGER = Logger.getLogger(Json.class.getSimpleName());

    private static GsonBuilder gsonBuilder = null;

    private Gson gson;

    private Json(Gson gson) {
        this.gson = gson;
    }

    public static Json get() {

        if (gsonBuilder == null) {
            gsonBuilder = new GsonBuilder();
            gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        }

        return new Json(gsonBuilder.create());
    }

    public static void registerTypeAdapter(Type type, Object typeAdapter) {
        gsonBuilder.registerTypeAdapter(type, typeAdapter);
    }

    public static void registerTypeAdapterFactory(TypeAdapterFactory factory) {
        gsonBuilder.registerTypeAdapterFactory(factory);
    }

    public static void registerTypeHierarchyAdapter(Class<?> baseType, Object typeAdapter) {
        gsonBuilder.registerTypeHierarchyAdapter(baseType, typeAdapter);
    }

    /***************************************************************************************
     *                             JSON String to Java Objects                             *
     ***************************************************************************************/

    public <T> T objectFromString(String json, Class<T> modelClass) throws RuntimeException {

        return gson.fromJson(json, modelClass);
    }

    public JsonElement jsonFromString(String json) throws RuntimeException {

        return gson.fromJson(json, JsonElement.class);
    }

    public <T> Collection<T> collectionFromString(String json) throws RuntimeException {

        Type desiredType = new TypeToken<Collection<T>>() {
        }.getType();

        return gson.fromJson(json, desiredType);
    }


    /***************************************************************************************
     *                                Java Objects to JSON                                 *
     ***************************************************************************************/

    public <T> JsonElement objectToJson(T object) throws RuntimeException {

        Type type = new TypeToken<T>() {
        }.getType();

        return gson.toJsonTree(object, type);
    }

    public <T> JsonElement collectionToJson(List<T> list) throws RuntimeException {

        Type type = new TypeToken<List<T>>() {
        }.getType();

        return gson.toJsonTree(list, type);
    }

    public <K, V> JsonElement mapToJson(Map<K, V> map) throws RuntimeException {

        Type type = new TypeToken<Map<K, V>>() {
        }.getType();

        return gson.toJsonTree(map, type);
    }

    public <T> JsonElement arrayToJson(T[] array) throws RuntimeException {

        Type type = new TypeToken<T[]>() {
        }.getType();

        return gson.toJsonTree(array, type);
    }

    /***************************************************************************************
     *                             Java Objects to JSON String                             *
     ***************************************************************************************/

    public <T> String objectToString(T object) throws RuntimeException {

        Type type = new TypeToken<T>() {
        }.getType();

        return gson.toJson(object, type);
    }

    public <T> String collectionToString(List<T> list) throws RuntimeException {

        Type type = new TypeToken<List<T>>() {
        }.getType();

        return gson.toJson(list, type);
    }

    public <K, V> String mapToString(Map<K, V> map) throws RuntimeException {

        Type type = new TypeToken<Map<K, V>>() {
        }.getType();

        return gson.toJson(map, type);
    }

    public <T> String arrayToString(T[] array) throws RuntimeException {

        Type type = new TypeToken<T[]>() {
        }.getType();

        return gson.toJson(array, type);
    }

    /***************************************************************************************
     *                                     Helpers                                         *
     ***************************************************************************************/

    /**
     * Read object from an {@link InputStream} as JSON
     *
     * @param inputStream {@link InputStream} stream to read from
     */
    public <T> T readFromStream(InputStream inputStream, Class<T> modelClass) throws RuntimeException,
            UnsupportedEncodingException {

        Reader reader = new InputStreamReader(inputStream, "UTF-8");
        return gson.fromJson(reader, modelClass);
    }

    /**
     * Write object to an {@link OutputStream} as JSON
     *
     * @param object       Object to write
     * @param outputStream {@link OutputStream} stream to write to
     */
    public <T> void writeToStream(T object, OutputStream outputStream) throws RuntimeException,
            IOException {

        Type type = new TypeToken<T>() {
        }.getType();

        Writer writer = new OutputStreamWriter(outputStream);
        gson.toJson(object, type, writer);
        writer.flush();
    }
}






















