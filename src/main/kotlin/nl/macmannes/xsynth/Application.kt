package nl.macmannes.xsynth

import nl.macmannes.xsynth.domain.Program
import nl.macmannes.xsynth.domain.Program.Type.*
import nl.macmannes.xsynth.domain.ProgramFactory
import nl.macmannes.xsynth.extensions.toPositiveInt
import org.apache.commons.cli.*


fun main(args: Array<String>) {
    println("##############")
    println("### XSynth ###")
    println("##############")
    println()

    val options = Options()
            .addOption("m", "mode", true, "XFM2 or XVA1 mode")
            .addOption("dsl", "dsl-test", false, "Tests the DSL")
            .addOption("lc", "list-comports", false, "Lists all available comports")
            .addOption("gp", "get-program", false, "Gets current program")
            .addOption("ip", "init-program", false, "Inits program")
            .addOption(
                Option.builder("c")
                    .longOpt("comport")
                    .argName("port")
                    .hasArg()
                    .desc("The comport to use (you can use the number or the name which you get from the --list-comports command )\"")
                    .build()
            )
            .addOption(
                Option.builder("u")
                    .longOpt("upload")
                    .argName("file")
                    .hasArg()
                    .desc("Reads a program from disk and uploads it in the XFM2 buffer")
                    .build()
            )
            .addOption("h", "help", false, "Shows this help text")

    val commandLine: CommandLine
    try {
        commandLine = DefaultParser().parse(options, args)
    } catch (e: ParseException) {
        System.err.println(e.message)
        printHelp(options)
        return
    }

    val mode: String? = commandLine.getOptionValue("m")

    val programType: Program.Type = if (mode.equals("XVA1", ignoreCase = true)) XVA1 else XFM2

    val comport: String? = commandLine.getOptionValue("c")
    val inputFileName: String? = commandLine.getOptionValue("u")

    when {
        commandLine.hasOption("dsl") -> testDsl()
        commandLine.hasOption("") -> printHelp(options)
        commandLine.hasOption("lc") -> {
            XSynthService().listComPorts()
        }
        comport != null -> {
            when {
                (commandLine.hasOption("ip")) -> XSynthService().initProgram(comport)
                (commandLine.hasOption("gp")) -> XSynthService().getActiveProgram(comport, programType)
                (inputFileName != null) -> XSynthService().uploadProgram(comport, inputFileName, programType)
                else -> XSynthService().test(comport)
            }
        }
        else -> {
            System.err.println("Please specify which comport you want to use")
            printHelp(options)
        }

    }

}

fun testDsl() {
    val program = ProgramFactory.createXFM2Program()

    val nameInts = program.name.toByteArray().map { it.toPositiveInt() }
    println("nameInts: $nameInts")

    val name = String(nameInts.map { it.toByte() }.toByteArray())
    println("name from Ints: $name")

    val keyPaths = program.parametersByKeyPath.keys.joinToString("\n")
    println("KeyPaths for parameters:\n$keyPaths")

    program.name = "Hallo"

    val yaml = program.toString()
    println("$yaml\n\n")
    val parsedProgram = ProgramFactory.fromYaml(yaml)
    println("$parsedProgram\n\n")

    val parameterMap = program.parametersByNumber
    println("Parameter 5 = ${parameterMap[5]?.value}")
    println("Parameter 11 = ${parameterMap[11]?.value}")
    parameterMap[286]?.let { it.value = 201 }
    println("Parameter 286 = ${parameterMap[286]?.value}")
    println("Parameter 286 (original = ${ProgramFactory.createXFM2Program().parametersByNumber[286]?.value})")

}

private fun printHelp(options: Options?) {
    HelpFormatter().printHelp("xsynth", options)
}