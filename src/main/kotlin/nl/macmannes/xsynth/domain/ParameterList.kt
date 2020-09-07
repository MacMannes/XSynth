package nl.macmannes.xsynth.domain

data class ParameterList(
    val name: String,
    val comment: String?,
    val parameters: List<Parameter>,
    val type: Parameter.Type
) : Element {
    override fun render(builder: StringBuilder, indent: String) {
        comment?.let { builder.append("\n$indent#$it:\n") }
        builder.append("$indent$name:")
        when (type) {
            Parameter.Type.BITWISE -> builder.append("\n")
            Parameter.Type.INTEGER -> builder.append("  { ")
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