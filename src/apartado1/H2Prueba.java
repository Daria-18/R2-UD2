package apartado1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class H2Prueba {

	public static void main(String[] args) {

		try {
			//H2 crea directamente el archivo en la conexión si no existe, incluyendo carpetas
			Connection connection = DriverManager.getConnection("jdbc:h2:./db/test");
			System.out.println("Conexión correcta\n");
			
			crearTabla(connection);
			//insertarTablaDefault(connection);
			recuperarTabla(connection);
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Método que crea la estructura de la tabla que llamaremos contadores.
	 * @param Conexión SQL
	 * @throws SQLException
	 */
	
	public static void crearTabla(Connection connection) throws SQLException {
		
		String sqlCreacion = "create table if not exists contadores("
				+ "nombre varchar(255),"
				+ "cuenta int);";			
		
		PreparedStatement creacion = connection.prepareStatement(sqlCreacion);
		creacion.execute();	
		
		System.out.println("Tabla creada correctamente");
		
	}
	
	/**
	 * Método que inserta en la tabla creada anteriormente, los campos nombre y cuenta usando un iterador, que hace 
	 * aumentos de 3 en cada cuenta
	 * @param Conexión SQL
	 * @throws SQLException
	 */
	
	public static void insertarTablaDefault(Connection connection) throws SQLException {
		 String sqlInsercion = "insert into contadores values(?,?)";
		 
			for (int i=0; i<100;i++) {
				PreparedStatement insercion = connection.prepareStatement(sqlInsercion);
				
				insercion.setString(1, "Nombre: " + i);
				insercion.setInt(2,i*3);
				
				insercion.execute();
				System.out.println("Inserción correcta");
			}
		
	}
	
	/**
	 * Método que recupera todos los datos de la tabla contadores, y los trata como un ResultSet que después se imprimirán en pantalla
	 * @param Conexión SQL
	 * @throws SQLException
	 */
	
	public static void recuperarTabla(Connection connection) throws SQLException{
		String selectQuery = "SELECT * FROM contadores";
        PreparedStatement statement = connection.prepareStatement(selectQuery);

        // execute the query and get the result set
        ResultSet resultSet = statement.executeQuery();
        System.out.println("Obteniendo los resultados de la tabla:");

        // iterate through the result set and print the data
        while (resultSet.next()) {
        	String nombreAux = resultSet.getString("nombre");
            int contadorAux = resultSet.getInt("cuenta");			
         // print the retrieved data
            System.out.println(nombreAux + " Cuenta: " + contadorAux);
        }
		
	}

}
