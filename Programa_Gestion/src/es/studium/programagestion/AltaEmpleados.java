package es.studium.programagestion;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
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

public class AltaEmpleados extends Frame implements ActionListener, WindowListener{

	private static final long serialVersionUID = 1L;

	// Crear componentes
	Label lblAlta = new Label("Alta Empleados");
	Label lblNombre = new Label("Nombre:  ");
	Label lblApellidos = new Label("Apellidos:");
	TextField txtNombre = new TextField(15);
	TextField txtApellidos = new TextField(15);
	Button btnAlta = new Button("Alta");
	Button btnLimpiar = new Button("Limpiar");

	// Paneles
	Panel pnlSuperior = new Panel();
	Panel pnlComponentes = new Panel();
	Panel pnlBotones = new Panel();

	// Necesario para conectar con la BD
	String driver = "com.mysql.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/farmaciapr2?autoReconnect=true&useSSL=false";
	String login = "admin";
	String password = "Studium2018;";
	String sentencia = null;
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	AltaEmpleados()
	{
		setTitle("Alta Empleados");
		colocarIcono();
		pnlSuperior.add(lblAlta);
		pnlComponentes.add(lblNombre);
		pnlComponentes.add(txtNombre);
		pnlComponentes.add(lblApellidos);
		pnlComponentes.add(txtApellidos);
		pnlBotones.add(btnAlta);
		pnlBotones.add(btnLimpiar);
		// Aplicarle una fuente a la etiqueta y tama�o
		lblAlta.setFont(new java.awt.Font("Times New Roman", 1, 18));
		lblNombre.setFont(new java.awt.Font("Times New Roman", 0, 14));
		lblApellidos.setFont(new java.awt.Font("Times New Roman", 0, 14));
		btnAlta.setFont(new java.awt.Font("Times New Roman", 0, 14)); 
		btnLimpiar.setFont(new java.awt.Font("Times New Roman", 0, 14));

		// A�adir los paneles
		add(pnlSuperior, BorderLayout.NORTH);
		add(pnlComponentes, BorderLayout.CENTER);
		add(pnlBotones, BorderLayout.SOUTH);

		setSize(270,180);
		setLocationRelativeTo(null);
		btnAlta.addActionListener(this);
		btnLimpiar.addActionListener(this);
		addWindowListener(this);
		setResizable(false);
		setVisible(true);
	}

	public void colocarIcono() {
		Toolkit mipantalla = Toolkit.getDefaultToolkit();
		Image miIcono = mipantalla.getImage("farmacia.png");
		setIconImage(miIcono);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String NombreEmpleado = txtNombre.getText();
		String ApellidosEmpleado = txtApellidos.getText();

		if (btnAlta.equals(arg0.getSource())) {
			// Si est�n vac�os los campos
			if (NombreEmpleado.equals("") | ApellidosEmpleado.equals("")) {
				JOptionPane.showMessageDialog(null, "Error en el Alta", "Error", JOptionPane.ERROR_MESSAGE);
			}

			else
			{
				try
				{
					Class.forName(driver);
					connection = DriverManager.getConnection(url, login, password);
					//Crear una sentencia
					statement = connection.createStatement();
					sentencia = "INSERT INTO empleados VALUES(NULL, '"+NombreEmpleado+"', '"+ApellidosEmpleado+"');";
					// Ejecutar la sentencia
					statement.executeUpdate(sentencia);
					JOptionPane.showMessageDialog(null, "El Alta se ha realizado", "Alta Correcta", JOptionPane.INFORMATION_MESSAGE);

					Guardar_Movimientos gm = new Guardar_Movimientos();
					try {
						gm.registrar("administrador]" + "["+sentencia+"");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}

				catch (ClassNotFoundException cnfe)
				{
					JOptionPane.showMessageDialog(null, "No se puede cargar el Driver", "Error", JOptionPane.ERROR_MESSAGE);
				}

				catch (SQLException sqle)
				{
					JOptionPane.showMessageDialog(null, "Error en el Alta", "Error", JOptionPane.ERROR_MESSAGE);
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
						JOptionPane.showMessageDialog(null, "No se puede cerrar la conexi�n la Base De Datos", "Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		}

		if (btnLimpiar.equals(arg0.getSource())){
			// Seleccionar contenido del textField Nombre y limpiarlo
			txtNombre.selectAll();
			txtNombre.setText("");

			// Seleccionar contenido del textField APELLIDOS Y limpiarlo
			txtApellidos.selectAll();
			txtApellidos.setText("");
		}
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		if (this.isActive()) {
			this.setVisible(false);
			new MenuPrincipal();
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {}

	@Override
	public void windowClosed(WindowEvent arg0) {}

	@Override
	public void windowDeactivated(WindowEvent arg0) {}

	@Override
	public void windowDeiconified(WindowEvent arg0) {}

	@Override
	public void windowIconified(WindowEvent arg0) {}

	@Override
	public void windowOpened(WindowEvent arg0) {}
}