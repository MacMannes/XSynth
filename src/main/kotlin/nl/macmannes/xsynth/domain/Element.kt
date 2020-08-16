package nl.macmannes.xsynth.domain

interface Element {
    fun render(builder: StringBuilder, indent: String)
}