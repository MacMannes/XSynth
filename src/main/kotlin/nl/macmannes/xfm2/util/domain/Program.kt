package nl.macmannes.xfm2.util.domain

import nl.macmannes.xfm2.util.extensions.intFromBinary

data class Program(
    var shortName: String,
    var longName: String?,
    val sections: List<Section>
) : Element {
    val name: String
        get() {
            val long = longName
            return if (long.isNullOrEmpty()) shortName else long
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
        builder.append("${indent}XFM2-Program:\n")
        val newIndent = "$indent  "

        builder.append("${newIndent}ShortName: $shortName\n")
        longName?.let { builder.append("${newIndent}LongName: $it\n") }

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