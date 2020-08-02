package net.imklib2

import net.imglib2.type.numeric.IntegerType
import net.imglib2.type.numeric.RealType
import net.imglib2.type.numeric.integer.*
import net.imglib2.type.numeric.real.DoubleType
import net.imglib2.type.numeric.real.FloatType
import net.imklib2.RealTypeExtensions.Companion.createWithValue


class RealTypeExtensions {
    companion object {
        // createWithValue
        fun <T: RealType<T>> T.createWithValue(value: Byte) = createVariable().also { it.setReal(value.toDouble()) }
        fun <T: RealType<T>> T.createWithValue(value: Short) = createVariable().also { it.setReal(value.toDouble()) }
        fun <T: RealType<T>> T.createWithValue(value: Int) = createVariable().also { it.setReal(value.toDouble()) }
        fun <T: RealType<T>> T.createWithValue(value: Long) = createVariable().also { it.setReal(value.toDouble()) }
        fun <T: RealType<T>> T.createWithValue(value: Float) = createVariable().also { it.setReal(value) }
        fun <T: RealType<T>> T.createWithValue(value: Double) = createVariable().also { it.setReal(value) }

        fun <T: RealType<T>> Byte.asType(type: T) = type.createWithValue(this)
        fun <T: RealType<T>> Short.asType(type: T) = type.createWithValue(this)
        fun <T: RealType<T>> Int.asType(type: T) = type.createWithValue(this)
        fun <T: RealType<T>> Long.asType(type: T) = type.createWithValue(this)
        fun <T: RealType<T>> Float.asType(type: T) = type.createWithValue(this)
        fun <T: RealType<T>> Double.asType(type: T) = type.createWithValue(this)

        fun Float.asType() = asType(FloatType())
        fun Double.asType() = asType(DoubleType())

        // conversion
        val <T: RealType<T>> T.floatType get() = when(this) {
            is FloatType -> copy()
            else -> FloatType(realFloat)
        }
        val <T: RealType<T>> T.doubleType get() = when(this) {
            is DoubleType -> copy()
            else -> DoubleType(realDouble)
        }

        // add
        operator fun <T: RealType<T>> T.plusAssign(value: T) = add(value)
        operator fun <T: RealType<T>> T.plus(value: T) = copy().also { it += value }
        // T + primitive type
        operator fun <T: RealType<T>> T.plusAssign(value: Byte) = setReal(realDouble + value)
        operator fun <T: RealType<T>> T.plus(value: Byte) = createWithValue(value).also { it += this }
        operator fun <T: RealType<T>> T.plusAssign(value: Short) = setReal(realDouble + value)
        operator fun <T: RealType<T>> T.plus(value: Short) = createWithValue(value).also { it += this }
        operator fun <T: RealType<T>> T.plusAssign(value: Int) = setReal(realDouble + value)
        operator fun <T: RealType<T>> T.plus(value: Int) = createWithValue(value).also { it += this }
        operator fun <T: RealType<T>> T.plusAssign(value: Long) = setReal(realDouble + value)
        operator fun <T: RealType<T>> T.plus(value: Long) = createWithValue(value).also { it += this }
        operator fun <T: RealType<T>> T.plusAssign(value: Float) = setReal(realDouble + value)
        operator fun <T: RealType<T>> T.plus(value: Float) = createWithValue(value).also { it += this }
        operator fun <T: RealType<T>> T.plusAssign(value: Double) = setReal(realDouble + value)
        operator fun <T: RealType<T>> T.plus(value: Double) = createWithValue(value).also { it += this }
        // primitive type + T
        operator fun <T: RealType<T>> Byte.plus(value: T) = value + this
        operator fun <T: RealType<T>> Short.plus(value: T) = value + this
        operator fun <T: RealType<T>> Int.plus(value: T) = value + this
        operator fun <T: RealType<T>> Long.plus(value: T) = value + this
        operator fun <T: RealType<T>> Float.plus(value: T) = value + this
        operator fun <T: RealType<T>> Double.plus(value: T) = value + this

        // subtract
        operator fun <T: RealType<T>> T.minusAssign(value: T) = sub(value)
        operator fun <T: RealType<T>> T.minus(value: T) = copy().also { it -= value }
        // T - primitive type
        operator fun <T: RealType<T>> T.minusAssign(value: Byte) = setReal(realDouble - value)
        operator fun <T: RealType<T>> T.minus(value: Byte) = copy().also { it -= value }
        operator fun <T: RealType<T>> T.minusAssign(value: Short) = setReal(realDouble - value)
        operator fun <T: RealType<T>> T.minus(value: Short) = copy().also { it -= value }
        operator fun <T: RealType<T>> T.minusAssign(value: Int) = setReal(realDouble - value)
        operator fun <T: RealType<T>> T.minus(value: Int) = copy().also { it -= value }
        operator fun <T: RealType<T>> T.minusAssign(value: Long) = setReal(realDouble - value)
        operator fun <T: RealType<T>> T.minus(value: Long) = copy().also { it -= value }
        operator fun <T: RealType<T>> T.minusAssign(value: Float) = setReal(realDouble - value)
        operator fun <T: RealType<T>> T.minus(value: Float) = copy().also { it -= value }
        operator fun <T: RealType<T>> T.minusAssign(value: Double) = setReal(realDouble - value)
        operator fun <T: RealType<T>> T.minus(value: Double) = copy().also { it -= value }
        // primitive type - T
        operator fun <T: RealType<T>> Byte.minus(value: T) = asType(value) - value
        operator fun <T: RealType<T>> Short.minus(value: T) = asType(value) - value
        operator fun <T: RealType<T>> Int.minus(value: T) = asType(value) - value
        operator fun <T: RealType<T>> Long.minus(value: T) = asType(value) - value
        operator fun <T: RealType<T>> Float.minus(value: T) = asType(value) - value
        operator fun <T: RealType<T>> Double.minus(value: T) = asType(value) - value

        // multiply
        operator fun <T: RealType<T>> T.timesAssign(value: T) = mul(value)
        operator fun <T: RealType<T>> T.times(value: T) = copy().also { it *= value }
        // T * primitive type
        operator fun <T: RealType<T>> T.timesAssign(value: Byte) = mul(value.toDouble())
        operator fun <T: RealType<T>> T.times(value: Byte) = copy().also { it *= value }
        operator fun <T: RealType<T>> T.timesAssign(value: Short) = mul(value.toDouble())
        operator fun <T: RealType<T>> T.times(value: Short) = copy().also { it *= value }
        operator fun <T: RealType<T>> T.timesAssign(value: Int) = mul(value.toDouble())
        operator fun <T: RealType<T>> T.times(value: Int) = copy().also { it *= value }
        operator fun <T: RealType<T>> T.timesAssign(value: Long) = mul(value.toDouble())
        operator fun <T: RealType<T>> T.times(value: Long) = copy().also { it *= value }
        operator fun <T: RealType<T>> T.timesAssign(value: Float) = mul(value)
        operator fun <T: RealType<T>> T.times(value: Float) = copy().also { it *= value }
        operator fun <T: RealType<T>> T.timesAssign(value: Double) = mul(value)
        operator fun <T: RealType<T>> T.times(value: Double) = copy().also { it *= value }
        // primitive type * T
        operator fun <T: RealType<T>> Byte.times(value: T) = value * this
        operator fun <T: RealType<T>> Short.times(value: T) = value * this
        operator fun <T: RealType<T>> Int.times(value: T) = value * this
        operator fun <T: RealType<T>> Long.times(value: T) = value * this
        operator fun <T: RealType<T>> Float.times(value: T) = value * this
        operator fun <T: RealType<T>> Double.times(value: T) = value * this

        // divide
        operator fun <T: RealType<T>> T.divAssign(value: T) = div(value)
        // T / primitive type
        operator fun <T: RealType<T>> T.divAssign(value: Byte) = setReal(realDouble / value)
        operator fun <T: RealType<T>> T.div(value: Byte) = copy().also { it /= value }
        operator fun <T: RealType<T>> T.divAssign(value: Short) = setReal(realDouble / value)
        operator fun <T: RealType<T>> T.div(value: Short) = copy().also { it /= value }
        operator fun <T: RealType<T>> T.divAssign(value: Int) = setReal(realDouble / value)
        operator fun <T: RealType<T>> T.div(value: Int) = copy().also { it /= value }
        operator fun <T: RealType<T>> T.divAssign(value: Long) = setReal(realDouble / value)
        operator fun <T: RealType<T>> T.div(value: Long) = copy().also { it /= value }
        operator fun <T: RealType<T>> T.divAssign(value: Float) = setReal(realDouble / value)
        operator fun <T: RealType<T>> T.div(value: Float) = copy().also { it /= value }
        operator fun <T: RealType<T>> T.divAssign(value: Double) = setReal(realDouble / value)
        operator fun <T: RealType<T>> T.div(value: Double) = copy().also { it /= value }
        // primitive Type / T
        operator fun <T: RealType<T>> Byte.div(value: T) = asType(value).also { it /= value }
        operator fun <T: RealType<T>> Short.div(value: T) = asType(value).also { it /= value }
        operator fun <T: RealType<T>> Int.div(value: T) = asType(value).also { it /= value }
        operator fun <T: RealType<T>> Long.div(value: T) = asType(value).also { it /= value }
        operator fun <T: RealType<T>> Float.div(value: T) = asType(value).also { it /= value }
        operator fun <T: RealType<T>> Double.div(value: T) = asType(value).also { it /= value }
    }
}

