package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.ControladorPartida;


public class GerenciadorInterface {

    public static void iniciar(ControladorPartida controlador) {

        JanelaInicial inicial = new JanelaInicial();

        inicial.setAcaoNovoJogo(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JanelaPersonagens personagens = new JanelaPersonagens(controlador);
                personagens.setVisible(true);

                inicial.dispose();
            }
        });

        inicial.setAcaoContinuar(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                try {

                    controlador.carregarPartida("src/salvamentos/jogo_anterior");

                    JanelaJogo jogo =
                        new JanelaJogo(controlador);

                    jogo.setVisible(true);

                    inicial.dispose();

                } catch (Exception ex) {

                    ex.printStackTrace();

                    javax.swing.JOptionPane.showMessageDialog(
                        null,
                        "Erro ao carregar partida!"
                    );
                }
            }
        });

        inicial.setVisible(true);
    }
}