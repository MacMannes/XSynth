package nl.macmannes.xfm2.util.domain.external.json


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import nl.macmannes.xfm2.util.domain.Parameter

@Serializable
data class xParameter(
    @SerialName("Par#")
    var number: Int = 0,

    @SerialName("Value")
    var value: Int = 0
) {
    fun toInternalModel(): Parameter {
        return Parameter(number, value)
    }
}