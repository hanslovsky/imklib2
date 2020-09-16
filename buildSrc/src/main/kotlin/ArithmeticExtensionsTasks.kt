import java.nio.file.Files

open class GenerateArithmeticExtensionsTask : ExtensionsTask("Arithmetic") {

    @org.gradle.api.tasks.TaskAction
    fun runTask() {
        for ((`as`, name) in typeFileMapping) {
            println("generating arithmetic extensions for $`as` ($name)")
            Files.write(name.second.toPath(), generateArithmeticExtensions(`as`, name.first).withHeader.toByteArray())
            println("successfully generated arithmetic extensions for $`as` ($name)")
            println("${name.second.exists()} ${name.second.toPath()}")
        }
    }

    companion object {
        const val name = "generateArithmeticExtensions"
    }

}

open class GenerateArithmeticScalarExtensionsTask : ExtensionsTask("ArithmeticScalar") {

    @org.gradle.api.tasks.TaskAction
    fun runTask() {
        for ((`as`, name) in typeFileMapping) {
            println("generating arithmetic scalar extensions for $`as` ($name)")
            Files.write(name.second.toPath(), generateArithmeticScalarExtensions(`as`, name.first).withHeader.toByteArray())
        }
    }

    companion object {
        const val name = "generateArithmeticScalarExtensions"
    }

}
