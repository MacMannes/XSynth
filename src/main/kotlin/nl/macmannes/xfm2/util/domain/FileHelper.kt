package nl.macmannes.xfm2.util.domain

import nl.macmannes.xfm2.util.domain.external.json.JsonHelper
import nl.macmannes.xfm2.util.domain.external.sysex.SysExHelper
import nl.macmannes.xfm2.util.domain.external.xfm2.XFM2BankHelper
import java.io.File

object FileHelper {
    fun readFile(filePath: String): Program {
        val file = File(filePath)
        if (file.exists()) {
            return when (file.extension) {
                "json" -> JsonHelper.readFile(filePath)
                "yaml" -> ProgramFactory.fromYaml(File(filePath).readText())
                "syx" -> SysExHelper.readSysExFile(file)
                "prg" -> XFM2BankHelper.readProgram(file)
                else -> throw Exception("Could not determine file type of `${file.name}``")
            }
        } else {
            throw Exception("Could not find file `$filePath`")
        }
    }
}