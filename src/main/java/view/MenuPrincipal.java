package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;
import controller.ClientesController;
import controller.FuncionarioController;
import controller.QuiosqueController;
import controller.ReservasController;
import model.entities.ClientesEntity;
import model.entities.FuncionariosEntity;
import model.entities.QuiosqueEntity;
import model.entities.ReservasEntity;
import model.repositories.ResevasRepository;
import model.service.ReservaService;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import java.awt.Font;
import javax.swing.JTable;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import java.sql.Date;

public class MenuPrincipal extends JFrame {
	EntityManagerFactory emf = Persistence.createEntityManagerFactory("bancoQuiosque");
	EntityManager em = emf.createEntityManager();

	private ReservasController reservaController;
	private FuncionarioController funcionarioController;
	ClientesController clientesController = new ClientesController(em);
	QuiosqueController quiosqueController = new QuiosqueController(em);

	private JComboBox<String> comboBoxQuiosque;
	private JComboBox<String> comboBoxCliente;
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private CardLayout cardLayout;
	private JTextField txtNome;
	private JTextField txtEmail;
	private JTextField txtSenha;
	private JTextField txtTelefone;
	private JTextField num;
	private JTextField local;
	private JTextField txtDiaria;
	private JTextField txtTotal;
	private Connection conn;
	private JDateChooser dateChooserInicio;
	private JDateChooser dateChooserFim;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		// look-and-feel FlatLaf
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
		} catch (UnsupportedLookAndFeelException ex) {
			ex.printStackTrace();
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new MenuPrincipal().setVisible(true);
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public MenuPrincipal() {
		funcionarioController = new FuncionarioController();
		quiosqueController = new QuiosqueController(em);
		clientesController = new ClientesController(em);

		dateChooserInicio = new JDateChooser();
		dateChooserFim = new JDateChooser();
		comboBoxQuiosque = new JComboBox<>();
		comboBoxCliente = new JComboBox<>();
		txtDiaria = new JTextField();
		txtTotal = new JTextField();

		ResevasRepository reservaRepository = new ResevasRepository(em);
		ReservaService reservaService = new ReservaService(reservaRepository);
		reservaController = new ReservasController(reservaService);

		setBackground(new Color(255, 255, 255));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 995, 586);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(61, 106, 131));
		setJMenuBar(menuBar);

		JButton btnHome = new JButton("Inicio");
		btnHome.setIcon(new ImageIcon("C:\\Users\\Cristiely\\Downloads\\lar.png"));
		menuBar.add(btnHome);
		btnHome.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPainel("homePanel");
			}
		});

		JMenu mnFuncionario = new JMenu("Gerenciar Funcionarios");
		menuBar.add(mnFuncionario);

		JMenuItem miCriarUsuario = new JMenuItem("Criar Usuário");
		mnFuncionario.add(miCriarUsuario);
		miCriarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPainel("criarUsuarioPanel");
			}
		});

		JMenuItem miListarUsuario = new JMenuItem("Gerenciar");
		mnFuncionario.add(miListarUsuario);
		miListarUsuario.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPainel("listarUsuarioPanel");
			}
		});

		JMenu mnQuiosque = new JMenu("Gerenciar Quiosques");
		menuBar.add(mnQuiosque);

		JMenuItem miCriarQuiosque = new JMenuItem("Criar");
		mnQuiosque.add(miCriarQuiosque);
		miCriarQuiosque.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPainel("criarQuiosquePanel");
			}
		});

		JMenuItem miEditarQuiosque = new JMenuItem("Gerenciar");
		mnQuiosque.add(miEditarQuiosque);
		miEditarQuiosque.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPainel("gerenciarQuiosquePanel");
			}
		});

		JMenu mnClientes = new JMenu("Gerenciar Clientes");
		menuBar.add(mnClientes);

		JMenuItem miCriarCliente = new JMenuItem("Criar");
		mnClientes.add(miCriarCliente);
		miCriarCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPainel("criarClientePanel");
			}
		});

		JMenuItem miEditarCliente = new JMenuItem("Gerenciar");
		mnClientes.add(miEditarCliente);
		miEditarCliente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPainel("gerenciarClientePanel");
			}
		});

		JMenu mnReservas = new JMenu("Gerenciar Reservas");
		menuBar.add(mnReservas);

		JMenuItem miFazerReserva = new JMenuItem("Fazer reserva");
		mnReservas.add(miFazerReserva);
		miFazerReserva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mostrarPainel("fazerReservaPanel");
			}
		});

		JMenuItem miListarReserva = new JMenuItem("Listar");
		mnReservas.add(miListarReserva);
		miListarReserva.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listarUsuarioPanel();
				cardLayout.show(contentPane, "listarReservaPanel");
			}
		});

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		cardLayout = new CardLayout();
		contentPane.setLayout(cardLayout);
		setContentPane(contentPane);

		homePanel();
		criarUsuarioPanel();
		listarUsuarioPanel();
		criarQuiosquePanel();
		gerenciarQuiosquePanel();
		criarClientePanel();
		gerenciarClientePanel();
		fazerReservaPanel();
		listarReservaPanel();

		cardLayout.show(contentPane, "homePanel");
	}

	private void mostrarPainel(String panelName) {
		cardLayout.show(contentPane, panelName);
	}

	private void homePanel() {
		JPanel homePanel = new JPanel();
		homePanel.setBackground(new Color(207, 224, 233));
		homePanel.setLayout(null);

		getContentPane().add(homePanel, "homePanel");

		JButton btnNewButton = new JButton("Sair");
		btnNewButton.setBackground(new Color(43, 85, 85));
		btnNewButton.addActionListener(e -> {
			Login loginForm = new Login();
			loginForm.setVisible(true);
		});
		btnNewButton.setIcon(new ImageIcon("C:\\Users\\Cristiely\\Downloads\\sair.png"));
		btnNewButton.setBounds(846, 455, 98, 36);
		homePanel.add(btnNewButton);

		CategoryDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBackground(new Color(61, 106, 131));
		chartPanel.setPreferredSize(new Dimension(600, 400));
		chartPanel.setBounds(141, 38, 600, 400);
		homePanel.add(chartPanel);

		contentPane.add(homePanel, "homePanel");
	}

	private CategoryDataset createDataset() {
		List<ReservasEntity> reservas = fetchReservasFromDatabase();
		return createDatasetFromReservas(reservas);
	}

	private List<ReservasEntity> fetchReservasFromDatabase() {
		return em.createQuery("SELECT r FROM ReservasEntity r", ReservasEntity.class).getResultList();
	}

	private CategoryDataset createDatasetFromReservas(List<ReservasEntity> reservas) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int[] monthlyReservations = new int[12];

		for (ReservasEntity reserva : reservas) {
			int month = reserva.getDataInicio().getMonthValue() - 1;
			if (month >= 0 && month < 12) {
				monthlyReservations[month]++;
			}
		}

		String[] months = { "Jan", "Fev", "Mar", "Abr", "Mai", "Jun", "Jul", "Ago", "Set", "Out", "Nov", "Dez" };

		for (int i = 0; i < monthlyReservations.length; i++) {
			dataset.addValue(monthlyReservations[i], "Reservas", months[i]);
		}

		return dataset;
	}

	private JFreeChart createChart(CategoryDataset dataset) {
		JFreeChart chart = ChartFactory.createBarChart("Reservas por Mês", "Mês", "Quantidade", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		CategoryPlot plot = (CategoryPlot) chart.getPlot();

		BarRenderer renderer = (BarRenderer) plot.getRenderer();
		renderer.setSeriesPaint(0, new Color(61, 106, 131));

		return chart;
	}

	// ============================ FUNCIONARIO =========================================
	private void criarUsuarioPanel() {
		JPanel criarUsuarioPanel = new JPanel();
		criarUsuarioPanel.setBackground(new Color(207, 224, 233));
		criarUsuarioPanel.setLayout(null);

		contentPane.add(criarUsuarioPanel, "criarUsuarioPanel");

		JLabel lblNewLabel = new JLabel("Cadastrar Novo Usuario");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblNewLabel.setBounds(167, 25, 239, 14);
		criarUsuarioPanel.add(lblNewLabel);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(54, 86, 390, 334);
		criarUsuarioPanel.add(panel);
		panel.setLayout(null);

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(266, 287, 75, 23);
		panel.add(btnSalvar);
		btnSalvar.setBackground(new Color(183, 247, 192));

		JButton btnLimparCampos = new JButton("Limpar Campos");
		btnLimparCampos.setBounds(35, 287, 116, 23);
		panel.add(btnLimparCampos);
		btnLimparCampos.setBackground(new Color(183, 247, 192));

		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(24, 25, 80, 14);
		panel.add(lblNome);

		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(24, 67, 80, 14);
		panel.add(lblEmail);

		JLabel lblSenha = new JLabel("Senha:");
		lblSenha.setBounds(24, 104, 80, 14);
		panel.add(lblSenha);

		JLabel lblTelefone = new JLabel("Telefone:");
		lblTelefone.setBounds(24, 146, 80, 14);
		panel.add(lblTelefone);

		JLabel lblCargo = new JLabel("Cargo:");
		lblCargo.setBounds(24, 198, 80, 14);
		panel.add(lblCargo);

		JComboBox<String> cbCargo = new JComboBox<>();
		cbCargo.setBounds(114, 194, 227, 22);
		panel.add(cbCargo);
		cbCargo.setBackground(new Color(255, 245, 240));
		cbCargo.setModel(new DefaultComboBoxModel<String>(new String[] { "Caixa", "Atendente", "Administrador" }));

		txtTelefone = new JTextField();
		txtTelefone.setBounds(114, 143, 227, 20);
		panel.add(txtTelefone);
		txtTelefone.setBackground(new Color(255, 245, 240));
		txtTelefone.setColumns(10);

		txtSenha = new JTextField();
		txtSenha.setBounds(114, 101, 227, 20);
		panel.add(txtSenha);
		txtSenha.setBackground(new Color(255, 245, 240));
		txtSenha.setColumns(10);

		txtEmail = new JTextField();
		txtEmail.setBounds(114, 64, 227, 20);
		panel.add(txtEmail);
		txtEmail.setBackground(new Color(255, 245, 240));
		txtEmail.setColumns(10);

		txtNome = new JTextField();
		txtNome.setBounds(114, 22, 227, 20);
		panel.add(txtNome);
		txtNome.setBackground(new Color(255, 245, 240));
		txtNome.setColumns(10);

		JLabel lblNewLabel_5 = new JLabel("");
		lblNewLabel_5.setIcon(new ImageIcon("C:\\Users\\Cristiely\\Downloads\\Reset password-cuate (1).png"));
		lblNewLabel_5.setBounds(416, 25, 495, 501);
		criarUsuarioPanel.add(lblNewLabel_5);
		btnLimparCampos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limparCampos();
			}
		});
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nome = txtNome.getText();
				String email = txtEmail.getText();
				String senha = txtSenha.getText();
				String telefone = txtTelefone.getText();
				String cargo = (String) cbCargo.getSelectedItem();

				FuncionariosEntity novofuncionario = new FuncionariosEntity();

				novofuncionario.setNome(nome);
				novofuncionario.setEmail(email);
				novofuncionario.setSenha(senha);
				novofuncionario.setTelefone(telefone);
				novofuncionario.setCargo(cargo);

				try {
					if (!em.getTransaction().isActive()) {
						funcionarioController.createFuncionario(novofuncionario);

						JOptionPane.showMessageDialog(null, "Novo usuário criado com sucesso.");
					}

				} catch (Exception ex) {
					ex.printStackTrace();
					if (em.getTransaction().isActive()) {
						em.getTransaction().rollback();
					}
					JOptionPane.showMessageDialog(null, "Erro ao criar novo usuário: " + ex.getMessage());
				}
			}
		});
	}

	protected void limparCampos() {
		txtNome.setText("");
		txtEmail.setText("");
		txtSenha.setText("");
		txtTelefone.setText("");
	}

	private void listarUsuarioPanel() {
		JPanel listarUsuarioPanel = new JPanel();
		listarUsuarioPanel.setBackground(new Color(207, 224, 233));
		listarUsuarioPanel.setLayout(null);

		JLabel lblListarUsuarios = new JLabel("Lista de Usuários");
		lblListarUsuarios.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblListarUsuarios.setBounds(343, 11, 200, 20);
		listarUsuarioPanel.add(lblListarUsuarios);

		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("ID");
		tableModel.addColumn("Nome");
		tableModel.addColumn("Cargo");
		tableModel.addColumn("Contato");

		List<FuncionariosEntity> usuarios = funcionarioController.findAll();
		if (usuarios != null) {
			for (FuncionariosEntity usuario : usuarios) {
				Object[] rowData = { usuario.getId(), usuario.getNome(), usuario.getCargo(), usuario.getTelefone() };
				tableModel.addRow(rowData);
			}
		}

		JTable table = new JTable(tableModel);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(50, 85, 880, 300);
		listarUsuarioPanel.add(scrollPane);

		JTextField txtId = new JTextField();
		txtId.setBackground(new Color(255, 255, 255));
		txtId.setBounds(50, 50, 620, 23);
		listarUsuarioPanel.add(txtId);
		txtId.setColumns(10);

		JButton btnBuscarPorId = new JButton("Buscar por ID");
		btnBuscarPorId.setBackground(new Color(183, 247, 192));
		btnBuscarPorId.setBounds(680, 50, 120, 23);
		listarUsuarioPanel.add(btnBuscarPorId);
		btnBuscarPorId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String idStr = txtId.getText().trim();
				if (!idStr.isEmpty()) {
					try {
						Long id = Long.parseLong(idStr);
						FuncionariosEntity usuario = funcionarioController.findFuncionarioById(id);
						if (usuario != null) {

							tableModel.setRowCount(0);

							Object[] rowData = { usuario.getId(), usuario.getNome(), usuario.getCargo(),
									usuario.getTelefone() };
							tableModel.addRow(rowData);
						} else {
							JOptionPane.showMessageDialog(null, "Nenhum usuário encontrado com o ID informado.");
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "ID deve ser um número válido.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Por favor, informe um ID para buscar.");
				}
			}
		});

		JButton btnListarTodos = new JButton("Atualizar");
		btnListarTodos.setBackground(new Color(183, 247, 192));
		btnListarTodos.setBounds(810, 50, 120, 23);
		listarUsuarioPanel.add(btnListarTodos);
		btnListarTodos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.setRowCount(0);
				List<FuncionariosEntity> usuarios = funcionarioController.findAll();
				if (usuarios != null) {
					for (FuncionariosEntity usuario : usuarios) {
						Object[] rowData = { usuario.getId(), usuario.getNome(), usuario.getCargo(),
								usuario.getTelefone() };
						tableModel.addRow(rowData);
					}
				}
			}
		});

		contentPane.add(listarUsuarioPanel, "listarUsuarioPanel");

		JButton btnEditar_1 = new JButton("Editar");
		btnEditar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "Selecione um usuário para editar.");
					return;
				}

				Long id = (Long) table.getValueAt(selectedRow, 0);

				abrirJanelaEdicao(id);
			}
		});
		btnEditar_1.setBackground(new Color(183, 247, 192));
		btnEditar_1.setBounds(343, 404, 120, 23);
		listarUsuarioPanel.add(btnEditar_1);

		JButton btnEditar_1_1 = new JButton("Excluir");
		btnEditar_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(listarUsuarioPanel, "Por favor, selecione um usuário para excluir.");
					return;
				}

				long userId = (long) table.getValueAt(selectedRow, 0);

				int option = JOptionPane.showConfirmDialog(listarUsuarioPanel,
						"Tem certeza que deseja excluir este usuário?", "Confirmar Exclusão",
						JOptionPane.YES_NO_OPTION);
				if (option != JOptionPane.YES_OPTION) {
					return;
				}

				try {
					funcionarioController.deleteFuncionarioById(userId);
					JOptionPane.showMessageDialog(listarUsuarioPanel, "Usuário excluído com sucesso.");

					tableModel.removeRow(selectedRow);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(listarUsuarioPanel, "Erro ao excluir usuário: " + ex.getMessage());
				}
			}
		});
		btnEditar_1_1.setBackground(new Color(183, 247, 192));
		btnEditar_1_1.setBounds(550, 404, 120, 23);
		listarUsuarioPanel.add(btnEditar_1_1);
	}

	private void abrirJanelaEdicao(Long id) {
		FuncionariosEntity usuario = funcionarioController.findFuncionarioById(id);
		if (usuario != null) {

			JDialog dialog = new JDialog();
			dialog.setTitle("Editar Usuário");
			dialog.setSize(400, 300);
			dialog.setModal(true);

			JPanel editarPanel = new JPanel();
			editarPanel.setLayout(null);

			JLabel lblEditarUsuario = new JLabel("Editar Usuário");
			lblEditarUsuario.setBounds(50, 20, 200, 14);
			editarPanel.add(lblEditarUsuario);

			JLabel lblNome = new JLabel("Nome:");
			lblNome.setBounds(50, 50, 80, 14);
			editarPanel.add(lblNome);

			JTextField txtNomeEdit = new JTextField(usuario.getNome());
			txtNomeEdit.setBounds(140, 47, 200, 20);
			editarPanel.add(txtNomeEdit);

			JLabel lblEmail = new JLabel("Email:");
			lblEmail.setBounds(50, 80, 80, 14);
			editarPanel.add(lblEmail);

			JTextField txtEmailEdit = new JTextField(usuario.getEmail());
			txtEmailEdit.setBounds(140, 77, 200, 20);
			editarPanel.add(txtEmailEdit);

			JLabel lblSenha = new JLabel("Senha:");
			lblSenha.setBounds(50, 110, 80, 14);
			editarPanel.add(lblSenha);

			JTextField txtSenhaEdit = new JTextField(usuario.getSenha());
			txtSenhaEdit.setBounds(140, 107, 200, 20);
			editarPanel.add(txtSenhaEdit);

			JLabel lblTelefone = new JLabel("Telefone:");
			lblTelefone.setBounds(50, 140, 80, 14);
			editarPanel.add(lblTelefone);

			JTextField txtTelefoneEdit = new JTextField(usuario.getTelefone());
			txtTelefoneEdit.setBounds(140, 137, 200, 20);
			editarPanel.add(txtTelefoneEdit);

			JLabel lblCargo = new JLabel("Cargo:");
			lblCargo.setBounds(50, 170, 80, 14);
			editarPanel.add(lblCargo);

			JComboBox<String> cbCargoEdit = new JComboBox<>();
			cbCargoEdit
					.setModel(new DefaultComboBoxModel<String>(new String[] { "Caixa", "Atendente", "Administrador" }));
			cbCargoEdit.setSelectedItem(usuario.getCargo());
			cbCargoEdit.setBounds(140, 167, 200, 20);
			editarPanel.add(cbCargoEdit);

			JButton btnSalvar = new JButton("Salvar");
			btnSalvar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					usuario.setNome(txtNomeEdit.getText());
					usuario.setEmail(txtEmailEdit.getText());
					usuario.setSenha(txtSenhaEdit.getText());
					usuario.setTelefone(txtTelefoneEdit.getText());
					usuario.setCargo((String) cbCargoEdit.getSelectedItem());

					try {
						funcionarioController.updateFuncionario(usuario);
						JOptionPane.showMessageDialog(null, "Usuário atualizado com sucesso.");

						listarUsuarioPanel();
						dialog.dispose();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Erro ao atualizar usuário: " + ex.getMessage());
					}
				}
			});
			btnSalvar.setBounds(50, 200, 100, 23);
			editarPanel.add(btnSalvar);

			JButton btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.dispose();
				}
			});
			btnCancelar.setBounds(160, 200, 100, 23);
			editarPanel.add(btnCancelar);

			dialog.getContentPane().add(editarPanel);
			 dialog.setLocationRelativeTo(null);

			dialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "Usuário não encontrado.");
		}
	}

