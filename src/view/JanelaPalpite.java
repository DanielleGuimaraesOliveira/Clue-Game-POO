package view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;

import controller.ControladorPartida;

public class JanelaPalpite extends JDialog {

	private boolean palpiteResolvido;

	public JanelaPalpite(JFrame janelaPai, ControladorPartida controlador) {
		super(janelaPai, "Fazer Palpite", true);

		setSize(520, 240);
		setLocationRelativeTo(janelaPai);
		setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		String[] suspeitos = {
			"Sr. Verde",
			"Srta. Scarlet",
			"Coronel Mustard",
			"Professor Plum",
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
		};

		JComboBox<String> comboSuspeito = new JComboBox<>(suspeitos);
		JComboBox<String> comboArma = new JComboBox<>(armas);
		JComboBox<String> comboComodo = new JComboBox<>(comodos);

		JLabel lblResultado = new JLabel("Escolha uma combinação e confirme o palpite.");
		JButton btnResolver = new JButton("Verificar Palpite");
		JButton btnSair = new JButton("Sair e Passar Turno");

		btnResolver.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String resultado = controlador.realizarPalpite(
					(String) comboSuspeito.getSelectedItem(),
					(String) comboArma.getSelectedItem(),
					(String) comboComodo.getSelectedItem()
				);

				if (resultado == null) {
					lblResultado.setText("Nenhum jogador mostrou carta.");
				} else {
					lblResultado.setText("Carta mostrada: " + resultado);
				}
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

		add(new JLabel("Suspeito:"));
		add(comboSuspeito);
		add(new JLabel("Arma:"));
		add(comboArma);
		add(new JLabel("Cômodo:"));
		add(comboComodo);
		add(lblResultado);
		add(btnResolver);
		add(btnSair);
	}

	public boolean isPalpiteResolvido() {
		return palpiteResolvido;
	}
}
