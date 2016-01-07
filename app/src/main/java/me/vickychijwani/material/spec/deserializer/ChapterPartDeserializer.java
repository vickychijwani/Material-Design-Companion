package me.vickychijwani.material.spec.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import me.vickychijwani.material.spec.entity.ArticleList;
import me.vickychijwani.material.spec.entity.ChapterIntro;
import me.vickychijwani.material.spec.entity.ChapterIntroWithModules;
import me.vickychijwani.material.spec.entity.ChapterPart;
import me.vickychijwani.material.spec.entity.Figure;

public class ChapterPartDeserializer implements JsonDeserializer<ChapterPart> {

    @Override
    public ChapterPart deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        ChapterPart chapterPart = null;
        switch (type) {
            case Figure.TYPE:
                if (jsonObject.has("src") && jsonObject.has("mediaType")) {
                    chapterPart = context.deserialize(json, Figure.class);
                }
                break;
            case ChapterIntro.TYPE:
                if (jsonObject.has("html")) {
                    chapterPart = context.deserialize(json, ChapterIntro.class);
                } else if (jsonObject.has("modules")) {
                    chapterPart = context.deserialize(json, ChapterIntroWithModules.class);
                }
                break;
            case ArticleList.TYPE:
                if (jsonObject.has("articles")) {
                    chapterPart = context.deserialize(json, ArticleList.class);
                }
                break;
        }

        if (chapterPart == null) {
            throw new IllegalArgumentException("Is this really a valid ChapterPart?");
        }
        return chapterPart;
    }

}
