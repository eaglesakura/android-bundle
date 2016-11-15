package com.eaglesakura.android.saver;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bundleへのステート保存を行う変数へ設定する
 */
@Keep
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface BundleState {

    /**
     * デフォルト挙動を行う際のタイプを指定する
     */
    @Keep
    @NonNull
    SaveType value() default SaveType.Default;

    /**
     * Bundle制御クラスを取得する
     *
     * Collector.class以外を返したとき、そのクラスを優先して利用する
     */
    @Keep
    @NonNull
    Class<? extends Collector> collector() default Collector.class;
}
