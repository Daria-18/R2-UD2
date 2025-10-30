package apartado1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ContadorEnBD {

	public static void main(String[] args) {
		final String sqlConsulta = "SELECT cuenta FROM contadores WHERE nombre=?;";
		final String sqlActualización = "UPDATE contadores SET cuenta=? WHERE nombre=?;";
		final String claveContador = "contador1";

		try {
			Connection connection = DriverManager.getConnection("jdbc:mariadb://localhost:3306/contadores",
					"contadores", "987654321");
			PreparedStatement consulta = connection.prepareStatement(sqlConsulta);
			PreparedStatement actualización = connection.prepareStatement(sqlActualización);
			int cuenta = 0;

			consulta.setString(1, claveContador);
			actualización.setString(2, claveContador);
			for (int i = 0; i < 1000; i++) {
				ResultSet res = consulta.executeQuery();
				if (res.next()) {
					cuenta = res.getInt(1) + 1;
					actualización.setInt(1, cuenta);
					actualización.executeQuery();
				}
				// else break;
				else
					System.out.println("Error");
				// if (i%10==0) System.out.println(i/10 + "%");
			}
			System.out.println("Valor final: " + cuenta);

		} // try
		catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // main

} // class
