package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.Observador;
import controller.ControladorPartida;

public class JanelaJogo extends JFrame implements Observador {
    private final ControladorPartida controlador;
    private PainelTabuleiro painelTabuleiro;

    private JLabel lblTurno;
    private JLabel lblImagemDado1;
    private JLabel lblImagemDado2;
    private JButton btnRolarDados;
    private JLabel lblTextoPassos;

    private JComboBox<Integer> comboDado1;
    private JComboBox<Integer> comboDado2;
    private JCheckBox checkModoTeste;

    public JanelaJogo(ControladorPartida controlador) {
        this.controlador = controlador;

        setTitle("Clue Tabuleiro | Dani e Judy");
        setSize(860, 665);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        controlador.registrarObservador(this);

        Image imagemMapa = null;
        try {
            imagemMapa = ImageIO.read(getClass().getResource("/assets/img/Tabuleiros/Tabuleiro-Clue-C.jpg"));
        } catch (Exception e) {
            System.out.println("Erro ao carregar imagem do tabuleiro");
        }

        painelTabuleiro = new PainelTabuleiro(imagemMapa, controlador, this);
        controlador.registrarObservador(painelTabuleiro);

        add(painelTabuleiro, BorderLayout.CENTER);
        add(criaMenuLateral(), BorderLayout.EAST);

        setLocationRelativeTo(null);
        atualizaInterfaceNovoTurno();
        painelTabuleiro.repaint();
    }

