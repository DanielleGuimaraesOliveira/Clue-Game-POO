package Interfaces;

import java.util.List;

import model.PecaSuspeito;

public interface IJogador {
    String getNome();
    PecaSuspeito getPersonagem();
    List<ICarta> getMao();
    boolean isEliminado();
    boolean isPossuiBlocoDeNotas();
    ICasa getPosicaoAtual();
}
