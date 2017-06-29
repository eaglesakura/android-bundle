package com.eaglesakura.android.saver;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 配列系の値をsave/restoreする
 */
class BundleArrayCollector implements Collector {
    @Override
    public void onSaveInstance(Bundle state, String key, Object srcObject, Field srcField) throws Throwable {
        Object value = srcField.get(srcObject);
        Class<?> type = srcField.getType();
        if (boolean[].class.equals(type)) {
            state.putBooleanArray(key, (boolean[]) value);
        } else if (byte[].class.equals(type)) {
            state.putByteArray(key, (byte[]) value);
        } else if (short[].class.equals(type)) {
            state.putShortArray(key, (short[]) value);
        } else if (int[].class.equals(type)) {
            state.putIntArray(key, (int[]) value);
        } else if (long[].class.equals(type)) {
            state.putLongArray(key, (long[]) value);
        } else if (float[].class.equals(type)) {
            state.putFloatArray(key, (float[]) value);
        } else if (double[].class.equals(type)) {
            state.putDoubleArray(key, (double[]) value);
        } else if (String[].class.equals(type)) {
            state.putStringArray(key, (String[]) value);
        } else if (Parcelable[].class.equals(type)) {
            state.putParcelableArray(key, (Parcelable[]) value);
        } else if (List.class.equals(type) || ArrayList.class.equals(type)) {
            Class genericClass = LightSaver.getListGenericClass(srcField);
            List putList = (List) value;

            if (putList != null && !(putList instanceof ArrayList)) {
                // ArrayList以外は詰め込み直す
                List tempList = new ArrayList(putList.size());
                for (Object temp : putList) {
                    tempList.add(temp);
                }

                putList = tempList;
            }

            if (String.class.equals(genericClass)) {
                state.putStringArrayList(key, (ArrayList<String>) putList);
            } else if (Integer.class.equals(genericClass)) {
                state.putIntegerArrayList(key, (ArrayList<Integer>) putList);
            } else if (LightSaver.asSubClass(genericClass, Parcelable.class)) {
                state.putParcelableArrayList(key, (ArrayList<? extends Parcelable>) putList);
            } else {
                throw new IllegalStateException("key:" + key);
            }
        } else {
            throw new IllegalStateException("key:" + key);
        }
    }

    @Override
    public void onRestoreInstance(Bundle state, String key, Object dstObject, Field dstField) throws Throwable {
        Class<?> type = dstField.getType();
        Object value;

        if (boolean[].class.equals(type)) {
            value = state.getBooleanArray(key);
        } else if (byte[].class.equals(type)) {
            value = state.getByteArray(key);
        } else if (short[].class.equals(type)) {
            value = state.getShortArray(key);
        } else if (int[].class.equals(type)) {
            value = state.getIntArray(key);
        } else if (long[].class.equals(type)) {
            value = state.getLongArray(key);
        } else if (float[].class.equals(type)) {
            value = state.getFloatArray(key);
        } else if (double[].class.equals(type)) {
            value = state.getDoubleArray(key);
        } else if (String[].class.equals(type)) {
            value = state.getStringArray(key);
        } else if (Parcelable[].class.equals(type)) {
            value = state.getParcelableArray(key);
        } else if (List.class.equals(type) || ArrayList.class.equals(type)) {
            Class genericClass = LightSaver.getListGenericClass(dstField);
            if (String.class.equals(genericClass)) {
                value = state.getStringArrayList(key);
            } else if (Integer.class.equals(genericClass)) {
                value = state.getIntegerArrayList(key);
            } else if (LightSaver.asSubClass(genericClass, Parcelable.class)) {
                value = state.getParcelableArrayList(key);
            } else {
                throw new IllegalStateException("key:" + key);
            }
        } else {
            throw new IllegalStateException("key:" + key);
        }

        dstField.set(dstObject, value);
    }
}
