package nl.macmannes.xfm2.util.domain

data class OperatorParameters(
    val name: String,
    val comment: String?,
    val parameters: List<Parameter>,
    val type: Parameter.Type
) : Element {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$name:")
        comment?.let { builder.append("\n$indent  #$it\n") }
        if (type == Parameter.Type.INTEGER) {
            val parameterIndent = if (comment != null) indent else ""
            builder.append("$parameterIndent  { ")
        }
        parameters.forEachIndexed { index, p ->
            if (type == Parameter.Type.BITWISE) {
                builder.append("$indent  ${p.name}: ")
                p.render(builder, "")
                builder.append("\n")
            } else {
                if (index > 0) {
                    builder.append(", ")
                }
                builder.append("${p.name}: ")
                p.render(builder, "")
            }
        }
        if (type == Parameter.Type.INTEGER) {
            builder.append(" }")
        }
    }
}