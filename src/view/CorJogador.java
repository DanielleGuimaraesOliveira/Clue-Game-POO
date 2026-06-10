package view;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

class CorJogador{

    private static final Map<String, Color> cores =
        new HashMap<>();

    static {

        cores.put(
            "Srta. Scarlet",
            Color.RED
        );

        cores.put(
            "Coronel Mustard",
            Color.YELLOW
        );

        cores.put(
            "Sra. White",
            Color.WHITE
        );

        cores.put(
            "Rev. Green",
            Color.GREEN
        );

        cores.put(
            "Sra. Peacock",
            Color.BLUE
        );

        cores.put(
            "Prof. Plum",
            new Color(128, 0, 128)
        );
    }

    public static Color getCor(String nome) {

        return cores.getOrDefault(
            nome,
            Color.BLACK
        );
    }
}