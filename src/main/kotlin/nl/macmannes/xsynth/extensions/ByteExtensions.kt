package nl.macmannes.xsynth.extensions

fun ByteArray.toHex(): String {
    return joinToString(" ") { it.toHex() }
}

fun Byte.toHex() = "%02x".format(this)

fun Pair<Byte, Byte>.toInt(bits:Int = 7): Int {
    val msb = first.toPositiveInt()
    val lsb = second.toPositiveInt()

    return (msb shl bits) + lsb
}

fun Byte.toPositiveInt() = toInt() and 0xFF