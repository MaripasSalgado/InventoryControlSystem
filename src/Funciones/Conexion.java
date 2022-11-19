package Funciones;
import static Funciones.Sistema.Estado;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

// codigo para obtener y realizar la conexion con la base de datos
public class Conexion {
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String usuario = "root"; 
    public static final String contraseña = "";
    public static final String URL ="jdbc:mysql://localhost:3306/inventario?autoReconnect=true&useSSL=false";
    
    private Connection conn;
    
    public Conexion(){
        conn = null;
    }
// Metodo para crear conexion con la base de datos    
    public Connection getConnection()
    {
        try 
        {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(URL,usuario,contraseña);
        } catch (ClassNotFoundException | SQLException e) 
        {
                Sistema ingreso = new Sistema();
                ingreso.Estado = Estado.Desconectada;
            JOptionPane.showMessageDialog(null,"Error al conectar con la base de datos" + "\n" + "La base de datos se encuentra "+ ingreso.Estado);
            System.exit(0);
        }
        return conn;
    }
    // Metodo que Cierra la conexion con la base de datos
    public void desconectar(){
        try {
            conn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(),"Error al cerrar la conexion con la base de datos", JOptionPane.ERROR_MESSAGE);
        }
    }
    
}
