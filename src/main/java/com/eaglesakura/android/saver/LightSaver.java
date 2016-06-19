package com.eaglesakura.android.saver;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightSaver {

    private final Object mTarget;

    private final Bundle mState;

    private final Map<Class, Collector> mFreezers;

    private String mTag;

    private LightSaver(Object target, Bundle state, String keyTag, Map<Class, Collector> freezers) {
        mTarget = target;
        mState = state;
        mFreezers = freezers;
        mTag = keyTag;

        if (mTarget == null || mState == null || mFreezers == null) {
            throw new IllegalStateException();
        }
    }

    @NonNull
    private String getKey(@NonNull Field field) {
        String result = mTarget.getClass().toString() + "@" + field.getName();

        if (isEmpty(mTag)) {
            return result;
        } else {
            return mTag + "@" + result;
        }
    }

    @NonNull
    private Collector getFreezer(@NonNull Field field) {
        Class<?> type = field.getType();
        Collector result = mFreezers.get(type);
        if (result == null) {
            if (instanceOf(type, Parcelable.class)) {
                result = mFreezers.get(Parcelable.class);
            } else if (instanceOf(type, Serializable.class)) {
                result = mFreezers.get(Serializable.class);
            }
        }

        if (result == null) {
            throw new IllegalStateException("Field : " + field.getName());
        }

        return result;
    }

    /**
     * ステートの保存を行う
     */
    private void save() {
        List<Field> fields = listAnnotationFields(mTarget.getClass(), BundleState.class);
        for (Field field : fields) {
            field.setAccessible(true);
            String key = getKey(field);
            Collector freezer = getFreezer(field);
            if (freezer == null) {
                throw new IllegalStateException("freeze failed :: " + key);
            }

            try {
                freezer.onSaveInstance(mState, key, mTarget, field);
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }

    /***
     * ステートのレストアを行う
     */
    private void restore() {
        List<Field> fields = LightSaver.listAnnotationFields(mTarget.getClass(), BundleState.class);
        for (Field field : fields) {
            field.setAccessible(true);
            String key = getKey(field);
            Collector freezer = getFreezer(field);
            if (freezer == null) {
                throw new IllegalStateException("freeze failed :: " + key);
            }

            try {
                freezer.onRestoreInstance(mState, key, mTarget, field);
            } catch (Throwable e) {
                throw new IllegalStateException(e);
            }
        }
    }

    public static Builder create(Bundle state) {
        return new Builder().state(state);
    }

    private static Map<Class, Collector> sDefaultFreezer;

    private static Map<Class, Collector> getDefaultFreezer() {
        if (sDefaultFreezer == null) {
            synchronized (LightSaver.class) {
                if (sDefaultFreezer == null) {
                    sDefaultFreezer = new HashMap<>();

                    {
                        BundlePrimitiveCollector primitiveFreezer = new BundlePrimitiveCollector();
                        sDefaultFreezer.put(boolean.class, primitiveFreezer);
                        sDefaultFreezer.put(byte.class, primitiveFreezer);
                        sDefaultFreezer.put(short.class, primitiveFreezer);
                        sDefaultFreezer.put(int.class, primitiveFreezer);
                        sDefaultFreezer.put(long.class, primitiveFreezer);
                        sDefaultFreezer.put(float.class, primitiveFreezer);
                        sDefaultFreezer.put(double.class, primitiveFreezer);
                        sDefaultFreezer.put(String.class, primitiveFreezer);
                        sDefaultFreezer.put(Serializable.class, primitiveFreezer);
                        sDefaultFreezer.put(Parcelable.class, primitiveFreezer);
                    }
                    {
                        BundleArrayCollector arrayFreezer = new BundleArrayCollector();
                        sDefaultFreezer.put(boolean[].class, arrayFreezer);
                        sDefaultFreezer.put(byte[].class, arrayFreezer);
                        sDefaultFreezer.put(short[].class, arrayFreezer);
                        sDefaultFreezer.put(int[].class, arrayFreezer);
                        sDefaultFreezer.put(long[].class, arrayFreezer);
                        sDefaultFreezer.put(float[].class, arrayFreezer);
                        sDefaultFreezer.put(double[].class, arrayFreezer);
                        sDefaultFreezer.put(String[].class, arrayFreezer);
                        sDefaultFreezer.put(Parcelable[].class, arrayFreezer);
                        sDefaultFreezer.put(ArrayList.class, arrayFreezer);
                    }
                }
            }
        }
        return sDefaultFreezer;
    }

    public static class Builder {
        private Object mTargetObject;

        private Bundle mState;

        private Map<Class, Collector> mFreezerMap = new HashMap<>(getDefaultFreezer());

        private String mTag;

        Builder() {
            // デフォルトのフリーザを指定
        }

        private Builder state(Bundle bundle) {
            mState = bundle;
            return this;
        }

        public Builder target(Object target) {
            mTargetObject = target;
            if (target instanceof Fragment) {
                tag(((Fragment) target).getTag());
            }
            return this;
        }

        public Builder freezer(Class clazz, Collector freezer) {
            mFreezerMap.put(clazz, freezer);
            return this;
        }

        /**
         * 同じClassが複数ある場合、タグを指定することで保存・復旧時に競合が発生しないようにする
         */
        public Builder tag(String keyTag) {
            mTag = keyTag;
            return this;
        }

        public Builder save() {
            new LightSaver(mTargetObject, mState, mTag, mFreezerMap).save();
            return this;
        }

        /**
         * restoreを行う。
         * Bundleがnullである場合は何もしない
         */
        public Builder restore() {
            if (mState != null) {
                new LightSaver(mTargetObject, mState, mTag, mFreezerMap).restore();
            }
            return this;
        }
    }

    static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 指定したAnnotationが含まれたフィールド(public以外を含む)一覧を返す
     *
     * AnnotationにはRuntime属性が付与されてなければならない
     */
    static <T extends Annotation> List<Field> listAnnotationFields(Class srcClass, Class<T> annotationClass) {
        List<Field> result = new ArrayList<>();

        while (!srcClass.equals(Object.class)) {
            for (Field field : srcClass.getDeclaredFields()) {
                T annotation = field.getAnnotation(annotationClass);
                if (annotation != null) {
                    result.add(field);
                }
            }

            srcClass = srcClass.getSuperclass();
        }

        return result;
    }

    /**
     * ListがImplされている場合はtrueを返却する
     */
    static boolean isListInterface(Class<?> clazz) {
        try {
            return clazz.asSubclass(List.class) != null;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Listの引数に指定されたClassを取得する
     */
    static Class getListGenericClass(Field field) {
        if (!isListInterface(field.getType())) {
            throw new IllegalArgumentException();
        }

        try {
            Type genericType = field.getGenericType();
            return (Class) ((ParameterizedType) genericType).getActualTypeArguments()[0];
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    static boolean instanceOf(Class checkType, Class clazz) {
        try {
            return checkType.asSubclass(clazz) != null;
        } catch (Exception e) {
        }
        return false;
    }

}