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

        int OFFSET_X = 14;
        int OFFSET_Y = 12;
        int TAMANHO_CASA = 24;
        
        int xLogico = (e.getX() - OFFSET_X) / TAMANHO_CASA;
        int yLogico = (e.getY() - OFFSET_Y) / TAMANHO_CASA;

        System.out.println("CLICK -> pixel(" + e.getX() + "," + e.getY() + ") lógico(" + xLogico + "," + yLogico + ")");

        int valorDados = controlador.getValorDados();

        if (valorDados == 0) {
            System.out.println( "Role os dados primeiro!");

            return;
        }

        int resultado = controlador.processaClickTela(xLogico, yLogico, valorDados);

        painel.repaint();

        if (resultado == 1) {
            JanelaPalpite popup = new JanelaPalpite(janelaPai, controlador, controlador.getComodoJogadorAtual());
            popup.setVisible(true);

            janelaPai.resetaDadosEPassaTurno();
            painel.repaint();
        }

        else if (resultado == 2) {
            janelaPai.resetaDadosEPassaTurno();
        }

        else {
            System.out.println("Casa inválida.");
        }
    }
 }