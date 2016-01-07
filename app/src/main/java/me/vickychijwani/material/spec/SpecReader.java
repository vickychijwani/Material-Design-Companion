package me.vickychijwani.material.spec;

import android.content.res.AssetManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.InputStream;

import me.vickychijwani.material.spec.deserializer.ChapterPartDeserializer;
import me.vickychijwani.material.spec.deserializer.ModulePartDeserializer;
import me.vickychijwani.material.spec.entity.Chapter;
import me.vickychijwani.material.spec.entity.ChapterPart;
import me.vickychijwani.material.spec.entity.Index;
import me.vickychijwani.material.spec.entity.ModulePart;

public class SpecReader {

    public static final String TAG = "DataReader";

    public final Gson mGson;

    public SpecReader() {
        mGson = new GsonBuilder()
                .registerTypeAdapter(ChapterPart.class, new ChapterPartDeserializer())
                .registerTypeAdapter(ModulePart.class, new ModulePartDeserializer())
                .create();
    }

    public Index getIndex(@NonNull AssetManager assetManager) {
        return mGson.fromJson(readJsonFile("spec/index.json", assetManager), Index.class);
    }

    public Chapter getChapter(String filepath, @NonNull AssetManager assetManager) {
        return mGson.fromJson(readJsonFile(filepath, assetManager), Chapter.class);
    }

    private String readJsonFile(String path, AssetManager assetManager) {
        String jsonString = null;
        try {
            InputStream is = assetManager.open(path);
            int size = is.available();
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
            is.close();
            jsonString = new String(buffer, "utf-8");
        } catch (IOException e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return jsonString;
    }

}
