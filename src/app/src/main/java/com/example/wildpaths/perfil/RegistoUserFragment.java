package com.example.wildpaths.perfil;

import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.wildpaths.MainActivity;
import com.example.wildpaths.R;
import com.example.wildpaths.adapters.PasswordAdapter;
import com.google.android.material.textfield.TextInputEditText;


public class RegistoUserFragment extends Fragment {
    //Dados User
    String userName,email,pw,bio;
    //Textos Traduziveis
    private static TextView tituloPagReg,tituloNameFieldReg,tituloEmailFieldReg,tiuloPwFieldReg;
    private static TextInputEditText userNameFieldReg,emailFieldReg,pwFieldReg,pwFieldRegConf;
    private static CheckBox lembrarContaReg;
    private static Button bEfetuarRegisto;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistoUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistoUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistoUserFragment newInstance(String param1, String param2) {
        RegistoUserFragment fragment = new RegistoUserFragment();
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

        final String secretKey = "secrete";
        PasswordAdapter pwAdapter = new PasswordAdapter();

        View view = inflater.inflate(R.layout.fragment_registo_user, container, false);

        //Hide keyboard on touch
        RelativeLayout relativeLayout = view.findViewById(R.id.perfilRegisto_holder);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(view == relativeLayout && motionEvent.getAction()== MotionEvent.ACTION_DOWN){
                    MainActivity.imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
                }
                return false;
            }
        });

        //Titulos e avisos
        tituloPagReg = view.findViewById(R.id.tituloPagReg);
        TextView avisosRegisto = view.findViewById(R.id.avisosRegisto);

        //Campos dados de login
        tituloNameFieldReg = view.findViewById(R.id.tituloUserNameFieldReg);
        userNameFieldReg = view.findViewById(R.id.userNameFieldReg);
        tituloEmailFieldReg = view.findViewById(R.id.tituloEmailFieldReg);
        emailFieldReg = view.findViewById(R.id.emailFieldReg);
        tiuloPwFieldReg = view.findViewById(R.id.tituloPwFieldReg);
        pwFieldReg = view.findViewById(R.id.pwFieldReg);
        pwFieldRegConf = view.findViewById(R.id.pwFieldRegConf);

        //Butão EfetuarReg
        bEfetuarRegisto = view.findViewById(R.id.bEfetuarRegisto);
        bEfetuarRegisto.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.white))));

        //Check Box Lembrar Conta
        lembrarContaReg = view.findViewById(R.id.checkbox_lembrarContaReg);

        //Tamanho letra
        tituloPagReg.setTextSize(10 * getResources().getDisplayMetrics().density);

        tituloNameFieldReg.setTextSize(8 * getResources().getDisplayMetrics().density);
        userNameFieldReg.setTextSize(6 * getResources().getDisplayMetrics().density);
        tituloEmailFieldReg.setTextSize(8 * getResources().getDisplayMetrics().density);
        emailFieldReg.setTextSize(6 * getResources().getDisplayMetrics().density);
        tiuloPwFieldReg.setTextSize(8 * getResources().getDisplayMetrics().density);
        pwFieldReg.setTextSize(6 * getResources().getDisplayMetrics().density);
        pwFieldRegConf.setTextSize(6 * getResources().getDisplayMetrics().density);

        avisosRegisto.setTextSize(5 * getResources().getDisplayMetrics().density);
        bEfetuarRegisto.setTextSize(5 * getResources().getDisplayMetrics().density);

        //Alterção Lingua
        langUpdate();

        //bEfetuarRegisto
        bEfetuarRegisto.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(view == bEfetuarRegisto && motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    bEfetuarRegisto.setEnabled(false);
                    avisosRegisto.setText("");

                        if(userNameFieldReg.getText().toString().isEmpty() || emailFieldReg.getText().toString().isEmpty() ||
                                pwFieldReg.getText().toString().isEmpty() || !pwFieldRegConf.getText().toString().matches(pwFieldReg.getText().toString())){
                            if(MainActivity.lang==0){
                                if(pwFieldRegConf.getText().toString().matches(pwFieldReg.getText().toString())){
                                    avisosRegisto.setText(R.string.aviso_dadosIncompletos_PT);
                                }else{
                                    pwFieldReg.setError(getString(R.string.aviso_passwordNoMatch_PT),null);
                                    pwFieldRegConf.setError(getString(R.string.aviso_passwordNoMatch_PT),null);
                                }
                            }else if(!pwFieldRegConf.getText().toString().matches(pwFieldReg.getText().toString())){
                                pwFieldReg.setError(getString(R.string.aviso_passwordNoMatch_EN),null);
                                pwFieldRegConf.setError(getString(R.string.aviso_passwordNoMatch_EN),null);

                            }else{
                                if(MainActivity.lang == 0){
                                    avisosRegisto.setText(R.string.aviso_dadosIncompletos_PT);
                                }else{
                                    avisosRegisto.setText(R.string.aviso_dadosIncompletos_EN);
                                }
                            }

                        }else {
                            userName = userNameFieldReg.getText().toString();
                            email = emailFieldReg.getText().toString();
                            pw = pwAdapter.encrypt(pwFieldReg.getText().toString(),"secrete");
                            if(MainActivity.lang == 0){
                                bio = "Olá esta é a minha conta no WildPaths";
                            }else{
                                bio = "Hi this is my profile on WildPaths";
                            }

                            new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(lembrarContaReg.isChecked()){
                                                MainActivity.logedIn = true;
                                                MainActivity.userName = userName;
                                                MainActivity.userPw = pw;
                                                MainActivity.bLogOut.setVisibility(View.VISIBLE);
                                                MainActivity.appDataAdapter.saveAppData();
                                            }
                                            try{
                                                MainActivity.bdThread.execute(()->{
                                                    MainActivity.bd.insertUser(userName,email,pw,bio,null,null);
                                                });
                                            }catch (Exception e ){
                                            }
                                        }
                                    });
                                    MainActivity.navBar.setSelectedItemId(MainActivity.navBar.getMenu().findItem(R.id.Perfil).getItemId());
                                }
                            }).start();
                        }

                    bEfetuarRegisto.setEnabled(true);
                }
                return false;
            }
        });
        // Inflate the layout for this fragment
        return view;
    }

    public static void langUpdate(){
        //Alteração da lingua (0-PT; 1-EN)
        if (MainActivity.lang == 0) {
            tituloPagReg.setText(R.string.titulo_pagRegUser_PT);

            tituloNameFieldReg.setText(R.string.titulo_campoUserNameReg_PT);
            userNameFieldReg.setHint(R.string.hint_campo_user_PT);
            tituloEmailFieldReg.setText(R.string.titulo_campoEmailReg);
            emailFieldReg.setHint(R.string.hint_campo_emailReg);
            tiuloPwFieldReg.setText(R.string.titulo_campo_pw_PT);
            pwFieldReg.setHint(R.string.hint_campo_pwReg_PT);
            pwFieldRegConf.setHint(R.string.hint_campo_pwRegConf_PT);

            lembrarContaReg.setText(R.string.titulo_campo_guardarSessão_PT);
            bEfetuarRegisto.setText(R.string.texto_bEfetuarReg_PT);

        } else {
            tituloPagReg.setText(R.string.titulo_pagRegUser_EN);

            tituloNameFieldReg.setText(R.string.titulo_campoUserNameReg_EN);
            userNameFieldReg.setHint(R.string.hint_campo_user_EN);
            tituloEmailFieldReg.setText(R.string.titulo_campoEmailReg);
            emailFieldReg.setHint(R.string.hint_campo_emailReg);
            tiuloPwFieldReg.setText(R.string.titulo_campo_pw_EN);
            pwFieldReg.setHint(R.string.hint_campo_pwReg_EN);
            pwFieldRegConf.setHint(R.string.hint_campo_pwRegConf_EN);

            lembrarContaReg.setText(R.string.titulo_campo_guardarSessão_EN);
            bEfetuarRegisto.setText(R.string.texto_bEfetuarReg_EN);
        }
    }
}