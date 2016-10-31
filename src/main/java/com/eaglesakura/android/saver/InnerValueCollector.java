package com.eaglesakura.android.saver;

import android.os.Bundle;

import java.lang.reflect.Field;

/**
 * 内部パラメータを新たなBundleに書き込む
 */
class InnerValueCollector implements Collector {
    @Override
    public void onSaveInstance(Bundle state, String key, Object srcObject, Field srcField) throws Throwable {
        Object value = srcField.get(srcObject);
        if (value != null) {
            Bundle newBundle = new Bundle();
            LightSaver.create(newBundle)
                    .target(value)
                    .save();

            state.putBundle(key, newBundle);
        }
    }

    @Override
    public void onRestoreInstance(Bundle state, String key, Object dstObject, Field dstField) throws Throwable {
        Bundle innerValues = state.getBundle(key);
        if (innerValues != null) {
            Object obj = dstField.get(dstObject);
            if (obj == null) {
                throw new NullPointerException("@BundleState[value == null]");
            }

            LightSaver.create(innerValues)
                    .target(obj)
                    .restore();
        }
    }

    private static final InnerValueCollector sInstance = new InnerValueCollector();

    static InnerValueCollector getInstance() {
        return sInstance;
    }
}
