package nl.macmannes.xfm2.util.domain.external.json


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.macmannes.xfm2.util.domain.Parameter

@Serializable
data class Parameter(
    @SerialName("Par#")
    var number: Int = 0,

    @SerialName("Value")
    var value: Int = 0
)