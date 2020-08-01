package nl.macmannes.xfm2.util.extensions

fun ByteArray.toHex(): String {
    return joinToString(" ") { "%02X".format(it) }
}