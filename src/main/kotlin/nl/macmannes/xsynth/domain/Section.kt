package nl.macmannes.xsynth.domain

data class Section(
    val name: String,
    val keyPath: KeyPath,
    val children: List<Element>
) : Element {

    val parameters: List<Parameter>
        get() {
            return children.map {
                when (it) {
                    is Section -> it.parameters
                    is ParameterList -> it.parameters
                    is Parameter -> listOf(it)
                    else -> listOf()
                }
            }.flatten()
        }
    val sectionNames: Set<String>
        get() {
            val childrenNames = children.mapNotNull {
                when (it) {
                    is Section -> it.sectionNames
                    else -> null
                }
            }.flatten().toTypedArray()

            return setOf(name, *childrenNames)

        }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("\n$indent$name:\n")
        for (c in children) {
            c.render(builder, "$indent  ")
            builder.append("\n")
        }
    }

}