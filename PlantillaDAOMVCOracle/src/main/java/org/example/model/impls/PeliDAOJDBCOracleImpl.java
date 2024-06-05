package org.example.model.impls;

import org.example.model.daos.DAO;
import org.example.model.entities.Pelicula;
import org.example.model.exceptions.DAOException;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PeliDAOJDBCOracleImpl implements DAO<Pelicula> {
    static final String url = "jdbc:oracle:thin:@//localhost:1521/xe";
    static final String usuari = "C##HR";
    static final String pas = "HR";



    public void creaTaula() throws DAOException {
        //Declaració de variables del mètode
        Connection con = null;
        CallableStatement cstmt = null;
        //Accés a la BD usant l'API JDBC
        try {
            con = DriverManager.getConnection(url, usuari, pas);
            String crea = "{call crea_taula_pelis}";
            cstmt = con.prepareCall (crea);
            cstmt.execute();

        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                if (cstmt != null) cstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }

        }
    }



    public void inserta(Pelicula peli) throws DAOException {
        String sql = "INSERT INTO PELICULES (id, titol, nota, estat) VALUES (?, ?, ?, ?)";
        try(Connection conn = DriverManager.getConnection(url, usuari, pas);
           PreparedStatement st = conn.prepareStatement(sql);
        ) {
            st.setLong(1, peli.getId());
            st.setString(2, peli.getTitol());
            st.setDouble(3, peli.getNota());
            st.setObject(4, peli.getEstat());
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DAOException(1);
        }
    }

    public void update (Pelicula peli) throws DAOException {
        String sql = "UPDATE PELICULES SET titol = ?,nota = ?, estat = ?  WHERE id = ?";
        try(Connection conn = DriverManager.getConnection(url, usuari, pas);
            PreparedStatement st = conn.prepareStatement(sql);
        ) {
            st.setString(1, peli.getTitol());
            st.setDouble(2, peli.getNota());
            st.setObject(3, peli.getEstat());
            st.setObject(4, peli.getId());
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DAOException(1);
        }
    }

    public void borra (Pelicula peli) throws DAOException {
        String sql = "DELETE FROM PELICULES WHERE id = ?";
        try(Connection conn = DriverManager.getConnection(url, usuari, pas);
            PreparedStatement st = conn.prepareStatement(sql);
        ) {

            st.setObject(1, peli.getId());
            st.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DAOException(1);
        }
    }

    public Long getId(Pelicula peli) throws DAOException {
        Long id = null;
        String sql = "SELECT id FROM PELICULES WHERE titol=? AND nota=? AND estat=?";
        try(Connection conn = DriverManager.getConnection(url, usuari, pas);
            PreparedStatement st = conn.prepareStatement(sql);
        ) {
            st.setString(1, peli.getTitol());
            st.setDouble(2, peli.getNota());
            st.setObject(3, peli.getEstat());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                id = rs.getLong("id");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new DAOException(1);
        }
        return id;
    }
    public Long maxId() throws DAOException {
        //Declaració de variables del mètode
        Connection con = null;
        CallableStatement cs = null;
        //Accés a la BD usant l'API JDBC
        Long maxId;
        try {
            con = DriverManager.getConnection(url, usuari, pas);
            cs = con.prepareCall("BEGIN ? := max_id; END;");
            cs.registerOutParameter(1, java.sql.Types.NUMERIC);
            cs.execute();
            maxId = (long) cs.getInt(1);

        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                if (cs != null) cs.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }

        }
        return maxId;
    }
    @Override
    public Pelicula get(Long id) throws DAOException {

        //Declaració de variables del mètode
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Pelicula peli = null;

        //Accés a la BD usant l'API JDBC
        try {

            con = DriverManager.getConnection(url, usuari, pas);


//            st = con.prepareStatement("SELECT * FROM estudiant WHERE id=?;");
            st = con.createStatement();

//            st = con.prepareStatement("SELECT * FROM estudiant WHERE id=?;");
//            st.setLong(1, id);
            rs = st.executeQuery("SELECT * FROM PELICULES;");
                peli = new Pelicula(rs.getLong(1), rs.getString(2));
                st.close();
            if (rs.next()) {
                peli = new Pelicula(Long.valueOf(rs.getString(1)), rs.getString(2));
            }
        } catch (SQLException throwables) {
            throw new DAOException(1);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }

        }
        return peli;
    }

    @Override
    public List<Pelicula> getAll() throws DAOException {
        //Declaració de variables del mètode
        List<Pelicula> pelis = new ArrayList<>();

        //Accés a la BD usant l'API JDBC
        try (Connection con = DriverManager.getConnection(url, usuari, pas);
             PreparedStatement st = con.prepareStatement("SELECT * FROM PELICULES");
             ResultSet rs = st.executeQuery();
        ) {

            while (rs.next()) {
                pelis.add(new Pelicula(rs.getLong("id"), rs.getString("titol"),rs.getDouble("nota"), rs.getObject("estat"),
                        new TreeSet<Pelicula.Fitxa>()));
            }
        } catch (SQLException throwables) {
            int tipoError = throwables.getErrorCode();
            //System.out.println(tipoError+" "+throwables.getMessage());
            switch(throwables.getErrorCode()){
                case 17002: //l'he obtingut posant un sout en el throwables.getErrorCode()
                    tipoError = 0;
                    break;
                default:
                    tipoError = 1;  //error desconegut
            }
            throw new DAOException(tipoError);
        }


        return pelis;
    }

    @Override
    public void save(Pelicula obj) throws DAOException {

    }
}
