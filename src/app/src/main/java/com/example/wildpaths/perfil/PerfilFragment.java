package com.example.wildpaths.perfil;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wildpaths.MainActivity;
import com.example.wildpaths.R;
import com.example.wildpaths.adapters.PasswordAdapter;
import com.example.wildpaths.adapters.TrilhoAdapter;
import com.example.wildpaths.trilhos.selecionado.TrilhoSelecionado;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.atomic.AtomicBoolean;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {


    public static boolean editPerfil = false;
    private static Fragment pagTrilho;
    public static RecyclerView recyclerPerfil;
    public boolean created = false;

    //Textos traduziveis
    private static TextView perfil_bioTitulo,tituloUserNameField, tituloPagLogin, tituloPWfield, bRegisto;
    private static Button perfil_bPublicacoes, perfil_bFavoritos, bLogin;
    private static TextInputEditText userNameField, pwField;
    private static CheckBox lembrarConta;

    //Dados User
    public static String bio;
    public static Drawable imgUser = null, imgBcUser = null;
    //Rvs
    private TrilhoAdapter adapterTrilhosPub = null, adapterTrilhosFav = null;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PerfilFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static PerfilFragment newInstance(String param1, String param2) {
        PerfilFragment fragment = new PerfilFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layoutLogin = inflater.inflate(R.layout.fragment_perfil_login, container, false),
             layoutLogedIn = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Dedependendo dos dados de login do user usa o layout de pagina de perfil ou de login
        if(MainActivity.logedIn==false){
            //Metodo que preenche o layout de sesao iniciada com os dados do user
            return login(layoutLogin);
        }else{
            //metodo que recebe os dados de login do use enviados no layout de login
            return logedIn(layoutLogedIn);
        }
    }

    //Loged In
    private View logedIn (View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        //Titulos campos, aviso e fotos
                        TextInputEditText perfil_userName = view.findViewById(R.id.perfil_userName),
                                perfil_bio = view.findViewById(R.id.perfil_bio);
                        perfil_bioTitulo = view.findViewById(R.id.perfil_bioTitulo);
                        Button bEditarPerfil = view.findViewById(R.id.bEditarPerfil);
                        perfil_bFavoritos = view.findViewById(R.id.perfil_bFavoritos);
                        perfil_bPublicacoes = view.findViewById(R.id.perfil_bPublicacoes);
                        ImageView imgPerfilBackground = view.findViewById(R.id.imgPerfilBackground);
                        imgPerfilBackground.setScaleType(ImageView.ScaleType.FIT_XY);
                        CircleImageView imgPerfilLogedIn = view.findViewById(R.id.imgPerfilLogedIn);
                        recyclerPerfil = view.findViewById(R.id.recyclerPerfil);

                        //Extras(IG)
                        perfil_bio.setEnabled(false);
                        perfil_userName.setEnabled(false);
                        bEditarPerfil.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.black))));
                        recyclerPerfil.setVerticalScrollBarEnabled(false);

                        //Tamanho letra
                        perfil_userName.setTextSize(10 * getResources().getDisplayMetrics().density);
                        perfil_bio.setTextSize(6 * getResources().getDisplayMetrics().density);
                        perfil_bioTitulo.setTextSize(8 * getResources().getDisplayMetrics().density);

                        //Alteração Lingua
                        langUpdate();

                        //Prenchimento com os dados do user (Nome user, bio, ft perfil, ft background)
                        //Pensar maneira de esconder carregamento dos dados
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                   imgUser = MainActivity.imgAdapter.decode((byte[]) MainActivity.bd.getValue("user", "userPic", "userId", String.valueOf(MainActivity.userId), null, null), String.valueOf(MainActivity.userId) + MainActivity.userName + "Pf", getContext());
                                   imgBcUser = MainActivity.imgAdapter.decode((byte[]) MainActivity.bd.getValue("user", "userBcPic", "userId", String.valueOf(MainActivity.userId), null, null), String.valueOf(MainActivity.userId) + MainActivity.userName + "Bc", getContext());
                                   bio = MainActivity.bd.getValue("user", "bio", "userId", String.valueOf(MainActivity.userId), null, null).toString();
                                   //Adapters
                                   adapterTrilhosFav = new TrilhoAdapter(MainActivity.userId, (byte) 2);
                                   adapterTrilhosPub = new TrilhoAdapter(MainActivity.userId, (byte) 1);
                                } catch (Exception e) {
                                }
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        perfil_bio.setText(bio);
                                        perfil_userName.setText(MainActivity.userName);

                                        if(imgUser == null){
                                            System.out.println("NAO HA IMAGEM PERFIL");
                                            imgPerfilLogedIn.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.img_user_holder));
                                        }
                                        if(imgBcUser == null){
                                            System.out.println("NAO HA IMAGEM BC");
                                            imgPerfilBackground.setVisibility(View.INVISIBLE);
                                        }
                                        if(imgBcUser != null && imgUser != null){
                                            imgPerfilLogedIn.setImageDrawable(imgUser);
                                            imgPerfilBackground.setImageDrawable(imgBcUser);
                                        }
                                    }
                                });
                            }
                        }).start();

                        //Editar Perfil
                        bEditarPerfil.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {

                                if (view == bEditarPerfil && motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                    if (editPerfil == false) {
                                        editPerfil = true;
                                        bEditarPerfil.setBackground(getResources().getDrawable(R.drawable.save));
                                        bEditarPerfil.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.black))));

                                        //Desliga a edição de textos nos campos do userName e da Bio
                                        perfil_bio.setEnabled(true);
                                        perfil_userName.setEnabled(true);
                                        imgPerfilLogedIn.setEnabled(true);
                                        imgPerfilBackground.setEnabled(true);

                                        //Desliga a alteração de imagens de ft de peril e background
                                        /*imgPerfilLogedIn.setBackground(getResources().getDrawable(R.drawable.plus));

                                        imgPerfilBackground.setBackground(getResources().getDrawable(R.drawable.plus));*/



                                        //Alteração

                                    } else {
                                        editPerfil = false;
                                        bEditarPerfil.setBackground(getResources().getDrawable(R.drawable.edit));
                                        bEditarPerfil.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.black))));

                                        //Liga a edição de textos nos campos do userName e da Bio
                                        perfil_bio.setEnabled(false);
                                        perfil_userName.setEnabled(false);

                                        //atualizar os campos de username e bio na Base de dados
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                       MainActivity.bdThread.execute(()->{
                                                             try {
                                                                 MainActivity.bd.updateValue("user","userPic",MainActivity.imgAdapter.encode(imgUser),"userId",String.valueOf(MainActivity.userId));
                                                             }catch (Exception e ){
                                                                 System.out.println("Erro ao atualizar a foto de perfil" + e);
                                                             }

                                                             try{
                                                                 MainActivity.bd.updateValue("user","userBcPic",MainActivity.imgAdapter.encode(imgBcUser),"userId",String.valueOf(MainActivity.userId));
                                                             }catch (Exception e){
                                                                 System.out.println("Erro ao atualizar a foto de fundo" + e);
                                                             }

                                                             try{
                                                                 MainActivity.bd.updateValue("user","userName",perfil_userName.getText().toString(),"userId",String.valueOf(MainActivity.userId));
                                                                 MainActivity.userName = perfil_userName.getText().toString();
                                                             }catch (Exception e){
                                                                 System.out.println("Erro ao atualizar nome de utilizadro" + e);
                                                             }

                                                             try{
                                                                 MainActivity.bd.updateValue("user","bio",perfil_bio.getText().toString(),"userId",String.valueOf(MainActivity.userId));
                                                             }catch (Exception e){
                                                                 System.out.println("Erro ao atualizar a bio" + e);
                                                             }
                                                             MainActivity.appDataAdapter.saveAppData();
                                                       });
                                                    }
                                                });
                                            }
                                        }).start();

                                    }
                                }
                                return false;
                            }
                        });

                        //Ver publicações
                        perfil_bPublicacoes.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {

                                if (view == perfil_bPublicacoes && motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                                    //Alterações visuais do butão de publicações
                                    perfil_bPublicacoes.setEnabled(false);
                                    perfil_bPublicacoes.setTextColor(getResources().getColor(R.color.white));
                                    perfil_bPublicacoes.setBackgroundColor(getResources().getColor(R.color.translucidBlack));
                                    perfil_bPublicacoes.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.translucidBlack)));

                                    //Alterações visuais do butão de favoritos
                                    perfil_bFavoritos.setEnabled(true);
                                    perfil_bFavoritos.setTextColor(getResources().getColor(R.color.black));
                                    perfil_bFavoritos.setBackgroundColor(Color.TRANSPARENT);
                                    perfil_bFavoritos.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));

                                    //Recycler Trilhos
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (recyclerPerfil.getAdapter() == null) {
                                                        recyclerPerfil.setAdapter(adapterTrilhosPub);
                                                    } else {
                                                        recyclerPerfil.swapAdapter(adapterTrilhosPub, true);
                                                    }
                                                }
                                            });
                                        }
                                    }).start();
                                }
                                return false;
                            }
                        });

                        //Ver Favoritos
                        perfil_bFavoritos.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {

                                if (view == perfil_bFavoritos && motionEvent.getAction() == MotionEvent.ACTION_DOWN) {

                                    //Alterações visuais do butão de favoritos
                                    perfil_bFavoritos.setEnabled(false);
                                    perfil_bFavoritos.setTextColor(getResources().getColor(R.color.white));
                                    perfil_bFavoritos.setBackgroundColor(getResources().getColor(R.color.translucidBlack));
                                    perfil_bFavoritos.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.translucidBlack)));

                                    //Alterações visuais do butão de publicações
                                    perfil_bPublicacoes.setEnabled(true);
                                    perfil_bPublicacoes.setTextColor(getResources().getColor(R.color.black));
                                    perfil_bPublicacoes.setBackgroundColor(Color.TRANSPARENT);
                                    perfil_bPublicacoes.setBackgroundTintList(ColorStateList.valueOf(Color.TRANSPARENT));

                                    //Recycler Trilhos Favoritos (No construtor do adapter verificar se são pedidos apenas os favoritos)
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            getActivity().runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (recyclerPerfil.getAdapter() == null) {
                                                        recyclerPerfil.setAdapter(adapterTrilhosFav);
                                                    } else {
                                                        recyclerPerfil.swapAdapter(adapterTrilhosFav, true);
                                                    }
                                                }
                                            });
                                        }
                                    }).start();
                                }
                                return false;
                            }
                        });
                    }
                });
            }
        }).start();
        return view;
    }

    //Login
    private View login(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        final String secretKey = "secrete";
                        PasswordAdapter pwAdapter = new PasswordAdapter();

                        //Hide keyboard on touch
                        RelativeLayout relativeLayout = view.findViewById(R.id.perfilLogin_holder);
                        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {
                                if (view == relativeLayout && motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                    MainActivity.imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
                                }
                                return false;
                            }
                        });

                        //Titulos campos, e aviso
                        tituloUserNameField = view.findViewById(R.id.tituloUserNameField);
                        TextView avisosLogin = view.findViewById(R.id.avisosLogin);
                        bRegisto = view.findViewById(R.id.bRegisto);
                        tituloPWfield = view.findViewById(R.id.tituloPWfield);
                        tituloPagLogin = view.findViewById(R.id.tituloPagLogin);

                        //Campos dados de login
                        userNameField = view.findViewById(R.id.userNameField);
                        pwField = view.findViewById(R.id.pwField);

                        //Butao Login
                        bLogin = view.findViewById(R.id.bLogin);
                        bLogin.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.white))));

                        //Check Box Lembrar Conta
                        lembrarConta = view.findViewById(R.id.checkbox_lembrarConta);

                        //Tamanho letra
                        tituloPagLogin.setTextSize(10 * getResources().getDisplayMetrics().density);

                        tituloUserNameField.setTextSize(8 * getResources().getDisplayMetrics().density);
                        userNameField.setTextSize(6 * getResources().getDisplayMetrics().density);

                        tituloPWfield.setTextSize(8 * getResources().getDisplayMetrics().density);
                        pwField.setTextSize(6 * getResources().getDisplayMetrics().density);

                        avisosLogin.setTextSize(5 * getResources().getDisplayMetrics().density);
                        bRegisto.setTextSize(5 * getResources().getDisplayMetrics().density);

                        //Alteração Lingua
                        langUpdate();

                        //Login(Ação do butão e verificação dos dados introduzidos com a b.d)
                        bLogin.setOnTouchListener(new View.OnTouchListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {

                                if (v == bLogin && event.getAction() == MotionEvent.ACTION_DOWN) {

                                    //Remove o teclado virtual
                                    MainActivity.imm.hideSoftInputFromWindow(bLogin.getWindowToken(), 0);

                                    String user = userNameField.getText().toString(), pw = pwAdapter.encrypt(pwField.getText().toString(), secretKey);

                                    //Verificar se os dados de inicio de sessao estao corretos com a base de dados
                                    if (TextUtils.isEmpty(userNameField.getText()) == true && TextUtils.isEmpty(pwField.getText()) == false) {
                                        if (MainActivity.lang == 0) {
                                            avisosLogin.setText(R.string.aviso_noUsername_PT);
                                        } else {

                                        }

                                        bLogin.setEnabled(true);
                                    } else if (TextUtils.isEmpty(pwField.getText()) == true && TextUtils.isEmpty(userNameField.getText()) == false) {
                                        if (MainActivity.lang == 0) {
                                            avisosLogin.setText(R.string.aviso_noPw_PT);
                                        }
                                        bLogin.setEnabled(true);

                                    } else if (!TextUtils.isEmpty(userNameField.getText()) && !TextUtils.isEmpty(pwField.getText())) {

                                        bLogin.setEnabled(false);
                                        AtomicBoolean dadosV = new AtomicBoolean(false),
                                                dadosC = new AtomicBoolean(false);
                                        //iniciar animação do butao de login
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    int userId = (int) MainActivity.bd.getValue("user", "userId", "userName", user, "userPw", pw);
                                                    if (userId > 0) {

                                                        MainActivity.userId = userId;

                                                        if (lembrarConta.isChecked()) {
                                                            MainActivity.logedIn = true;
                                                            MainActivity.userName = user;
                                                            MainActivity.userPw = pw;
                                                            MainActivity.bLogOut.setVisibility(View.VISIBLE);
                                                            MainActivity.appDataAdapter.saveAppData();
                                                        } else {
                                                            MainActivity.logedIn = true;
                                                            MainActivity.userName = user;
                                                            MainActivity.userPw = pw;
                                                            MainActivity.bLogOut.setVisibility(View.VISIBLE);
                                                        }
                                                        dadosC.set(true);
                                                        dadosV.set(true);
                                                    } else {
                                                        dadosV.set(true);
                                                    }
                                                } catch (Exception e) {
                                                    dadosC.set(false);
                                                    dadosV.set(true);
                                                }
                                            }
                                        }).start();

                                        do {
                                            if (dadosC.get() == false) {
                                                if (MainActivity.lang == 0) {
                                                    avisosLogin.setText(R.string.aviso_dadosErrados_PT);
                                                } else {
                                                    avisosLogin.setText(R.string.aviso_dadosErrados_PT);
                                                }
                                            } else {
                                                avisosLogin.setText("");
                                            }
                                        } while (dadosV.get() == false);

                                        //Recarrega a pagina de perfil
                                        MainActivity.navBar.setSelectedItemId(MainActivity.navBar.getMenu().getItem(2).getItemId());
                                    } else {
                                        if (MainActivity.lang == 0) {
                                            avisosLogin.setText(R.string.aviso_dadaosIncompletos_PT);
                                        } else {
                                            avisosLogin.setText(R.string.aviso_dadaosIncompletos_EN);
                                        }
                                        bLogin.setEnabled(true);
                                    }
                                    return true;
                                }
                                return false;
                            }
                        });

                        //Butão de registo de um novo utilizador
                        bRegisto.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View view, MotionEvent motionEvent) {

                                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                                    bRegisto.setTextColor(getResources().getColor(R.color.blue));

                                    Fragment regUser = new RegistoUserFragment();

                                    FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.conteudoPaginas, regUser);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();

                                }

                                return false;
                            }
                        });
                    }
                });
                created = true;
            }

        }).start();

        return view;
    }

    public static void langUpdate(){
        try{
            //Alteração da lingua (0-PT; 1-EN)
            if(MainActivity.lang==0){
                perfil_bioTitulo.setText(R.string.titulo_pagLogedInBio_PT);
                perfil_bPublicacoes.setText(R.string.texto_bMeusTrilhos_PT);
                perfil_bFavoritos.setText(R.string.texto_bTrilhosFav_PT);
            }else{
                perfil_bioTitulo.setText(R.string.titulo_pagLogedInBio_EN);
                perfil_bPublicacoes.setText(R.string.texto_bMeusTrilhos_EN);
                perfil_bFavoritos.setText(R.string.texto_bTrilhosFav_EN);
            }
        }catch (Exception error){}

        try {
            //Alteração da lingua (0-PT; 1-EN)
            if(MainActivity.lang==0){
                tituloUserNameField.setText(R.string.titulo_campo_user_PT);
                userNameField.setHint(R.string.hint_campo_user_PT);
                tituloPWfield.setText(R.string.titulo_campo_pw_PT);
                pwField.setHint(R.string.hint_campo_pw);
                bLogin.setText(R.string.texto_bLogin_PT);
                tituloPagLogin.setText(R.string.titulo_pagLogin_PT);
                lembrarConta.setText(R.string.titulo_campo_guardarSessão_PT);
                bRegisto.setText(R.string.texto_bCriarUser_PT);
            }else{
                tituloUserNameField.setText(R.string.titulo_campo_user_EN);
                userNameField.setHint(R.string.hint_campo_user_EN);
                tituloPWfield.setText(R.string.titulo_campo_pw_EN);
                pwField.setHint(R.string.hint_campo_pw);
                bLogin.setText(R.string.texto_bLogin_EN);
                tituloPagLogin.setText(R.string.titulo_pagLogin_EN);
                lembrarConta.setText(R.string.titulo_campo_guardarSessão_EN);
                bRegisto.setText(R.string.texto_bCriarUser_EN);
            }
        }catch (Exception error){}
    }

    public static void trilhoSelecionado(View trilho, FragmentTransaction fragmentTransaction){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //cria o fragment do trilho selecionado
                pagTrilho = new TrilhoSelecionado(recyclerPerfil.getChildLayoutPosition(trilho));
                fragmentTransaction.replace(R.id.conteudoPaginas, pagTrilho);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                //coloca o rv dos trilhos invisivel
                recyclerPerfil.setVisibility(View.INVISIBLE);

            }
        }).start();
    }

}