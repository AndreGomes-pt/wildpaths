
package com.example.wildpaths;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.wildpaths.adapters.AppDataAdapter;
import com.example.wildpaths.adapters.ImagensAdapter;
import com.example.wildpaths.db.Bd;
import com.example.wildpaths.perfil.PerfilFragment;
import com.example.wildpaths.perfil.RegistoUserFragment;
import com.example.wildpaths.trilhos.Trilhos;
import com.example.wildpaths.trilhos.selecionado.TrilhoSelecionado;
import com.example.wildpaths.trilhos.trilho.Trilho;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    //BLogOut
    public static ImageButton bLogOut;

    //Threads
    public static Executor bdThread = Executors.newCachedThreadPool();
    //Trilhos e Comentarios
    public static List<Trilho> trilhos = new ArrayList<>();
    private Fragment fragmentPags;//fragment usado para a mudança de paginas e pagDefenições

    //Dados de login do utilizador(Nome, pw)
    public static String userName, userPw;
    public static int userId;

    //Adapter de imagens
    public static ImagensAdapter imgAdapter = new ImagensAdapter();

    //Base de Dados
    public static Bd bd;

    //Opçoes app
    public static boolean logedIn =false;
    public static byte lang = 0; //Lingua selecionada
    public static AppDataAdapter appDataAdapter;
    public static InputMethodManager imm;

    //NavBar ( percisa ser public para q as outras paginas possam redirecionar para outras)
    public static BottomNavigationView navBar;

    //BdLive
    public static AtomicBoolean BDALIVE = new AtomicBoolean(false);

    private void bdStart(){

        //Inicialização da Bd
        bdThread.execute(() -> {
            bd = new Bd();
            BDALIVE.set(true);
        });

        while(BDALIVE.get() == false){
            try{
                Thread.sleep(100);
            }catch (Exception e ){
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Permissões
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1 );
        }
        bdStart();
        bLogOut = findViewById(R.id.bLogOut);
        bLogOut.setVisibility(View.INVISIBLE);

        bLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == bLogOut && logedIn == true){
                    appDataAdapter.delAppData();
                    logedIn = false;
                    userName = "";
                    userPw = "";
                    bLogOut.setVisibility(View.INVISIBLE);
                    MainActivity.navBar.setSelectedItemId(MainActivity.navBar.getSelectedItemId());
                }
            }
        });

        //Import das configurações da app e dos dados login
        new Thread(new Runnable() {
            @Override
            public void run() {
            appDataAdapter = new AppDataAdapter(getApplicationContext());
            if(appDataAdapter.getAlive()==true){
                lang = appDataAdapter.lang;
                try {
                    if (!appDataAdapter.userName.equals("") && !appDataAdapter.pw.equals("")) {
                        //Verificar se os dados de sessao são validos com a bd
                        AtomicBoolean dadosV = new AtomicBoolean(false);
                        userName = appDataAdapter.userName;
                        userPw = appDataAdapter.pw;

                        bdThread.execute(() -> {
                            try{
                                Object id = bd.getValue("user", "userId", "userName", userName,"userPw",userPw);
                                if (id != null) {
                                    userId = Integer.parseInt(String.valueOf(id));
                                    logedIn = true;
                                    bLogOut.setVisibility(View.VISIBLE);
                                    dadosV.set(true);
                                }
                            }catch (Exception e ){
                                dadosV.set(true);
                            }
                        });
                        while (dadosV.get() == false) {
                            try {
                                Thread.sleep(100);
                            } catch (Exception e) {
                            }
                        }
                    }
                }catch (Exception e ){
                    System.out.println("Erro ao ler os dados salvos da aplicação : " + e);
                }
            }
        }}).start();


        //Ação dos butoes nav bar
        navBar= findViewById(R.id.navBar);
        opcaoSelecionadaNav(navBar.getMenu().findItem(R.id.Home));
        navBar.getMenu().findItem(R.id.Home).setChecked(true);
        navBar.setOnNavigationItemSelectedListener(item -> {
            opcaoSelecionadaNav(item);
            return true;
        });

        //Butão das definições
        ImageButton bLang = findViewById(R.id.bLangMain);
        if(lang == 0){
           bLang.setBackground(getResources().getDrawable(R.drawable.lang_pt));
        }else{
            bLang.setBackground(getResources().getDrawable(R.drawable.lang_en));
        }

        bLang.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(v == bLang && event.getAction() == MotionEvent.ACTION_DOWN){
                    //fazer a mudança de lingua
                    if(lang == 0){
                        lang = 1;
                        bLang.setBackground(getResources().getDrawable(R.drawable.lang_en));
                        appDataAdapter.saveAppData();
                        langUpdate();

                    }else{
                        lang = 0;
                        bLang.setBackground(getResources().getDrawable(R.drawable.lang_pt));
                        appDataAdapter.saveAppData();
                        langUpdate();
                    }
                }
                return false;
            }
        });

        langUpdate();

        boolean r = true;
        while(r == true){
            try{
                this.imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            }catch (Exception erro){}

            if(imm!=null){
                r = false;
            }
        }

    }

    private void langUpdate(){
        //Alteração da lingua da app
        if(lang==0){
            //navBar
            navBar.getMenu().getItem(0).setTitle(R.string.titulo_opCriar_PT);
            navBar.getMenu().getItem(1).setTitle(R.string.titulo_opTrilhos_PT);
            navBar.getMenu().getItem(2).setTitle(R.string.titulo_opPerfil_PT);

            //Perfil e Login
            try{
                PerfilFragment.langUpdate();
            }catch (Exception error){}

            //Registo
            try{
                RegistoUserFragment.langUpdate();
            }catch (Exception error){}

            //Trilho Selecionado
            try{
                TrilhoSelecionado.langUpdate();
            }catch(Exception error){}

            //Registo Trilho
            try{
                Criar.langUpdate();
            }catch (Exception error) {}

        }else {
            //navBar
            navBar.getMenu().getItem(0).setTitle(R.string.titulo_opCriar_EN);
            navBar.getMenu().getItem(1).setTitle(R.string.titulo_opTrilhos_EN);
            navBar.getMenu().getItem(2).setTitle(R.string.titulo_opPerfil_EN);

            //Perfil e Login
            try{
                PerfilFragment.langUpdate();
            }catch (Exception error){}

            //Registo
            try{
                RegistoUserFragment.langUpdate();
            }catch (Exception error){}

            //Trilho Selecionado
            try{
                TrilhoSelecionado.langUpdate();
            }catch(Exception error){}

            //Registo Trilho
            try{
                Criar.langUpdate();
            }catch (Exception error){}
        }

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) { //Remove a barra de navegação do android
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void opcaoSelecionadaNav(MenuItem item){
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (item.getItemId()) {
                    case R.id.CriarTrilho:
                        fragmentPags = new Criar();
                        break;

                    default:
                        fragmentPags = new Trilhos();
                        break;

                    case R.id.Perfil:
                        fragmentPags = new PerfilFragment();
                        break;
                }

                //Muda o fragment atual
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.conteudoPaginas, fragmentPags);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }).start();
    }

    //Fazer a mudança de fragment na propria pagina trilhos se possivel
    public void trilhoSelecionado (final View viewTrilho){
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(viewTrilho.getParent()==Trilhos.rvTrilhos){
                    Trilhos.trilhoSelecionado(viewTrilho, getSupportFragmentManager().beginTransaction());
                }else{
                    PerfilFragment.trilhoSelecionado(viewTrilho,getSupportFragmentManager().beginTransaction());
                }
            }
        }).start();
    }

    //Imagens da galeria

    private View viewImagem;
    public void selecImgGaleria(final View viewImagem) {
        this.viewImagem = viewImagem;
        boolean good2Goo = false;

        switch (viewImagem.getId()){

            case R.id.imgPerfilLogedIn:
                if(PerfilFragment.editPerfil==true){
                    good2Goo = true;
                }
                break;
            case R.id.imgPerfilBackground:
                if(PerfilFragment.editPerfil==true){
                    good2Goo = true;
                }
                break;
            default:
                int counter = 0;

                for(char x : Criar.imgSelecionadas){
                    if(x == '0' ){
                        counter++;
                    }
                }
                if(counter != 0){
                    good2Goo = true;
                }
                break;
        }

        if(good2Goo == true){
            System.out.println("TOU ATIVADA");
            final CharSequence[] options;
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            if(MainActivity.lang == 0){
                options = new CharSequence[]{"Escolher da galeria", "Cancelar"};
                builder.setTitle("Adicionar foto");
            }else{
                builder.setTitle("Add Photo");
                options = new CharSequence[]{"Choose from Gallery", "Cancel"};
            }

            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int item) {
                    if (options[item].equals("Choose from Gallery") || options[item].equals("Escolher da galeria"))
                    {
                        Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 1);
                    }
                    else if (options[item].equals("Cancel") || options[item].equals("Cancelar")) {
                        dialog.dismiss();
                    }
                }
            });
            builder.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                switch (viewImagem.getId()){

                    case R.id.imgPerfilLogedIn:
                        CircleImageView tempPf = (CircleImageView) viewImagem;
                        tempPf.setImageDrawable(new BitmapDrawable(getResources(),(BitmapFactory.decodeFile(picturePath))));
                        PerfilFragment.imgUser = new BitmapDrawable(getResources(),(BitmapFactory.decodeFile(picturePath)));
                        break;

                    case R.id.imgPerfilBackground:
                        ImageView tempBc = (ImageView) viewImagem;
                        tempBc.setImageDrawable(new BitmapDrawable(getResources(),(BitmapFactory.decodeFile(picturePath))));
                        PerfilFragment.imgBcUser = new BitmapDrawable(getResources(),(BitmapFactory.decodeFile(picturePath)));
                        break;

                    case R.id.bAdicionarImg:
                        int counter = 0;
                        boolean espaçoLiverEncontrado = false;
                        for(char x : Criar.imgSelecionadas){
                            if(x == '0' && espaçoLiverEncontrado == false){
                                espaçoLiverEncontrado = true;
                                switch (counter){
                                    case 0:
                                        Criar.imgTrilho1 = new BitmapDrawable(getResources(),(BitmapFactory.decodeFile(picturePath)));
                                        Criar.imgSelecionadas[counter] = '1';
                                        Criar.updateAddImgs();
                                        break;
                                    case 1:
                                        Criar.imgTrilho2 = new BitmapDrawable(getResources(),(BitmapFactory.decodeFile(picturePath)));
                                        Criar.imgSelecionadas[counter] = '1';
                                        Criar.updateAddImgs();
                                        break;
                                    case 2:
                                        Criar.imgTrilho3 = new BitmapDrawable(getResources(),(BitmapFactory.decodeFile(picturePath)));
                                        Criar.imgSelecionadas[counter] = '1';
                                        Criar.updateAddImgs();
                                        break;
                                    case 3:
                                        Criar.imgTrilho4 = new BitmapDrawable(getResources(),(BitmapFactory.decodeFile(picturePath)));
                                        Criar.imgSelecionadas[counter] = '1';
                                        Criar.updateAddImgs();
                                        break;
                                }
                            }
                            counter++;
                        }
                        break;
                }


                System.out.println("A IMAGEM FOI PORCURADA E GUARDADA");

            }
        }
    }

}
