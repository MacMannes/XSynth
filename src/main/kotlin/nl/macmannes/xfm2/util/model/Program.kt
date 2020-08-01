package nl.macmannes.xfm2.util.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
)