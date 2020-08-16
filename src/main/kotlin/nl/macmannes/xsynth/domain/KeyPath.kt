package nl.macmannes.xsynth.domain

data class KeyPath(val value: String = "") {
    fun byAppending(newValue: String): KeyPath {
        return if (value.isEmpty()) KeyPath(newValue) else KeyPath(
            "$value.$newValue"
        )
    }
}