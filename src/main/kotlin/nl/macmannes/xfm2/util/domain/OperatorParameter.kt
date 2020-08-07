package nl.macmannes.xfm2.util.domain

import nl.macmannes.xfm2.util.extensions.toBinaryString

class OperatorParameter(
    keyPath: KeyPath,
    operatorNumber: Int,
    number: Int,
    value: Int,
    comment: String?,
    type: Type
): Parameter(keyPath, "OP$operatorNumber", number, value, comment, type) {

    override fun render(builder: StringBuilder, indent: String) {
        val valueToPrint: String = if (type == Type.INTEGER) value.toString() else "'${value.toBinaryString()}'"
        builder.append(valueToPrint)
    }
}