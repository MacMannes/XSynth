package nl.macmannes.xsynth.domain

import net.mamoe.yamlkt.*

object ProgramFactory {
    fun fromYaml(yaml: String, type: Program.Type = Program.Type.XFM2): Program {
        //TODO: Determine type from YAML itself
        val program = when(type) {
            Program.Type.XFM2 -> createXFM2Program()
            Program.Type.XVA1 -> createXVA1Program()
        }

        val map: Map<String?, Any?> = Yaml.default.parseMap(yaml)
        val firstElementName = "${type.name}-Program"
        (map[firstElementName] as? Map<*, *>)?.let {
            processYamlProgram(
                it,
                program
            )
        }

        return program
    }

    fun fromParameterValuePairs(
        parameters: List<Pair<Int, Int>>,
        type: Program.Type
    ): Program {
        val program = create(type)
        parameters.forEach { program.setParameter(it) }

        if (program.nameRange != null) {
            val nameFromParameters = String(
                parameters
                    .filter { it.first in program.nameRange }
                    .sortedBy { it.first }
                    .map { if (it.second in 32..126) it.second else 32 }
                    .map { it.toByte() }
                    .toByteArray()
            ).trim()
            if (nameFromParameters.isNotEmpty()) {
                program.name = nameFromParameters
            }
        }

        return program
    }

    fun create(type: Program.Type): Program = when (type) {
        Program.Type.XFM2 -> createXFM2Program()
        Program.Type.XVA1 -> createXVA1Program()
    }

