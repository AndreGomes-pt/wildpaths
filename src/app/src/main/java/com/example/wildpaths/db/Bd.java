package com.example.wildpaths.db;

import com.example.wildpaths.trilhos.selecionado.comentario.Comentario;
import com.example.wildpaths.trilhos.trilho.Trilho;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bd {

    private static final String URL = "jdbc:mysql://89.214.113.2 6/virtuals_andre";
    private static final String USER = "virtuals_andre";
    private static final String PASSWORD = "7fa(.ZQj67AP";

    private static Connection connection;

    public Bd() {
    }

    // Get Trails
    public List<Trilho> getTrails(int userId,byte filtro) {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("SUCCESS");
        } catch (Exception erro) {
            System.out.println("Erro ao conectar com a base de dados: " + erro);
        }

        Trilho dadosTrilhos[] = null;
        int nTrilhosExistente = 0;
        ResultSet result = null;
        String sql = "SELECT * FROM trail";

        switch (filtro){
            case 0:
                try {
                    result = connection.prepareStatement("SELECT COUNT(*) FROM trail ").executeQuery();
                    if (result.next()) {
                        nTrilhosExistente = result.getInt("COUNT(*)");
                    }

                    dadosTrilhos = new Trilho[nTrilhosExistente];
                }catch (Exception e){
                }
                break;

            case 1:
                try {
                    result = connection.prepareStatement("SELECT COUNT(*) FROM trail WHERE userId = '"+userId+"'").executeQuery();
                    if (result.next()) {
                        nTrilhosExistente = result.getInt("COUNT(*)");
                    }

                    dadosTrilhos = new Trilho[nTrilhosExistente];

                    sql = sql + " WHERE userId = '" + userId + "'" ;

                }catch (Exception e){
                }
                break;

            case 2:
                Trilho [] dadosTrilhosFav = null;
                try {
                    int nTrilhosFavoritos = 0;
                    System.out.println("Id do CORNOOOOOOOOOOOOO : " +userId);
                    ResultSet resultFav = connection.prepareStatement("SELECT COUNT(*) FROM favtrail WHERE userId = '" + userId + "'").executeQuery();

                    if (resultFav.next()) {
                        nTrilhosFavoritos = resultFav.getInt("COUNT(*)");
                        dadosTrilhosFav = new Trilho[nTrilhosFavoritos];
                    }
                    System.out.println("N Trilhos Favoritos : " + nTrilhosFavoritos);

                    //Id dos trilhos marcados como fav pelo user
                    int [] id = new int[nTrilhosFavoritos];
                    resultFav = connection.prepareStatement("SELECT * FROM favtrail WHERE userId = '" + userId + "'").executeQuery();
                    int temp = 0;
                    while(resultFav.next()){
                        id [temp] = resultFav.getInt("trailId");
                        temp++;
                    }

                    //Trilhos comentarios
                    for(int x = 0;x < nTrilhosFavoritos;x++){
                        resultFav = connection.prepareStatement("SELECT * FROM trail WHERE trailId = '" + String.valueOf(id[x])+"'").executeQuery();
                        if(resultFav.next()){
                            byte [] img1 = null, img2 = null, img3 = null, img4 = null;

                            int tempTimer = 0;
                            // Imagen 1 do trilho
                            resultFav.getBlob("trailPic1");
                            if (!resultFav.wasNull()) {
                                img1 = new byte[(int) resultFav.getBlob("trailPic1").length()];
                                for (byte b : resultFav.getBytes("trailPic1")) {
                                    img1[tempTimer] = b;
                                    tempTimer++;
                                }
                                tempTimer = 0;
                            }

                            // Imagen 2 do trilho
                            resultFav.getBlob("trailPic2");
                            if (!resultFav.wasNull()) {
                                img2 = new byte[(int) resultFav.getBlob("trailPic2").length()];
                                for (byte b : resultFav.getBytes("trailPic2")) {
                                    img2[tempTimer] = b;
                                    tempTimer++;
                                }
                                tempTimer = 0;
                            }

                            // Imagen 3 do trilho
                            resultFav.getBlob("trailPic3");
                            if (!resultFav.wasNull()) {
                                img3 = new byte[(int) resultFav.getBlob("trailPic3").length()];
                                for (byte b : resultFav.getBytes("trailPic3")) {
                                    img3[tempTimer] = b;
                                    tempTimer++;
                                }
                                tempTimer = 0;
                            }

                            // Imagen 4 do trilho
                            resultFav.getBlob("trailPic4");
                            if (!resultFav.wasNull()) {
                                img4 = new byte[(int) resultFav.getBlob("trailPic4").length()];
                                for (byte b : resultFav.getBytes("trailPic4")) {
                                    img4[tempTimer] = b;
                                    tempTimer++;
                                }
                            }

                            dadosTrilhosFav[x] = Trilho.GenTrilho.gen()
                                    .setIdTrilho(resultFav.getInt("trailId"))
                                    .setIdCriador(resultFav.getInt("userId"))
                                    .setTituloTrilho(resultFav.getString("trailTitle"))
                                    .setCatgTrilho(resultFav.getString("trailCatg").toCharArray())
                                    .setDescricaoTrilho(resultFav.getString("description"))
                                    .setDistanciaTrilho(resultFav.getDouble("totalDistance"))
                                    .setImgsTrilho(img1, img2, img3, img4)
                                    .setLatitude(result.getDouble("latitude"))
                                    .setLongitude(result.getDouble("longitude"))
                                    .output();
                        }
                    }
                    resultFav.close();
                }catch (Exception e){
                    System.out.printf("Erro ao conseguir os trilhos favoritos : " + e);
                }
                return Arrays.asList(dadosTrilhosFav);
        }

        try {
            result = connection.prepareStatement(sql).executeQuery();

            for (int x = 0; x < nTrilhosExistente; x++) {
                byte[] imgTrilho1 = null, imgTrilho2 = null, imgTrilho3 = null, imgTrilho4 = null;

                if (result.next()) {

                    int tempTimer = 0;
                    // Imagen 1 do trilho
                    result.getBlob("trailPic1");
                    if (!result.wasNull()) {
                        imgTrilho1 = new byte[(int) result.getBlob("trailPic1").length()];
                        for (byte b : result.getBytes("trailPic1")) {
                            imgTrilho1[tempTimer] = b;
                            tempTimer++;
                        }
                        tempTimer = 0;
                    }

                    // Imagen 2 do trilho
                    result.getBlob("trailPic2");
                    if (!result.wasNull()) {
                        imgTrilho2 = new byte[(int) result.getBlob("trailPic2").length()];
                        for (byte b : result.getBytes("trailPic2")) {
                            imgTrilho2[tempTimer] = b;
                            tempTimer++;
                        }
                        tempTimer = 0;
                    }

                    // Imagen 3 do trilho
                    result.getBlob("trailPic3");
                    if (!result.wasNull()) {
                        imgTrilho3 = new byte[(int) result.getBlob("trailPic3").length()];
                        for (byte b : result.getBytes("trailPic3")) {
                            imgTrilho3[tempTimer] = b;
                            tempTimer++;
                        }
                        tempTimer = 0;
                    }

                    // Imagen 4 do trilho
                    result.getBlob("trailPic4");
                    if (!result.wasNull()) {
                        imgTrilho4 = new byte[(int) result.getBlob("trailPic4").length()];
                        for (byte b : result.getBytes("trailPic4")) {
                            imgTrilho4[tempTimer] = b;
                            tempTimer++;
                        }
                    }

                    dadosTrilhos[x] = Trilho.GenTrilho.gen()
                            .setIdTrilho(result.getInt("trailId"))
                            .setIdCriador(result.getInt("userId"))
                            .setTituloTrilho(result.getString("trailTitle"))
                            .setCatgTrilho(result.getString("trailCatg").toCharArray())
                            .setDescricaoTrilho(result.getString("description"))
                            .setDistanciaTrilho(result.getDouble("totalDistance"))
                            .setImgsTrilho(imgTrilho1, imgTrilho2, imgTrilho3, imgTrilho4)
                            .setLatitude(result.getDouble("latitude"))
                            .setLongitude(result.getDouble("longitude"))
                            .output();
                }
            }
            result.close();
        } catch (Exception erro) {
            System.out.println("Erro ao conseguir os trilhos : " + erro);
            return null;
        }
        try{
            connection.close();
        }catch (Exception e ){
        }

        return Arrays.asList(dadosTrilhos);
    }

    // Get Coments
    public List<Comentario> getComments(int trailId) {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("SUCCESS");
        } catch (Exception erro) {
            System.out.println("Erro ao conectar com a base de dados: " + erro);
        }

        int nComments = 0;
        List<Comentario> dadosComentarios = new ArrayList<>();

        try {
            ResultSet resultCount = connection.prepareStatement("SELECT COUNT(*) FROM comment WHERE trailId = '"+trailId+"'")
                    .executeQuery();
            if (resultCount.next()) {
                nComments = resultCount.getInt("COUNT(*)");
            }

            ResultSet result = connection.prepareStatement("SELECT * FROM comment WHERE trailId = '"+trailId+"'").executeQuery();
            ResultSet userData = null;

            for (int x = 0; x < nComments; x++) {

                if (result.next()) {
                    int idCreator = result.getInt("userId");

                    userData = connection
                            .prepareStatement("SELECT userName,userPic FROM user WHERE userId = " + idCreator)
                            .executeQuery();

                    if (userData.next()) {
                        byte[] userPic = null;
                        try{
                            userPic = new byte[(int) userData.getBlob("userPic").length()];
                            int tempTimer = 0;
                            for (byte b : userData.getBytes("userPic")) {
                                userPic[tempTimer] = b;
                                tempTimer++;
                            }
                        }catch (Exception e){
                            System.out.println("O UTILIZADOR QUE POSTOU O COMENTARIO NÃO TEM FOTO DE PERFIL");
                        }

                        dadosComentarios.add(Comentario.GenComentario.gen()
                                .setnomeUtilizador(userData.getString("userName"))
                                .setFtUtilizador(userPic)
                                .setConteudo(result.getString("content"))
                                .output());
                    }
                }

            }
        } catch (Exception erro) {
            System.out.println("Erro ao conseguir os comentarios : " + erro);
        }

        return dadosComentarios;
    }

    // Insert Trail
    public void insertTrail(String trailTitle, String description, double totalDistance, char[] trailCatg, byte[] img1,
            byte[] img2, byte[] img3, byte[] img4, int userId, double latitude,double longitude) {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("SUCCESS");
        } catch (Exception erro) {
            System.out.println("Erro ao conectar com a base de dados: " + erro);
        }

        String catg = "";
        for (char c : trailCatg) {
            catg = catg + c;
        }

        String sql = "INSERT INTO trail (userId,trailTitle,trailCatg,description,totalDistance,trailPic1,trailPic2,trailPic3,trailPic4,latitude,longitude) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, trailTitle);
            preparedStatement.setString(3, catg);
            preparedStatement.setString(4, description);
            preparedStatement.setDouble(5, totalDistance);

            preparedStatement.setBytes(6, img1);

            preparedStatement.setBytes(7, img2);

            preparedStatement.setBytes(8, img3);

            preparedStatement.setBytes(9, img4);

            preparedStatement.setDouble(10,latitude);

            preparedStatement.setDouble(11,longitude);

            preparedStatement.execute();

            preparedStatement.close();

        } catch (Exception erro) {
            System.out.println("Erro ao inserir trilho : " + erro);
        }
        try{
            connection.close();
        }catch (Exception e ){
        }
    }

    // Insert Comments
    public void insertComment(int userId, int trailId, String content) {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("SUCCESS");
        } catch (Exception erro) {
            System.out.println("Erro ao conectar com a base de dados: " + erro);
        }

        String sql = "INSERT INTO comment (userId,trailId,content) VALUES (?,?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, userId);
            preparedStatement.setInt(2, trailId);
            preparedStatement.setString(3, content);

            preparedStatement.execute();

            preparedStatement.close();
        } catch (Exception e) {
            System.out.println("Erro ao inserir comentario : " + e);
        }

        try{
            connection.close();
        }catch (Exception e ){
        }
    }

    // Insert User
    public void insertUser(String userName, String email, String userPw,String bio, byte[] userBcPic, byte[] userPic) {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("SUCCESS");
        } catch (Exception erro) {
            System.out.println("Erro ao conectar com a base de dados: " + erro);
        }

        String sql = "INSERT INTO user (userName,email,userPw,userBcPic,userPic,bio) VALUES (?,?,?,?,?,?)";
        String sqlVerify1 = "SELECT userId FROM user WHERE userName = '"+userName+"'";
        String sqlVerify2 = "SELECT userId FROM user WHERE email = '"+email+"'";

        try {
            boolean temp1 = false, temp2 = false;

            //Verificação da existencia de um registo
            ResultSet userNameVerification = connection.prepareStatement(sqlVerify1).executeQuery();
            try{
                if(userNameVerification.next()){
                    System.out.println("EXISTEEEE");
                }else{
                    userNameVerification.close();
                    temp1 = true;
                }
            }catch (Exception e){
            }

            ResultSet emailVerification = connection.prepareStatement(sqlVerify2).executeQuery();
            try{
                if(emailVerification.next()){
                    System.out.println("EXISTEEEEE");
                }else{
                    emailVerification.close();
                    temp2 = true;
                }
            }catch (Exception e){
            }

            if(temp1 == true && temp2 == true){
                System.out.println("A inserir utilzador");
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                preparedStatement.setString(1, userName);
                preparedStatement.setString(2, email);
                preparedStatement.setString(3, userPw);
                preparedStatement.setBytes(4, userBcPic);
                preparedStatement.setBytes(5, userPic);
                preparedStatement.setString(6,bio);

                preparedStatement.execute();

            }else{
                System.out.println("NAO FUNFAAASAAA");
            }

        } catch (Exception e) {
            System.out.println("Erro ao inserir utilizador : " + e);
        }

        try{
            connection.close();
        }catch (Exception e ){
        }

    }

    // Insert FavTrail
    public void insertFav(int trailId, int userId) {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("SUCCESS");
        } catch (Exception erro) {
            System.out.println("Erro ao conectar com a base de dados: " + erro);
        }

        String sql = "INSERT INTO favtrail (trailId, userId) VALUES (?,?)";

        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, trailId);
            preparedStatement.setInt(2, userId);

            preparedStatement.execute();

            preparedStatement.close();
        } catch (Exception e) {
            System.out.println("Erro ao inserir o trilho como favorito : " + e);
        }

        try{
            connection.close();
        }catch (Exception e ){
        }
    }

    // Get specific value
    public Object getValue(String table, String columnLabel, String param1, String param2, String param3, String param4) {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("SUCCESS");
        } catch (Exception erro) {
            System.out.println("Erro ao conectar com a base de dados: " + erro);
        }

        ResultSet result = null;


        String sql = "SELECT * FROM " + table;
        if (param1 != null && param2 != null) {
            sql = sql + " WHERE " + param1 + " = '" + param2 + "'";
        }
        if(param3 != null && param4 != null){
            sql = sql + " AND " + param3 + " = '" + param4 + "'";
        }

        try {
            result = connection.prepareStatement(sql).executeQuery();

            if (result.next()) {
                return result.getObject(columnLabel);
            }
        } catch (Exception erro) {
            System.out.println("Erro ao conseguir valor : " + erro);
        }

        try{
            connection.close();
        }catch (Exception e ){
        }
        return null;
    }

    // Update specific Value
    public Object updateValue(String table, String columnLabel, Object newValue, String param1, String param2) {
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("SUCCESS");
        } catch (Exception erro) {
            System.out.println("Erro ao conectar com a base de dados: " + erro);
        }

        String sql = "UPDATE " + table + " SET " + columnLabel + " = " + newValue;
        String sql2 = "UPDATE " + table + " SET " + columnLabel + " = ?" + " WHERE " + param1 + " = ?";
        if (param1 != null && param2 != null) {
            sql = sql + " WHERE " + param1 + " = " + param2;
        }

        try {
            PreparedStatement preparedStmt = connection.prepareStatement(sql2);
            preparedStmt.setObject(1, newValue);
            preparedStmt.setObject(2, param2);
            preparedStmt.executeUpdate();

            preparedStmt.close();

        } catch (Exception erro) {
            System.out.println("Erro ao atualizar : " + erro);
        }

        try{
            connection.close();
        }catch (Exception e ){
        }

        return null;
    }

    //Delete Entry
    public void deleteEntry(String table,String param1, String param2,String param3,String param4){
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("SUCCESS");
        } catch (Exception erro) {
            System.out.println("Erro ao conectar com a base de dados: " + erro);
        }

          String sql ="DELETE FROM " + table + " WHERE " + param1 + " = " + param2;

          if(param3 != null && param4 != null){
              sql = sql + " AND " + param3 + " = " + param4;
          }

          try{
              PreparedStatement preparedStatement = connection.prepareStatement(sql);
              preparedStatement.execute();
          }catch (Exception e ){
              System.out.println("Erro ao eliminar o registo : " + e);
          }

        try{
            connection.close();
        }catch (Exception e ){
        }
    }

}
