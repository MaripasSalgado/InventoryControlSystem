package Funciones;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class SalidaProductos {


    public static PreparedStatement PS;


    public static ResultSet RS;


    public static Conexion CN;


    public static DefaultTableModel DT;

    public static final String SQL_INSERT_SALIDA = "INSERT INTO salida (sal_factura, sal_pro_codigo, sal_fecha, sal_cantidad) values (?,?,?,?)";


    public static String SQL_SELECT_SALIDA = "SELECT sal_factura, sal_fecha, sal_pro_codigo, pro_descripcion, sal_cantidad FROM salida INNER JOIN producto ON sal_pro_codigo = pro_codigo";

// Metodo que crea la conexion con la base de datos MySQL
    public SalidaProductos(){
        PS = null;
        CN = new Conexion();
    }
// Metodo dedicado a crear la tabla en la que se desplegaran los datos
    private DefaultTableModel setTitulosSalida(){
        DT = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        DT.addColumn("N° de Factura");
        DT.addColumn("Fecha");
        DT.addColumn("Código de Producto");
        DT.addColumn("Descripción");
        DT.addColumn("Cantidad");
        return DT;
    }
    
//Metodo dedicado a obtener los datos de la base de datos MySQL y listarlos en la tabla anteriormente creada
    public DefaultTableModel getDatosSalida(){
        try {
            setTitulosSalida();
            PS = CN.getConnection().prepareStatement(SQL_SELECT_SALIDA);
            RS = PS.executeQuery();
            Object[] fila = new Object[5];
            while(RS.next()){
                fila[0] = RS.getString(1);
                fila[1] = RS.getDate(2);
                fila[2] = RS.getString(3);
                fila[3] = RS.getString(4);
                fila[4] = RS.getInt(5);
                DT.addRow(fila);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los datos."+e.getMessage());
        } finally{
            PS = null;
            RS = null;
            CN.desconectar();
        }
        return DT;
    }
    
// Metodo dedicado a registrar la salida de algun producto
    public int registrarSalida(String nfactura, String codigo, Date fecha, int cantidad){
        int res=0;
        try {
            PS = CN.getConnection().prepareStatement(SQL_INSERT_SALIDA);
            PS.setString(1, nfactura);
            PS.setString(2, codigo);
            PS.setDate(3, fecha);
            PS.setInt(4, cantidad);
            res = PS.executeUpdate();
            if(res > 0){
                JOptionPane.showMessageDialog(null, "Salida realizada con éxito.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo registrar la salida.");
            System.err.println("Error al registrar la salida." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
        return res;
    }
    
// Metodo dedicado a verificar el stock de los productos en el Inventario
    public int verificarStock(String codigo){
        int res=0;
        try {
            PS = CN.getConnection().prepareStatement("SELECT inv_stock from inventario where inv_pro_codigo='"+codigo+"'");
            RS = PS.executeQuery();
            
            while(RS.next()){
                res = RS.getInt(1);
            }
            
        } catch (SQLException e) {
            System.err.println("Error al devolver cantidad de registros." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
        return res;
    }
}
