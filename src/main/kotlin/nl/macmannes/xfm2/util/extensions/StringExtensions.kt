package nl.macmannes.xfm2.util.extensions

fun String.byteArrayFromHexString() = this.chunked(2).map { it.toInt(16).toByte() }.toByteArray()

fun String.intFromBinary(): Int {
    var num = this.toLongOrNull()
    return if (num != null) {
        var decimalNumber = 0
        var i = 0
        var remainder: Long

        while (num.toInt() != 0) {
            remainder = num % 10
            num /= 10
            decimalNumber += (remainder * Math.pow(2.0, i.toDouble())).toInt()
            ++i
        }
        decimalNumber
    } else {
        0
    }
}