    fun createXFM2Program(): Program = program {
        type = Program.Type.XFM2
        name = "XFM2-INITIAL"
        section("OperatorStructureAndParameters") {
            section("InterconnectingOperators") {
                operatorParameters("ALGO") {
                    comment = "Bitwise parameters: -, OP6, OP5, OP4, OP3, OP2, OP1, OUT"
                    type = Parameter.Type.BITWISE

                    parameter(1) { number = 1; value = 5 }
                    parameter(2) { number = 2 }
                    parameter(3) { number = 3; value = 17 }
                    parameter(4) { number = 4 }
                    parameter(5) { number = 5; value = 33 }
                    parameter(6) { number = 6 }

                }
                operatorParameters("FEEDBACK") {
                    comment = "Bitwise parameters: -, -, OP6, OP5, OP4, OP3, OP2, OP1"
                    type = Parameter.Type.BITWISE

                    parameter(1) { number = 7 }
                    parameter(2) { number = 8 }
                    parameter(3) { number = 9 }
                    parameter(4) { number = 10 }
                    parameter(5) { number = 11; value = 250 }
                    parameter(6) { number = 12 }
                }
            }

            section("OscillatorSynchronisation") {
                parameter("SYNC") {
                    number = 13
                }
                operatorParameters("PHASE") {
                    comment = "Sets the initial phase in degrees. 0 = 0 deg; 1 = 90; 2 = 180; 3 = 270"
                    parameter(1) { number = 286 }
                    parameter(2) { number = 287 }
                    parameter(3) { number = 288 }
                    parameter(4) { number = 289 }
                    parameter(5) { number = 290 }
                    parameter(6) { number = 291 }
                }
            }

            section("PitchControls") {
                parameter("MODE") {
                    number = 14
                    type = Parameter.Type.BITWISE
                    comment =
                        "Bitwise parameter: -, -, OP6, OP5, OP4, OP3, OP2, OP1; 0 = track keyboard; 1 = sync to note-on"

                }
                operatorParameters("RATIO") {
                    comment = "i.e. 3 = 3:1 ratio"
                    parameter(1) { number = 15 }
                    parameter(2) { number = 16 }
                    parameter(3) { number = 17 }
                    parameter(4) { number = 18 }
                    parameter(5) { number = 19 }
                    parameter(6) { number = 20 }
                }
                operatorParameters("RATIO_FINE") {
                    comment = "i.e. 0 = 1:1 ratio; 255 ~= 2:1 ratio"
                    parameter(1) { number = 21 }
                    parameter(2) { number = 22 }
                    parameter(3) { number = 23 }
                    parameter(4) { number = 24 }
                    parameter(5) { number = 25 }
                    parameter(6) { number = 26 }
                }
                operatorParameters("FINE") {
                    comment = "Adjusts the operator pitch, in cents (128 = 0 cents)"
                    parameter(1) { number = 27 }
                    parameter(2) { number = 28 }
                    parameter(3) { number = 29 }
                    parameter(4) { number = 30 }
                    parameter(5) { number = 31 }
                    parameter(6) { number = 32 }
                }
            }

            section("OperatorLevel") {
                operatorParameters("LEVEL") {
                    parameter(1) { number = 33 }
                    parameter(2) { number = 34 }
                    parameter(3) { number = 35 }
                    parameter(4) { number = 36 }
                    parameter(5) { number = 37 }
                    parameter(6) { number = 38 }
                }
                operatorParameters("LEVEL_L") {
                    parameter(1) { number = 256 }
                    parameter(2) { number = 258 }
                    parameter(3) { number = 260 }
                    parameter(4) { number = 262 }
                    parameter(5) { number = 264 }
                    parameter(6) { number = 266 }
                }
                operatorParameters("LEVEL_R") {
                    parameter(1) { number = 257 }
                    parameter(2) { number = 259 }
                    parameter(3) { number = 261 }
                    parameter(4) { number = 263 }
                    parameter(5) { number = 265 }
                    parameter(6) { number = 267 }
                }
                operatorParameters("VELO_SENS") {
                    parameter(1) { number = 39 }
                    parameter(2) { number = 40 }
                    parameter(3) { number = 41 }
                    parameter(4) { number = 42 }
                    parameter(5) { number = 43 }
                    parameter(6) { number = 44 }
                }
            }

            section("KeyboardTracking") {
                operatorParameters("KEY_BP") {
                    parameter(1) { number = 45 }
                    parameter(2) { number = 46 }
                    parameter(3) { number = 47 }
                    parameter(4) { number = 48 }
                    parameter(5) { number = 49 }
                    parameter(6) { number = 50 }
                }
                operatorParameters("KEY_LDEPTH") {
                    parameter(1) { number = 51 }
                    parameter(2) { number = 52 }
                    parameter(3) { number = 53 }
                    parameter(4) { number = 54 }
                    parameter(5) { number = 55 }
                    parameter(6) { number = 56 }
                }
                operatorParameters("KEY_RDEPTH") {
                    parameter(1) { number = 57 }
                    parameter(2) { number = 58 }
                    parameter(3) { number = 59 }
                    parameter(4) { number = 60 }
                    parameter(5) { number = 61 }
                    parameter(6) { number = 62 }
                }
                operatorParameters("KEY_LCURVE") {
                    parameter(1) { number = 63 }
                    parameter(2) { number = 64 }
                    parameter(3) { number = 65 }
                    parameter(4) { number = 66 }
                    parameter(5) { number = 67 }
                    parameter(6) { number = 68 }
                }
                operatorParameters("KEY_RCURVE") {
                    parameter(1) { number = 69 }
                    parameter(2) { number = 70 }
                    parameter(3) { number = 71 }
                    parameter(4) { number = 72 }
                    parameter(5) { number = 73 }
                    parameter(6) { number = 74 }
                }
            }



            section("Amplitude Envelop Generator") {
                operatorParameters("L0") {
                    parameter(1) { number = 181 }
                    parameter(2) { number = 182 }
                    parameter(3) { number = 183 }
                    parameter(4) { number = 184 }
                    parameter(5) { number = 185 }
                    parameter(6) { number = 186 }
                }
                operatorParameters("L1") {
                    parameter(1) { number = 75 }
                    parameter(2) { number = 76 }
                    parameter(3) { number = 77 }
                    parameter(4) { number = 78 }
                    parameter(5) { number = 79 }
                    parameter(6) { number = 80 }
                }
                operatorParameters("L2") {
                    parameter(1) { number = 82 }
                    parameter(2) { number = 83 }
                    parameter(3) { number = 84 }
                    parameter(4) { number = 85 }
                    parameter(5) { number = 86 }
                    parameter(6) { number = 87 }
                }
                operatorParameters("L3") {
                    parameter(1) { number = 89 }
                    parameter(2) { number = 90 }
                    parameter(3) { number = 91 }
                    parameter(4) { number = 92 }
                    parameter(5) { number = 93 }
                    parameter(6) { number = 94 }
                }
                operatorParameters("L4") {
                    parameter(1) { number = 96 }
                    parameter(2) { number = 97 }
                    parameter(3) { number = 98 }
                    parameter(4) { number = 99 }
                    parameter(5) { number = 100 }
                    parameter(6) { number = 101 }
                }
                operatorParameters("L5") {
                    parameter(1) { number = 193 }
                    parameter(2) { number = 194 }
                    parameter(3) { number = 195 }
                    parameter(4) { number = 196 }
                    parameter(5) { number = 197 }
                    parameter(6) { number = 198 }
                }

                operatorParameters("DLY") {
                    parameter(1) { number = 187 }
                    parameter(2) { number = 188 }
                    parameter(3) { number = 189 }
                    parameter(4) { number = 190 }
                    parameter(5) { number = 191 }
                    parameter(6) { number = 192 }
                }
                operatorParameters("R1") {
                    parameter(1) { number = 103 }
                    parameter(2) { number = 104 }
                    parameter(3) { number = 105 }
                    parameter(4) { number = 106 }
                    parameter(5) { number = 107 }
                    parameter(6) { number = 108 }
                }
                operatorParameters("R2") {
                    parameter(1) { number = 110 }
                    parameter(2) { number = 111 }
                    parameter(3) { number = 112 }
                    parameter(4) { number = 113 }
                    parameter(5) { number = 114 }
                    parameter(6) { number = 115 }
                }
                operatorParameters("R3") {
                    parameter(1) { number = 117 }
                    parameter(2) { number = 118 }
                    parameter(3) { number = 119 }
                    parameter(4) { number = 120 }
                    parameter(5) { number = 121 }
                    parameter(6) { number = 122 }
                }
                operatorParameters("R4") {
                    parameter(1) { number = 124 }
                    parameter(2) { number = 125 }
                    parameter(3) { number = 126 }
                    parameter(4) { number = 127 }
                    parameter(5) { number = 128 }
                    parameter(6) { number = 129 }
                }
                operatorParameters("R5") {
                    parameter(1) { number = 199 }
                    parameter(2) { number = 200 }
                    parameter(3) { number = 201 }
                    parameter(4) { number = 202 }
                    parameter(5) { number = 203 }
                    parameter(6) { number = 204 }
                }

                operatorParameters("RATE_KEY") {
                    parameter(1) { number = 140 }
                    parameter(2) { number = 141 }
                    parameter(3) { number = 142 }
                    parameter(4) { number = 143 }
                    parameter(5) { number = 144 }
                    parameter(6) { number = 145 }
                }
                parameter("EG_LOOP") { number = 244 }
                parameter("EG_LOOP_SEG") { number = 245 }
            }

            section("AmplitudeModulationSensitivity") {
                operatorParameters("AMS") {
                    parameter(1) { number = 159 }
                    parameter(2) { number = 160 }
                    parameter(3) { number = 161 }
                    parameter(4) { number = 162 }
                    parameter(5) { number = 163 }
                    parameter(6) { number = 164 }
                }
            }

            section("PitchModulationSensitivity") {
                operatorParameters("PMS") {
                    parameter(1) { number = 222 }
                    parameter(2) { number = 223 }
                    parameter(3) { number = 224 }
                    parameter(4) { number = 225 }
                    parameter(5) { number = 226 }
                    parameter(6) { number = 227 }
                }
            }

            section("Oscillators") {
                operatorParameters("WAVE_1") {
                    parameter(1) { number = 236 }
                    parameter(2) { number = 237 }
                    parameter(3) { number = 238 }
                    parameter(4) { number = 239 }
                    parameter(5) { number = 240 }
                    parameter(6) { number = 241 }
                }
                operatorParameters("WAVE_2") {
                    parameter(1) { number = 268 }
                    parameter(2) { number = 269 }
                    parameter(3) { number = 270 }
                    parameter(4) { number = 271 }
                    parameter(5) { number = 272 }
                    parameter(6) { number = 273 }
                }
                operatorParameters("W_MODE") {
                    parameter(1) { number = 274 }
                    parameter(2) { number = 275 }
                    parameter(3) { number = 276 }
                    parameter(4) { number = 277 }
                    parameter(5) { number = 278 }
                    parameter(6) { number = 279 }
                }
                operatorParameters("W_RATIO_SENS") {
                    parameter(1) { number = 280 }
                    parameter(2) { number = 281 }
                    parameter(3) { number = 282 }
                    parameter(4) { number = 283 }
                    parameter(5) { number = 284 }
                    parameter(6) { number = 285 }
                }
            }
        }

        section("ProgramSettings") {
            section("PitchEnvelopGenerator") {
                parameter("L0") { number = 205 }
                parameter("L1") { number = 130 }
                parameter("L2") { number = 131 }
                parameter("L3") { number = 132 }
                parameter("L4") { number = 133 }
                parameter("L5") { number = 207 }

                parameter("DLY") { number = 206 }

                parameter("R1") { number = 134 }
                parameter("R2") { number = 135 }
                parameter("R3") { number = 136 }
                parameter("R4") { number = 137 }
                parameter("R5") { number = 208 }

                parameter("RATE_KEY") { number = 146 }
                parameter("RANGE") { number = 138 }
                parameter("VELO") { number = 139 }
            }

            section("LowFrequencyOscillator") {
                parameter("WAVE") { number = 153 }
                parameter("SPEED") { number = 151 }
                parameter("SYNC") { number = 152 }
                parameter("FADE") { number = 154 }
                parameter("DEPTH_PITCH") { number = 149 }
                parameter("DEPTH_AMP") { number = 150 }
            }

            section("Arpeggiator") {
                parameter("MODE") { number = 450 }
                parameter("TEMPO") { number = 451 }
                parameter("MUL") { number = 453 }
                parameter("OCTAVES") { number = 454 }
            }

            section("PerformanceControls") {
                parameter("CTL_1H") { number = 420 }
                parameter("CTL_1L") { number = 421 }
                parameter("CTL_2H") { number = 422 }
                parameter("CTL_2L") { number = 423 }
                parameter("CTL_3H") { number = 424 }
                parameter("CTL_3L") { number = 425 }
                parameter("CTL_4H") { number = 426 }
                parameter("CTL_4L") { number = 427 }
            }

            section("Other") {
                parameter("BEND_UP") { number = 172 }
                parameter("BEND_DN") { number = 273 }
                parameter("TRANSPOSE") { number = 174 }
                parameter("VOLUME") { number = 180 }
                parameter("PAN") { number = 221 }
                parameter("VELO_OFFSET") { number = 242 }
                parameter("EG RESTART") { number = 246 }
                parameter("LEGATO") { number = 228 }
                parameter("PORTA_MODE") { number = 229 }
                parameter("PORTA_TIME") { number = 230 }
                parameter("TUNING") { number = 251 }
                parameter("OUTPUT") { number = 411 }
                parameter("GAIN") { number = 412 }
                parameter("WAVE_SET") { number = 252 }
            }

            section("Modulations") {
                section("PitchLFO") {
                    parameter("AFTER") { number = 157 }
                    parameter("WHEEL") { number = 155 }
                    parameter("BREATH") { number = 209 }
                    parameter("FOOT") { number = 211 }
                }
                section("AmplitudeLFO") {
                    parameter("AFTER") { number = 158 }
                    parameter("WHEEL") { number = 156 }
                    parameter("BREATH") { number = 210 }
                    parameter("FOOT") { number = 212 }
                }
                section("EGBias") {
                    parameter("AFTER") { number = 213 }
                    parameter("WHEEL") { number = 214 }
                    parameter("BREATH") { number = 215 }
                    parameter("FOOT") { number = 216 }
                }
                section("Pitch") {
                    parameter("AFTER") { number = 217 }
                    parameter("WHEEL") { number = 218 }
                    parameter("BREATH") { number = 219 }
                    parameter("FOOT") { number = 220 }
                }
            }
        }

        section("Effects") {
            section("BitCrusher") {
                parameter("DEPTH") { number = 380 }
            }
            section("Decimator") {
                parameter("DEPTH") { number = 370 }
            }
            section("Filter") {
                parameter("LO") { number = 320 }
                parameter("HI") { number = 321 }
            }
            section("Chorus/Flanger") {
                parameter("DRY") { number = 360 }
                parameter("WET") { number = 361 }
                parameter("MODE") { number = 362 }
                parameter("SPEED") { number = 363 }
                parameter("DEPTH") { number = 364 }
                parameter("FEEDBACK") { number = 365 }
                parameter("LR_PHASE") { number = 366 }
            }
            section("Phaser") {
                parameter("DRY") { number = 310 }
                parameter("WET") { number = 311 }
                parameter("MODE") { number = 312 }
                parameter("SPEED") { number = 314 }
                parameter("DEPTH") { number = 313 }
                parameter("FEEDBACK") { number = 315 }
                parameter("OFFSET") { number = 316 }
                parameter("STAGES") { number = 317 }
                parameter("LR_PHASE") { number = 318 }
            }
            section("AmplitudeModulation") {
                parameter("DEPTH") { number = 332 }
                parameter("SPEED") { number = 330 }
                parameter("RANGE") { number = 331 }
                parameter("LR_PHASE") { number = 333 }
            }
            section("Delay") {
                parameter("DRY") { number = 300 }
                parameter("WET") { number = 301 }
                parameter("MODE") { number = 302 }
                parameter("TIME") { number = 303 }
                parameter("FEEDBACK") { number = 304 }
                parameter("LO") { number = 305 }
                parameter("HI") { number = 306 }
                parameter("TEMPO") { number = 307 }
                parameter("MUL") { number = 308 }
                parameter("DIV") { number = 309 }
            }
            section("FXRouting") {
                parameter("ROUTING") { number = 410 }
            }
            section("Reverb") {
                parameter("DRY") { number = 390 }
                parameter("WET") { number = 391 }
                parameter("MODE") { number = 392 }
                parameter("TIME") { number = 393 }
                parameter("FEEDBACK") { number = 394 }
            }
        }
    }

