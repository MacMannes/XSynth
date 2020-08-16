package nl.macmannes.xsynth

import jssc.SerialPort
import jssc.SerialPortException
import jssc.SerialPortList
import nl.macmannes.xsynth.domain.FileHelper
import nl.macmannes.xsynth.domain.Parameter
import nl.macmannes.xsynth.domain.Program
import nl.macmannes.xsynth.domain.ProgramFactory
import nl.macmannes.xsynth.extensions.toFormattedHexString
import java.util.concurrent.TimeUnit


class XSynthService() {
    fun listComPorts() {
        println("Available COM ports:")
        SerialPortList.getPortNames().forEachIndexed {index, name ->
            println("(${index + 1}) $name")
        }
    }

    fun test(comPortName: String) {
        getSerialPort(comPortName)?.let { serialPort ->
            try {
                openPort(serialPort)

                Thread.sleep(1000)

                //Writes data to port
                println("Send `d` command")

                serialPort.addEventListener { event ->
                    if (event.isRXCHAR) {
                        println("Received ${event.eventValue} bytes...")
                        val bytes: ByteArray = serialPort.readBytes(event.eventValue)

                        bytes.forEachIndexed { index, byte ->
                            if (index > 0 && index % 10 == 0) {
                                print(" ")
                            }
                            if (index > 0 && index % 20 == 0) {
                                println()
                            }
                            print(String.format("%02X ", byte))
                        }
                        println()
                    }
                }

                serialPort.writeBytes(byteArrayOf('d'.toByte()))

                Thread.sleep(500)

//                println("Setting parameter 450 to 5")
//                val paramNumber = 450
//                serialPort.writeIntArray(intArrayOf(115, 255, paramNumber - 256, 5))
//                Thread.sleep(500)
//
//                println("reading parameter 450 ")
//                serialPort.writeIntArray(intArrayOf(103, 255, 450 - 256))
//                Thread.sleep(500)
//
//
//                println("Sleeping for a while")
//                Thread.sleep(TimeUnit.SECONDS.toMillis(10))
//
//
//                println("Send `r 0` command")
//                serialPort.writeBytes(byteArrayOf('r'.toByte(), 0))
//                Thread.sleep(500)


                println("Closing COM port")
                serialPort.closePort()
            } catch (ex: SerialPortException) {
                System.err.println("Error: ${ex.exceptionType}")
            }
        }
    }

    fun uploadProgram(comPortName: String, fileName: String, type: Program.Type) {
        println("Reading program `$fileName`")

        getSerialPort(comPortName)?.let { serialPort ->
            try {
                val program = FileHelper.readFile(fileName, type)
                val name = program.name
                println("Successfully read program `$name` from disk")

                openPort(serialPort)

                println("Sending program to XMF2 buffer...")

                program.parameters
                        .filter { it.number != 255 } // Parameter 255 can't be set
                        .forEach { parameter ->
                            writeParameter(parameter, serialPort)
                        }

                println("Done")

                println("Verifying...")

                var errorList = verify(serialPort, program)
                if (errorList.isNotEmpty()) {
                    println("Some parameters could not be sent. Retrying in a while...")
                    TimeUnit.MILLISECONDS.sleep(400)
                    errorList
                            .map { error -> program.parameters.first { it.number == error.number} }
                            .forEach { parameter ->
                                writeParameter(parameter, serialPort)
                            }
                    errorList = verify(serialPort, program)
                }

                if (errorList.isEmpty()) {
                    println("OK. Successfully put program `$name` into buffer")
                } else {
                    System.err.println("Error: Could not put program into buffer. Errors: $errorList")
                }

                println("Closing COM port")
                serialPort.closePort()

//                println("\n\n${YamlHelper.createYaml(program)}")

            } catch (e: Exception) {
                System.err.println("Error: Could not put program into buffer (${e.message})")
            }
        }
    }

    private fun verify(serialPort: SerialPort, program: Program): List<Parameter> {
        serialPort.writeByte('d'.toByte())
        val currentValues = serialPort.readIntArray(512, 3000).toList()
        val errorList = program.parameters
                .filter { it.number != 255 } // Parameter 255 can't be set
                .mapNotNull { parameter -> if (parameter.value == currentValues[parameter.number]) null else parameter }
        return errorList
    }

    private fun writeParameter(parameter: Parameter, serialPort: SerialPort) {
        val parameterNumber: IntArray = if (parameter.number < 256) {
            intArrayOf(parameter.number)
        } else {
            intArrayOf(255, parameter.number - 256)
        }

        val intsToSend = intArrayOf(115, *parameterNumber, parameter.value)
        println("Setting #${parameter.number} to ${parameter.value} (${intsToSend.toList().joinToString(" ") { "%02x".format(it) }})")

        serialPort.writeIntArray(intsToSend)
    }


    fun initProgram(comPortName: String) {
        getSerialPort(comPortName)?.let { serialPort ->
            try {
                openPort(serialPort)

                serialPort.writeByte('i'.toByte())

                println("Closing COM port")
                serialPort.closePort()

            } catch (e: Exception) {
                System.err.println("Error: Could not init program (${e.message})")
            }
        }
    }

    fun getActiveProgram(comPortName: String, type: Program.Type) {
        getSerialPort(comPortName)?.let { serialPort ->
            try {
                openPort(serialPort)

                serialPort.writeByte('d'.toByte())
                val currentValues = serialPort.readIntArray(512, 3000).toList()
                val parameters = currentValues.mapIndexed { number, value -> number to value }
                val program = ProgramFactory.fromParameterValuePairs(parameters, type)

                println("Closing COM port")
                serialPort.closePort()

                println("---------- HEX -----------")
                println(currentValues.toFormattedHexString())

                println("---------- YAML ----------")
                println(program)
                println("--------------------------")

            } catch (e: Exception) {
                System.err.println("Error: Could not put program into buffer (${e.message})")
            }
        }
    }

    private fun openPort(serialPort: SerialPort) {
        println("Opening COM port ${serialPort.portName}")
        serialPort.openPort()
        serialPort.setParams(
                500000,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE
        )
    }

    private fun getSerialPort(name: String): SerialPort? = try {
        val serialPortNumber = name.toIntOrNull()
        val serialPortName = if (serialPortNumber != null) SerialPortList.getPortNames()[serialPortNumber - 1] else name
        SerialPort(serialPortName)
    } catch (e: Exception) {
        System.err.println("Error: Could find comport `$name`")
        null
    }

}