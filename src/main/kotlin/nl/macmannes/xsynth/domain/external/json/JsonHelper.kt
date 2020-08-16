package nl.macmannes.xsynth.domain.external.json

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.io.File


object JsonHelper {
    private val json: Json by lazy {
        Json(JsonConfiguration.Stable)
    }

    fun readFile(filePath: String, type: nl.macmannes.xsynth.domain.Program.Type): nl.macmannes.xsynth.domain.Program {
        return json.parse(Program.serializer(), File(filePath).readText()).toInternalModel(type)
    }
}