import com.squareup.kotlinpoet.*
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import net.imglib2.RandomAccessible
import net.imglib2.RandomAccessibleInterval
import net.imglib2.RealRandomAccessible
import net.imglib2.type.Type
import net.imglib2.type.numeric.NumericType
import net.imglib2.type.numeric.integer.*
import net.imglib2.type.numeric.real.DoubleType
import net.imglib2.type.numeric.real.FloatType
import net.imglib2.type.operators.Add
import net.imglib2.type.operators.Div
import net.imglib2.type.operators.Mul
import net.imglib2.type.operators.Sub
import java.lang.Integer.max
import kotlin.reflect.KClass

private val signedIntegers = arithmeticTypes.filter { it.second == identifiers.signedInteger }.map { it.first }
private val unsignedIntegers = arithmeticTypes.filter { it.second == identifiers.unsignedInteger }.map { it.first }

private val arithmeticTypeCombinations = mutableListOf<Triple<KClass<*>, KClass<*>, KClass<*>>>().also {
    for (i in arithmeticTypes.indices) {
        val (kc1, c1) = arithmeticTypes[i]
        for (k in i + 1 until arithmeticTypes.size) {
            val (kc2, c2) = arithmeticTypes[k]
            val o = if (c1 == identifiers.signedInteger && c2 == identifiers.unsignedInteger) {
                val idx1 = signedIntegers.indexOf(kc1)
                val idx2 = unsignedIntegers.indexOf(kc2)
                if (idx1 < idx2) signedIntegers[idx1] else signedIntegers[max(idx2 - 1, 0)]
            } else
                kc1
            it += Triple(kc1, kc2, o)
        }
    }
}

private data class ON(val name: String, val operatorName: String, val type: KClass<*>)

//private val names = arrayOf("plus" to "+", "minus" to "-", "times" to "*", "div" to "/")
private val names = arrayOf(
        ON("plus", "+", Add::class),
        ON("minus", "-", Sub::class),
        ON("times", "*", Mul::class),
        ON("div", "/", Div::class))

 //, RandomAccessibleInterval::class, RealRandomAccessible::class)


fun generateArithmeticExtensions(`as`: String, fileName: String): String {
    val kotlinFile =  FileSpec.builder("net.imglib2.imklib", fileName)
    val container = containers[`as`] ?: error("Key `$`as`' not present in $containers")
    kotlinFile.addAliasedImport(container, `as`)
    kotlinFile.addUnaryPlusMinus(container)
    for ((name, operatorName, type) in names) {
        var index = 0
        kotlinFile.addFunction(generatePlusSameGenericTypes(name = name, operator = operatorName, container = container, t = type, jvmName = "${name}_${++index}"))

        for ((t1, t2, o) in arithmeticTypeCombinations) {
            kotlinFile.addFunction(generatePlusConverting(name, operatorName, container, t1, t2, o, jvmName = "${name}_${++index}"))
            kotlinFile.addFunction(generatePlusConverting(name, operatorName, container, t2, t1, o, jvmName = "${name}_${++index}"))
        }
    }
    return StringBuilder().also { sb -> kotlinFile.build().writeTo(sb) }.toString()
}

private fun generatePlusSameGenericTypes(name: String, operator: String, container: KClass<*>, t: KClass<*>, jvmName: String): FunSpec {
    return generatePlusSameGenericTypes(name, operator, container.asClassName(), t, jvmName)
}

private fun generatePlusSameGenericTypes(name: String, operator: String, container: ClassName, t: KClass<*>, jvmName: String): FunSpec {

    // very helpful GitHub issue: https://github.com/square/kotlinpoet/issues/812
    val genericT = TypeVariableName("T")
    val typeOfT = Type::class.asTypeName().parameterizedBy(genericT)
    val tOfT = t.asTypeName().parameterizedBy(genericT)
    val boundedT = TypeVariableName("T", typeOfT, tOfT)
    val parameterizedContainer = container.parameterizedBy(genericT)

    return FunSpec
            .builder(name)
            .addAnnotation(AnnotationSpec.builder(JvmName::class).addMember("name = %S", jvmName).build())
            .addModifiers(KModifier.OPERATOR)
            .addTypeVariable(boundedT)
            .receiver(parameterizedContainer)
            .addParameter("that", parameterizedContainer)
            .returns(parameterizedContainer)
            .addStatement("return convert(that, type) { t, u, v -> v.set(t); v ${operator}= u }")
            .build()
}

private fun generatePlusConverting(
        name: String,
        operator: String,
        container: KClass<*>,
        t1: KClass<*>,
        t2: KClass<*>,
        o: KClass<*>,
        jvmName: String): FunSpec {
    return generatePlusConverting(name, operator, container.asTypeName(), t1, t2, o, jvmName)
}

private fun generatePlusConverting(
        name: String,
        operator: String,
        container: ClassName,
        t1: KClass<*>,
        t2: KClass<*>,
        o: KClass<*>,
        jvmName: String): FunSpec {
    return generatePlusConverting(name, operator, container, t1.asTypeName(), t2.asTypeName(), o.asTypeName(), jvmName)
}

private fun generatePlusConverting(
        name: String,
        operator: String,
        container: ClassName,
        t1: ClassName,
        t2: ClassName,
        o: ClassName,
        jvmName: String): FunSpec {
    return FunSpec
            .builder(name)
            .addAnnotation(AnnotationSpec.builder(JvmName::class).addMember("name = %S", jvmName).build())
            .addModifiers(KModifier.OPERATOR)
            .receiver(container.parameterizedBy(t1))
            .addParameter("that", container.parameterizedBy(t2))
            .returns(container.parameterizedBy(o))
            // Need · to add non-breaking space
            .addStatement("return this.asType(${o.simpleName}())·$operator·that.asType(${o.simpleName}())")
            .build()
}

private fun FileSpec.Builder.addUnaryPlusMinus(container: ClassName): FileSpec.Builder {
    val genericT = TypeVariableName("T")
    val typeOfT = NumericType::class.asTypeName().parameterizedBy(genericT)
    val boundedT = TypeVariableName("T", typeOfT)
    val parameterizedContainer = container.parameterizedBy(genericT)
    val unaryPlus = FunSpec
            .builder("unaryPlus")
            .addModifiers(KModifier.OPERATOR)
            .addTypeVariable(boundedT)
            .receiver(parameterizedContainer)
            .addStatement("return this")
            .build()
    val unaryMinus = FunSpec
            .builder("unaryMinus")
            .addModifiers(KModifier.OPERATOR)
            .addTypeVariable(boundedT)
            .receiver(parameterizedContainer)
            .addStatement("return convert(type)·{·s,·t ->·t.set(s);·t.mul(-1.0) }")
            .build()
    return this
            .addFunction(unaryPlus)
            .addFunction(unaryMinus)
}