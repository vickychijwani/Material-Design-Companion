package me.vickychijwani.material.spec.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import me.vickychijwani.material.spec.entity.ArticleList;
import me.vickychijwani.material.spec.entity.ChapterIntro;
import me.vickychijwani.material.spec.entity.ChapterIntroWithHtml;
import me.vickychijwani.material.spec.entity.ChapterIntroWithModules;
import me.vickychijwani.material.spec.entity.ChapterPart;
import me.vickychijwani.material.spec.entity.Figure;
import me.vickychijwani.material.spec.entity.Image;
import me.vickychijwani.material.spec.entity.Video;

public class ChapterPartDeserializer implements JsonDeserializer<ChapterPart> {

    @Override
    public ChapterPart deserialize(JsonElement json, Type typeOfT,
                                   JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        if (! jsonObject.has("type")) {
            return ParseUtil.throwIfDebug("Invalid module part");
        }
        String type = jsonObject.get("type").getAsString();
        ChapterPart chapterPart;
        switch (type) {
            case "figure":
                chapterPart = getFigure(context, jsonObject);
                break;
            case "intro":
                chapterPart = getChapterIntro(context, jsonObject);
                break;
            case "article-list":
                chapterPart = getArticleList(context, jsonObject);
                break;
            default:
                return ParseUtil.throwIfDebug("Unrecognized ChapterPart with type: " + type);
        }

        return chapterPart;
    }

    private static ChapterIntro getChapterIntro(JsonDeserializationContext context, JsonObject json) {
        if (json.has("html")) {
            return context.deserialize(json, ChapterIntroWithHtml.class);
        } else if (json.has("modules")) {
            return context.deserialize(json, ChapterIntroWithModules.class);
        } else {
            return ParseUtil.throwIfDebug("Invalid chapter intro");
        }
    }

    private static Figure getFigure(JsonDeserializationContext context, JsonObject json) {
        if (! json.has("src") || ! json.has("mediaType")) {
            return ParseUtil.throwIfDebug("Invalid figure");
        }
        String mediaType = json.get("mediaType").getAsString();
        if ("image".equals(mediaType)) {
            return context.deserialize(json, Image.class);
        } else if ("video".equals(mediaType)) {
            return context.deserialize(json, Video.class);
        } else {
            return ParseUtil.throwIfDebug("Unrecognized figure media type: " + mediaType);
        }
    }

    public static ArticleList getArticleList(JsonDeserializationContext context, JsonObject json) {
        if (json.has("articles")) {
            return context.deserialize(json, ArticleList.class);
        } else {
            return ParseUtil.throwIfDebug("Invalid article list");
        }
    }

}
