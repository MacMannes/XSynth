package nl.macmannes.xfm2.util.domain.external.yaml

import kotlinx.serialization.Serializable
import nl.macmannes.xfm2.util.domain.Parameter

@Serializable
data class Program(
    var shortName: String = "",
    var longName: String? = null,
    var parameters: Map<Int, Int> = mapOf()
) {
    fun toInternalModel(): nl.macmannes.xfm2.util.domain.Program {
        return nl.macmannes.xfm2.util.domain.Program(
                shortName = this.shortName,
                longName = this.longName,
                parameters = this.parameters
                        .map { Parameter(it.key, it.value) }
                        .toMutableList()
        )
    }

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