class IntegerTypeExtensions {
    companion object {
        // createWithValue
        fun <T: IntegerType<T>> T.createWithValue(value: Byte) = createVariable().also { it.setInteger(value.toInt()) }
        fun <T: IntegerType<T>> T.createWithValue(value: Short) = createVariable().also { it.setInteger(value.toInt()) }
        fun <T: IntegerType<T>> T.createWithValue(value: Int) = createVariable().also { it.setInteger(value) }
        fun <T: IntegerType<T>> T.createWithValue(value: Long) = createVariable().also { it.setInteger(value) }

        fun <T: IntegerType<T>> Byte.asType(type: T) = type.createWithValue(this)
        fun <T: IntegerType<T>> Short.asType(type: T) = type.createWithValue(this)
        fun <T: IntegerType<T>> Int.asType(type: T) = type.createWithValue(this)
        fun <T: IntegerType<T>> Long.asType(type: T) = type.createWithValue(this)

        fun Byte.asType() = asType(ByteType())
        fun Short.asType() = asType(ShortType())
        fun Int.asType() = asType(IntType())
        fun Long.asType() = asType(LongType())

        fun Byte.asUnsignedType() = asType(UnsignedByteType())
        fun Short.asUnsignedType() = asType(UnsignedShortType())
        fun Int.asUnsignedType() = asType(UnsignedIntType())
        fun Long.asUnsignedType() = asType(UnsignedLongType())

        // conversion
        val <T: IntegerType<T>> T.byteType get() = when(this) {
            is ByteType -> copy()
            else -> ByteType().also { it.setInteger(integerLong) }
        }
        val <T: IntegerType<T>> T.unsignedByteType get() = when(this) {
            is UnsignedByteType -> copy()
            else -> UnsignedByteType().also { it.setInteger(integerLong) }
        }
        val <T: IntegerType<T>> T.shortType get() = when(this) {
            is ShortType -> copy()
            else -> ShortType().also { it.setInteger(integerLong) }
        }
        val <T: IntegerType<T>> T.unsignedShortType get() = when(this) {
            is UnsignedShortType -> copy()
            else -> UnsignedShortType().also { it.setInteger(integerLong) }
        }
        val <T: IntegerType<T>> T.intType get() = when(this) {
            is IntType -> copy()
            else -> IntType().also { it.setInteger(integerLong) }
        }
        val <T: IntegerType<T>> T.unsignedIntType get() = when(this) {
            is UnsignedIntType -> copy()
            else -> UnsignedIntType().also { it.setInteger(integerLong) }
        }
        val <T: IntegerType<T>> T.longType get() = when(this) {
            is LongType -> copy()
            else -> LongType().also { it.setInteger(integerLong) }
        }
        val <T: IntegerType<T>> T.unsignedLongType get() = when(this) {
            is UnsignedLongType -> copy()
            else -> UnsignedLongType().also { it.setInteger(integerLong) }
        }

        // add
        // T + primitive type
        operator fun <T: IntegerType<T>> T.plusAssign(value: Byte) = setInteger(integerLong + value)
        operator fun <T: IntegerType<T>> T.plus(value: Byte) = copy().also { it += value }
        operator fun <T: IntegerType<T>> T.plusAssign(value: Short) = setInteger(integerLong + value)
        operator fun <T: IntegerType<T>> T.plus(value: Short) = copy().also { it += value }
        operator fun <T: IntegerType<T>> T.plusAssign(value: Int) = setInteger(integerLong + value)
        operator fun <T: IntegerType<T>> T.plus(value: Int) = copy().also { it += value }
        operator fun <T: IntegerType<T>> T.plusAssign(value: Long) = setInteger(integerLong + value)
        operator fun <T: IntegerType<T>> T.plus(value: Long) = copy().also { it += value }
        // primitive type + T
        operator fun <T: IntegerType<T>> Byte.plus(value: T) = value + this
        operator fun <T: IntegerType<T>> Short.plus(value: T) = value + this
        operator fun <T: IntegerType<T>> Int.plus(value: T) = value + this
        operator fun <T: IntegerType<T>> Long.plus(value: T) = value + this

        // subtract
        // T - primitive type
        operator fun <T: IntegerType<T>> T.minusAssign(value: Byte) = setInteger(integerLong - value)
        operator fun <T: IntegerType<T>> T.minus(value: Byte) = copy().also { it -= value }
        operator fun <T: IntegerType<T>> T.minusAssign(value: Short) = setInteger(integerLong - value)
        operator fun <T: IntegerType<T>> T.minus(value: Short) = copy().also { it -= value }
        operator fun <T: IntegerType<T>> T.minusAssign(value: Int) = setInteger(integerLong - value)
        operator fun <T: IntegerType<T>> T.minus(value: Int) = copy().also { it -= value }
        operator fun <T: IntegerType<T>> T.minusAssign(value: Long) = setInteger(integerLong - value)
        operator fun <T: IntegerType<T>> T.minus(value: Long) = copy().also { it -= value }
        // primitive type - T
        operator fun <T: IntegerType<T>> Byte.minus(value: T) = asType(value) - value
        operator fun <T: IntegerType<T>> Short.minus(value: T) = asType(value) - value
        operator fun <T: IntegerType<T>> Int.minus(value: T) = asType(value) - value
        operator fun <T: IntegerType<T>> Long.minus(value: T) = asType(value) - value

        // multiply
        // T * primitive type
        operator fun <T: IntegerType<T>> T.timesAssign(value: Byte) = setInteger(integerLong * value)
        operator fun <T: IntegerType<T>> T.times(value: Byte) = copy().also { it *= value }
        operator fun <T: IntegerType<T>> T.timesAssign(value: Short) = setInteger(integerLong * value)
        operator fun <T: IntegerType<T>> T.times(value: Short) = copy().also { it *= value }
        operator fun <T: IntegerType<T>> T.timesAssign(value: Int) = setInteger(integerLong * value)
        operator fun <T: IntegerType<T>> T.times(value: Int) = copy().also { it *= value }
        operator fun <T: IntegerType<T>> T.timesAssign(value: Long) = setInteger(integerLong * value)
        operator fun <T: IntegerType<T>> T.times(value: Long) = copy().also { it *= value }
        // primitive type * T
        operator fun <T: IntegerType<T>> Byte.times(value: T) = value * this
        operator fun <T: IntegerType<T>> Short.times(value: T) = value * this
        operator fun <T: IntegerType<T>> Int.times(value: T) = value * this
        operator fun <T: IntegerType<T>> Long.times(value: T) = value * this

        // divide
        // T / primtive type
        operator fun <T: IntegerType<T>> T.divAssign(value: Byte) = setInteger(integerLong / value)
        operator fun <T: IntegerType<T>> T.div(value: Byte) = copy().also { it /= value }
        operator fun <T: IntegerType<T>> T.divAssign(value: Short) = setInteger(integerLong / value)
        operator fun <T: IntegerType<T>> T.div(value: Short) = copy().also { it /= value }
        operator fun <T: IntegerType<T>> T.divAssign(value: Int) = setInteger(integerLong / value)
        operator fun <T: IntegerType<T>> T.div(value: Int) = copy().also { it /= value }
        operator fun <T: IntegerType<T>> T.divAssign(value: Long) = setInteger(integerLong / value)
        operator fun <T: IntegerType<T>> T.div(value: Long) = copy().also { it /= value }
        // primitive type / T
        operator fun <T: IntegerType<T>> Byte.div(value: T) = asType(value).also { it /= value }
        operator fun <T: IntegerType<T>> Short.div(value: T) = asType(value).also { it /= value }
        operator fun <T: IntegerType<T>> Int.div(value: T) = asType(value).also { it /= value }
        operator fun <T: IntegerType<T>> Long.div(value: T) = asType(value).also { it /= value }
    }

}