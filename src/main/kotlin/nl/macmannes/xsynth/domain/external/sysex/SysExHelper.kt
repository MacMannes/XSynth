package nl.macmannes.xsynth.domain.external.sysex

import nl.macmannes.xsynth.domain.Program
import nl.macmannes.xsynth.domain.ProgramFactory
import nl.macmannes.xsynth.extensions.toHex
import nl.macmannes.xsynth.extensions.toInt
import java.io.File

object SysExHelper {
    fun readSysExFile(file: File, type: Program.Type): Program {
        val name = file.name

        val sysEx = file.readBytes()
        println("Read SysEx file with ${sysEx.size} bytes}")

        val header = sysEx.take(5).toByteArray().toHex()
        val trailer = sysEx.last().toHex()

        if (header == "f0 43 00 00 00" && trailer == "f7") {
            val data = sysEx.toList().subList(3, sysEx.lastIndex - 2)
            println("SysEx data valid. Total ${data.size} bytes")

            val parameters: List<Pair<Int, Int>> = data.chunked(2)
                    .map { (it.first() to it.last()).toInt(7) }
                    .mapIndexed { index, value -> index to value }

            return ProgramFactory.fromParameterValuePairs(parameters, type).apply {
                this.name = name
            }

        } else {
            throw Exception("$name is not a valid XFM2 syx file")
        }
    }
}