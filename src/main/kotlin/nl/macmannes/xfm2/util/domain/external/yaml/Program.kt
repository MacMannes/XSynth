package nl.macmannes.xfm2.util.domain.external.yaml

import kotlinx.serialization.Serializable

@Serializable
data class Program(
    var shortName: String = "",
    var longName: String? = null,
    var parameters: Map<Int, Int> = mapOf()
) {
    companion object {
        fun from(program: nl.macmannes.xfm2.util.domain.Program): Program {
            return Program(
                shortName = program.shortName,
                longName = program.longName,
                parameters = program.parameters
                    .map { it.number to it.value }
                    .toMap()
            )

        }
    }
}