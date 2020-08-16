package nl.macmannes.xsynth.domain

import nl.macmannes.xsynth.domain.external.json.JsonHelper
import nl.macmannes.xsynth.domain.external.sysex.SysExHelper
import nl.macmannes.xsynth.domain.external.xfm2.XFM2BankHelper
import java.io.File

object FileHelper {
    fun readFile(filePath: String, type: Program.Type): Program {
        val file = File(filePath)
        if (file.exists()) {
            return when (file.extension) {
                "json" -> JsonHelper.readFile(filePath, type)
                "yaml" -> ProgramFactory.fromYaml(File(filePath).readText(), type)
                "syx" -> SysExHelper.readSysExFile(file, type)
                "prg" -> XFM2BankHelper.readProgram(file, type)
                else -> throw Exception("Could not determine file type of `${file.name}``")
            }
        } else {
            throw Exception("Could not find file `$filePath`")
        }
    }
}