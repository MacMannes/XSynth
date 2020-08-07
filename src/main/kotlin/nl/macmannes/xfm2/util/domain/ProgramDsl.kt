package nl.macmannes.xfm2.util.domain

@DslMarker
annotation class ProgramDsl

@ProgramDsl
class ProgramBuilder {
    var shortName = "INITIAL-PROGRAM"
    var longName: String? = null

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
        return Program(shortName, longName, sections)
    }

    /**
     * This method shadows the [co.zsmb.villagedsl.advanced.dsl1.village] method when inside the scope
     * of a [ProgramBuilder], so that villages can't be nested.
     */
    @Suppress("UNUSED_PARAMETER")
    @Deprecated(level = DeprecationLevel.ERROR,
        message = "Programs can't be nested.")
    fun programs(param: () -> Unit = {}) {
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

    fun operatorParameters(name: String = "", setup: OperatorParametersBuilder.() -> Unit = {}) {
        val operatorParameterBuilder = OperatorParametersBuilder(
            keyPath.byAppending(this.name),
            name
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
class OperatorParametersBuilder(
    private val keyPath: KeyPath,
    initialName: String
) {
    var name: String = initialName
    var comment: String? = null
    var type: Parameter.Type = Parameter.Type.INTEGER

    private val parameters = mutableListOf<OperatorParameter>()

    operator fun OperatorParameter.unaryPlus() {
        parameters += this
    }

    fun parameter(operatorNumber: Int, setup: ParameterBuilder.() -> Unit = {}) {
        val parameterBuilder = OperatorParameterBuilder(
            keyPath.byAppending(name),
            operatorNumber
        )
        parameterBuilder.type = type
        parameterBuilder.setup()
        parameters += parameterBuilder.build()
    }

    fun build(): OperatorParameters {
        assert(parameters.size == 6) { "OperatorParameters should contain 6 parameters" }
        return OperatorParameters(name, comment, parameters, type)
    }
}

class OperatorParameterBuilder(keyPath: KeyPath, private val operatorNumber: Int) : ParameterBuilder(keyPath, "OP$operatorNumber") {
    override fun build(): OperatorParameter {
        return OperatorParameter(
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
