package com.eaglesakura.android.saver;

import com.eaglesakura.util.RandomUtil;

import org.junit.Test;

import android.os.Bundle;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;

public class LightSaverTest extends UnitTestCase {

    @Test
    public void オブジェクトの保存と復旧が行える() throws Throwable {

        for (int i = 0; i < 10000; ++i) {
            Bundle state = new Bundle();
            SampleObject saveTarget = new SampleObject();
            SampleObject restoreTarget = new SampleObject();

            InnerValue saveTargetInner = saveTarget.mValue;

            assertNotEquals(saveTarget, restoreTarget);

            LightSaver.create(state)
                    .target(saveTarget).save();

            LightSaver.create(state)
                    .target(restoreTarget).restore();

            assertEquals(saveTarget, restoreTarget);

            // インスタンスの同一性が保たれている
            assertTrue(saveTarget.mValue == saveTargetInner);
            assertTrue(restoreTarget.mValue != saveTargetInner);
        }
    }

    public enum EnumValue {
        Value0,
        Value1,
        Value2,
        Value3,
        Value4,
    }

    public static class InnerValue {
        @BundleState
        String mString = RandomUtil.randString();

        @BundleState
        String mNullString = null;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            InnerValue that = (InnerValue) o;

            if (mString != null ? !mString.equals(that.mString) : that.mString != null)
                return false;
            return mNullString != null ? mNullString.equals(that.mNullString) : that.mNullString == null;

        }

