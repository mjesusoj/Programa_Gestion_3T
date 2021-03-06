package es.studium.programagestion;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class BajaProductos extends Frame implements ActionListener, WindowListener{

	private static final long serialVersionUID = 1L;

	// Crear componentes 
	Label lblBaja = new Label("Baja Productos");
	Choice chcSeleccionarProducto = new Choice();
	Button btnBaja = new Button("Baja");

	// Paneles
	Panel pnlSuperior = new Panel();
	Panel pnlChoice = new Panel(); 
	Panel pnlBaja = new Panel();

	// Dialogo Informativo 
	Dialog diainformativo = new Dialog(this, true);
	// Componentes
	Label lblConfirmacion = new Label("�Est�s seguro de realizar la Baja?");
	Button btnSi = new Button("S�");
	Button btnNo = new Button("No");

	// Base de Datos
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/farmaciapr2?autoReconnect=true&useSSL=false";
	String login = "admin";
	String password = "Studium2018;";
	String sentencia = null;
	static Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	BajaProductos()
	{
		setTitle("Baja Productos");
		colocarIcono();
		pnlSuperior.add(lblBaja);
		lblBaja.setFont(new java.awt.Font("Times New Roman", 1, 18));
		chcSeleccionarProducto.add("Seleccione producto a dar de Baja");
		insertarProductos();
		pnlChoice.add(chcSeleccionarProducto);
		pnlBaja.add(btnBaja);
		add(pnlSuperior, BorderLayout.NORTH);
		add(pnlChoice, BorderLayout.CENTER);
		add(pnlBaja, BorderLayout.SOUTH);

		// Componentes di�logo informativo
		diainformativo.setTitle("Comprobaci�n Baja");
		diainformativo.setLayout(new FlowLayout());
		diainformativo.setBackground(Color.decode("#d9d9d9"));
		diainformativo.add(lblConfirmacion);
		lblConfirmacion.setFont(new java.awt.Font("Times New Roman", 0, 15)); 
		diainformativo.add(btnSi);
		diainformativo.add(btnNo);
		btnSi.setFont(new java.awt.Font("Times New Roman", 0, 15));
		btnNo.setFont(new java.awt.Font("Times New Roman", 0, 15)); 
		diainformativo.setSize(250,100);
		diainformativo.addWindowListener(this);
		btnBaja.addActionListener(this);
		btnSi.addActionListener(this);
		btnNo.addActionListener(this);
		diainformativo.setLocationRelativeTo(null);
		diainformativo.setResizable(false);
		diainformativo.setVisible(false);	

		setSize(300,250);
		addWindowListener(this);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	public void colocarIcono() {
		Toolkit mipantalla = Toolkit.getDefaultToolkit();
		Image miIcono = mipantalla.getImage("farmacia.png");
		setIconImage(miIcono);
	}

	public void insertarProductos() {
		sentencia = "SELECT * FROM productos ORDER BY 1;";

		try
		{
			Class.forName(driver);
			connection = DriverManager.getConnection(url, login, password);
			//Crear una sentencia
			statement = connection.createStatement();
			// Ejecutar la sentencia
			rs = statement.executeQuery(sentencia);
			while (rs.next()) {
				int idProducto = rs.getInt("idProducto");
				String nombreProducto = rs.getString("nombreProducto");
				String marcaProducto = rs.getString("marcaProducto");
				String precioProducto = rs.getString("precioProducto");
				chcSeleccionarProducto.add(idProducto + " " + nombreProducto + " " + marcaProducto + " " + precioProducto + "�");
			}
		}

		catch (ClassNotFoundException cnfe)
		{
			JOptionPane.showMessageDialog(null, "No se puede cargar el driver", "Error", JOptionPane.ERROR_MESSAGE);
		}
		catch (SQLException sqle)
		{
			JOptionPane.showMessageDialog(null, "Error, en la Baja", "Error", JOptionPane.ERROR_MESSAGE);
		}
		desconectar();
	}

	public static void desconectar() {
		try
		{
			if(connection!=null)
			{
				connection.close();
			}
		}
		catch (SQLException se)
		{
			JOptionPane.showMessageDialog(null, "No se puede cerrar la conexi�n con la BD", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (btnBaja.equals(arg0.getSource())){
			if (chcSeleccionarProducto.getSelectedItem().equals("Seleccione producto a dar de Baja")) {
				JOptionPane.showMessageDialog(null, "No puede seleccionar ese elemento, ya que es informativo", "Error", JOptionPane.INFORMATION_MESSAGE);
			}else{
				diainformativo.setVisible(true);
			}
		}

		else if (btnSi.equals(arg0.getSource())) {
			String [] eliminarespacios = chcSeleccionarProducto.getSelectedItem().split(" ");

			String idProducto = eliminarespacios[0];
			
			try {
				Class.forName(driver);
				connection = DriverManager.getConnection(url, login, password);
				sentencia = "DELETE FROM productos WHERE idProducto = '"+idProducto+"';";
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				statement.executeUpdate(sentencia);
				JOptionPane.showMessageDialog(null, "Baja Correcta", "Baja Realizada", JOptionPane.OK_CANCEL_OPTION);
			}

			catch (ClassNotFoundException cnfe)
			{
				JOptionPane.showMessageDialog(null, "Hay un problema al cargar el driver", "Error", JOptionPane.ERROR_MESSAGE);
			}
			catch (SQLException sqle)
			{
				JOptionPane.showMessageDialog(null, "Error, en la Baja", "Error", JOptionPane.ERROR_MESSAGE);
			}
			desconectar();

			Guardar_Movimientos gm = new Guardar_Movimientos();
			try {
				gm.registrar("administrador]" + "["+sentencia+"");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		else if (btnNo.equals(arg0.getSource())) {
			diainformativo.setVisible(false);
			setVisible(true);
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		if (this.isActive()) {
			setVisible(false);
			new MenuPrincipal();
		}else if(diainformativo.isActive()){
			diainformativo.setVisible(false);
			this.dispose();
			new BajaProductos();
		}
	}

	public void windowActivated(WindowEvent arg0) {}
	public void windowClosed(WindowEvent arg0) {}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0) {}
	public void windowOpened(WindowEvent arg0) {}
}