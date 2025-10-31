package apartado1;

import java.sql.*;


public class BaseSQLite {

	public static void main(String[] args){
		Connection miConexion=null;
		try {
			String ruta="jdbc:sqlite:./db/contadores.db";
			 miConexion= DriverManager.getConnection(ruta);
			 System.out.println("Conexion establecida");
			 
			 String crear="CREATE TABLE IF NOT EXISTS contadores(nombre varchar(10) primary key, cuenta int)";
			 String insertar="INSERT INTO contadores VALUES(?,?)";
			 String leer="SELECT * FROM contadores";
			 PreparedStatement stmt = miConexion.prepareStatement(crear);
	         stmt.executeUpdate();
	         System.out.println("Tabla creada");
			 
			 
			 for(int i=0;i<100;i++) {
				 String nombre="contador"+i;
				 PreparedStatement statement=miConexion.prepareStatement(insertar);
				 statement.setString(1,nombre);
				 statement.setInt(2, i);
				 statement.executeUpdate();
			 }
			 System.out.println("Tabla rellenada correctamente");
			 
			 PreparedStatement ps=miConexion.prepareStatement(leer);
			 ResultSet rs=ps.executeQuery();
			 while(rs.next()) {
				 String nombre=rs.getString("nombre");
				 int cont=rs.getInt("cuenta");
				 
				 System.out.println("nombre= "+nombre+" cuenta= "+cont);
			 }
			 
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}
	}

}
