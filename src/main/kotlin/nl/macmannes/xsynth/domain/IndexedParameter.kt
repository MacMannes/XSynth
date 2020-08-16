package nl.macmannes.xsynth.domain

import nl.macmannes.xsynth.extensions.toBinaryString

open class IndexedParameter(
    prefix: String,
    keyPath: KeyPath,
    operatorNumber: Int,
    number: Int,
    value: Int,
    comment: String?,
    type: Type
): Parameter(keyPath, "$prefix$operatorNumber", number, value, comment, type) {

    override fun render(builder: StringBuilder, indent: String) {
        val valueToPrint: String = if (type == Type.INTEGER) value.toString() else "'${value.toBinaryString()}'"
        builder.append(valueToPrint)
    }
}