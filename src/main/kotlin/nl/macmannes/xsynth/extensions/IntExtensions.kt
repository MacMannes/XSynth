package nl.macmannes.xsynth.extensions

fun Int.toBinaryString(len: Int = 8): String {
    return String.format(
            "%" + len + "s",
            Integer.toBinaryString(this)
    ).replace(" ".toRegex(), "0")
}

fun List<Int>.toFormattedHexString(): String {
    val stringBuilder = StringBuilder()

    this.forEachIndexed { index, byte ->
        if (index == 0) {
            stringBuilder.append("000: ")
        } else if (index > 0 && index % 20 == 0) {
            stringBuilder.append("\n")
            stringBuilder.append(index.toString().padStart(3, '0'))
            stringBuilder.append(": ")
        } else if (index > 0 && index % 10 == 0) {
            stringBuilder.append("   ")
            stringBuilder.append(index.toString().padStart(3, '0'))
            stringBuilder.append(": ")
        }

        stringBuilder.append(String.format("%02X ", byte))
    }

    stringBuilder.append("\b")
    return stringBuilder.toString()
}