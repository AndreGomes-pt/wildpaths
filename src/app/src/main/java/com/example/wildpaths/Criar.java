package com.example.wildpaths;


import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.tomtom.sdk.location.GeoPoint;
import com.tomtom.sdk.map.display.MapOptions;
import com.tomtom.sdk.map.display.TomTomMap;
import com.tomtom.sdk.map.display.gesture.MapClickListener;
import com.tomtom.sdk.map.display.image.Image;
import com.tomtom.sdk.map.display.image.ImageFactory;
import com.tomtom.sdk.map.display.marker.Marker;
import com.tomtom.sdk.map.display.marker.MarkerOptions;
import com.tomtom.sdk.map.display.ui.MapFragment;
import com.tomtom.sdk.map.display.ui.MapReadyCallback;

import java.util.Locale;
import java.util.OptionalDouble;

public class Criar extends Fragment {

    //Localizacao
    private double latitude = 0, longitude = 0;

    //Mapa
    private MapFragment mapFragment;
    private MapOptions mapOptions;
    private Marker previousMarker;

    //Textos
    private static TextView tituloRegistoTrilho, tituloCampoTituloTrilho, tituloCampoDistanciaTrilho, tituloCampoDescricaoTrilho, avisoNoConta;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //Imagens Inseridas
    public static char[] imgSelecionadas = new char[]{'0', '0', '0', '0'};
    public static Drawable imgTrilho1 = null, imgTrilho2 = null, imgTrilho3 = null, imgTrilho4 = null;
    private static ImageView imgAdicionada1;
    private static ImageView imgAdicionada2;
    private static ImageView imgAdicionada3;
    private static ImageView imgAdicionada4;

    //Categoria do trilho
    private static char[] categorias = new char[]{'0', '0', '0', '0'};

    //Butões
    private static Button bRegTrilho;

    private String mParam1;
    private String mParam2;

    public Criar() {
    }

    public static Criar newInstance(String param1, String param2) {
        Criar fragment = new Criar();
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
        // Inflate the layout for this fragment
        View viewLogedIn = inflater.inflate(R.layout.fragment_criar_login, container, false);
        View viewDef = inflater.inflate(R.layout.fragment_criar_def, container, false);

        if (MainActivity.logedIn == true) {
            return logedIn(viewLogedIn);
        } else {
            return def(viewDef);
        }
    }

    public View def(View view) {
        avisoNoConta = view.findViewById(R.id.textoAvisoCriar);
        langUpdate();
        return view;
    }

    public View logedIn(View view) {

        //Icon Marcacao
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
        Image image = ImageFactory.INSTANCE.fromBitmap(bitmap);

        //Mapa
        mapOptions = new MapOptions("5xx6AW7jThtbA0ZG1OnqCT6ANVFSjyRY");
        mapFragment = MapFragment.Companion.newInstance(mapOptions);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.mapaCriacao, mapFragment)
                .commit();

        mapFragment.getMapAsync(new MapReadyCallback() {
            @Override
            public void onMapReady(@NonNull TomTomMap tomTomMap) {
                if (MainActivity.lang == 0) {
                    tomTomMap.setLanguage(Locale.forLanguageTag("pt-PT"));
                } else {
                    tomTomMap.setLanguage(Locale.forLanguageTag("en-GB"));
                }
                tomTomMap.showHillShading();

                //Marca o lugar que o user selecionou
                tomTomMap.addMapClickListener(new MapClickListener() {
                    @Override
                    public boolean onMapClicked(@NonNull GeoPoint geoPoint) {

                        if (previousMarker != null) {
                            previousMarker.remove();
                        }
                        //cria ponto no mapa pra o user saber
                        latitude = geoPoint.getLatitude();
                        longitude = geoPoint.getLongitude();

                        PointF placementAnchor = new PointF(0.5f, 1.0f);
                        MarkerOptions markerOptions = new MarkerOptions(
                                geoPoint,
                                image,
                                image,
                                image,
                                placementAnchor,
                                placementAnchor,
                                placementAnchor,
                                "",
                                null,
                                ""
                        );

                        previousMarker = tomTomMap.addMarker(markerOptions);


                        return false;
                    }
                });
            }});

