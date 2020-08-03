package nl.macmannes.xfm2.util

import jssc.SerialPort
import jssc.SerialPortException
import jssc.SerialPortList
import nl.macmannes.xfm2.util.domain.FileHelper
import java.util.concurrent.TimeUnit


class XFM2Service {
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

    fun readProgram(comPortName: String, fileName: String) {
        println("Reading program `$fileName`")

        getSerialPort(comPortName)?.let { serialPort ->
            try {
                val program = FileHelper.readFile(fileName)
                println("Successfully read program `${program.name}` from disk")

                openPort(serialPort)
                Thread.sleep(500)

                println("Sending program to XMF2 buffer...")

                program.parameters
                        .forEach { parameter ->
                    val parameterNumber: IntArray = if (parameter.number > 254) {
                        intArrayOf(255, parameter.number - 256)
                    } else {
                        intArrayOf(parameter.number)
                    }

                    val intsToSend = intArrayOf(115, *parameterNumber, parameter.value)
//                    println("Setting #${parameter.number} to ${parameter.value} (${intsToSend.toList().joinToString(" ") { "%02x".format(it) }})")

                    serialPort.writeIntArray(intsToSend)
                }

                println("Done")

                println("Verifying...")

                serialPort.writeByte('d'.toByte())
                val statusList = serialPort.readIntArray(512, 3000).toList()
                        .mapIndexed { index, value -> if (value == program.parameters[index].value) 0 else 1 }
                if (statusList.contains(1)) {
                    System.err.println("Error: Could not put program into buffer")
                } else {
                    println("OK. Successfully put program `${program.name}` into buffer")
                }

                println("Closing COM port")
                serialPort.closePort()


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