    fun createXVA1Program(): Program = program {
        type = Program.Type.XVA1
        name = "XVA1-INITIAL"
        nameRange = 480..505

        section("Oscillators") {
            oscillatorParameters("OSC_ON-OFF") {
                parameter(1) { number = 1; value = 1 }
                parameter(2) { number = 2; value = 1 }
                parameter(3) { number = 3}
                parameter(4) { number = 4 }
            }

            parameter("SYNC") {
                comment = "4-bit number, one per oscillator. 0 = unsync'ed, 1 = key-on phase restart"
                type = Parameter.Type.BITWISE
                number = 5
                value = 15
            }

            parameter("MODE") { number = 6 }

            oscillatorParameters("PHASE") {
                comment = "Sets the initial phase in degrees. 0 = 0 deg; 1 = 90; 2 = 180; 3 = 270"
                parameter(1) { number = 7 }
                parameter(2) { number = 8 }
                parameter(3) { number = 9 }
                parameter(4) { number = 10 }
            }

            oscillatorParameters("WAVE") {
                comment = "0 = saw up, 1 = saw down, 2 = square (pulse), 3 = triangle, 4 = sine, 5 = noise"
                parameter(1) { number = 11 }
                parameter(2) { number = 12 }
                parameter(3) { number = 13 }
                parameter(4) { number = 14 }
            }

            oscillatorParameters("P_WIDTH") {
                comment = "128 = square"
                parameter(1) { number = 15; value = 128 }
                parameter(2) { number = 16; value = 128 }
                parameter(3) { number = 17; value = 128 }
                parameter(4) { number = 18; value = 128 }
            }

            oscillatorParameters("TRANSPOSE") {
                parameter(1) { number = 19; value = 116 }
                parameter(2) { number = 20; value = 104 }
                parameter(3) { number = 21; value = 128 }
                parameter(4) { number = 22; value = 128 }
            }

            oscillatorParameters("DETUNE") {
                parameter(1) { number = 23; value = 128 }
                parameter(2) { number = 24; value = 128 }
                parameter(3) { number = 25; value = 122 }
                parameter(4) { number = 26; value = 135 }
            }

            oscillatorParameters("DRIFT") {
                parameter(1) { number = 260 }
                parameter(2) { number = 261 }
                parameter(3) { number = 262 }
                parameter(4) { number = 263 }
            }

            oscillatorParameters("LEVEL") {
                parameter(1) { number = 27 }
                parameter(2) { number = 28 }
                parameter(3) { number = 29 }
                parameter(4) { number = 30 }
            }

            oscillatorParameters("LEVEL_L") {
                parameter(1) { number = 31 }
                parameter(2) { number = 33 }
                parameter(3) { number = 35 }
                parameter(4) { number = 37 }
            }

            oscillatorParameters("LEVEL_R") {
                parameter(1) { number = 32 }
                parameter(2) { number = 34 }
                parameter(3) { number = 36 }
                parameter(4) { number = 38 }
            }

            oscillatorParameters("VELO_SENS") {
                parameter(1) { number = 39 }
                parameter(2) { number = 40 }
                parameter(3) { number = 41 }
                parameter(4) { number = 42 }
            }

            oscillatorParameters("KEY_BP") {
                parameter(1) { number = 43 }
                parameter(2) { number = 44 }
                parameter(3) { number = 45 }
                parameter(4) { number = 46 }
            }

            oscillatorParameters("KEY_LDEPTH") {
                parameter(1) { number = 47 }
                parameter(2) { number = 48 }
                parameter(3) { number = 49 }
                parameter(4) { number = 50 }
            }

            oscillatorParameters("KEY_RDEPTH") {
                parameter(1) { number = 51 }
                parameter(2) { number = 52 }
                parameter(3) { number = 53 }
                parameter(4) { number = 54 }
            }

            oscillatorParameters("KEY_LCURVE") {
                parameter(1) { number = 55 }
                parameter(2) { number = 56 }
                parameter(3) { number = 57 }
                parameter(4) { number = 58 }
            }

            oscillatorParameters("KEY_RCURVE") {
                parameter(1) { number = 59 }
                parameter(2) { number = 60 }
                parameter(3) { number = 61 }
                parameter(4) { number = 62 }
            }

            oscillatorParameters("PMS") {
                parameter(1) { number = 63 }
                parameter(2) { number = 64 }
                parameter(3) { number = 65 }
                parameter(4) { number = 66 }
            }

            oscillatorParameters("AMS") {
                parameter(1) { number = 67 }
                parameter(2) { number = 68 }
                parameter(3) { number = 69 }
                parameter(4) { number = 70 }
            }

            parameter("RING_MOD_3-4") { number = 271 }
        }

        section("Filters") {
            parameter("TYPE") { number = 71 }
            parameter("CUTOFF_1") { number = 72 }
            parameter("CUTOFF_2") { number = 78 }
            parameter("RESO_1") { number = 77 }
            parameter("RESO_2") { number = 79 }
            parameter("VELOCITY") { number = 73 }
            parameter("KBD_TRACK") { number = 74 }
            parameter("EG_DEPTH") { number = 75 }
            parameter("EG_VELOCITY") { number = 76 }
            parameter("VELOCITY_RESO") { number = 276 }
            parameter("KBD_TRACK_RESO") { number = 277 }
            parameter("DRIVE") { number = 275 }
        }

        section("EnvelopGenerators") {
            section("PITCH") {
                parameter("L0") { number = 80 }
                parameter("L1") { number = 85 }
                parameter("L2") { number = 90 }
                parameter("L3") { number = 95 }
                parameter("L4") { number = 100 }
                parameter("L5") { number = 105 }
                parameter("DLY") { number = 110 }
                parameter("R1") { number = 115 }
                parameter("R2") { number = 120 }
                parameter("R3") { number = 125 }
                parameter("R4") { number = 130 }
                parameter("R5") { number = 135 }
            }

            section("CUTOFF") {
                parameter("L0") { number = 81 }
                parameter("L1") { number = 86 }
                parameter("L2") { number = 91 }
                parameter("L3") { number = 96 }
                parameter("L4") { number = 101 }
                parameter("L5") { number = 106 }
                parameter("DLY") { number = 111 }
                parameter("R1") { number = 116 }
                parameter("R2") { number = 121 }
                parameter("R3") { number = 126 }
                parameter("R4") { number = 131 }
                parameter("R5") { number = 136 }
            }

            section("AMP") {
                parameter("L0") { number = 82 }
                parameter("L1") { number = 87 }
                parameter("L2") { number = 92 }
                parameter("L3") { number = 97 }
                parameter("L4") { number = 102 }
                parameter("L5") { number = 107 }
                parameter("DLY") { number = 112 }
                parameter("R1") { number = 117 }
                parameter("R2") { number = 122 }
                parameter("R3") { number = 127 }
                parameter("R4") { number = 132 }
                parameter("R5") { number = 137 }
            }

            section("Global") {
                parameter("EG_LOOP") { number = 145 }
                parameter("EG_LOOP_SEG") { number = 146 }
                parameter("EG_RESTART") { number = 147 }
                parameter("PITCH_RANGE") { number = 148 }
                parameter("PITCH_VELO") { number = 149 }
            }
        }

        section("StepSequencer") {
            parameter("ON-OFF") { number = 428 }
            parameter("VELOCITY") { number = 429 }
            parameter("STEPS") { number = 430 }
            parameter("TEMPO") { number = 431 }
            parameter("MUL") { number = 432 }
            parameter("TRANSPOSE") { number = 433 }
            parameter("STEP_1") { number = 434 }
            parameter("STEP_2") { number = 435 }
            parameter("STEP_3") { number = 436 }
            parameter("STEP_4") { number = 437 }
            parameter("STEP_5") { number = 438 }
            parameter("STEP_6") { number = 439 }
            parameter("STEP_7") { number = 440 }
            parameter("STEP_8") { number = 441 }
            parameter("STEP_9") { number = 442 }
            parameter("STEP_10") { number = 443 }
            parameter("STEP_11") { number = 444 }
            parameter("STEP_12") { number = 445 }
            parameter("STEP_13") { number = 446 }
            parameter("STEP_14") { number = 447 }
            parameter("STEP_15") { number = 448 }
            parameter("STEP_16") { number = 449 }
        }
        
        section("PerformanceControls") {
            parameter("CTL_1H") { number = 400 }
            parameter("CTL_1L") { number = 401 }
            parameter("CTL_2H") { number = 402 }
            parameter("CTL_2L") { number = 403 }
            parameter("CTL_3H") { number = 404 }
            parameter("CTL_3L") { number = 405 }
            parameter("CTL_4H") { number = 406 }
            parameter("CTL_4L") { number = 407 }
            parameter("CTL_5H") { number = 408 }
            parameter("CTL_5L") { number = 409 }
            parameter("CTL_6H") { number = 410 }
            parameter("CTL_6L") { number = 411 }
            parameter("CTL_7H") { number = 412 }
            parameter("CTL_7L") { number = 413 }
            parameter("CTL_8H") { number = 414 }
            parameter("CTL_8L") { number = 415 }
        }
        
        section("LowFrequenceOscillators") {
            section("LFO1") {
                parameter("WAVE") { number = 160 }
                parameter("RANGE") { number = 166 }
                parameter("SPEED") { number = 161 }
                parameter("SYNC") { number = 162 }
                parameter("FADE") { number = 163 }
                parameter("DEPTH_PITCH") { number = 164 }
                parameter("DEPTH_AMP") { number = 165 }
            }
            section("LFO2") {
                parameter("WAVE") { number = 170 }
                parameter("RANGE") { number = 176 }
                parameter("SPEED") { number = 171 }
                parameter("SYNC") { number = 172 }
                parameter("FADE") { number = 173 }
                parameter("DEPTH_PW") { number = 174 }
                parameter("DEPTH_CUTOFF") { number = 175 }
            }
        }

        section("LFO-Modulators") {
            section("PITCH_LFO") {
                parameter("AFTER") { number = 180 }
                parameter("WHEEL") { number = 181 }
                parameter("BREATH") { number = 182 }
                parameter("FOOT") { number = 183 }
            }

            section("PW_LFO") {
                parameter("AFTER") { number = 184 }
                parameter("WHEEL") { number = 185 }
                parameter("BREATH") { number = 186 }
                parameter("FOOT") { number = 187 }
            }
            
            section("FILTER_LFO") {
                parameter("AFTER") { number = 188 }
                parameter("WHEEL") { number = 189 }
                parameter("BREATH") { number = 190 }
                parameter("FOOT") { number = 191 }
            }

            section("AMP_LFO") {
                parameter("AFTER") { number = 192 }
                parameter("WHEEL") { number = 193 }
                parameter("BREATH") { number = 194 }
                parameter("FOOT") { number = 195 }
            }
        }

        section("MidiModulations") {
            section("Pitch") {
                parameter("AFTER") { number = 200 }
                parameter("BREATH") { number = 201 }
                parameter("FOOT") { number = 202 }
                parameter("TEMP") { number = 220 }
                parameter("VC0") { number = 221 }
                parameter("VC1") { number = 222 }
                parameter("RND") { number = 203 }
            }
            
            section("PulseWidth") {
                parameter("AFTER") { number = 204 }
                parameter("WHEEL") { number = 205 }
                parameter("BREATH") { number = 206 }
                parameter("FOOT") { number = 207 }
                parameter("TEMP") { number = 223 }
                parameter("VC0") { number = 224 }
                parameter("VC1") { number = 225 }
            }

            section("Cutoff") {
                parameter("AFTER") { number = 208 }
                parameter("WHEEL") { number = 209 }
                parameter("BREATH") { number = 210 }
                parameter("FOOT") { number = 211 }
                parameter("TEMP") { number = 226 }
                parameter("VC0") { number = 227 }
                parameter("VC1") { number = 228 }
            }
            
            section("Volume") {
                parameter("AFTER") { number = 212 }
                parameter("WHEEL") { number = 213 }
                parameter("BREATH") { number = 214 }
                parameter("FOOT") { number = 215 }
                parameter("TEMP") { number = 229 }
                parameter("VC0") { number = 230 }
                parameter("VC1") { number = 231 }
            }
        }

        section("ProgramSettings") {
            section("Global") {
                parameter("TRANSPOSE") { number = 241 }
                parameter("BEND_UP") { number = 242 }
                parameter("BEND_DN") { number = 243 }
                parameter("LEGATO") { number = 244 }
                parameter("PORTA_MODE") { number = 245 }
                parameter("PORTA_TIME") { number = 246 }
                parameter("PAN") { number = 247 }
                parameter("VOLUME") { number = 248 }
                parameter("VELO_OFFSET") { number = 249 }
                parameter("TUNING") { number = 251 }
                parameter("TEMP_OFFSET") { number = 239 }
            }
            
            section("Arpeggiator") {
                parameter("MODE") { number = 450 }
                parameter("TEMPO") { number = 451 }
                parameter("MUL") { number = 453 }
                parameter("OCTAVES") { number = 454 }
            }
            
            section("Output") {
                parameter("VOLUME") { number = 509 }
                parameter("GAIN_PRE_FX") { number = 510 }
                parameter("GAIN_POST_FX") { number = 511 }
            }
        }

        section("Effects") {
            section("Bandwidth") {
                parameter("BW") { number = 340 }
            }

            section("Distortion") {
                parameter("ON OFF") { number = 350 }
                parameter("GAIN PRE") { number = 351 }
                parameter("GAIN POST") { number = 352 }
                parameter("FILTER POST") { number = 353 }
            }

            section("Bitcrusher") {
                parameter("DEPTH") { number = 380 }
            }

            section("Decimator") {
                parameter("DEPTH") { number = 370 }
            }

            section("Filter") {
                parameter("LO") { number = 320 }
                parameter("HI") { number = 321 }
            }

            section("Chorus/Flanger") {
                parameter("DRY") { number = 360 }
                parameter("WET") { number = 361 }
                parameter("MODE") { number = 362 }
                parameter("SPEED") { number = 363 }
                parameter("DEPTH") { number = 364 }
                parameter("FEEDBACK") { number = 365 }
                parameter("LR PHASE") { number = 366 }
            }

            section("Phaser") {
                parameter("DRY") { number = 310 }
                parameter("WET") { number = 311 }
                parameter("MODE") { number = 312 }
                parameter("DEPTH") { number = 313 }
                parameter("SPEED") { number = 314 }
                parameter("FEEDBACK") { number = 315 }
                parameter("OFFSET") { number = 316 }
                parameter("STAGES") { number = 317 }
                parameter("LR PHASE") { number = 318 }
            }

            section("Amplitude Modulator") {
                parameter("DEPTH") { number = 330 }
                parameter("SPEED") { number = 331 }
                parameter("RANGE") { number = 332 }
                parameter("LR PHASE") { number = 333 }
            }

            section("Delay") {
                parameter("DRY") { number = 300 }
                parameter("WET") { number = 301 }
                parameter("MODE") { number = 302 }
                parameter("TIME") { number = 303 }
                parameter("FEEDBACK") { number = 304 }
                parameter("LO") { number = 305 }
                parameter("HI") { number = 306 }
                parameter("TEMPO") { number = 307 }
                parameter("MUL") { number = 308 }
                parameter("DIV") { number = 309 }
                parameter("MOD SPEED") { number = 298 }
                parameter("MOD DEPTH") { number = 299 }
                parameter("SMEAR") { number = 291 }
                parameter("2X") { number = 293 }
            }

            section("Early Reflections") {
                parameter("DRY") { number = 294 }
                parameter("WET") { number = 295 }
                parameter("MODE") { number = 296 }
                parameter("FEEDBACK") { number = 297 }
            }

            section("Reverb") {
                parameter("DRY") { number = 390 }
                parameter("WET") { number = 391 }
                parameter("MODE") { number = 392 }
                parameter("DECAY") { number = 393 }
                parameter("DAMP") { number = 394 }
                parameter("MOD SPEED") { number = 395 }
                parameter("MOD DEPTH") { number = 396 }
            }

            section("Effects Routing") {
                parameter("ROUTING") { number = 508 }
            }
        }
    }

