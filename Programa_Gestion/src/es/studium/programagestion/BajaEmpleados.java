package es.studium.programagestion;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.*;

import javax.swing.JOptionPane;

public class BajaEmpleados extends Frame implements ActionListener, WindowListener{

	private static final long serialVersionUID = 1L;

	// Crear componentes 
	Label lblBaja = new Label("Baja Empleados");
	Choice chcSeleccion = new Choice();
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
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	BajaEmpleados()
	{
		// Almacenamos en mipantalla el sistema nativo de pantallas, el tama�o por defecto de la pantalla, nos servira para poner el icono
		Toolkit mipantalla = Toolkit.getDefaultToolkit();

		setTitle("Baja Empleados");
		pnlSuperior.add(lblBaja);
		lblBaja.setFont(new java.awt.Font("Times New Roman", 1, 18)); 
		chcSeleccion.add("Elija uno...");
		insertarEmpleados();
		pnlChoice.add(chcSeleccion);
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
		diainformativo.setSize(280,100);
		diainformativo.addWindowListener(this);
		btnBaja.addActionListener(this);
		btnSi.addActionListener(this);
		btnNo.addActionListener(this);
		diainformativo.setLocationRelativeTo(null);
		diainformativo.setResizable(false);
		diainformativo.setVisible(false);

		// Establecer un icono a la aplicaci�n
		Image miIcono = mipantalla.getImage("src//farmacia.png");
		// Colocar Icono
		setIconImage(miIcono);
		setSize(350,200);
		addWindowListener(this);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	public void insertarEmpleados() {
		sentencia = "SELECT * FROM empleados ORDER BY 1;";

		try
		{
			Class.forName(driver);
			connection = DriverManager.getConnection(url, login, password);
			//Crear una sentencia
			statement = connection.createStatement();
			// Ejecutar la sentencia
			rs = statement.executeQuery(sentencia);
			while (rs.next()) {
				int idEmpleado = rs.getInt("idEmpleado");
				String nombreEmpleado = rs.getString("nombreEmpleado");
				String apellidosEmpleado = rs.getString("apellidosEmpleado");
				chcSeleccion.add(idEmpleado + " " + nombreEmpleado + " " + apellidosEmpleado);
			}
		}

		catch (ClassNotFoundException cnfe)
		{
			System.out.println("Error 1: "+cnfe.getMessage());
		}
		catch (SQLException sqle)
		{
			JOptionPane.showMessageDialog(null, "Error, en la Baja", "Error", JOptionPane.ERROR_MESSAGE);
		}
		finally
		{
			try
			{
				if(connection!=null)
				{
					connection.close();
				}
			}
			catch (SQLException se)
			{
				System.out.println("No se puede cerrar la conexi�n la Base De Datos");
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (btnBaja.equals(arg0.getSource())){
			diainformativo.setVisible(true);
		}

		else if (btnSi.equals(arg0.getSource())) {
			String [] eliminarespacios = chcSeleccion.getSelectedItem().split(" ");

			String idEmpleado = eliminarespacios[0];

			try {
				Class.forName(driver);
				connection = DriverManager.getConnection(url, login, password);
				//Crear una sentencia
				sentencia = "DELETE FROM empleados WHERE idEmpleado = '"+idEmpleado+"';";
				statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				System.out.println(sentencia);
				statement.executeUpdate(sentencia);
			}

			catch (ClassNotFoundException cnfe)
			{
				System.out.println("Error 1: "+cnfe.getMessage());
			}
			catch (SQLException sqle)
			{
				JOptionPane.showMessageDialog(null, "Error, en la Baja", "Error", JOptionPane.ERROR_MESSAGE);
			}
			finally
			{
				try
				{
					if(connection!=null)
					{
						connection.close();
					}
				}
				catch (SQLException se)
				{
					System.out.println("No se puede cerrar la conexi�n la Base De Datos");
				}
			}

			Guardar_Movimientos gm = new Guardar_Movimientos();
			try {
				gm.registrar("administrador]" + "["+sentencia+"");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			JOptionPane.showMessageDialog(null, "Baja Correcta", "Baja Realizada", JOptionPane.OK_CANCEL_OPTION);
		}

		if(btnNo.equals(arg0.getSource())) {
			diainformativo.setVisible(false);
			setVisible(true);
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		if (this.isActive()) {
			setVisible(false);
			new MenuPrincipal(null);
		}else if(diainformativo.isActive()){
			diainformativo.setVisible(false);
			this.setVisible(true);
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
	}
}