    private JPanel criaMenuLateral() {
        JPanel painel = new JPanel();
        painel.setPreferredSize(new Dimension(250, 0));
        painel.setBackground(new Color(240, 240, 240));
        painel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        painel.setLayout(new javax.swing.BoxLayout(painel, javax.swing.BoxLayout.Y_AXIS ));
        
        painel.add(javax.swing.Box.createVerticalGlue());
        Dimension tamanhoBotao = new Dimension(180, 40);

        lblTurno = new JLabel("Turno de: " + (controlador.getJogadorAtual() != null ? controlador.getJogadorAtual().getPersonagem().getNome() : "---"), SwingConstants.CENTER);
        lblTurno.setFont(new Font("Arial", Font.BOLD, 16));
        lblTurno.setAlignmentX(CENTER_ALIGNMENT);
        
        painel.add(lblTurno);

        Integer[] facesDados = {1, 2, 3, 4, 5, 6};
        comboDado1 = new JComboBox<>(facesDados);
        comboDado2 = new JComboBox<>(facesDados);
        checkModoTeste = new JCheckBox("Modo Teste");
        checkModoTeste.setOpaque(false);

        JPanel painelTesteDados = new JPanel();
        painelTesteDados.add(new JLabel("D1: "));
        painelTesteDados.add(comboDado1);
        painelTesteDados.add(new JLabel("D2: "));
        painelTesteDados.add(comboDado2);
        painelTesteDados.add(checkModoTeste);
        painel.add(painelTesteDados);
        
 
        lblImagemDado1 = new JLabel(carregarImagemDado(1), SwingConstants.CENTER);
        lblImagemDado2 = new JLabel(carregarImagemDado(1), SwingConstants.CENTER);

        JPanel painelDadosLadoALado = new JPanel(new java.awt.FlowLayout( java.awt.FlowLayout.CENTER, 0, 0 ));
        painelDadosLadoALado.setBackground(new Color(240, 240, 240));
        painelDadosLadoALado.add(lblImagemDado1);
        painelDadosLadoALado.add(lblImagemDado2);


        btnRolarDados = new JButton("🎲 Rolar Dados");
        lblTextoPassos = new JLabel("Aguardando rolagem...", SwingConstants.CENTER);
        lblTextoPassos.setFont(new Font("Arial", Font.ITALIC, 13));
        
        JPanel painelAreaDados = new JPanel();
        painelAreaDados.setOpaque(false);
        painelAreaDados.setLayout(new javax.swing.BoxLayout(painelAreaDados, javax.swing.BoxLayout.Y_AXIS ));

        painelDadosLadoALado.setAlignmentX(CENTER_ALIGNMENT);
        
        btnRolarDados.setAlignmentX(CENTER_ALIGNMENT);
        lblTextoPassos.setAlignmentX(CENTER_ALIGNMENT);

        painelAreaDados.add(painelDadosLadoALado);
        painelAreaDados.add(btnRolarDados);
        painelAreaDados.add(javax.swing.Box.createVerticalStrut(4));
        painelAreaDados.add(lblTextoPassos);
        painelAreaDados.setAlignmentX(CENTER_ALIGNMENT);
        
        btnRolarDados.setPreferredSize(tamanhoBotao);
        btnRolarDados.setMaximumSize(tamanhoBotao);
        btnRolarDados.setAlignmentX(CENTER_ALIGNMENT);
        
        painel.add(painelAreaDados);
        JButton btnSalvar = new JButton("Salvar Partida");
        
        btnRolarDados.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int d1;
                int d2;

                if (checkModoTeste.isSelected()) {
                    d1 = (Integer) comboDado1.getSelectedItem();
                    d2 = (Integer) comboDado2.getSelectedItem();
                    controlador.definirResultadoDados(d1, d2);
                } else {
                    int[] resultadoDados = controlador.lancarDados();
                    d1 = resultadoDados[0];
                    d2 = resultadoDados[1];
                }
                
                atualizarDadosNaTela(d1, d2);
                atualizaInterfaceNovoTurno();
                btnSalvar.setEnabled(false);
            }
        });

        JButton btnAcusacao = new JButton("Fazer Acusação Final");
        JButton btnBloco = new JButton("Bloco de Anotações");
        JButton btnPassagem = new JButton("Usar Passagem Secreta");
        JButton btnMostrarCartas = new JButton("Mostrar Cartas");
        
        painel.add(javax.swing.Box.createVerticalStrut(40));
 
        btnSalvar.setPreferredSize(tamanhoBotao);
        btnSalvar.setMaximumSize(tamanhoBotao);
        btnSalvar.setAlignmentX(CENTER_ALIGNMENT);

        btnBloco.setPreferredSize(tamanhoBotao);
        btnBloco.setMaximumSize(tamanhoBotao);
        btnBloco.setAlignmentX(CENTER_ALIGNMENT);
        
        btnPassagem.setPreferredSize(tamanhoBotao);
        btnPassagem.setMaximumSize(tamanhoBotao);
        btnPassagem.setAlignmentX(CENTER_ALIGNMENT);
        
        btnAcusacao.setPreferredSize(tamanhoBotao);
        btnAcusacao.setMaximumSize(tamanhoBotao);
        btnAcusacao.setAlignmentX(CENTER_ALIGNMENT);
        
        btnMostrarCartas.setPreferredSize(tamanhoBotao);
        btnMostrarCartas.setMaximumSize(tamanhoBotao);
        btnMostrarCartas.setAlignmentX(CENTER_ALIGNMENT);
        
        btnPassagem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (controlador.getValorDados() > 0) {
                    return;
                }

                if (controlador.usaPassagemSecreta()) {
                    painelTabuleiro.repaint();
               
                    JanelaPalpite popUp = new JanelaPalpite(JanelaJogo.this, controlador, controlador.getComodoJogadorAtual());
                    popUp.setVisible(true);
                    resetaDadosEPassaTurno();
                }
            }
        });

        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
					controlador.salvarPartida("src/salvamentos/jogo_anterior");
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
            }
        });

        btnBloco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JanelaBlocoNotas bloco = new JanelaBlocoNotas(JanelaJogo.this, controlador);
                bloco.setVisible(true);
            }
        });
        
        btnMostrarCartas.addActionListener(
        	    new ActionListener() {
        	        @Override
        	        public void actionPerformed(ActionEvent e) {
        	            JanelaCartas janelaCarta = new JanelaCartas( JanelaJogo.this, controlador);
        	            janelaCarta.setVisible(true);
        	        }
        	    }
        	);
        
        btnAcusacao.addActionListener(
        	    new ActionListener() {
        	        @Override
        	        public void actionPerformed(ActionEvent e) {
        	            // Abre a janela passando a JanelaJogo como pai e o controlador
        	            JanelaAcusacao janelaAcusacao = new JanelaAcusacao(JanelaJogo.this, controlador);
        	            janelaAcusacao.setVisible(true);
        	        }
        	    }
        	);

        painel.add(btnAcusacao);
        painel.add(btnSalvar);
        painel.add(btnBloco);
        painel.add(btnPassagem);
        painel.add(btnMostrarCartas);

        painel.add(javax.swing.Box.createVerticalGlue());
        
        return painel;
    }

    private void atualizarDadosNaTela(int d1, int d2) {
        lblImagemDado1.setIcon(carregarImagemDado(d1));
        lblImagemDado2.setIcon(carregarImagemDado(d2));
        lblTextoPassos.setText("Você tirou " + controlador.getValorDados() + " passos!");
    }

    private ImageIcon carregarImagemDado(int valorFace) {
        try {
            Image img = ImageIO.read(getClass().getResource("/assets/img/Dados/dado" + valorFace + ".jpg"));
            Image imgRedimensionada = img.getScaledInstance(75, 75, Image.SCALE_SMOOTH);
            return new ImageIcon(imgRedimensionada);
        } catch (Exception e) {
            System.out.println("Erro ao carregar a imagem: /assets/img/Dados/dado" + valorFace + ".jpg");
            return null;
        }
    }

    public int getValorSimuladoDados() {
        return controlador.getValorDados();
    }

    public void resetaDadosEPassaTurno() {
        controlador.zerarDadosRolados();
        atualizaInterfaceNovoTurno();
    }

    public void atualizaInterfaceNovoTurno() {
        if (controlador.getJogadorAtual() != null) {
            lblTurno.setText("Turno de: " + controlador.getJogadorAtual().getPersonagem().getNome());
        }

        if (controlador.getValorDados() == 0) {
            btnRolarDados.setEnabled(true);
            lblTextoPassos.setText("Aguardando rolagem...");
        } else {
            btnRolarDados.setEnabled(false);
        }
    }

    @Override
    public void atualizar() {
        atualizaInterfaceNovoTurno();
        if (painelTabuleiro != null) {
            painelTabuleiro.repaint();
        }
    }
}