    private fun processYamlProgram(yamlProgram: Map<*, *>, program: Program) {
        println("yamlProgram: $yamlProgram")
        yamlProgram["Name"]?.toString()?.let { program.name = it }

        val sectionNames = program.sectionNames
        yamlProgram.forEach { entry ->
            (entry.key as? String)?.let { key ->
                if (sectionNames.contains(key)) {
                    (entry.value as? Map<*, *>)?.let { section ->
                        processMap(
                            section,
                            KeyPath(key), program
                        )
                    }
                }
            }
        }
    }

    private fun processMap(map: Map<*, *>, keyPath: KeyPath, program: Program) {
        println("Processing KeyPath: ${keyPath.value}")

        val parametersByKeyPath = program.parametersByKeyPath

        map.forEach { entry ->
            (entry.key as? String)?.let { key ->
                val newKeyPath = keyPath.byAppending(key)
                val parameter = parametersByKeyPath[newKeyPath.value]
                if (parameter != null) {
                    processParameter(
                        entry.value,
                        parameter,
                        newKeyPath,
                        program
                    )
                } else {
                    (entry.value as? Map<*, *>)?.let { section ->
                        processMap(
                            section,
                            newKeyPath,
                            program
                        )
                    }
                }
            }
        }
    }

    private fun processParameter(value: Any?, parameter: Parameter, keyPath: KeyPath, program: Program) {
        if (value != null) {
            println("Processing Parameter: ${keyPath.value}")
            when {
                (parameter.type == Parameter.Type.INTEGER) -> {
                    (value as? Int)?.let {
                        program.setParameter(parameter.number to it)
                    }
                }
                (parameter.type == Parameter.Type.BITWISE)  -> {
                    val stringValue = value.toString()
                    program.setParameterBits(parameter.number to stringValue)
                }
            }
        }
    }
}