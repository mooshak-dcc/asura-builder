package pt.up.fc.dcc.asura.builder.base.utils;

import com.google.gson.JsonElement;

/**
 * Base class to be extended by objects that can be converted to JSON
 *
 * @author José Carlos Paiva <code>josepaiva94@gmail.com</code>
 */
public class JsonObject {

    /**
     * Convert object to JSON
     *
     * @return {@link JsonElement} JSON element that represents this object
     */
    public JsonElement toJson() {
        return Json.get().objectToJson(this);
    }
}
