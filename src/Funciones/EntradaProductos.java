package Funciones;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;


public class EntradaProductos {
    private PreparedStatement PS;
    private ResultSet RS;
    private final Conexion CN;
    private DefaultTableModel DT;
    private final String SQL_INSERT_ENTRADA = "INSERT INTO entrada (ent_factura, ent_pro_codigo, ent_fecha, ent_cantidad) values (?,?,?,?)";
    private final String SQL_SELECT_ENTRADA = "SELECT ent_factura, ent_fecha, ent_pro_codigo, pro_descripcion, ent_cantidad FROM entrada INNER JOIN producto ON ent_pro_codigo = pro_codigo";
    
// crea conexion con la base de datos
    public EntradaProductos(){
        PS = null;
        CN = new Conexion();
    }
// Metodo que crea la tabla que se desplegara durante el programa
    private DefaultTableModel setTitulosEntrada(){
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
    
// Metodo que obtiene los datos de la base de datos de MySQL y los lista en la tabla anteriormente creada
    public DefaultTableModel getDatosEntradas(){
        try {
            setTitulosEntrada();
            PS = CN.getConnection().prepareStatement(SQL_SELECT_ENTRADA);
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
    
// Metodo para registrar la entrada de un producto en la base de datos
    public int registrarEntrada(String nfactura, String codigo, Date fecha, int cantidad){
        int res=0;
        try {
            PS = CN.getConnection().prepareStatement(SQL_INSERT_ENTRADA);
            PS.setString(1, nfactura);
            PS.setString(2, codigo);
            PS.setDate(3, fecha);
            PS.setInt(4, cantidad);
            res = PS.executeUpdate();
            if(res > 0){
                JOptionPane.showMessageDialog(null, "Entrada realizada con éxito.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo registrar la entrada.");
            System.err.println("Error al registrar la entrada." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
        return res;
    }
}
