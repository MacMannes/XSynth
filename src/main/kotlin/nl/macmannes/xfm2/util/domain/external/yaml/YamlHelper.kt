package nl.macmannes.xfm2.util.domain.external.yaml

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration
import java.io.File

object YamlHelper {
    private val yaml: Yaml by lazy {
        Yaml(configuration = YamlConfiguration(encodeDefaults = false))
    }

    fun createYaml(from: nl.macmannes.xfm2.util.domain.Program): String {
        return yaml.stringify(Program.serializer(), Program.from(from))
    }

    fun readFile(filePath: String): nl.macmannes.xfm2.util.domain.Program {
        return yaml.parse(Program.serializer(), File(filePath).readText()).toInternalModel()
    }

}
