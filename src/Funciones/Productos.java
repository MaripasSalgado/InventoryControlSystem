package Funciones;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Productos {
    private PreparedStatement PS;
    private ResultSet RS;
    private final Conexion CN;
    private DefaultTableModel DT;
    private final String SQL_INSERT_PRODUCTOS = "INSERT INTO producto (pro_codigo,pro_descripcion) values (?,?)";
    private final String SQL_SELECT_PRODUCTOS = "SELECT *FROM producto";
    
// Metodo que establece la conexion con la base de datos de MySQL
    public Productos(){
        PS = null;
        CN = new Conexion();
    }
// Metodo que crea la tabla en la que se desplegaran los datos durante la ejecucion del programa
    private DefaultTableModel setTitulosProductos(){
        DT = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
        };
        DT.addColumn("Código");
        DT.addColumn("Descripción");
        return DT;
    }
// Metodo que obtiene los datos de la Base de datos de MySQL y los despliega en la tabla anteriormente creada
    public DefaultTableModel getDatosProductos(){
        try {
            setTitulosProductos();
            PS = CN.getConnection().prepareStatement(SQL_SELECT_PRODUCTOS);
            RS = PS.executeQuery();
            Object[] fila = new Object[2];
            while(RS.next()){
                fila[0] = RS.getString(1);
                fila[1] = RS.getString(2);  
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
// Metodo dedidaco para registrar algun producto nuevo en el sistema y en la base de datos MySQL
    public int registrarProducto(String codigo, String descripcion){
        int res=0;
        try {
            PS = CN.getConnection().prepareStatement(SQL_INSERT_PRODUCTOS);
            PS.setString(1, codigo);
            PS.setString(2, descripcion);
            res = PS.executeUpdate();
            if(res > 0){
                JOptionPane.showMessageDialog(null, "Producto registrado con éxito.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No se pudo registrar el producto.");
            System.err.println("Error al registrar el producto." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
        return res;
    }
    
// Metodo dedicado a insertar el producto en la tabla de inventario y la base de datos MySQL
    public void insertarProductoInventario(String codigoProducto){
        int res;
        try {
            PS = CN.getConnection().prepareStatement("CALL NUEVO_PRODUCTO('"+codigoProducto+"')");
            PS.executeQuery();
        } catch (SQLException e) {
            System.err.println("Error al insertar registro en la tabla inventario." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
    }
    
//Metodo dedicado a verificar el codigo en el metodo de inventario y la base da datos MySQL
    public int verificarCodigoInventario(String codigo){
        int res=0;
        try {
            PS = CN.getConnection().prepareStatement("SELECT count(inv_pro_codigo) from inventario where inv_pro_codigo='"+codigo+"'");
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
    
// Metodo dedicado a actualizar los productos en el sistema y la base de datos MySQL
    public int actualizarProducto(String codigo, String descripcion, String codigo_old){
        String SQL = "UPDATE producto SET pro_codigo='"+codigo+"',pro_descripcion='"+descripcion+"' WHERE pro_codigo='"+codigo_old+"'";
        int res=0;
        try {
            PS = CN.getConnection().prepareStatement(SQL);
            res = PS.executeUpdate();
            if(res > 0){
                JOptionPane.showMessageDialog(null, "Producto actualizado con éxito");
            }
        } catch (SQLException e) {
            System.err.println("Error al modificar los datos del cliente." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
        return res;
    }
//Metodo dedicado a eliminar productos del programa y la base de datos MySQL
    public int eliminarProducto(String codigo){
        String SQL = "DELETE from producto WHERE pro_codigo ='"+codigo+"'";
        int res=0;
        try {
            PS = CN.getConnection().prepareStatement(SQL);
            res = PS.executeUpdate();
            if(res > 0){
                JOptionPane.showMessageDialog(null, "Producto eliminado con éxito");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "No es posible eliminar el producto.");
            System.err.println("Error al eliminar producto." +e.getMessage());
        } finally{
            PS = null;
            CN.desconectar();
        }
        return res;
    }
    
}
