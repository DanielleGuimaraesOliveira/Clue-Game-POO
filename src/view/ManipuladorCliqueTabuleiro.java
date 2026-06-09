package view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import Interfaces.IJogador;
import controller.ControladorPartida;

 class ManipuladorCliqueTabuleiro extends MouseAdapter {

    private ControladorPartida controlador;
    private JanelaJogo janelaPai;
    private PainelTabuleiro painel;

    public ManipuladorCliqueTabuleiro(
        ControladorPartida controlador,
        JanelaJogo janelaPai,
        PainelTabuleiro painel
    ) {

        this.controlador = controlador;
        this.janelaPai = janelaPai;
        this.painel = painel;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

        int larguraCasa =
            painel.getWidth() / 24;

        int alturaCasa =
            painel.getHeight() / 25;

        int xLogico =
            e.getX() / larguraCasa;

        int yLogico =
            e.getY() / alturaCasa;

        int valorDados =
            controlador.getValorDados();

        if (valorDados == 0) {

            System.out.println(
                "Role os dados primeiro!"
            );

            return;
        }

        IJogador jogadorAntes =
            controlador.getJogadorAtual();

        boolean entrouComodo =
            controlador.processaClickTela(
                xLogico,
                yLogico,
                valorDados
            );

        painel.repaint();

        if (entrouComodo) {

            JanelaPalpite popup =
                new JanelaPalpite(
                    janelaPai,
                    controlador
                );

            popup.setVisible(true);

            janelaPai.resetaDadosEPassaTurno();

            painel.repaint();

        } else {

            IJogador jogadorDepois =
                controlador.getJogadorAtual();

            if (jogadorAntes != jogadorDepois) {

                janelaPai.resetaDadosEPassaTurno();

            } else {

                System.out.println(
                    "Casa inválida!"
                );
            }
        }
    }
}