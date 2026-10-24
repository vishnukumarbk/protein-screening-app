package com.nuclera.protein.screening.app;

import java.util.List;

public final class ExperimentFixtures {

    // Based on examples given
    public static final List<String> VALID_SEQUENCES = List.of(
            "MKTAYIAVRNDQGSKLEA",
            "MKTAYIAFPTLWGSKLEA",
            "MKTAYIACIMHYGSKLEA",
            "MKTAYIADNRQEPGSKLEA",
            "MKTAYIATWFGVLGSKLEA",
            "MKTAYIAYHCKIMGSKLEA",
            "MKTAYIASAPLTNGSKLEA",
            "MKTAYIAQVGDRFEGSKLEA",
            "MKTAYIAWMYNPITGSKLEA",
            "MKTAYIALKHCSAVGSKLEA",
            "MKTAYIAEDRTQGNGSKLEA",
            "MKTAYIAGHSTAVCGSKLEA",
            "MKTAYIAFVIYPWMLGSKLEA",
            "MKTAYIANQTSRHKDGSKLEA",
            "MKTAYIAAGCLEVPTGSKLEA",
            "MKTAYIAMWYFQNRIGSKLEA",
            "MKTAYIADEKRHSTNQGSKLEA",
            "MKTAYIAVLIMFWYPGGSKLEA",
            "MKTAYIACASTNQGHRGSKLEA",
            "MKTAYIAPWIFYEDKVGSKLEA");

    public static final List<String> INVALID_SEQUENCES = List.of(
            "MAEGQLFSHRTPVNDK",
            "MAEGWYCITRTPVNDK",
            "MAEGDNRGVRTPVNDK",
            "MAEGKMPAERTPVNDK",
            "MAEGGTSLWYRTPVNDK",
            "MAEGHCFVIMRTPVNDK",
            "MAEGNEQDKRRTPVNDK",
            "MAEGPAVTGSRTPVNDK",
            "MAEGYWMFILVRTPVNDK",
            "MAEGSQNTHGARTPVNDK",
            "MAEGEDKRPCVRTPVNDK",
            "MAEGIMYFWNTRTPVNDK",
            "MAEGAVLGSTPCRTPVNDK",
            "MAEGRKHDENQYRTPVNDK",
            "MAEGFWMIVLTSRTPVNDK",
            "MAEGNQGPAHCERTPVNDK",
            "MAEGKRDEHNQSTRTPVNDK",
            "MAEGMFWYVILPARTPVNDK",
            "MAEGCGTSNAQHRRTPVNDK",
            "MAEGVIDEKWGSYPFRTPVNDK");

    private ExperimentFixtures() {
    }
}
