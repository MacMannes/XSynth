package nl.macmannes.xsynth.domain

import nl.macmannes.xsynth.extensions.intFromBinary

data class Program(
    var type: Type,
    var name: String,
    val sections: List<Section>,
    val nameRange: IntRange? = null
) : Element {
    enum class Type {
        XFM2,
        XVA1
    }

    val parameters: List<Parameter> by lazy { sections.map { it.parameters }.flatten() }
    val parametersByNumber: Map<Int, Parameter> by lazy { parameters.map { it.number to it }.toMap() }
    val parametersByKeyPath: Map<String, Parameter> by lazy { parameters.map { it.keyPath.byAppending(it.name).value to it }.toMap() }
    val sectionNames: Set<String> by lazy {
        sections.map { it.sectionNames }
            .flatten()
            .toSet()
    }

    fun setParameter(param: Pair<Int, Int>) {
        if (param.second <= 256) {
            parametersByNumber[param.first]?.let { it.value = param.second }
        }
    }

    fun setParameterBits(param: Pair<Int, String>) {
        parametersByNumber[param.first]?.let {
            val value = param.second.intFromBinary()
            if (value <= 256) {
                it.value = value
            }
        }
    }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("${indent}${type.name}-Program:\n")
        val newIndent = "$indent  "

        builder.append("${newIndent}Name: $name\n")

        builder.append("\n")

        sections.forEach { section ->
            section.render(builder, newIndent)
        }
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, "")
        return builder.toString()
    }
}