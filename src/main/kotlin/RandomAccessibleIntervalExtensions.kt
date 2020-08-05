package net.imglib2.imklib

import net.imglib2.Interval
import net.imglib2.Localizable
import net.imglib2.Sampler
import net.imglib2.converter.BiConverter
import net.imglib2.converter.Converter
import net.imglib2.converter.Converters
import net.imglib2.converter.readwrite.SamplerConverter
import net.imglib2.type.Type
import net.imglib2.type.numeric.ComplexType
import net.imglib2.type.numeric.IntegerType
import net.imglib2.type.numeric.NumericType
import net.imglib2.type.numeric.RealType
import net.imglib2.type.numeric.integer.*
import net.imglib2.type.numeric.real.DoubleType
import net.imglib2.type.numeric.real.FloatType
import net.imglib2.util.ConstantUtils
import net.imglib2.view.Views
import kotlin.math.E
import net.imglib2.RandomAccessible as RA
import net.imglib2.RandomAccessibleInterval as RAI

fun <T> constant(constant: T, interval: Interval) = ConstantUtils.constantRandomAccessibleInterval(constant, interval)
fun <T: NumericType<T>> zeros(interval: Interval, type: T) = type.also { it.setZero() }.let { constant(it, interval) }
fun zeros(interval: Interval) = zeros(interval, DoubleType())
fun <T: NumericType<T>> ones(interval: Interval, type: T) = type.also { it.setOne() }.let { constant(it, interval) }
fun ones(interval: Interval) = ones(interval, DoubleType())

fun <T> RAI<T>.translate(vararg translation: Long) = Views.translate(this, *translation)
fun <T> RAI<T>.translate(translation: Localizable) = translate(*translation.positionAsLongArray())
fun <T> RAI<T>.translateInverse(vararg translation: Long) = Views.translateInverse(this, *translation)
fun <T> RAI<T>.translateInverse(translation: Localizable) = translateInverse(*translation.positionAsLongArray())
operator fun <T> RAI<T>.plus(translation: LongArray) = translate(*translation)
operator fun <T> RAI<T>.plus(translation: Localizable) = translate(translation)
operator fun <T> RAI<T>.minus(translation: LongArray) = translateInverse(*translation)
operator fun <T> RAI<T>.minus(translation: Localizable) = translateInverse(translation)

val <T> RAI<T>.type get() = this[minAsPoint()]
val <T: Type<T>> RAI<T>.type get() = this[minAsPoint()].createVariable()

val <T> RAI<T>.iterable get() = Views.iterable(this)
val <T> RAI<T>.flatIterable get() = Views.flatIterable(this)

fun <T, U: Type<U>> RAI<T>.convert(u: U, converter: Converter<T, U>) = Converters.convert(this, converter, u)
inline fun <T, U: Type<U>> RAI<T>.convert(u : U, crossinline converter: (T, U) -> Unit) = convert(u, Converter { a, b -> converter(a, b) })
fun <T, U, V: Type<V>> RAI<T>.convert(that: RAI<U>, v: V, converter: BiConverter<T, U, V>) = Converters.convert(this, that, converter, v)
inline fun <T, U, V: Type<V>> RAI<T>.convert(that: RAI<U>, v: V, crossinline converter: (T, U, V) -> Unit) = convert(that, v, BiConverter { a, b, c -> converter(a, b, c) })
fun <T, U: Type<U>> RAI<T>.convert(converter: SamplerConverter<in T, U>) = Converters.convert(this, converter)
inline fun <T, U: Type<U>> RAI<T>.convert(crossinline converter: (Sampler<out T>) -> U) = convert(SamplerConverter{ t: net.imglib2.Sampler<out T> -> converter(t) })

fun <C: ComplexType<C>, R: RealType<R>> RAI<C>.real(type: R) = convert(ComplexPart.REAL.converter(type))
fun <C: ComplexType<C>, R: RealType<R>> RAI<C>.imaginary(type: R) = convert(ComplexPart.IMAGINARY.converter(type))
val <C: ComplexType<C>> RAI<C>.real get() = real(DoubleType())
val <C: ComplexType<C>> RAI<C>.imaginary get() = imaginary(DoubleType())

fun <T: Type<T>> RAI<T>.extendValue(extension: T) = Views.extendValue(this, extension)
fun <T: RealType<T>> RAI<T>.extendValue(extension: Float) = Views.extendValue(this, extension)
fun <T: RealType<T>> RAI<T>.extendValue(extension: Double) = Views.extendValue(this, extension)
fun <T: IntegerType<T>> RAI<T>.extendValue(extension: Int) = Views.extendValue(this, extension)
fun <T: IntegerType<T>> RAI<T>.extendValue(extension: Long) = Views.extendValue(this, extension)
fun <T> RAI<T>.extendBorder() = Views.extendBorder(this)
fun <T: NumericType<T>> RAI<T>.extendZero() = Views.extendZero(this)
fun <T> RAI<T>.extendMirrorDouble() = Views.extendMirrorDouble(this)
fun <T> RAI<T>.extendMirrorSingle() = Views.extendMirrorSingle(this)

