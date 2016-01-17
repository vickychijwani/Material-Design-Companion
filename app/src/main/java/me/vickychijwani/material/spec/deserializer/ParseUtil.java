package me.vickychijwani.material.spec.deserializer;

import android.support.annotation.CheckResult;
import android.support.annotation.Nullable;

import me.vickychijwani.material.BuildConfig;

abstract class ParseUtil {

    @Nullable
    @CheckResult
    public static <T> T throwIfDebug(String message) {
        if (BuildConfig.DEBUG) {
            throw new RuntimeException(message);
        } else {
            return null;
        }
    }

}