        //Butoes
        ImageButton bAddImg = view.findViewById(R.id.bAdicionarImg) ,
                bCatg1 = view.findViewById(R.id.catgSelector1),
                bCatg2 = view.findViewById(R.id.catgSelector2),
                bCatg3 = view.findViewById(R.id.catgSelector3),
                bCatg4 = view.findViewById(R.id.catgSelector4);
        bRegTrilho = view.findViewById(R.id.bRegistoTrilho);
        bRegTrilho.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.white))));

        //Textos e Campos
        tituloRegistoTrilho = view.findViewById(R.id.tituloRegistoTrilho);
        tituloCampoTituloTrilho = view.findViewById(R.id.tituloCampoTituloTrilho);
        tituloCampoDistanciaTrilho = view.findViewById(R.id.tituloCampoDistanciaTrilho);
        tituloCampoDescricaoTrilho = view.findViewById(R.id.tituloCampoDescricaoTrilho);

        TextInputEditText campoTituloTrilho = view.findViewById(R.id.campoTituloTrilho),
                campoDistanciaTrilho = view.findViewById(R.id.campoDistanciaTrilho),
                campoDescricao = view.findViewById(R.id.campoDescricao);

        //Tamanhos
        tituloRegistoTrilho.setTextSize(10 * getResources().getDisplayMetrics().density);
        tituloCampoTituloTrilho.setTextSize(8 * getResources().getDisplayMetrics().density);
        tituloCampoDistanciaTrilho.setTextSize(8 * getResources().getDisplayMetrics().density);
        tituloCampoDescricaoTrilho.setTextSize(8 * getResources().getDisplayMetrics().density);
        campoTituloTrilho.setTextSize(6 * getResources().getDisplayMetrics().density);
        campoDistanciaTrilho.setTextSize(6 * getResources().getDisplayMetrics().density);
        campoDescricao.setTextSize(6 * getResources().getDisplayMetrics().density);
        bRegTrilho.setTextSize(5 * getResources().getDisplayMetrics().density);

        //Imagens Adicionadas
        imgAdicionada1 = view.findViewById(R.id.imgAdicionada1);
        imgAdicionada2 = view.findViewById(R.id.imgAdicionada2);
        imgAdicionada3 = view.findViewById(R.id.imgAdicionada3);
        imgAdicionada4 = view.findViewById(R.id.imgAdicionada4);

        imgAdicionada1.setImageDrawable(null);
        imgAdicionada2.setImageDrawable(null);
        imgAdicionada3.setImageDrawable(null);
        imgAdicionada4.setImageDrawable(null);
        imgTrilho1 = null;
        imgTrilho2 = null;
        imgTrilho3 = null;
        imgTrilho4 = null;
        imgSelecionadas = new char[]{'0', '0', '0', '0'};
        categorias = new char[] {'0','0','0','0'};

        //Escode o Teclado
        RelativeLayout ecraCriar = view.findViewById(R.id.pagCriar);
        ecraCriar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v == ecraCriar && event.getAction() == MotionEvent.ACTION_DOWN){
                    MainActivity.imm.hideSoftInputFromWindow(ecraCriar.getWindowToken(), 0);
                }
                return false;
            }
        });

        //Butao adicionar imagens
        bAddImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(v == bAddImg && event.getAction() == MotionEvent.ACTION_DOWN){
                    boolean allUsed = false;
                    for(char x : imgSelecionadas){
                        if(x == '1'){
                            allUsed = true;
                        }else{
                            allUsed = false;
                        }
                    }
                }
                return false;
            }
        });

        //Butões categorias
        bCatg1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == bCatg1){
                    if(categorias[0] == '0'){
                        categorias[0] = '1';
                        bCatg1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    }else{
                        categorias[0] = '0';
                        bCatg1.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    }
                }
            }
        });
        bCatg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == bCatg2){
                    if(categorias[1] == '0'){
                        categorias[1] = '1';
                        bCatg2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    }else{
                        categorias[1] = '0';
                        bCatg2.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    }
                }
            }
        });
        bCatg3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == bCatg3){
                    if(categorias[2] == '0'){
                        categorias[2] = '1';
                        bCatg3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    }else{
                        categorias[2] = '0';
                        bCatg3.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    }
                }
            }
        });
        bCatg4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == bCatg4){
                    if(categorias[3] == '0'){
                        categorias[3] = '1';
                        bCatg4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    }else{
                        categorias[3] = '0';
                        bCatg4.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.black)));
                    }
                }
            }
        });

        //Butão Publicar Trilho
        bRegTrilho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == bRegTrilho){
                    bRegTrilho.setEnabled(false);
                    int nCatgs = 0;
                    for(char x : categorias){
                        if(x == '1'){
                            nCatgs++;
                        }
                    }
                    if(campoTituloTrilho.getText() != null && campoDistanciaTrilho.getText() != null
                            && campoDescricao.getText() != null && nCatgs != 0
                            && latitude != 0 && longitude != 0){
                        bRegTrilho.setEnabled(false);
                        //dados a inserir
                        String tituloTrilho = campoTituloTrilho.getText().toString(),
                                descricao = campoDescricao.getText().toString();

                        double distancia = 0;
                        OptionalDouble optional = null;
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            optional = OptionalDouble.of(Double.parseDouble(campoDistanciaTrilho.getText().toString()));
                            distancia = optional.getAsDouble();
                        }

                        //Inserção dos dados na bd
                        double finalDistancia = distancia;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                MainActivity.bdThread.execute(()->{
                                    try{
                                        MainActivity.bd.insertTrail(tituloTrilho,descricao, finalDistancia, categorias,
                                                MainActivity.imgAdapter.encode(imgTrilho1),
                                                MainActivity.imgAdapter.encode(imgTrilho2),
                                                MainActivity.imgAdapter.encode(imgTrilho3),
                                                MainActivity.imgAdapter.encode(imgTrilho4),
                                                MainActivity.userId,latitude,longitude);
                                        MainActivity.navBar.setSelectedItemId(MainActivity.navBar.getMenu().findItem(R.id.Home).getItemId());
                                    }catch (Exception e ){
                                        System.out.println("Erro ao inserir trilho" + e);

                                    }
                                });
                            }
                        }).start();

                        bRegTrilho.setEnabled(true);
                    }
                }
            }
        });



        langUpdate();

        return view;
    }
    public static void langUpdate(){
        try{
            if(MainActivity.lang == 0){
                tituloRegistoTrilho.setText(R.string.titulo_PagCriar_PT);
                tituloCampoTituloTrilho.setText(R.string.titulo_campoNomeTrilho_PT);
                tituloCampoDistanciaTrilho.setText(R.string.titulo_campoDistanciaTrilho_PT);
                tituloCampoDescricaoTrilho.setText(R.string.titulo_campoDescricao_PT);

                bRegTrilho.setText(R.string.texto_bRgistarTrilho_PT);
            }else{
                tituloRegistoTrilho.setText(R.string.titulo_PagCriar_EN);
                tituloCampoTituloTrilho.setText(R.string.titulo_campoNomeTrilho_EN);
                tituloCampoDistanciaTrilho.setText(R.string.titulo_campoDistanciaTrilho_EN);
                tituloCampoDescricaoTrilho.setText(R.string.titulo_campoDescricao_EN);

                bRegTrilho.setText(R.string.texto_bRgistarTrilho_EN);
            }
        }catch (Exception e ){
        }

        try{
            if(MainActivity.lang==0){
                avisoNoConta.setText("Inicie sessão para poder criar um trilho");
            }else{
                avisoNoConta.setText("You must be logged in to post a trail");
            }
        }catch (Exception e ){
        }

    }

    public static void updateAddImgs (){
        int counter = 0;
        for(char c : imgSelecionadas){
            if(c == '1'){
                switch (counter){
                    case 0:
                        imgAdicionada1.setImageDrawable(imgTrilho1);
                        break;
                    case 1:
                        imgAdicionada2.setImageDrawable(imgTrilho2);
                        break;
                    case 2:
                        imgAdicionada3.setImageDrawable(imgTrilho3);
                        break;
                    case 3:
                        imgAdicionada4.setImageDrawable(imgTrilho4);
                        break;
                }
            }
            counter++;
        }
    }
}