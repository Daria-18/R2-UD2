package apartado1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class H2Prueba {
	
  private static final String ARCHIVO_PROPERTIES = "properties.ini";
  private static final String ARCHIVO_BD = "test";

	public static void main(String[] args) {
		
		Properties properties = new Properties();		
		File propertiesFile = new File(ARCHIVO_PROPERTIES);

		//Creación si el archivo no existe
		if(!propertiesFile.exists()) {
			System.out.println("Se creará un archivo properties con nombre:" + ARCHIVO_PROPERTIES);
			try {
				crearProperties(propertiesFile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		//Carga del fichero properties
		try(FileInputStream inputStream = new FileInputStream(ARCHIVO_PROPERTIES)){
			properties.load(inputStream);
		}catch (IOException e) {
			System.out.println("Error en lectura");
		}
		
		//Para la concatenación
		// Valor archivo
		String pathIni = properties.getProperty("path");
		String userIni = properties.getProperty("user");
		String passIni = properties.getProperty("password");
		
		//bloque try de la conexión con H2
		try {
			//H2 crea directamente el archivo en la conexión si no existe, incluyendo carpetas
			// No se requiere usuario y contraseña 
			//Connection connection = DriverManager.getConnection("jdbc:h2:./db/test");
			Connection connection = DriverManager.getConnection("jdbc:h2:"+pathIni+ARCHIVO_BD); //Resultado igual al connection superior
			System.out.println("Conexión correcta\n");
			
			crearTabla(connection);
			insertarTablaDefault(connection);
			recuperarTabla(connection);
			
			connection.close();
		} catch (SQLException e) {
			System.out.println("Error en la conexión a la BBDD");
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
	
	
	/**
	 * Crea un archivo properties con path, user y password para el conector jdbc
	 * @param Archivo a crear
	 * @throws IOException en la creacion del fichero
	 */
	
	public static void crearProperties(File archivo) throws IOException {
		Properties pr = new Properties();
		pr.setProperty("path", "./db/"); //Directorio del proyecto
		pr.setProperty("user", "dam2"); //user placeholder
		pr.setProperty("password", "asdf.1234");
		
		FileOutputStream salida = new FileOutputStream(archivo);
		pr.store(salida, "Creacion del archivo: "+archivo.toString());
		System.out.println("Archivo " + archivo.toString() + " creado correctamente");
	}

}