//============================ QUIOSQUE   =====================================================================================================

	private void criarQuiosquePanel() {
		JPanel criarQuiosquePanel = new JPanel();
		criarQuiosquePanel.setBackground(new Color(207, 224, 233));
		criarQuiosquePanel.setLayout(null);

		JLabel lblCriarQuiosque = new JLabel("Criar Quiosque");
		lblCriarQuiosque.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCriarQuiosque.setBounds(204, 26, 200, 25);
		criarQuiosquePanel.add(lblCriarQuiosque);

		contentPane.add(criarQuiosquePanel, "criarQuiosquePanel");

		contentPane.add(criarQuiosquePanel, "criarQuiosquePanel");

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(97, 78, 402, 352);
		criarQuiosquePanel.add(panel);
		panel.setLayout(null);

		JLabel Lbnum = new JLabel("Numero");
		Lbnum.setBounds(43, 55, 46, 14);
		panel.add(Lbnum);

		JLabel lbLocalidade = new JLabel("Localidade");
		lbLocalidade.setBounds(43, 102, 96, 14);
		panel.add(lbLocalidade);

		JLabel lbcapacidade = new JLabel("capacidade");
		lbcapacidade.setBounds(42, 144, 86, 14);
		panel.add(lbcapacidade);

		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(42, 189, 86, 14);
		panel.add(lblStatus);

		JComboBox<String> cbStatus = new JComboBox<>();
		cbStatus.setBounds(114, 185, 231, 22);
		panel.add(cbStatus);
		cbStatus.setBackground(new Color(255, 245, 240));
		cbStatus.setModel(new DefaultComboBoxModel<>(new String[] { "Ativo", "Desativado" }));

		JSpinner capacidade = new JSpinner();
		capacidade.setBounds(114, 141, 231, 20);
		panel.add(capacidade);
		capacidade.setBackground(new Color(255, 245, 240));

		local = new JTextField();
		local.setBounds(114, 99, 231, 20);
		panel.add(local);
		local.setBackground(new Color(255, 245, 240));
		local.setColumns(10);

		num = new JTextField();
		num.setBounds(114, 52, 231, 20);
		panel.add(num);
		num.setBackground(new Color(255, 245, 240));
		num.setColumns(10);

		JButton btnNewButton_2 = new JButton("Limpar campo");
		btnNewButton_2.setBounds(43, 297, 113, 23);
		panel.add(btnNewButton_2);
		btnNewButton_2.setBackground(new Color(183, 247, 192));

		JButton btnNewButton_1 = new JButton("Salvar");
		btnNewButton_1.setBounds(240, 297, 105, 23);
		panel.add(btnNewButton_1);
		btnNewButton_1.setBackground(new Color(183, 247, 192));

		JLabel lblNewLabel_3 = new JLabel("");
		lblNewLabel_3.setIcon(new ImageIcon("C:\\Users\\Cristiely\\Downloads\\beach house-amico (1).png"));
		lblNewLabel_3.setBounds(446, 25, 565, 507);
		criarQuiosquePanel.add(lblNewLabel_3);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String numero = num.getText();
				String localidade = local.getText();
				int capacidadeValor = (Integer) capacidade.getValue();
				String status = (String) cbStatus.getSelectedItem();

				Boolean disponibilidadeStatus = null;
				if (status.equals("livre")) {
					disponibilidadeStatus = true;
				} else if (status.equals("ocupado")) {
					disponibilidadeStatus = false;
				}

				QuiosqueEntity novoQuiosque = new QuiosqueEntity();
				novoQuiosque.setNumero(Integer.parseInt(numero));
				novoQuiosque.setLocalidade(localidade);
				novoQuiosque.setCapacidade(capacidadeValor);
				novoQuiosque.setDisponibilidadeStatus(disponibilidadeStatus);

				try {
					if (!em.getTransaction().isActive()) {
						em.getTransaction().begin();
					}
					quiosqueController.createQuiosque(novoQuiosque);
					em.getTransaction().commit();
					JOptionPane.showMessageDialog(null, "Novo Quiosque criado com sucesso.");
					limparCamposquiosque();
				} catch (Exception ex) {
					ex.printStackTrace(); 
					if (em.getTransaction().isActive()) {
						em.getTransaction().rollback();
					}
					JOptionPane.showMessageDialog(null, "Erro ao criar novo Quiosque");
				}
			}
		});
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limparCamposquiosque();
			}
		});
	}

	private void limparCamposquiosque() {
		num.setText("");
		local.setText("");
	}

	private void gerenciarQuiosquePanel() {
		JPanel gerenciarQuiosquePanel = new JPanel();
		gerenciarQuiosquePanel.setBackground(new Color(207, 224, 233));
		gerenciarQuiosquePanel.setLayout(null);

		JLabel lblListarQuiosques = new JLabel("Lista de Quiosques");
		lblListarQuiosques.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblListarQuiosques.setBounds(343, 11, 200, 20);
		gerenciarQuiosquePanel.add(lblListarQuiosques);

		// Criar o modelo da tabela com colunas definidas
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("ID");
		tableModel.addColumn("Número");
		tableModel.addColumn("Localização");
		tableModel.addColumn("Disponivel");
		List<QuiosqueEntity> quiosques = quiosqueController.findAll();
		if (quiosques != null) {
			for (QuiosqueEntity quiosque : quiosques) {
				String disponibilidade = quiosque.getDisponibilidadeStatus() ? "Disponível" : "Ocupado";
				Object[] rowData = { quiosque.getId(), quiosque.getNumero(), quiosque.getLocalidade(),
						disponibilidade };
				tableModel.addRow(rowData);
			}
		}

		JTable table = new JTable(tableModel);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(50, 85, 880, 300);
		gerenciarQuiosquePanel.add(scrollPane);

		JTextField txtId = new JTextField();
		txtId.setBackground(new Color(255, 245, 240));
		txtId.setBounds(50, 50, 620, 23);
		gerenciarQuiosquePanel.add(txtId);
		txtId.setColumns(10);

		JButton btnBuscarPorId = new JButton("Buscar por ID");
		btnBuscarPorId.setBackground(new Color(183, 247, 192));
		btnBuscarPorId.setBounds(680, 50, 120, 23);
		gerenciarQuiosquePanel.add(btnBuscarPorId);
		btnBuscarPorId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String idStr = txtId.getText().trim();
				if (!idStr.isEmpty()) {
					try {
						Long id = Long.parseLong(idStr);
						QuiosqueEntity quiosque = quiosqueController.findQuiosqueById(id);
						if (quiosque != null) {
				
							tableModel.setRowCount(0);
					
							Object[] rowData = { quiosque.getId(), quiosque.getNumero(), quiosque.getLocalidade() };
							tableModel.addRow(rowData);
						} else {
							JOptionPane.showMessageDialog(null, "Nenhum quiosque encontrado com o ID informado.");
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "ID deve ser um número válido.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Por favor, informe um ID para buscar.");
				}
			}
		});

		JButton btnListarTodos = new JButton("Atualizar");
		btnListarTodos.setBackground(new Color(183, 247, 192));
		btnListarTodos.setBounds(810, 50, 120, 23);
		gerenciarQuiosquePanel.add(btnListarTodos);
		btnListarTodos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.setRowCount(0);
				List<QuiosqueEntity> quiosques = quiosqueController.findAll();
				if (quiosques != null) {
					for (QuiosqueEntity quiosque : quiosques) {
						String disponibilidade = quiosque.getDisponibilidadeStatus() ? "Ativo" : "Desativado";
						Object[] rowData = { quiosque.getId(), quiosque.getNumero(), quiosque.getLocalidade(),
								disponibilidade };
						tableModel.addRow(rowData);
					}
				}
			}
		});


		JButton btnEditar = new JButton("Editar");
		btnEditar.setBackground(new Color(183, 247, 192));
		btnEditar.setBounds(364, 404, 120, 23);
		gerenciarQuiosquePanel.add(btnEditar);
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "Selecione um quiosque para editar.");
					return;
				}

	
				Long id = (Long) table.getValueAt(selectedRow, 0);

			
				editarQuiosque(id);
			}
		});

	
		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.setBackground(new Color(183, 247, 192));
		btnExcluir.setBounds(534, 404, 120, 23);
		gerenciarQuiosquePanel.add(btnExcluir);
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
	
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(gerenciarQuiosquePanel,
							"Por favor, selecione um Quiosque para excluir.");
					return;
				}

				
				Long id = (Long) table.getValueAt(selectedRow, 0);

				
				int option = JOptionPane.showConfirmDialog(gerenciarQuiosquePanel,
						"Tem certeza que deseja excluir este Quiosque?", "Confirmar Exclusão",
						JOptionPane.YES_NO_OPTION);
				if (option != JOptionPane.YES_OPTION) {
					return;
				}

				try {
					if (!em.getTransaction().isActive()) {
						em.getTransaction().begin();
					}

				
					quiosqueController.deleteQuiosque(id);
					em.getTransaction().commit();
					JOptionPane.showMessageDialog(null, "Quiosque excluído com sucesso.");

					
					tableModel.removeRow(selectedRow);
				} catch (Exception ex) {
					ex.printStackTrace(); 
					if (em.getTransaction().isActive()) {
						em.getTransaction().rollback();
					}
					JOptionPane.showMessageDialog(null, "Erro ao excluir o Quiosque: " + ex.getMessage());
				}
			}
		});

		contentPane.add(gerenciarQuiosquePanel, "gerenciarQuiosquePanel");

	}

	private void editarQuiosque(long id) {
		QuiosqueEntity quiosque = quiosqueController.findQuiosqueById(id);
		if (quiosque != null) {
			
			JDialog dialog = new JDialog();
			dialog.setTitle("Editar Quiosque");
			dialog.setSize(400, 300);
			dialog.setModal(true); 

			JPanel editarPanel = new JPanel();
			editarPanel.setLayout(null);

			JLabel lblEditarQuiosque = new JLabel("Editar Quiosque");
			lblEditarQuiosque.setBounds(50, 20, 200, 14);
			editarPanel.add(lblEditarQuiosque);

			JLabel lblNumero = new JLabel("Número:");
			lblNumero.setBounds(50, 50, 80, 14);
			editarPanel.add(lblNumero);

			JTextField txtNumeroEdit = new JTextField(String.valueOf(quiosque.getNumero()));
			txtNumeroEdit.setBounds(140, 47, 200, 20);
			editarPanel.add(txtNumeroEdit);

			JLabel lblLocalidade = new JLabel("Localidade:");
			lblLocalidade.setBounds(50, 80, 80, 14);
			editarPanel.add(lblLocalidade);

			JTextField txtLocalidadeEdit = new JTextField(quiosque.getLocalidade());
			txtLocalidadeEdit.setBounds(140, 77, 200, 20);
			editarPanel.add(txtLocalidadeEdit);

			JLabel lblCapacidade = new JLabel("Capacidade:");
			lblCapacidade.setBounds(50, 110, 80, 14);
			editarPanel.add(lblCapacidade);

			JSpinner spnCapacidadeEdit = new JSpinner(
					new SpinnerNumberModel(quiosque.getCapacidade(), 0, Integer.MAX_VALUE, 1));
			spnCapacidadeEdit.setBounds(140, 107, 200, 20);
			editarPanel.add(spnCapacidadeEdit);

			JLabel lblStatus = new JLabel("Status:");
			lblStatus.setBounds(50, 140, 80, 14);
			editarPanel.add(lblStatus);

			JComboBox<String> cbStatusEdit = new JComboBox<>(new String[] { "Ativo", "Desativado" });
			cbStatusEdit.setSelectedItem(quiosque.getDisponibilidadeStatus() ? "Ativo" : "Desativado");
			cbStatusEdit.setBounds(140, 137, 200, 20);
			editarPanel.add(cbStatusEdit);

			JButton btnSalvar = new JButton("Salvar");
			btnSalvar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
				
					quiosque.setNumero(Integer.parseInt(txtNumeroEdit.getText()));
					quiosque.setLocalidade(txtLocalidadeEdit.getText());
					quiosque.setCapacidade((Integer) spnCapacidadeEdit.getValue());
					quiosque.setDisponibilidadeStatus(cbStatusEdit.getSelectedItem().equals("Ativo"));

					
					try {
						if (!em.getTransaction().isActive()) {
							em.getTransaction().begin();
						}
						quiosqueController.updateQuiosque(quiosque);
						em.getTransaction().commit();
						JOptionPane.showMessageDialog(null, "Quiosque atualizado com sucesso.");
						gerenciarQuiosquePanel(); 
						dialog.dispose(); 
					} catch (Exception ex) {
						ex.printStackTrace();
						if (em.getTransaction().isActive()) {
							em.getTransaction().rollback();
						}
						JOptionPane.showMessageDialog(null, "Erro ao atualizar quiosque: " + ex.getMessage());
					}
				}
			});
			btnSalvar.setBounds(50, 200, 100, 23);
			editarPanel.add(btnSalvar);

			JButton btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.dispose(); 
				}
			});
			btnCancelar.setBounds(160, 200, 100, 23);
			editarPanel.add(btnCancelar);

			dialog.getContentPane().add(editarPanel);

			 dialog.setLocationRelativeTo(null);
			// Exibir o dialog
			dialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "Quiosque não encontrado.");
		}
	}

	// ============================ Clientes =====================================================================================================
	private void criarClientePanel() {
		JPanel criarClientePanel = new JPanel();
		criarClientePanel.setBackground(new Color(207, 224, 233));
		criarClientePanel.setLayout(null);

		JLabel lblCriarCliente = new JLabel("Criar Cliente");
		lblCriarCliente.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblCriarCliente.setBounds(240, 11, 200, 28);
		criarClientePanel.add(lblCriarCliente);

		contentPane.add(criarClientePanel, "criarClientePanel");

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setIcon(new ImageIcon("C:\\Users\\Cristiely\\Downloads\\Online resume-cuate (1) (1).png"));
		lblNewLabel_2.setBounds(488, 49, 552, 453);
		criarClientePanel.add(lblNewLabel_2);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(93, 63, 421, 340);
		criarClientePanel.add(panel);
		panel.setLayout(null);

		JLabel lblNome = new JLabel("Nome");
		lblNome.setBounds(37, 32, 51, 14);
		panel.add(lblNome);

		JLabel lblStatus = new JLabel("Status");
		lblStatus.setBounds(37, 218, 86, 14);
		panel.add(lblStatus);

		JLabel lblTelefone = new JLabel("Telefone");
		lblTelefone.setBounds(37, 85, 96, 14);
		panel.add(lblTelefone);

		JLabel lblCpf = new JLabel("CPF");
		lblCpf.setBounds(37, 126, 86, 14);
		panel.add(lblCpf);

		JLabel lblEmail = new JLabel("Email");
		lblEmail.setBounds(37, 172, 86, 14);
		panel.add(lblEmail);

		JTextField nome = new JTextField();
		nome.setBounds(115, 29, 230, 20);
		panel.add(nome);
		nome.setBackground(new Color(255, 245, 240));
		nome.setColumns(10);

		JTextField telefone = new JTextField();
		telefone.setBounds(115, 82, 230, 20);
		panel.add(telefone);
		telefone.setBackground(new Color(255, 245, 240));
		telefone.setColumns(10);

		JTextField cpf = new JTextField();
		cpf.setBounds(115, 123, 230, 20);
		panel.add(cpf);
		cpf.setBackground(new Color(255, 245, 240));
		cpf.setColumns(10);

		JTextField email = new JTextField();
		email.setBounds(115, 169, 230, 20);
		panel.add(email);
		email.setBackground(new Color(255, 245, 240));
		email.setColumns(10);

		JComboBox<String> cbStatus = new JComboBox<>();
		cbStatus.setBounds(115, 214, 230, 22);
		panel.add(cbStatus);
		cbStatus.setBackground(new Color(255, 245, 240));
		cbStatus.setModel(new DefaultComboBoxModel<>(new String[] { "Ativo", "Desativado" }));

		JButton btnLimpar = new JButton("Limpar campo");
		btnLimpar.setBounds(37, 306, 113, 23);
		panel.add(btnLimpar);
		btnLimpar.setBackground(new Color(183, 247, 192));

		JButton btnSalvar = new JButton("Salvar");
		btnSalvar.setBounds(281, 306, 105, 23);
		panel.add(btnSalvar);
		btnSalvar.setBackground(new Color(183, 247, 192));
		btnSalvar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nomeCliente = nome.getText();
				String telefoneCliente = telefone.getText();
				String cpfCliente = cpf.getText();
				String emailCliente = email.getText();
				String statusCliente = (String) cbStatus.getSelectedItem();

				boolean userStatus = "Ativo".equals(statusCliente);

			
				boolean cpfExists = clientesController.verificarClientePorCPF(cpfCliente);

				if (cpfExists) {
					JOptionPane.showMessageDialog(null, "Já existe um cliente com o mesmo CPF.", "Erro",
							JOptionPane.ERROR_MESSAGE);
					return; 
				}

				ClientesEntity novoCliente = new ClientesEntity();
				novoCliente.setNome(nomeCliente);
				novoCliente.setTelefone(telefoneCliente);
				novoCliente.setCpf(cpfCliente);
				novoCliente.setEmail(emailCliente);
				novoCliente.setUserStatus(userStatus);

				try {
					if (!em.getTransaction().isActive()) {
						em.getTransaction().begin();
					}
					clientesController.createCliente(novoCliente);
					em.getTransaction().commit();
					JOptionPane.showMessageDialog(null, "Novo Cliente criado com sucesso.");
					 nome.setText("");
					 telefone.setText("");
				     cpf.setText("");
					 email.setText("");
					 cbStatus.setSelectedIndex(0);
				} catch (Exception ex) {
					ex.printStackTrace(); 
					if (em.getTransaction().isActive()) {
						em.getTransaction().rollback();
					}
					JOptionPane.showMessageDialog(null, "Erro ao criar novo Cliente", "Erro",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 nome.setText("");
				 telefone.setText("");
			     cpf.setText("");
				 email.setText("");
				 cbStatus.setSelectedIndex(0);
			}
		});
	}

	

	private void gerenciarClientePanel() {
		JPanel gerenciarClientePanel = new JPanel();
		gerenciarClientePanel.setBackground(new Color(207, 224, 233));
		gerenciarClientePanel.setLayout(null);

		JLabel lblListarClientes = new JLabel("Lista de Clientes");
		lblListarClientes.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblListarClientes.setBounds(343, 11, 200, 20);
		gerenciarClientePanel.add(lblListarClientes);

		
		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("ID");
		tableModel.addColumn("Nome");
		tableModel.addColumn("Contato");
		tableModel.addColumn("Status");

		List<ClientesEntity> clientes = clientesController.getAllClientes();
		if (clientes != null) {
			for (ClientesEntity cliente : clientes) {
				Object[] rowData = { cliente.getId(), cliente.getNome(), cliente.getTelefone(),
						cliente.getUserStatus() ? "Ativo" : "Desativado" };
				tableModel.addRow(rowData);
			}
		}

		JTable table = new JTable(tableModel);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(50, 85, 880, 300);
		gerenciarClientePanel.add(scrollPane);

		JTextField txtId = new JTextField();
		txtId.setBackground(new Color(255, 255, 255));
		txtId.setBounds(50, 50, 620, 23);
		gerenciarClientePanel.add(txtId);
		txtId.setColumns(10);

		JButton btnBuscarPorId = new JButton("Buscar por ID");
		btnBuscarPorId.setBackground(new Color(183, 247, 192));
		btnBuscarPorId.setBounds(680, 50, 120, 23);
		gerenciarClientePanel.add(btnBuscarPorId);
		btnBuscarPorId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String idStr = txtId.getText().trim();
				if (!idStr.isEmpty()) {
					try {
						Long id = Long.parseLong(idStr);
						ClientesEntity cliente = clientesController.getClienteById(id);
						if (cliente != null) {
							tableModel.setRowCount(0);
							Object[] rowData = { cliente.getId(), cliente.getNome(), cliente.getTelefone(),
									cliente.getUserStatus() ? "Ativo" : "Desativado" };
							tableModel.addRow(rowData);
						} else {
							JOptionPane.showMessageDialog(null, "Nenhum cliente encontrado com o ID informado.");
						}
					} catch (NumberFormatException ex) {
						JOptionPane.showMessageDialog(null, "ID deve ser um número válido.");
					}
				} else {
					JOptionPane.showMessageDialog(null, "Por favor, informe um ID para buscar.");
				}
			}
		});

		JButton btnListarTodos = new JButton("Atualizar");
		btnListarTodos.setBackground(new Color(183, 247, 192));
		btnListarTodos.setBounds(810, 50, 120, 23);
		gerenciarClientePanel.add(btnListarTodos);
		btnListarTodos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.setRowCount(0);
				List<ClientesEntity> clientes = clientesController.getAllClientes();
				if (clientes != null) {
					for (ClientesEntity cliente : clientes) {
						Object[] rowData = { cliente.getId(), cliente.getNome(), cliente.getTelefone(),
								cliente.getUserStatus() ? "Ativo" : "Desativado" };
						tableModel.addRow(rowData);
					}
				}
			}
		});
		JButton btnEditar = new JButton("Editar");
		btnEditar.setBackground(new Color(183, 247, 192));
		btnEditar.setBounds(360, 404, 120, 23);
		gerenciarClientePanel.add(btnEditar);
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "Selecione um cliente para editar.");
					return;
				}

				Long id = (Long) table.getValueAt(selectedRow, 0);
				editarCliente(id);
			}
		});

		JButton btnExcluir = new JButton("Excluir");
		btnExcluir.setBackground(new Color(183, 247, 192));
		btnExcluir.setBounds(550, 404, 120, 23);
		gerenciarClientePanel.add(btnExcluir);
		btnExcluir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(gerenciarClientePanel,
							"Por favor, selecione um cliente para excluir.");
					return;
				}

				Long id = (Long) table.getValueAt(selectedRow, 0);

				int option = JOptionPane.showConfirmDialog(gerenciarClientePanel,
						"Tem certeza que deseja excluir este cliente?", "Confirmar Exclusão",
						JOptionPane.YES_NO_OPTION);
				if (option != JOptionPane.YES_OPTION) {
					return;
				}

				try {
					if (!em.getTransaction().isActive()) {
						em.getTransaction().begin();
					}

					clientesController.deleteCliente(id);
					em.getTransaction().commit();
					JOptionPane.showMessageDialog(null, "Cliente excluído com sucesso.");

					tableModel.removeRow(selectedRow);
				} catch (Exception ex) {
					ex.printStackTrace(); 
					if (em.getTransaction().isActive()) {
						em.getTransaction().rollback();
					}
					JOptionPane.showMessageDialog(null, "Erro ao excluir o cliente: " + ex.getMessage());
				}
			}
		});

		contentPane.add(gerenciarClientePanel, "gerenciarClientePanel");
	}

	private void editarCliente(long id) {

		ClientesEntity cliente = clientesController.getClienteById(id);

		if (cliente != null) {
			JDialog dialog = new JDialog();
			dialog.setTitle("Editar Cliente");
			dialog.setSize(400, 300);
			dialog.setModal(true); 

			JPanel editarPanel = new JPanel();
			editarPanel.setLayout(null);

			JLabel lblEditarCliente = new JLabel("Editar Cliente");
			lblEditarCliente.setBounds(50, 20, 200, 14);
			editarPanel.add(lblEditarCliente);

			JLabel lblNome = new JLabel("Nome:");
			lblNome.setBounds(50, 50, 80, 14);
			editarPanel.add(lblNome);

			JTextField txtNomeEdit = new JTextField(cliente.getNome());
			txtNomeEdit.setBounds(140, 47, 200, 20);
			editarPanel.add(txtNomeEdit);

			JLabel lblTelefone = new JLabel("Telefone:");
			lblTelefone.setBounds(50, 80, 80, 14);
			editarPanel.add(lblTelefone);

			JTextField txtTelefoneEdit = new JTextField(cliente.getTelefone());
			txtTelefoneEdit.setBounds(140, 77, 200, 20);
			editarPanel.add(txtTelefoneEdit);

			JLabel lblCpf = new JLabel("CPF:");
			lblCpf.setBounds(50, 110, 80, 14);
			editarPanel.add(lblCpf);

			JTextField txtCpfEdit = new JTextField(cliente.getCpf());
			txtCpfEdit.setBounds(140, 107, 200, 20);
			editarPanel.add(txtCpfEdit);

			JLabel lblStatus = new JLabel("Status:");
			lblStatus.setBounds(50, 140, 80, 14);
			editarPanel.add(lblStatus);

			JComboBox<String> cbStatusEdit = new JComboBox<>(new String[] { "Ativo", "Desativado" });
			cbStatusEdit.setSelectedItem(cliente.getUserStatus() ? "Ativo" : "Desativado");
			cbStatusEdit.setBounds(140, 137, 200, 20);
			editarPanel.add(cbStatusEdit);

			JButton btnSalvar = new JButton("Salvar");
			btnSalvar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cliente.setNome(txtNomeEdit.getText());
					cliente.setTelefone(txtTelefoneEdit.getText());
					cliente.setCpf(txtCpfEdit.getText());


					boolean novoStatus = "Ativo".equals(cbStatusEdit.getSelectedItem());
					cliente.setUserStatus(novoStatus);

					try {
						if (!em.getTransaction().isActive()) {
							em.getTransaction().begin();
						}
						clientesController.updateCliente(cliente);
						em.getTransaction().commit();
						JOptionPane.showMessageDialog(null, "Cliente atualizado com sucesso.");
						dialog.dispose(); 
					} catch (Exception ex) {
						ex.printStackTrace();
						if (em.getTransaction().isActive()) {
							em.getTransaction().rollback();
						}
						JOptionPane.showMessageDialog(null, "Erro ao atualizar cliente: " + ex.getMessage());
					}
				}
			});

			btnSalvar.setBounds(50, 200, 100, 23);
			editarPanel.add(btnSalvar);

			JButton btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.dispose(); 
				}
			});
			btnCancelar.setBounds(160, 200, 100, 23);
			editarPanel.add(btnCancelar);
			dialog.getContentPane().add(editarPanel);
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "Cliente não encontrado.");
		}
	}

	// ============================ RESERVAS =====================================================================================================
	private void fazerReservaPanel() {
		JPanel fazerReservaPanel = new JPanel();
		fazerReservaPanel.setBackground(new Color(207, 224, 233));
		fazerReservaPanel.setLayout(null);

		JLabel lblFazerReserva = new JLabel("Fazer Reserva");
		lblFazerReserva.setBackground(new Color(0, 0, 0));
		lblFazerReserva.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblFazerReserva.setForeground(new Color(0, 0, 0));
		lblFazerReserva.setBounds(196, 11, 200, 24);
		fazerReservaPanel.add(lblFazerReserva);

		contentPane.add(fazerReservaPanel, "fazerReservaPanel");

		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 255, 255));
		panel.setBounds(33, 46, 532, 420);
		fazerReservaPanel.add(panel);
		panel.setLayout(null);

		JLabel lblInicio = new JLabel("Início");
		lblInicio.setBounds(30, 59, 46, 14);
		panel.add(lblInicio);

		JDateChooser dateChooserInicio = new JDateChooser();
		dateChooserInicio.setBounds(86, 53, 146, 20);
		panel.add(dateChooserInicio);

		JLabel lblFim = new JLabel("Fim");
		lblFim.setBounds(305, 59, 46, 14);
		panel.add(lblFim);

		JDateChooser dateChooserFim = new JDateChooser();
		dateChooserFim.setBounds(361, 53, 146, 20);
		panel.add(dateChooserFim);

		JLabel lblQuiosque = new JLabel("Quiosque");
		lblQuiosque.setBounds(30, 118, 100, 14);
		panel.add(lblQuiosque);

		comboBoxQuiosque = new JComboBox<>();
		comboBoxQuiosque.setBackground(new Color(255, 245, 240));
		comboBoxQuiosque.setBounds(86, 114, 149, 22);
		panel.add(comboBoxQuiosque);

		JLabel lblCliente = new JLabel("Cliente");
		lblCliente.setBounds(30, 176, 100, 14);
		panel.add(lblCliente);

		comboBoxCliente = new JComboBox<>();
		comboBoxCliente.setBackground(new Color(255, 245, 240));
		comboBoxCliente.setBounds(85, 172, 149, 22);
		panel.add(comboBoxCliente);

		JLabel lblDiaria = new JLabel("Diária");
		lblDiaria.setBounds(30, 233, 45, 14);
		panel.add(lblDiaria);

		txtDiaria = new JTextField();
		txtDiaria.setBackground(new Color(255, 245, 240));
		txtDiaria.setBounds(85, 230, 150, 20);
		panel.add(txtDiaria);
		txtDiaria.setColumns(10);

		JLabel lblTotal = new JLabel("Total");
		lblTotal.setBounds(305, 233, 46, 14);
		panel.add(lblTotal);

		txtTotal = new JTextField();
		txtTotal.setBackground(new Color(255, 245, 240));
		txtTotal.setColumns(10);
		txtTotal.setBounds(374, 230, 117, 20);
		txtTotal.setEditable(false);
		panel.add(txtTotal);

		JButton btnCadastrar = new JButton("Cadastrar");
		btnCadastrar.setBackground(new Color(183, 247, 192));
		btnCadastrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					LocalDate inicio = dateChooserInicio.getDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDate();
					LocalDate fim = dateChooserFim.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					String quiosqueNome = (String) comboBoxQuiosque.getSelectedItem();
					String clienteNome = (String) comboBoxCliente.getSelectedItem();
					String diariaStr = txtDiaria.getText();
					String totalStr = txtTotal.getText();

					if (quiosqueNome.isEmpty() || clienteNome.isEmpty() || diariaStr.isEmpty() || totalStr.isEmpty()) {
						JOptionPane.showMessageDialog(null, "Preencha todos os campos.");
						return;
					}
					Integer numeroQuiosque = Integer.parseInt(quiosqueNome); 
					QuiosqueEntity quiosque = quiosqueController.encontrarQuiosquePorNumero(numeroQuiosque);

					if (reservaController.isQuiosqueAlugadoNoPeriodo(quiosque, inicio, fim, null)) {
						JOptionPane.showMessageDialog(null, "Quiosque já está alugado no período selecionado.");
						return;
					}

					ClientesEntity cliente = clientesController.encontrarClientePorNome(clienteNome);

					BigDecimal diaria = new BigDecimal(diariaStr);
					BigDecimal total = new BigDecimal(totalStr);

					ReservasEntity novaReserva = new ReservasEntity();
					novaReserva.setDataInicio(inicio);
					novaReserva.setDataFim(fim);
					novaReserva.setQuiosque(quiosque);
					novaReserva.setCliente(cliente);
					novaReserva.setPrecoDiaria(diaria);
					novaReserva.setValorTotal(total);

					if (!em.getTransaction().isActive()) {
						em.getTransaction().begin();

						em.persist(novaReserva); 
						em.getTransaction().commit();
						JOptionPane.showMessageDialog(null, "Reserva criada com sucesso.");

						limparCampoReserva();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
					if (em.getTransaction().isActive()) {
						em.getTransaction().rollback();
					}
					JOptionPane.showMessageDialog(null, "Erro ao criar reserva: " + ex.getMessage());
				}
			}
		});

		btnCadastrar.setBounds(320, 363, 89, 23);
		panel.add(btnCadastrar);

		JButton btnLimparCampos = new JButton("Limpar campos");
		btnLimparCampos.setBackground(new Color(183, 247, 192));
		btnLimparCampos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limparCampoReserva();
			}
		});
		btnLimparCampos.setBounds(99, 363, 104, 23);
		panel.add(btnLimparCampos);

		comboBoxQuiosque.addItem("");
		comboBoxCliente.addItem("");
		carregarDados(comboBoxQuiosque, comboBoxCliente);

		JLabel lblNewLabel_4 = new JLabel("");
		lblNewLabel_4.setIcon(new ImageIcon("C:\\Users\\Cristiely\\Downloads\\Tiny house-cuate (1).png"));
		lblNewLabel_4.setBounds(473, 11, 486, 480);
		fazerReservaPanel.add(lblNewLabel_4);

		txtDiaria.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				calcularTotal();
			}

			public void removeUpdate(DocumentEvent e) {
				calcularTotal();
			}

			public void insertUpdate(DocumentEvent e) {
				calcularTotal();
			}

			private void calcularTotal() {
				try {
					LocalDate inicio = dateChooserInicio.getDate().toInstant().atZone(ZoneId.systemDefault())
							.toLocalDate();
					LocalDate fim = dateChooserFim.getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					long dias = ChronoUnit.DAYS.between(inicio, fim) + 1; 
					double diaria = Double.parseDouble(txtDiaria.getText());
					double total = dias * diaria;
					txtTotal.setText(String.valueOf(total));
				} catch (Exception ex) {
					txtTotal.setText("");
				}

			}
		});

		fazerReservaPanel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				carregarDados(comboBoxQuiosque, comboBoxCliente); 
			}
		});

	}

	private void limparCampoReserva() {
		try {
			dateChooserInicio.setDate(null);
			dateChooserFim.setDate(null);
			comboBoxQuiosque.setSelectedIndex(0);
			comboBoxCliente.setSelectedIndex(0);
			txtDiaria.setText("");
			txtTotal.setText("");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void carregarDados(JComboBox<String> comboBoxQuiosque, JComboBox<String> comboBoxCliente) {
		try {
			// Carregar clientes ativos
			List<ClientesEntity> clientes = clientesController.findClientesByUserStatus(true);

			SwingUtilities.invokeLater(() -> {
				comboBoxCliente.removeAllItems();
				comboBoxCliente.addItem(""); 
				for (ClientesEntity cliente : clientes) {
					comboBoxCliente.addItem(cliente.getNome());
				}
			});

			List<QuiosqueEntity> quiosques = quiosqueController.findQuiosquesByDisponibilidadeStatus(true);

			SwingUtilities.invokeLater(() -> {
				comboBoxQuiosque.removeAllItems();
				comboBoxQuiosque.addItem(""); 
				for (QuiosqueEntity quiosque : quiosques) {
					comboBoxQuiosque.addItem(String.valueOf(quiosque.getNumero()));
				}
			});

		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Erro ao carregar dados: " + ex.getMessage());
		}
	}

	private void listarReservaPanel() {
		JPanel listarReservaPanel = new JPanel();
		listarReservaPanel.setBackground(new Color(207, 224, 233));
		listarReservaPanel.setLayout(null);

		JLabel lblListarUsuarios = new JLabel("Lista de Reservas");
		lblListarUsuarios.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblListarUsuarios.setBounds(343, 11, 200, 20);
		listarReservaPanel.add(lblListarUsuarios);

		DefaultTableModel tableModel = new DefaultTableModel();
		tableModel.addColumn("ID");
		tableModel.addColumn("Inicio");
		tableModel.addColumn("Fim");
		tableModel.addColumn("Quiosque");
		tableModel.addColumn("Cliente");
		tableModel.addColumn("Total");

		List<ReservasEntity> reservas = reservaController.encontrarTodasReservas();
		if (reservas != null) {
			for (ReservasEntity reserva : reservas) {
				String quiosqueNumero = String.valueOf(reserva.getQuiosque().getNumero());
				String clienteNome = reserva.getCliente().getNome();
				Object[] rowData = { reserva.getId(), reserva.getDataInicio(), reserva.getDataFim(), quiosqueNumero,
						clienteNome, reserva.getValorTotal() };
				tableModel.addRow(rowData);
			}
		}

		JTable table = new JTable(tableModel);

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(50, 85, 880, 300);
		listarReservaPanel.add(scrollPane);

		JDateChooser dateChooserInicioFiltro = new JDateChooser();
		dateChooserInicioFiltro.setBounds(105, 54, 150, 20);
		listarReservaPanel.add(dateChooserInicioFiltro);

		JDateChooser dateChooserFimFiltro = new JDateChooser();
		dateChooserFimFiltro.setBounds(343, 54, 150, 20);
		listarReservaPanel.add(dateChooserFimFiltro);

		JButton btnListarTodos = new JButton("Atualizar");
		btnListarTodos.setBackground(new Color(183, 247, 192));
		btnListarTodos.setBounds(810, 50, 120, 23);
		listarReservaPanel.add(btnListarTodos);
		btnListarTodos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModel.setRowCount(0);
				List<ReservasEntity> reservas = reservaController.encontrarTodasReservas();
				if (reservas != null) {
					for (ReservasEntity reserva : reservas) {
						String quiosqueNumero = String.valueOf(reserva.getQuiosque().getNumero());
						String clienteNome = reserva.getCliente().getNome();
						Object[] rowData = { reserva.getId(), reserva.getDataInicio(), reserva.getDataFim(),
								quiosqueNumero, clienteNome, reserva.getValorTotal() };
						tableModel.addRow(rowData);
					}
				}
			}
		});

		JButton btnEditar_1 = new JButton("Editar");
		btnEditar_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(null, "Selecione um usuário para editar.");
					return;
				}

				Long id = (Long) table.getValueAt(selectedRow, 0);

				EdicaoReserva(id);
			}
		});
		btnEditar_1.setBackground(new Color(183, 247, 192));
		btnEditar_1.setBounds(298, 404, 120, 23);
		listarReservaPanel.add(btnEditar_1);

		JButton btnEditar_1_1 = new JButton("Excluir");
		btnEditar_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				int selectedRow = table.getSelectedRow();
				if (selectedRow == -1) {
					JOptionPane.showMessageDialog(listarReservaPanel, "Por favor, selecione uma reserva para excluir.");
					return;
				}

				long userId = (long) table.getValueAt(selectedRow, 0);

				int option = JOptionPane.showConfirmDialog(listarReservaPanel,
						"Tem certeza que deseja excluir este reserva?", "Confirmar Exclusão",
						JOptionPane.YES_NO_OPTION);
				if (option != JOptionPane.YES_OPTION) {
					return;
				}
			
				try {
					reservaController.excluirReserva(userId);
					JOptionPane.showMessageDialog(listarReservaPanel, "reserva excluído com sucesso.");

					
					tableModel.removeRow(selectedRow);
				} catch (Exception ex) {
					JOptionPane.showMessageDialog(listarReservaPanel, "Erro ao excluir reserva: " + ex.getMessage());
				}
			}
		});
		btnEditar_1_1.setBackground(new Color(183, 247, 192));
		btnEditar_1_1.setBounds(482, 404, 120, 23);
		listarReservaPanel.add(btnEditar_1_1);
		
		contentPane.add(listarReservaPanel, "listarReservaPanel");

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				java.util.Date inicioUtil = dateChooserInicioFiltro.getDate();
				java.util.Date fimUtil = dateChooserFimFiltro.getDate();
				
				if (inicioUtil == null || fimUtil == null) {
					JOptionPane.showMessageDialog(listarReservaPanel,
							"Por favor, selecione uma data de início e uma data de fim.");
					return; 
				}
				
				java.sql.Date inicio = new java.sql.Date(inicioUtil.getTime());
				java.sql.Date fim = new java.sql.Date(fimUtil.getTime());

				tableModel.setRowCount(0);
				
				List<ReservasEntity> reservas = reservaController.encontrarReservasPorData(inicio.toLocalDate(),
						fim.toLocalDate());
				if (reservas != null) {
					for (ReservasEntity reserva : reservas) {
						String quiosqueNumero = String.valueOf(reserva.getQuiosque().getNumero());
						String clienteNome = reserva.getCliente().getNome();
						Object[] rowData = { reserva.getId(), reserva.getDataInicio(), reserva.getDataFim(),
								quiosqueNumero, clienteNome, reserva.getValorTotal() };
						tableModel.addRow(rowData);
					}
				}
			}
		});

		btnBuscar.setBackground(new Color(183, 247, 192));
		btnBuscar.setBounds(503, 51, 120, 23);
		listarReservaPanel.add(btnBuscar);

		JLabel lblNewLabel_1 = new JLabel("Inicio");
		lblNewLabel_1.setBounds(49, 54, 46, 14);
		listarReservaPanel.add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("fim");
		lblNewLabel_1_1.setBounds(287, 60, 46, 14);
		listarReservaPanel.add(lblNewLabel_1_1);

	}

	private void EdicaoReserva(Long id) {
		ReservasEntity reserva = reservaController.encontrarReservaPorId(id);
		if (reserva != null) {
			JDialog dialog = new JDialog();
			dialog.setTitle("Editar Reserva");
			dialog.setSize(400, 300);
			dialog.setModal(true);

			JPanel editarPanel = new JPanel();
			editarPanel.setLayout(null);

			JLabel lblEditarReserva = new JLabel("Editar Reserva");
			lblEditarReserva.setBounds(50, 20, 200, 14);
			editarPanel.add(lblEditarReserva);

			JLabel lblQuiosque = new JLabel("Quiosque:");
			lblQuiosque.setBounds(50, 50, 80, 14);
			editarPanel.add(lblQuiosque);

			JComboBox<String> cbQuiosqueEdit = new JComboBox<>();
			for (QuiosqueEntity quiosque : quiosqueController.findQuiosquesByDisponibilidadeStatus(true)) {
				cbQuiosqueEdit.addItem(String.valueOf(quiosque.getNumero()));
			}
			cbQuiosqueEdit.setSelectedItem(String.valueOf(reserva.getQuiosque().getNumero()));
			cbQuiosqueEdit.setBounds(140, 47, 200, 20);
			editarPanel.add(cbQuiosqueEdit);

			JLabel lblCliente = new JLabel("Cliente:");
			lblCliente.setBounds(50, 80, 80, 14);
			editarPanel.add(lblCliente);

			JComboBox<String> cbClienteEdit = new JComboBox<>();
			for (ClientesEntity cliente : clientesController.findClientesByUserStatus(true)) {
				cbClienteEdit.addItem(cliente.getNome());
			}
			cbClienteEdit.setSelectedItem(reserva.getCliente().getNome());
			cbClienteEdit.setBounds(140, 77, 200, 20);
			editarPanel.add(cbClienteEdit);

			JLabel lblDataInicio = new JLabel("Data Início:");
			lblDataInicio.setBounds(50, 110, 80, 14);
			editarPanel.add(lblDataInicio);

			JDateChooser dateChooserInicioEdit = new JDateChooser(
					Date.from(reserva.getDataInicio().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			dateChooserInicioEdit.setBounds(140, 107, 200, 20);
			editarPanel.add(dateChooserInicioEdit);

			JLabel lblDataFim = new JLabel("Data Fim:");
			lblDataFim.setBounds(50, 140, 80, 14);
			editarPanel.add(lblDataFim);

			JDateChooser dateChooserFimEdit = new JDateChooser(
					Date.from(reserva.getDataFim().atStartOfDay(ZoneId.systemDefault()).toInstant()));
			dateChooserFimEdit.setBounds(140, 137, 200, 20);
			editarPanel.add(dateChooserFimEdit);

			JLabel lblValorTotal = new JLabel("Valor Total:");
			lblValorTotal.setBounds(50, 170, 80, 14);
			editarPanel.add(lblValorTotal);

			JTextField txtValorTotalEdit = new JTextField(reserva.getValorTotal().toString());
			txtValorTotalEdit.setBounds(140, 167, 200, 20);
			editarPanel.add(txtValorTotalEdit);

			JButton btnSalvar = new JButton("Salvar");
			btnSalvar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					try {
						reserva.setQuiosque(quiosqueController.encontrarQuiosquePorNumero(
								Integer.parseInt((String) cbQuiosqueEdit.getSelectedItem())));
						reserva.setCliente(
								clientesController.encontrarClientePorNome((String) cbClienteEdit.getSelectedItem()));

						java.util.Date dataInicioUtilDate = dateChooserInicioEdit.getDate();
						java.sql.Date dataInicioSqlDate = new java.sql.Date(dataInicioUtilDate.getTime());
						reserva.setDataInicio(dataInicioSqlDate.toLocalDate());

						java.util.Date dataFimUtilDate = dateChooserFimEdit.getDate();
						java.sql.Date dataFimSqlDate = new java.sql.Date(dataFimUtilDate.getTime());
						reserva.setDataFim(dataFimSqlDate.toLocalDate());

						BigDecimal valorTotal = new BigDecimal(txtValorTotalEdit.getText());
						reserva.setValorTotal(valorTotal);

						QuiosqueEntity quiosque = reserva.getQuiosque();
						LocalDate inicio = reserva.getDataInicio();
						LocalDate fim = reserva.getDataFim();

						if (reservaController.isQuiosqueAlugadoNoPeriodo(quiosque, inicio, fim, reserva.getId())) {
							JOptionPane.showMessageDialog(null, "Quiosque já está alugado no período selecionado.");
							return;
						}

						reservaController.atualizarReserva(reserva);
						JOptionPane.showMessageDialog(null, "Reserva atualizada com sucesso.");
						dialog.dispose();
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Erro ao atualizar reserva: " + ex.getMessage());
					}
				}
			});
			btnSalvar.setBounds(50, 200, 100, 23);
			editarPanel.add(btnSalvar);

			JButton btnCancelar = new JButton("Cancelar");
			btnCancelar.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dialog.dispose();
				}
			});
			btnCancelar.setBounds(160, 200, 100, 23);
			editarPanel.add(btnCancelar);

			dialog.getContentPane().add(editarPanel);
			 dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(null, "Reserva não encontrada.");
		}
	}
}
