package nl.macmannes.xsynth.domain.external.json


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.macmannes.xsynth.domain.Program
import nl.macmannes.xsynth.domain.ProgramFactory

@Serializable
data class Program(
    @SerialName("Short_Name")
    var shortName: String = "",

    @SerialName("Long_Name")
    var longName: String = "",

    @SerialName("Description")
    var description: String = "",

    @SerialName("Creator")
    var creator: String = "",

    @SerialName("date")
    var date: String = "",

    @SerialName("hash")
    var hash: String = "",

    @SerialName("parameters")
    var parameters: List<Parameter> = listOf()
) {
    fun toInternalModel(type: Program.Type): Program {
        val program = ProgramFactory.fromParameterValuePairs(
            this.parameters.map { it.number to it.value },
            type
        )

        program.name = shortName

        return program

    }
}