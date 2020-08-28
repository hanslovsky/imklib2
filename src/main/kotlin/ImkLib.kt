package net.imglib2.imklib

import net.imglib2.Interval
import net.imglib2.img.array.ArrayImg
import net.imglib2.img.array.ArrayImgs
import net.imglib2.imklib.net.imglib2.imklib.io.io as _io
import net.imglib2.type.NativeType
import net.imglib2.type.numeric.NumericType
import net.imglib2.type.numeric.complex.ComplexDoubleType
import net.imglib2.type.numeric.complex.ComplexFloatType
import net.imglib2.type.numeric.real.DoubleType
import net.imglib2.util.ConstantUtils
import java.math.BigInteger

object imklib {

    fun bits(vararg dim: Long) = ArrayImgs.bits(*dim)
    fun booleans(vararg dim: Long) = ArrayImgs.booleans(*dim)

    fun argbs(vararg dim: Long) = ArrayImgs.argbs(*dim)

    fun bytes(vararg dim: Long) = ArrayImgs.bytes(*dim)
    fun shorts(vararg dim: Long) = ArrayImgs.shorts(*dim)
    fun ints(vararg dim: Long) = ArrayImgs.ints(*dim)
    fun longs(vararg dim: Long) = ArrayImgs.longs(*dim)

    fun unsignedBytes(vararg dim: Long) = ArrayImgs.unsignedBytes(*dim)
    fun unsignedShorts(vararg dim: Long) = ArrayImgs.unsignedShorts(*dim)
    fun unsignedInts(vararg dim: Long) = ArrayImgs.unsignedInts(*dim)
    fun unsignedLongs(vararg dim: Long) = ArrayImgs.unsignedLongs(*dim)

    fun unsigned2Bits(vararg dim: Long) = ArrayImgs.unsigned2Bits(*dim)
    fun unsigned4Bits(vararg dim: Long) = ArrayImgs.unsigned4Bits(*dim)
    fun unsigned12Bits(vararg dim: Long) = ArrayImgs.unsigned12Bits(*dim)
    fun unsigned128Bits(vararg dim: Long) = ArrayImgs.unsigned128Bits(*dim)
    fun unsignedVariableBitLengths(nbits: Int, vararg dim: Long) = ArrayImgs.unsignedVariableBitLengths(nbits, *dim)

    fun floats(vararg dim: Long) = ArrayImgs.floats(*dim)
    fun doubles(vararg dim: Long) = ArrayImgs.doubles(*dim)

    fun complexFloats(vararg dim: Long) = ArrayImgs.complexFloats(*dim)
    fun complexDoubles(vararg dim: Long) = ArrayImgs.complexDoubles(*dim)

    // with initializers
    inline fun <T : NativeType<T>> ArrayImg<T, *>.init(init: (Int, T) -> Unit) = also { it.forEachIndexed { i, t -> init(i, t) } }

    inline fun bits(vararg dim: Long, init: (Int) -> Boolean) = bits(*dim).init { i, t -> t.set(init(i)) }
    inline fun booleans(vararg dim: Long, init: (Int) -> Boolean) = booleans(*dim).init { i, t -> t.set(init(i)) }

    inline fun argbs(vararg dim: Long, init: (Int) -> Int) = argbs(*dim).init { i, t -> t.set(init(i)) }

    inline fun bytes(vararg dim: Long, init: (Int) -> Byte) = bytes(*dim).init { i, t -> t.set(init(i)) }
    inline fun shorts(vararg dim: Long, init: (Int) -> Short) = shorts(*dim).init { i, t -> t.set(init(i)) }
    inline fun ints(vararg dim: Long, init: (Int) -> Int) = ints(*dim).init { i, t -> t.set(init(i)) }
    inline fun longs(vararg dim: Long, init: (Int) -> Long) = longs(*dim).init { i, t -> t.set(init(i)) }

    inline fun unsignedBytes(vararg dim: Long, init: (Int) -> Int) = unsignedBytes(*dim).init { i, t -> t.set(init(i)) }
    inline fun unsignedShorts(vararg dim: Long, init: (Int) -> Int) = unsignedShorts(*dim).init { i, t -> t.set(init(i)) }
    inline fun unsignedInts(vararg dim: Long, init: (Int) -> Long) = unsignedInts(*dim).init { i, t -> t.set(init(i)) }
    inline fun unsignedLongs(vararg dim: Long, init: (Int) -> Long) = unsignedLongs(*dim).init { i, t -> t.set(init(i)) }

    inline fun unsigned2Bits(vararg dim: Long, init: (Int) -> Long) = unsigned2Bits(*dim).init { i, t -> t.set(init(i)) }
    inline fun unsigned4Bits(vararg dim: Long, init: (Int) -> Long) = unsigned4Bits(*dim).init { i, t -> t.set(init(i)) }
    inline fun unsigned12Bits(vararg dim: Long, init: (Int) -> Long) = unsigned12Bits(*dim).init { i, t -> t.set(init(i)) }
    inline fun unsigned128Bits(vararg dim: Long, init: (Int) -> BigInteger) = unsigned128Bits(*dim).init { i, t -> t.set(init(i)) }
    inline fun unsignedVariableBitLengths(nbits: Int, vararg dim: Long, init: (Int) -> Long) = unsignedVariableBitLengths(nbits, *dim).init { i, t -> t.set(init(i)) }

    inline fun floats(vararg dim: Long, init: (Int) -> Float) = floats(*dim).init { i, t -> t.set(init(i)) }
    inline fun doubles(vararg dim: Long, init: (Int) -> Double) = doubles(*dim).init { i, t -> t.set(init(i)) }

    inline fun complexFloats(vararg dim: Long, init: (Int, ComplexFloatType) -> Unit) = complexFloats(*dim).init(init)
    inline fun complexDoubles(vararg dim: Long, init: (Int, ComplexDoubleType) -> Unit) = complexDoubles(*dim).init(init)


    // virtual constant RA & RAI
    fun <T> constant(constant: T, numDimensions: Int) = ConstantUtils.constantRandomAccessible(constant, numDimensions)
    fun <T> constant(constant: T, interval: Interval) = ConstantUtils.constantRandomAccessibleInterval(constant, interval)

    fun <T: NumericType<T>> zeros(type: T, numDimensions: Int) = type.also { it.setZero() }.let { constant(it, numDimensions) }
    fun <T: NumericType<T>> zeros(interval: Interval, type: T) = type.also { it.setZero() }.let { constant(it, interval) }
    fun zeros(numDimensions: Int) = zeros(DoubleType(), numDimensions)
    fun zeros(interval: Interval) = zeros(interval, DoubleType())

    fun <T: NumericType<T>> ones(type: T, numDimensions: Int) = type.also { it.setOne() }.let { constant(it, numDimensions) }
    fun <T: NumericType<T>> ones(interval: Interval, type: T) = type.also { it.setOne() }.let { constant(it, interval) }
    fun ones(numDimensions: Int) = ones(DoubleType(), numDimensions)
    fun ones(interval: Interval) = ones(interval, DoubleType())

    val io = _io

}