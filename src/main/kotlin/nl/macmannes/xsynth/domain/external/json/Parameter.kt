package nl.macmannes.xsynth.domain.external.json


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Parameter(
    @SerialName("Par#")
    var number: Int = 0,

    @SerialName("Value")
    var value: Int = 0
)