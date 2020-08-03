package nl.macmannes.xfm2.util.domain

data class Program(
        var shortName: String = "",
        var longName: String? = null,
        var parameters: List<Parameter> = listOf()
) {
    val name: String
        get() = longName ?: shortName
}