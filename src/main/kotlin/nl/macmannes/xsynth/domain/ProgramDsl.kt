package nl.macmannes.xsynth.domain

@DslMarker
annotation class ProgramDsl

@ProgramDsl
class ProgramBuilder {
    var type = Program.Type.XFM2
    var name = "INITIAL-PROGRAM"
    var nameRange: IntRange? = null

    private val sections = mutableListOf<Section>()

    operator fun Section.unaryPlus() {
        sections += this
    }

    fun section(name: String, setup: SectionBuilder.() -> Unit = {}) {
        val sectionBuilder = SectionBuilder(KeyPath(), name)
        sectionBuilder.setup()
        sections += sectionBuilder.build()
    }

    fun build(): Program {
        return Program(type, name, sections, nameRange)
    }

    /**
     * This method shadows the [nl.macmannes.xsynth.domain.program] method when inside the scope
     * of a [ProgramBuilder], so that programs can't be nested.
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(level = DeprecationLevel.ERROR,
        message = "Programs can't be nested.")
    fun program(param: () -> Unit = {}) {
    }

}


@ProgramDsl
class SectionBuilder(private val keyPath: KeyPath, val name: String) {
    private val children = mutableListOf<Element>()

    fun build(): Section {
        return Section(name, keyPath, children)
    }

    fun section(name: String = "", setup: SectionBuilder.() -> Unit = {}) {
        val sectionBuilder =
            SectionBuilder(keyPath.byAppending(this.name), name)
        sectionBuilder.setup()
        children += sectionBuilder.build()
    }

    fun operatorParameters(name: String = "", setup: ParameterListBuilder.() -> Unit = {}) {
        val operatorParameterBuilder = ParameterListBuilder(
            keyPath = keyPath.byAppending(this.name),
            initialName = name,
            parameterPrefix = "OP",
            max = 6
        )
        operatorParameterBuilder.setup()
        children += operatorParameterBuilder.build()
    }

    fun oscillatorParameters(name: String = "", setup: ParameterListBuilder.() -> Unit = {}) {
        val operatorParameterBuilder = ParameterListBuilder(
            keyPath = keyPath.byAppending(this.name),
            initialName = name,
            parameterPrefix = "OSC",
            max = 4
        )
        operatorParameterBuilder.setup()
        children += operatorParameterBuilder.build()
    }

    fun parameter(name: String = "", setup: ParameterBuilder.() -> Unit = {}) {
        val parameterBuilder =
            ParameterBuilder(keyPath.byAppending(this.name), name)
        parameterBuilder.setup()
        children += parameterBuilder.build()
    }
}

@ProgramDsl
open class ParameterListBuilder(
    private val keyPath: KeyPath,
    initialName: String,
    private val parameterPrefix: String,
    private val max: Int
) {
    var name: String = initialName
    var comment: String? = null
    var type: Parameter.Type = Parameter.Type.INTEGER

    private val parameters = mutableListOf<IndexedParameter>()

    operator fun IndexedParameter.unaryPlus() {
        parameters += this
    }

    fun parameter(operatorNumber: Int, setup: ParameterBuilder.() -> Unit = {}) {
        val parameterBuilder = IndexedParameterBuilder(
            keyPath.byAppending(name),
            parameterPrefix,
            operatorNumber
        )
        parameterBuilder.type = type
        parameterBuilder.setup()
        parameters += parameterBuilder.build()
    }

    fun build(): ParameterList {
        assert(parameters.size == max) { "ParameterList from type $parameterPrefix should contain $max parameters" }
        return ParameterList(name, comment, parameters, type)
    }
}

class IndexedParameterBuilder(keyPath: KeyPath, private val prefix: String, private val operatorNumber: Int) : ParameterBuilder(keyPath, "$prefix$operatorNumber") {
    override fun build(): IndexedParameter {
        return IndexedParameter(
            prefix,
            keyPath,
            operatorNumber,
            number,
            value,
            comment,
            type
        )
    }
}


@ProgramDsl
open class ParameterBuilder(val keyPath: KeyPath, initialName: String) {
    var name: String = initialName
    var number: Int = 0
    var value: Int = 0
    var comment: String? = null
    var type: Parameter.Type = Parameter.Type.INTEGER

    open fun build(): Parameter {
        return Parameter(keyPath, name, number, value, comment, type)
    }
}

fun program(setup: ProgramBuilder.() -> Unit): Program {
    val programBuilder = ProgramBuilder()
    programBuilder.setup()
    return programBuilder.build()
}
