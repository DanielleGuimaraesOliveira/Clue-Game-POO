package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import Interfaces.ICarta;
import controller.ControladorPartida;

public class JanelaPalpite extends JDialog {

	private boolean palpiteResolvido;

	public JanelaPalpite(JFrame janelaPai, ControladorPartida controlador, String comodoAtual) {
		super(janelaPai, "Fazer Palpite", true);

		setSize(520, 240);
		setLocationRelativeTo(janelaPai);
		
		setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		String[] suspeitos = {
			"Rev. Green",
			"Srta. Scarlet",
			"Coronel Mustard",
			"Prof. Plum",
			"Sra. Peacock",
			"Sra. White"
		};

		String[] armas = {
			"Corda",
			"Cano de Chumbo",
			"Faca",
			"Chave Inglesa",
			"Castiçal",
			"Revólver"
		};

		/*
		String[] comodos = {
			"Cozinha",
			"Salão de Baile",
			"Sala de Jantar",
			"Escritório",
			"Biblioteca",
			"Sala de Estar",
			"Jardim de Inverno",
			"Hall",
			"Sala de Música"
		};*/
		
		String[] comodos = { comodoAtual };

		JComboBox<String> comboSuspeito = new JComboBox<>(suspeitos);
		JComboBox<String> comboArma = new JComboBox<>(armas);
		
		JComboBox<String> comboComodo = new JComboBox<>(comodos);
		comboComodo.setEnabled(false); // trava a caixa de seleção

		// painel do topo
		JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.CENTER));
		painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
		JLabel lblResultado = new JLabel("Escolha uma combinação e confirme o palpite.");
		painelTopo.add(lblResultado);
		
		// painel central 
		JPanel painelForm = new JPanel(new GridLayout(3, 2, 10, 15));
        painelForm.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40)); // Margens internas
        
        // Adicionando os itens na ordem (Esquerda -> Direita)
        painelForm.add(new JLabel("Suspeito:", SwingConstants.RIGHT));
        painelForm.add(comboSuspeito);
        
        painelForm.add(new JLabel("Arma:", SwingConstants.RIGHT));
        painelForm.add(comboArma);
        
        painelForm.add(new JLabel("Cômodo:", SwingConstants.RIGHT));
        painelForm.add(comboComodo);
        
        // painel de baixo
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnResolver = new JButton("Verificar Palpite");
		
		JButton btnSair = new JButton("Sair e Passar Turno");
		btnSair.setEnabled(false);
		
		painelBotoes.add(btnResolver);
        painelBotoes.add(btnSair);

        // lógica botões
		btnResolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ICarta resultado = controlador.realizarPalpite(
					(String) comboSuspeito.getSelectedItem(),
					(String) comboArma.getSelectedItem(),
					(String) comboComodo.getSelectedItem()
				);
				
				String nomeDono = "Ninguem";
				
				/*
				if (resultado == null) {
					lblResultado.setText("Nenhum jogador mostrou carta.");
				} else {
					lblResultado.setText("Carta mostrada: " + resultado);
				}
				*/
				
				if (resultado != null) {
					for (Interfaces.IJogador jogador : controlador.getJogadores()) {
						// Verifica se a carta revelada está na mão deste jogador
                        if (jogador.getMao().contains(resultado)) {
                            nomeDono = jogador.getPersonagem().getNome();
                            break;
                        }
					}
				}
				
				JanelaResultadoPalpite popup = new JanelaResultadoPalpite(
						(JFrame) janelaPai, // passa a janela pai,
						controlador,
						resultado,
						nomeDono
				);
				popup.setVisible(true);
				
				comboSuspeito.setEnabled(false);
				comboArma.setEnabled(false);
				//comboComodo.setEnabled(false);
				
		        btnResolver.setEnabled(false);


		        btnSair.setEnabled(true);
			}
		});

		btnSair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				controlador.proximoTurno();
				palpiteResolvido = true;
				dispose();
			}
		});

		// adicionando tudo a janela principal
        add(painelTopo, BorderLayout.NORTH);
        add(painelForm, BorderLayout.CENTER);
        add(painelBotoes, BorderLayout.SOUTH);
	}

	public boolean isPalpiteResolvido() {
		return palpiteResolvido;
	}
}
