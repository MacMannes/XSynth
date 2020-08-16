package nl.macmannes.xsynth.domain.external.xfm2

import nl.macmannes.xsynth.domain.Program
import nl.macmannes.xsynth.domain.ProgramFactory
import nl.macmannes.xsynth.extensions.toPositiveInt
import java.io.File

object XFM2BankHelper {
    fun readProgram(file: File, type: Program.Type): Program {
        val name = file.name

        val data = file.readBytes()

        if (data.size == 512) {
            println("XFM2 program data valid. Total ${data.size} bytes")

            val parameters: List<Pair<Int, Int>> = data
                    .mapIndexed { index, value -> index to value.toPositiveInt() }

            return ProgramFactory.fromParameterValuePairs(parameters, type).apply {
                this.name = name
            }
        } else {
            throw Exception("$name is not a valid XFM2 program file (should be 512 bytes in size)")
        }
    }
}