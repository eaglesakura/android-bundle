package com.eaglesakura.android.saver;

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
     * JSON化して保存する場合はtrue
     *
     * 互換性が保証できる場合に使用する。
     */
    boolean json() default false;
}