fun <T: RealType<T>, U: RealType<U>> RAI<T>.asType(u: U) = if (u::class == type::class) this as RAI<U> else convert(u) { s, t -> t.setReal(s.realDouble) }
fun <T: IntegerType<T>, U: IntegerType<U>> RAI<T>.asType(u: U) = if (u::class == type::class) this as RAI<U> else convert(u) { s, t -> t.setInteger(s.integerLong) }
val <T: RealType<T>> RAI<T>.asBytes get() = asType(ByteType())
val <T: RealType<T>> RAI<T>.asShorts get() = asType(ShortType())
val <T: RealType<T>> RAI<T>.asInts get() = asType(IntType())
val <T: RealType<T>> RAI<T>.asLongs get() = asType(LongType())
val <T: RealType<T>> RAI<T>.asUnsignedBytes get() = asType(UnsignedByteType())
val <T: RealType<T>> RAI<T>.asUnsignedShorts get() = asType(UnsignedShortType())
val <T: RealType<T>> RAI<T>.asUnsignedInts get() = asType(UnsignedIntType())
val <T: RealType<T>> RAI<T>.asUnsignedLongs get() = asType(UnsignedLongType())
val <T: RealType<T>> RAI<T>.asFloats get() = asType(FloatType())
val <T: RealType<T>> RAI<T>.asDoubles get() = asType(DoubleType())

operator fun <T: RealType<T>> RAI<T>.unaryMinus() = convert(type) { s, t -> t.setReal(-s.realDouble) }
operator fun <T: RealType<T>> RAI<T>.unaryPlus() = this

operator fun <T: RealType<T>> RAI<T>.plus(that: RAI<T>) = convert(that, type) { t, u, v -> v.set(t); v += u }
operator fun <T: RealType<T>> RAI<T>.plus(that: RA<T>) = this + that[this]
operator fun <T: RealType<T>> RAI<T>.plus(value: T) = convert(type) { s, t -> t.set(s); t += value }
operator fun <T: RealType<T>> RAI<T>.plus(value: Byte) = this + type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.plus(value: Short) = this + type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.plus(value: Int) = this + type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.plus(value: Long) = this + type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.plus(value: Float) = this + type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.plus(value: Double) = this + type.also { it.setTo(value) }
operator fun <T: RealType<T>> T.plus(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Byte.plus(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Short.plus(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Int.plus(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Long.plus(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Float.plus(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Double.plus(that: RAI<T>) = that + this

operator fun <T: RealType<T>> RAI<T>.minus(that: RAI<T>) = convert(that, type) { t, u, v -> v.set(t); v -= u }
operator fun <T: RealType<T>> RAI<T>.minus(that: RA<T>) = this - that[this]
operator fun <T: RealType<T>> RAI<T>.minus(value: T) = convert(type) { s, t -> t.set(s); t -= value }
operator fun <T: RealType<T>> RAI<T>.minus(value: Byte) = this - type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.minus(value: Short) = this - type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.minus(value: Int) = this - type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.minus(value: Long) = this - type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.minus(value: Float) = this - type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.minus(value: Double) = this - type.also { it.setTo(value) }
operator fun <T: RealType<T>> T.minus(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Byte.minus(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Short.minus(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Int.minus(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Long.minus(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Float.minus(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Double.minus(that: RAI<T>) = that + this

operator fun <T: RealType<T>> RAI<T>.times(that: RAI<T>) = convert(that, type) { t, u, v -> v.set(t); v *= u }
operator fun <T: RealType<T>> RAI<T>.times(that: RA<T>) = this * that[this]
operator fun <T: RealType<T>> RAI<T>.times(value: T) = convert(type) { s, t -> t.set(s); t *= value }
operator fun <T: RealType<T>> RAI<T>.times(value: Byte) = this * type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.times(value: Short) = this * type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.times(value: Int) = this * type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.times(value: Long) = this * type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.times(value: Float) = this * type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.times(value: Double) = this * type.also { it.setTo(value) }
operator fun <T: RealType<T>> T.times(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Byte.times(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Short.times(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Int.times(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Long.times(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Float.times(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Double.times(that: RAI<T>) = that + this

operator fun <T: RealType<T>> RAI<T>.div(that: RAI<T>) = convert(that, type) { t, u, v -> v.set(t); v /= u }
operator fun <T: RealType<T>> RAI<T>.div(that: RA<T>) = this / that[this]
operator fun <T: RealType<T>> RAI<T>.div(value: T) = convert(type) { s, t -> t.set(s); t /= value }
operator fun <T: RealType<T>> RAI<T>.div(value: Byte) = this / type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.div(value: Short) = this / type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.div(value: Int) = this / type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.div(value: Long) = this / type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.div(value: Float) = this / type.also { it.setTo(value) }
operator fun <T: RealType<T>> RAI<T>.div(value: Double) = this / type.also { it.setTo(value) }
operator fun <T: RealType<T>> T.div(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Byte.div(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Short.div(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Int.div(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Long.div(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Float.div(that: RAI<T>) = that + this
operator fun <T: RealType<T>> Double.div(that: RAI<T>) = that + this

infix fun <T: RealType<T>> RAI<T>.`**`(exponent: RAI<T>) = convert(exponent, type) { t, u, v -> v.set(t); v.pow(u) }
infix fun <T: RealType<T>> RAI<T>.`**`(exponent: Double) = convert(type) { s, t -> t.set(s); t.pow(exponent) }
infix fun <T: RealType<T>> RAI<T>.`**`(exponent: Float) = convert(type) { s, t -> t.set(s); t.pow(exponent) }
infix fun <T: RealType<T>> RAI<T>.`**`(exponent: RealType<*>) = convert(type) { s, t -> t.set(s); t.pow(exponent) }
fun <T: RealType<T>> RAI<T>.exp(base: RAI<T>) = convert(base, type) { t, u, v -> v.set(u); v.pow(t) }
fun <T: RealType<T>> RAI<T>.exp(base: Double = E) = convert(type) { s, t -> t.set(s); t.exp(base) }
fun <T: RealType<T>> RAI<T>.exp(base: Float) = exp(base.toDouble())
fun <T: RealType<T>> RAI<T>.exp(base: RealType<*>) = exp(base.getRealDouble())