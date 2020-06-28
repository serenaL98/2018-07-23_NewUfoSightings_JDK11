package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Collegamento;
import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {

	public List<Sighting> loadAllSightings() {
		String sql = "SELECT * FROM sighting";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public List<State> loadAllStates() {
		String sql = "SELECT * FROM state";
		List<State> result = new ArrayList<State>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				result.add(state);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}

	public List<String> prendiForme(int anno){
		String sql = "SELECT DISTINCT s.shape forme" + 
				" FROM sighting s" + 
				" WHERE YEAR(s.datetime) = ? " + 
				" ORDER BY s.shape";
		List<String> lista = new ArrayList<>();
		
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, anno);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				String i = res.getString("forme");
				
				lista.add(i);
			}
			
			con.close();
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("ERROE DB: impossibile prendere le forme.");
		}
		
		return lista;
	}
	
	public List<Collegamento> prendiCollegamenti(String forma, int anno, Map<String, State> mappa){
		String sql = "SELECT s.state sta, s1.state top, COUNT(s.datetime) peso" + 
				" FROM sighting s,  sighting s1" + 
				" WHERE  s.shape = ? " + 
				"		AND s.shape = s1.shape" + 
				"		AND YEAR(s.datetime) = ? " + 
				"		AND YEAR(s.datetime) = YEAR(s1.datetime)" + 
				"		AND s.state< s1.state" + 
				" GROUP BY s.state, s1.state" + 
				" ORDER BY s.state, s1.state";
		List<Collegamento> lista = new ArrayList<>();
		
		try {
			Connection con = DBConnect.getConnection();
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, forma);
			st.setInt(2, anno);
			ResultSet res = st.executeQuery();
			
			while(res.next()) {
				State st1 = mappa.get(res.getString("sta").toUpperCase());
				State st2 = mappa.get(res.getString("top").toUpperCase());
				Integer peso = res.getInt("peso");
				
				Collegamento copl = new Collegamento(st1, st2, peso);
				
				lista.add(copl);
			}
			
			con.close();
			
		}catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("ERROE DB: impossibile prendere i collegamenti fra gli stati.");
		}
		
		return lista;
	}
}

