package nl.macmannes.xfm2.util.domain

import nl.macmannes.xfm2.util.extensions.toBinaryString

open class Parameter(
    val keyPath: KeyPath,
    val name: String,
    val number: Int,
    var value: Int,
    val comment: String?,
    val type: Type
) : Element {
    override fun render(builder: StringBuilder, indent: String) {
        comment?.let { builder.append("$indent#$it\n") }
        val valueToPrint: String = if (type == Type.INTEGER) value.toString() else "`${value.toBinaryString()}`"
        builder.append("$indent$name: $valueToPrint")
    }

    override fun toString(): String {
        val lastKeyPathElement = keyPath.value.substringAfterLast('.', "")
        val prefix = if (name.toIntOrNull() != null && lastKeyPathElement.isNotEmpty()) "${lastKeyPathElement}_" else ""
        return "$prefix$name: ($number, $value)"
    }

    enum class Type {
        INTEGER, BITWISE
    }
}