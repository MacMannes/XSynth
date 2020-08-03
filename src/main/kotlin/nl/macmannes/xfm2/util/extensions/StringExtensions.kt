package nl.macmannes.xfm2.util.extensions

fun String.byteArrayFromHexString() = this.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
