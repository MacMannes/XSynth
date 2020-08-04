package nl.macmannes.xfm2.util.domain.external.yaml

import com.charleskorn.kaml.Yaml
import com.charleskorn.kaml.YamlConfiguration

object YamlHelper {
    private val yaml: Yaml by lazy {
        Yaml(configuration = YamlConfiguration(encodeDefaults = false))
    }

    fun createYaml(from: nl.macmannes.xfm2.util.domain.Program): String {
        return yaml.stringify(Program.serializer(), Program.from(from))
    }
}
