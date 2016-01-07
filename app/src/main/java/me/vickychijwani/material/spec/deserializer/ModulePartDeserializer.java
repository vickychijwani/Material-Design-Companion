package me.vickychijwani.material.spec.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import me.vickychijwani.material.spec.entity.FigureGroup;
import me.vickychijwani.material.spec.entity.ModuleBody;
import me.vickychijwani.material.spec.entity.ModulePart;

public class ModulePartDeserializer implements JsonDeserializer<ModulePart> {

    @Override
    public ModulePart deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        switch (type) {
            case FigureGroup.TYPE:
                return context.deserialize(json, FigureGroup.class);
            case ModuleBody.TYPE:
                return context.deserialize(json, ModuleBody.class);
            default:
                return null;
        }
    }

}
