package me.vickychijwani.material.spec.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import me.vickychijwani.material.spec.entity.Figure;
import me.vickychijwani.material.spec.entity.Image;
import me.vickychijwani.material.spec.entity.Video;

public class FigureDeserializer implements JsonDeserializer<Figure> {

    @Override
    public Figure deserialize(JsonElement json, Type typeOfT,
                              JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        if (! jsonObject.has("src") || ! jsonObject.has("mediaType")) {
            throw new RuntimeException("Invalid figure");
        }
        String mediaType = jsonObject.get("mediaType").getAsString();
        if ("image".equals(mediaType)) {
            return context.deserialize(json, Image.class);
        } else if ("video".equals(mediaType)) {
            return context.deserialize(json, Video.class);
        } else {
            throw new RuntimeException("Unrecognized figure media type: " + mediaType);
        }
    }

}
