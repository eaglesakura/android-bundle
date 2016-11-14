package com.eaglesakura.android.saver;

/**
 * Bundle保存種別
 */
public enum SaveType {

    /**
     * デフォルト挙動
     */
    Default,

    /**
     * JSON化して保存する
     *
     * JSON互換性が保証できる場合に使用する。
     */
    Json,

    /**
     * インスタンスを上書きせず、内部パラメータのみを保存・上書きする。
     *
     * 対応しているのはLightSaver標準のFreezerのみ。
     */
    InnerValues,
}
