 package com.example.wildpaths.adapters;

 import static android.content.Context.MODE_PRIVATE;

 import android.content.Context;

 import com.example.wildpaths.MainActivity;

 import java.io.BufferedReader;
 import java.io.FileInputStream;
 import java.io.FileNotFoundException;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.InputStreamReader;

public class AppDataAdapter extends Thread {
    private boolean alive = false;
    private static final String FILE_NAME = "appData",
            LANG_FIELD = "lang|",
            USERNAME_FIELD = "username|",
            PW_FIELD = "pw|";

    private static Context context;

    // App data
    public byte lang;
    // User data
    public String userName, pw;

    public AppDataAdapter(Context context) {
        this.context = context;

        try {
            String linha;
            FileInputStream file = context.openFileInput(FILE_NAME);
            InputStreamReader dataStreamer = new InputStreamReader(file);
            BufferedReader dataReader = new BufferedReader(dataStreamer);
            this.alive = true;

            while ((linha = dataReader.readLine()) != null) {

                String field = "", value = "";
                boolean dataStart = false;

                // Verifica cada char da linha e separa por campo e valor
                for (char currChar : linha.toCharArray()) {

                    if (currChar == '|') {
                        dataStart = true;
                    }
                    if (dataStart == false && currChar != '|') {
                        field = field + currChar;
                    } else if (dataStart == true && currChar != '|') {
                        value = value + currChar;
                    }
                }

                // Guarda o valor obitdo na variavel correta
                System.out.println(field + "\n" + value);
                switch (field) {

                    case "lang":
                        if (value.equals("0")) {
                            lang = 0;
                        } else {
                            lang = 1;
                        }
                        break;

                    case "username":
                        userName = value;
                        break;

                    case "pw":
                        pw = value;
                        break;
                }

            }

            file.close();
            dataStreamer.close();
            dataReader.close();
        } catch (Exception e) {
        }

        // Criar o ficheiro caso ele n exista
        if (alive == false) {

            FileOutputStream file = null;
            try {
                String data = LANG_FIELD +"0"+ "\n" +
                               USERNAME_FIELD +""+ "\n" +
                               PW_FIELD +"";
                file = context.openFileOutput(FILE_NAME, MODE_PRIVATE);

                file.write(data.getBytes());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (file != null) {
                    try {
                        file.close();
                    } catch (IOException e) {
                    }
                }
            }
        }
    }

    public void saveAppData() {
        context.deleteFile(FILE_NAME);

        FileOutputStream file = null;
        try {
            String data = LANG_FIELD + MainActivity.lang + "\n" +
                    USERNAME_FIELD +MainActivity.userName+ "\n" +
                    PW_FIELD +MainActivity.userPw;

            file = context.openFileOutput(FILE_NAME, MODE_PRIVATE);
            file.write(data.getBytes());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (file != null) {
                try {
                    file.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public boolean getAlive() {
        return alive;
    }

    //Metodo apenas pra debug remover na versao final
    public void delAppData() {
        context.deleteFile(FILE_NAME);
    }
}

