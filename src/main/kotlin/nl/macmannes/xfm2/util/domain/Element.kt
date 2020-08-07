package nl.macmannes.xfm2.util.domain

interface Element {
    fun render(builder: StringBuilder, indent: String)
}