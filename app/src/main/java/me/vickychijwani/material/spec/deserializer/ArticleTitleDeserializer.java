package me.vickychijwani.material.spec.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import me.vickychijwani.material.spec.entity.ArticleTitle;

public class ArticleTitleDeserializer implements JsonDeserializer<ArticleTitle> {

    @Override
    public ArticleTitle deserialize(JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {
        return new ArticleTitle(json.getAsString());
    }

}
