package com.eaglesakura.android.saver;

import android.support.annotation.NonNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Bundleへのステート保存を行う変数へ設定する
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface BundleState {

    /**
     * デフォルト挙動を行う際のタイプを指定する
     */
    @NonNull
    SaveType value() default SaveType.Default;

    /**
     * Bundle制御クラスを取得する
     *
     * Collector.class以外を返したとき、そのクラスを優先して利用する
     */
    @NonNull
    Class<? extends Collector> collector() default Collector.class;
}
