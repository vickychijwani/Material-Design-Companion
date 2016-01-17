package me.vickychijwani.material.spec.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import me.vickychijwani.material.spec.entity.ModuleTitle;

public class ModuleTitleDeserializer implements JsonDeserializer<ModuleTitle> {

    @Override
    public ModuleTitle deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context) throws JsonParseException {
        return new ModuleTitle(json.getAsString());
    }

}