        @Override
        public int hashCode() {
            int result = mString != null ? mString.hashCode() : 0;
            result = 31 * result + (mNullString != null ? mNullString.hashCode() : 0);
            return result;
        }
    }

    public static class Pojo {
        public String stringValue = RandomUtil.randShortString();

        public Long longValue = System.currentTimeMillis();

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Pojo pojo = (Pojo) o;

            if (stringValue != null ? !stringValue.equals(pojo.stringValue) : pojo.stringValue != null)
                return false;
            return longValue != null ? longValue.equals(pojo.longValue) : pojo.longValue == null;

        }

        @Override
        public int hashCode() {
            int result = stringValue != null ? stringValue.hashCode() : 0;
            result = 31 * result + (longValue != null ? longValue.hashCode() : 0);
            return result;
        }
    }

    static class SampleObject {
        @BundleState
        boolean mBoolean = RandomUtil.randBool();
        @BundleState
        byte mByte = RandomUtil.randInt8();
        @BundleState
        short mShort = RandomUtil.randInt16();
        @BundleState
        int mInt = RandomUtil.randInt32();
        @BundleState
        long mLong = RandomUtil.randInt64();
        @BundleState
        float mFloat = RandomUtil.randFloat();
        @BundleState
        double mDouble = RandomUtil.randFloat();
        @BundleState
        String mString = RandomUtil.randShortString();

        @BundleState
        boolean[] mBooleanArray = {RandomUtil.randBool(), RandomUtil.randBool(), RandomUtil.randBool()};

        @BundleState
        boolean[] mNullBooleanArray;

        @BundleState
        byte[] mBytes = {RandomUtil.randInt8()};

        @BundleState
        byte[] mNullByteArray;

        @BundleState
        short[] mShorts = {RandomUtil.randInt8(), RandomUtil.randInt8(), RandomUtil.randInt8()};


        @BundleState
        short[] mNullShortArray;

        @BundleState
        int[] mInts = {RandomUtil.randInt8(), RandomUtil.randInt8(), RandomUtil.randInt8()};

        @BundleState
        int[] mNullIntArray;

        @BundleState
        long[] mLongs = {RandomUtil.randInt8(), RandomUtil.randInt8(), RandomUtil.randInt8()};

        @BundleState
        long[] mNullLongArray;

        @BundleState
        float[] mFloats = {RandomUtil.randInt8(), RandomUtil.randInt8(), RandomUtil.randInt8()};

        @BundleState
        float[] mNullFloatArray;

        @BundleState
        double[] mDoubles = {RandomUtil.randInt8(), RandomUtil.randInt8(), RandomUtil.randInt8()};

        @BundleState
        double[] mNullDoubleArray;

        @BundleState
        String[] mStrings = {RandomUtil.randShortString(), RandomUtil.randShortString(), RandomUtil.randShortString()};

        @BundleState
        String[] mNullStringArray;

        @BundleState
        ArrayList<String> mStringArrayList = new ArrayList<>(Arrays.asList(RandomUtil.randShortString(), RandomUtil.randShortString(), RandomUtil.randShortString()));

        @BundleState
        ArrayList<String> mNullStringArrayList;

        @BundleState
        ArrayList<Integer> mIntegerArrayList = new ArrayList<>(Arrays.asList(RandomUtil.randInt32(), null, RandomUtil.randInt32(), RandomUtil.randInt32()));

        @BundleState
        ArrayList<Integer> mNullIntegerArrayList;

        @BundleState
        ArrayList<Bundle> mParcelableArrayList = new ArrayList<>(Arrays.asList(new Bundle(), null, new Bundle()));

        @BundleState
        ArrayList<Bundle> mNullParcelableArrayList;

        @BundleState
        EnumValue mEnumValue = RandomUtil.randEnum(EnumValue.class);

        @BundleState
        EnumValue mNullEnumValue;

        @BundleState(SaveType.Json)
        Pojo mPojo = new Pojo();

        @BundleState(SaveType.Json)
        Pojo mNullPojo = null;

        @BundleState(SaveType.InnerValues)
        final InnerValue mFinalValue = new InnerValue();

        @BundleState(SaveType.InnerValues)
        InnerValue mValue = new InnerValue();

        @BundleState(SaveType.InnerValues)
        final InnerValue mNullValue = null;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            SampleObject that = (SampleObject) o;

            if (mBoolean != that.mBoolean) return false;
            if (mByte != that.mByte) return false;
            if (mShort != that.mShort) return false;
            if (mInt != that.mInt) return false;
            if (mLong != that.mLong) return false;
            if (Float.compare(that.mFloat, mFloat) != 0) return false;
            if (Double.compare(that.mDouble, mDouble) != 0) return false;
            if (mString != null ? !mString.equals(that.mString) : that.mString != null)
                return false;
            if (!Arrays.equals(mBooleanArray, that.mBooleanArray)) return false;
            if (!Arrays.equals(mNullBooleanArray, that.mNullBooleanArray)) return false;
            if (!Arrays.equals(mBytes, that.mBytes)) return false;
            if (!Arrays.equals(mNullByteArray, that.mNullByteArray)) return false;
            if (!Arrays.equals(mShorts, that.mShorts)) return false;
            if (!Arrays.equals(mNullShortArray, that.mNullShortArray)) return false;
            if (!Arrays.equals(mInts, that.mInts)) return false;
            if (!Arrays.equals(mNullIntArray, that.mNullIntArray)) return false;
            if (!Arrays.equals(mLongs, that.mLongs)) return false;
            if (!Arrays.equals(mNullLongArray, that.mNullLongArray)) return false;
            if (!Arrays.equals(mFloats, that.mFloats)) return false;
            if (!Arrays.equals(mNullFloatArray, that.mNullFloatArray)) return false;
            if (!Arrays.equals(mDoubles, that.mDoubles)) return false;
            if (!Arrays.equals(mNullDoubleArray, that.mNullDoubleArray)) return false;
            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            if (!Arrays.equals(mStrings, that.mStrings)) return false;
            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            if (!Arrays.equals(mNullStringArray, that.mNullStringArray)) return false;
            if (mStringArrayList != null ? !mStringArrayList.equals(that.mStringArrayList) : that.mStringArrayList != null)
                return false;
            if (mNullStringArrayList != null ? !mNullStringArrayList.equals(that.mNullStringArrayList) : that.mNullStringArrayList != null)
                return false;
            if (mIntegerArrayList != null ? !mIntegerArrayList.equals(that.mIntegerArrayList) : that.mIntegerArrayList != null)
                return false;
            if (mNullIntegerArrayList != null ? !mNullIntegerArrayList.equals(that.mNullIntegerArrayList) : that.mNullIntegerArrayList != null)
                return false;
            if (mParcelableArrayList != null ? !mParcelableArrayList.equals(that.mParcelableArrayList) : that.mParcelableArrayList != null)
                return false;
            if (mNullParcelableArrayList != null ? !mNullParcelableArrayList.equals(that.mNullParcelableArrayList) : that.mNullParcelableArrayList != null)
                return false;
            if (mEnumValue != that.mEnumValue) return false;
            if (mNullEnumValue != that.mNullEnumValue) return false;
            if (mPojo != null ? !mPojo.equals(that.mPojo) : that.mPojo != null) return false;
            if (mNullPojo != null ? !mNullPojo.equals(that.mNullPojo) : that.mNullPojo != null)
                return false;
            if (mFinalValue != null ? !mFinalValue.equals(that.mFinalValue) : that.mFinalValue != null)
                return false;
            if (mValue != null ? !mValue.equals(that.mValue) : that.mValue != null) return false;
            return mNullValue != null ? mNullValue.equals(that.mNullValue) : that.mNullValue == null;

        }

        @Override
        public int hashCode() {
            int result;
            long temp;
            result = (mBoolean ? 1 : 0);
            result = 31 * result + (int) mByte;
            result = 31 * result + (int) mShort;
            result = 31 * result + mInt;
            result = 31 * result + (int) (mLong ^ (mLong >>> 32));
            result = 31 * result + (mFloat != +0.0f ? Float.floatToIntBits(mFloat) : 0);
            temp = Double.doubleToLongBits(mDouble);
            result = 31 * result + (int) (temp ^ (temp >>> 32));
            result = 31 * result + (mString != null ? mString.hashCode() : 0);
            result = 31 * result + Arrays.hashCode(mBooleanArray);
            result = 31 * result + Arrays.hashCode(mNullBooleanArray);
            result = 31 * result + Arrays.hashCode(mBytes);
            result = 31 * result + Arrays.hashCode(mNullByteArray);
            result = 31 * result + Arrays.hashCode(mShorts);
            result = 31 * result + Arrays.hashCode(mNullShortArray);
            result = 31 * result + Arrays.hashCode(mInts);
            result = 31 * result + Arrays.hashCode(mNullIntArray);
            result = 31 * result + Arrays.hashCode(mLongs);
            result = 31 * result + Arrays.hashCode(mNullLongArray);
            result = 31 * result + Arrays.hashCode(mFloats);
            result = 31 * result + Arrays.hashCode(mNullFloatArray);
            result = 31 * result + Arrays.hashCode(mDoubles);
            result = 31 * result + Arrays.hashCode(mNullDoubleArray);
            result = 31 * result + Arrays.hashCode(mStrings);
            result = 31 * result + Arrays.hashCode(mNullStringArray);
            result = 31 * result + (mStringArrayList != null ? mStringArrayList.hashCode() : 0);
            result = 31 * result + (mNullStringArrayList != null ? mNullStringArrayList.hashCode() : 0);
            result = 31 * result + (mIntegerArrayList != null ? mIntegerArrayList.hashCode() : 0);
            result = 31 * result + (mNullIntegerArrayList != null ? mNullIntegerArrayList.hashCode() : 0);
            result = 31 * result + (mParcelableArrayList != null ? mParcelableArrayList.hashCode() : 0);
            result = 31 * result + (mNullParcelableArrayList != null ? mNullParcelableArrayList.hashCode() : 0);
            result = 31 * result + (mEnumValue != null ? mEnumValue.hashCode() : 0);
            result = 31 * result + (mNullEnumValue != null ? mNullEnumValue.hashCode() : 0);
            result = 31 * result + (mPojo != null ? mPojo.hashCode() : 0);
            result = 31 * result + (mNullPojo != null ? mNullPojo.hashCode() : 0);
            result = 31 * result + (mFinalValue != null ? mFinalValue.hashCode() : 0);
            result = 31 * result + (mValue != null ? mValue.hashCode() : 0);
            result = 31 * result + (mNullValue != null ? mNullValue.hashCode() : 0);
            return result;
        }
    }
}
