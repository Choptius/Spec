package com.choptius.spec.domain.model

abstract class AstronomicalObject(
    open val declination: Double,
    open val rightAscension: Double,
    open val constellation: Constellation,
    open val magnitude: Double = UNDEFINED_MAGNITUDE,
    open val commonName: CharSequence,
    open var isInFavorites: Boolean,
)   {

    companion object {
        const val UNDEFINED_MAGNITUDE = -1488.0
    }

    enum class Constellation(val fullName: String) {
        And("Andromeda"), Ant("Antlia"), Aps("Apus"), Aqr("Aquarius"), Aql("Aquila"), Ara("Ara"), Ari(
            "Aries"
        ),
        Aur("Auriga"), Boo("Boötes"), Cae("Caelum"), Cam("Camelopardalis"), Cnc("Cancer"), CVn("Canes Venatici"), CMa(
            "Canis Major"
        ),
        CMi("Canis Minor"), Cap("Capricornus"), Car("Carina"), Cas("Cassiopeia"), Cen("Centaurus"), Cep(
            "Cepheus"
        ),
        Cet("Cetus"), Cha("Chamaeleon"), Cir("Circinus"), Col("Columba"), Com("Coma Berenices"), CrA(
            "Corona Austrina"
        ),
        CrB("Corona Borealis"), Crv("Corvus"), Crt("Crater"), Cru("Crux"), Cyg("Cygnus"), Del("Delphinus"), Dor(
            "Dorado"
        ),
        Dra("Draco"), Equ("Equuleus"), Eri("Eridanus"), For("Fornax"), Gem("Gemini"), Gru("Grus"), Her(
            "Hercules"
        ),
        Hor("Horologium"), Hya("Hydra"), Hyi("Hydrus"), Ind("Indus"), Lac("Lacerta"), Leo("Leo"), LMi(
            "Leo Minor"
        ),
        Lep("Lepus"), Lib("Libra"), Lup("Lupus"), Lyn("Lynx"), Lyr("Lyra"), Men("Mensa"), Mic("Microscopium"), Mon(
            "Monoceros"
        ),
        Mus("Musca"), Nor("Norma"), Oct("Octans"), Oph("Ophiuchus"), Ori("Orion"), Pav("Pavo"), Peg(
            "Pegasus"
        ),
        Per("Perseus"), Phe("Phoenix"), Pic("Pictor"), Psc("Pisces"), PsA("Piscis Austrinus"), Pup("Puppis"), Pyx(
            "Pyxis"
        ),
        Ret("Reticulum"), Sge("Sagitta"), Sgr("Sagittarius"), Sco("Scorpius"), Scl("Sculptor"), Sct(
            "Scutum"
        ),
        Ser("Serpens"), Sex("Sextans"), Tau("Taurus"), Tel("Telescopium"), Tri("Triangulum"), TrA("Triangulum Australe"), Tuc(
            "Tucana"
        ),
        UMa("Ursa Major"), UMi("Ursa Minor"), Vel("Vela"), Vir("Virgo"), Vol("Volans"), Vul("Vulpecula"),
        NC("");

        companion object {

            fun fromString(str: CharSequence): Constellation {
                if (str.toString().isNotEmpty()) {
                    for (constellation in values()) {
                        if (constellation.name == str) return constellation
                    }
                }
                return NC
            }
        }
    }

}