package nl.macmannes.xfm2.util

import nl.macmannes.xfm2.util.domain.ProgramFactory
import org.apache.commons.cli.*


fun main(args: Array<String>) {
    println("#################")
    println("### XFM2 Util ###")
    println("#################")
    println()

    val options = Options()
            .addOption("dsl", "dsl-test", false, "Tests the DSL")
            .addOption("lc", "list-comports", false, "Lists all available comports")
            .addOption("ga", "get-active-program", false, "Gets current program")
            .addOption(
                Option.builder("c")
                    .longOpt("comport")
                    .argName("port")
                    .hasArg()
                    .desc("The comport to use (you can use the number or the name which you get from the --list-comports command )\"")
                    .build()
            )
            .addOption(
                Option.builder("rp")
                    .longOpt("read-program")
                    .argName("file")
                    .hasArg()
                    .desc("Reads a program from disk and puts it in the XFM2 buffer")
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

    val comport: String? = commandLine.getOptionValue("c")
    val readProgramFileName: String? = commandLine.getOptionValue("rp")

    when {
        commandLine.hasOption("dsl") -> testDsl()
        commandLine.hasOption("") -> printHelp(options)
        commandLine.hasOption("lc") -> {
            XFM2Service().listComPorts()
        }
        comport != null -> {
            when {
                (commandLine.hasOption("ga")) -> XFM2Service().getActiveProgram(comport)
                (readProgramFileName != null) -> XFM2Service().readProgram(comport, readProgramFileName)
                else -> XFM2Service().test(comport)
            }
        }
        else -> {
            System.err.println("Please specify which comport you want to use")
            printHelp(options)
        }

    }

}

fun testDsl() {
    val program = ProgramFactory.createDefault()

    val keyPaths = program.parametersByKeyPath.keys.joinToString("\n")
    println("KeyPaths for parameters:\n$keyPaths")

    program.shortName = "Hallo"
    program.longName = "Hallo daar"

    val yaml = program.toString()
    println("$yaml\n\n")
    val parsedProgram = ProgramFactory.fromYaml(yaml)
    println("$parsedProgram\n\n")

    val parameterMap = program.parametersByNumber
    println("Parameter 5 = ${parameterMap[5]?.value}")
    println("Parameter 11 = ${parameterMap[11]?.value}")
    parameterMap[286]?.let { it.value = 201 }
    println("Parameter 286 = ${parameterMap[286]?.value}")
    println("Parameter 286 (original = ${ProgramFactory.createDefault().parametersByNumber[286]?.value})")

}

private fun printHelp(options: Options?) {
    HelpFormatter().printHelp("xfm2", options)
}