package view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

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

                JFileChooser chooser = new JFileChooser();

                chooser.setDialogTitle("Carregar partida");

                int resultado =
                    chooser.showOpenDialog(inicial);

                if (resultado == JFileChooser.APPROVE_OPTION) {

                    File arquivo =
                        chooser.getSelectedFile();

                    try {

                        controlador.carregarPartida(
                            arquivo.getAbsolutePath()
                        );

                        JanelaJogo jogo =
                            new JanelaJogo(controlador);

                        jogo.setVisible(true);

                        inicial.dispose();

                    } catch (Exception ex) {

                        ex.printStackTrace();

                        JOptionPane.showMessageDialog(
                            null,
                            "Erro ao carregar partida!"
                        );
                    }
                }
            }
        });
        inicial.setVisible(true);
    }
}