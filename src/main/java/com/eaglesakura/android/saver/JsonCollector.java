package com.eaglesakura.android.saver;

import com.eaglesakura.json.JSON;

import android.os.Bundle;

import java.lang.reflect.Field;

/**
 * JSONを経由してBundleを保存する
 */
class JsonCollector implements Collector {
    @Override
    public void onSaveInstance(Bundle state, String key, Object srcObject, Field srcField) throws Throwable {
        Object obj = srcField.get(srcObject);
        if (obj != null) {
            state.putString(key, JSON.encode(obj));
        }
    }

    @Override
    public void onRestoreInstance(Bundle state, String key, Object dstObject, Field dstField) throws Throwable {
        String string = state.getString(key);
        if (string != null) {
            Object obj = JSON.decode(string, dstField.getType());
            dstField.set(dstObject, obj);
        }
    }
}
