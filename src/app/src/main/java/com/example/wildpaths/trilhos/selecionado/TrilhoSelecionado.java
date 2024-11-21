package com.example.wildpaths.trilhos.selecionado;

import static com.example.wildpaths.R.id.recyclerComent;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wildpaths.MainActivity;
import com.example.wildpaths.R;
import com.example.wildpaths.adapters.ComentarioAdapter;
import com.example.wildpaths.trilhos.Trilhos;
import com.tomtom.sdk.location.GeoPoint;
import com.tomtom.sdk.map.display.MapOptions;
import com.tomtom.sdk.map.display.TomTomMap;
import com.tomtom.sdk.map.display.image.Image;
import com.tomtom.sdk.map.display.image.ImageFactory;
import com.tomtom.sdk.map.display.marker.MarkerOptions;
import com.tomtom.sdk.map.display.ui.MapFragment;
import com.tomtom.sdk.map.display.ui.MapReadyCallback;

import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;


//CERTAS FUNÇÕES COMO O BUTÃO DE FAVORITO E O BUTÃO DE COMENTAR SO PODEM FUNCIONAR SE O USER TIVER SECÇÃO INICIADA
public class TrilhoSelecionado extends Fragment {

    //Dados para identificar o trilho clicado
    private static int xTrilho, idTrilho;
    private static EditText contentC;
    private MapFragment mapFragment;
    private MapOptions mapOptions;

    private Context context;
    public static RecyclerView rvComent;

    //Textos traduziveis
    private static TextView tituloDescricao, tituloComentarios, avisoLoginTrilhoS;
    private static Button bComfirmC;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrilhoSelecionado(int xTrilho) {
        this.xTrilho = xTrilho;
        this.idTrilho = MainActivity.trilhos.get(xTrilho).getIdTrilho();//guarda o id do trilho para que se possa verificar se o mesmo é favorito e ir buscar os comentarios
    }

    // TODO: Rename and change types and number of parameters
    public static TrilhoSelecionado newInstance(String param1, String param2) {
        TrilhoSelecionado fragment = new TrilhoSelecionado(xTrilho);
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Alterar dados das views com os dados do trilho
        View logedIn = inflater.inflate(R.layout.fragment_trilho_selecionado_login, container, false);
        View def = inflater.inflate(R.layout.fragment_trilho_selecionado_def, container, false);

        if(MainActivity.logedIn==true){
            defineInfoTrilhoLogedIn(logedIn);
            return logedIn;
        }else{
            defineInfoTrilhoDef(def);
            return def;
        }
    }


    //Preenche as views com os dados do trilho
    private void defineInfoTrilhoLogedIn(View view) {
        context = Trilhos.rvTrilhos.getContext();

        //Scroll mapa e fts
        HorizontalScrollView mapaNFotosHolder = view.findViewById(R.id.mapNphotoHolder);
        mapaNFotosHolder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL ){
                    //Faz com que os scroll de "Snap" nos items
                    int scrollX = v.getScrollX();
                    int widthItem = getResources().getDisplayMetrics().widthPixels;
                    int mActiveFeature = ((scrollX + (widthItem/2))/widthItem);
                    int scrollTo = (mActiveFeature*widthItem);
                    mapaNFotosHolder.smoothScrollTo(scrollTo, 0);
                    return true;
                }
                else{
                    return false;
                }
            }
        });

        //Conteudo Comentario
        contentC = view.findViewById(R.id.conteudoComentario);
        //Controle Conteudo Comentario
        contentC.addTextChangedListener(new TextWatcher() {
            int maxChars = 25;

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int end = start + count;

                // Check if the number of characters on the last line is greater than the maximum
                int lineEnd = (start > 0) ? start - 1 : end;
                int lineLength = lineEnd - start;
                if (lineLength >= maxChars) {
                    String newLine = s.toString().substring(start, lineEnd);
                    int lastSpace = newLine.lastIndexOf(" ");
                    if (lastSpace != -1) {
                        contentC.getText().delete(start + lastSpace, lineEnd);
                    } else {
                        contentC.append("\n");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}
        });

        //Define o Recycler para os comentarios
        rvComent = view.findViewById(recyclerComent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rvComent.setAdapter(new ComentarioAdapter(idTrilho));
                        rvComent.setVerticalScrollBarEnabled(false);
                    }
                });
            }
        }).start();

        //Hide keyboard on touch
        RelativeLayout relativeLayout = view.findViewById(R.id.conteudoTrilhoSelect);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(view == relativeLayout && motionEvent.getAction()== MotionEvent.ACTION_DOWN){
                    MainActivity.imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
                }
                return false;
            }
        });

        //Mapa

        //Fotos do trilho(Max 4)
        ImageView imgTrilho1 = view.findViewById(R.id.imgTrilhoS_1), imgTrilho2 = view.findViewById(R.id.imgTrilhoS_2),
                  imgTrilho3 = view.findViewById(R.id.imgTrilhoS_3), imgTrilho4 = view.findViewById(R.id.imgTrilhoS_4);
        imgTrilho1.setScaleType(ImageView.ScaleType.FIT_XY);
        imgTrilho2.setScaleType(ImageView.ScaleType.FIT_XY);
        imgTrilho3.setScaleType(ImageView.ScaleType.FIT_XY);
        imgTrilho4.setScaleType(ImageView.ScaleType.FIT_XY);

        //Define o tamanho do mapa e das fotos do trilho
        imgTrilho1.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;
        imgTrilho2.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;
        imgTrilho3.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;
        imgTrilho4.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;

        //Verifica a quantidade de imagens associadas ao trilho e remove as ImageViews não utilizadas
        for (int y = 0; y < 4; y++) {
            if (MainActivity.trilhos.get(xTrilho).getImgTrilho(y)==null) {
                switch (y) {
                    case 0:
                        imgTrilho1.setVisibility(View.GONE);
                        break;
                    case 1:
                        imgTrilho2.setVisibility(View.GONE);
                        break;
                    case 2:
                        imgTrilho3.setVisibility(View.GONE);
                        break;
                    case 3:
                        imgTrilho4.setVisibility(View.GONE);
                        break;
                }
            } else {
                switch (y) {
                    case 0:
                        AtomicBoolean temp = new AtomicBoolean(false);
                        int finalY = y;
                        MainActivity.bdThread.execute(() -> {
                            try{
                                imgTrilho1.setImageDrawable(MainActivity.imgAdapter.decode(MainActivity.trilhos.get(xTrilho).getImgTrilho(finalY),MainActivity.trilhos.get(xTrilho).getIdTrilho()+MainActivity.trilhos.get(xTrilho).getTituloTrilho()+ finalY,getContext()));
                                temp.set(true);
                            }catch (Exception e){
                                temp.set(true);
                            }
                        });
                        while (temp.get()==false){
                            try{
                                Thread.sleep(100);
                            }catch (Exception e ){
                            }
                        }
                        break;
                    case 1:
                         AtomicBoolean temp2 = new AtomicBoolean(false);
                         int finalY2 = y;
                         MainActivity.bdThread.execute(() -> {
                             try{
                                 imgTrilho2.setImageDrawable(MainActivity.imgAdapter.decode(MainActivity.trilhos.get(xTrilho).getImgTrilho(finalY2),MainActivity.trilhos.get(xTrilho).getIdTrilho()+MainActivity.trilhos.get(xTrilho).getTituloTrilho()+ finalY2,getContext()));
                                 temp2.set(true);
                             }catch (Exception e){
                                 temp2.set(true);
                             }
                         });
                        while (temp2.get()==false){
                            try{
                                Thread.sleep(100);
                            }catch (Exception e ){
                            }
                        }
                         break;
                    case 2:
                        AtomicBoolean temp3 = new AtomicBoolean(false);
                        int finalY3 = y;
                        MainActivity.bdThread.execute(() -> {
                            try{
                                imgTrilho3.setImageDrawable(MainActivity.imgAdapter.decode(MainActivity.trilhos.get(xTrilho).getImgTrilho(finalY3),MainActivity.trilhos.get(xTrilho).getIdTrilho()+MainActivity.trilhos.get(xTrilho).getTituloTrilho()+ finalY3,getContext()));
                                temp3.set(true);
                            }catch (Exception e){
                                temp3.set(true);
                            }
                        });
                        while (temp3.get()==false){
                            try{
                                Thread.sleep(100);
                            }catch (Exception e ){
                            }
                        }
                        break;
                    case 3:
                        AtomicBoolean temp4 = new AtomicBoolean(false);
                        int finalY4 = y;
                        MainActivity.bdThread.execute(() -> {
                            try{
                                imgTrilho4.setImageDrawable(MainActivity.imgAdapter.decode(MainActivity.trilhos.get(xTrilho).getImgTrilho(finalY4),MainActivity.trilhos.get(xTrilho).getIdTrilho()+MainActivity.trilhos.get(xTrilho).getTituloTrilho()+ finalY4,getContext()));
                                temp4.set(true);
                            }catch (Exception e){
                                temp4.set(true);
                            }
                        });
                        while (temp4.get()==false){
                            try{
                                Thread.sleep(100);
                            }catch (Exception e ){
                            }
                        }
                        break;
                }
            }

            //Verificar estado "Favorito"
            AtomicBoolean temp = new AtomicBoolean(false);
            ImageButton bFav = view.findViewById(R.id.bFavorito);


            MainActivity.bdThread.execute(() -> {
                try{
                    if ((int)MainActivity.bd.getValue("favtrail","userId","trailId",String.valueOf(idTrilho),"userId",String.valueOf(MainActivity.userId)) == MainActivity.userId) {
                        bFav.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.white))));
                        temp.set(true);
                    } else {
                        bFav.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.black))));
                        temp.set(true);
                    }
                }catch (Exception e ) {
                    bFav.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.black))));
                    temp.set(true);
                }
            });

            while (temp.get() == false){
              try{
                  Thread.sleep(100);
              }catch (Exception e ){
              }
            }

            //Textos
            TextView tituloTrilhoS = view.findViewById(R.id.tituloTrilhoS),
                    distanciaTrilho = view.findViewById(R.id.distanciaTrilhoS),
                    descricaoTrilho = view.findViewById(R.id.descricaoTrilho);
            tituloTrilhoS.setText(MainActivity.trilhos.get(xTrilho).getTituloTrilho());
            distanciaTrilho.setText(String.valueOf(MainActivity.trilhos.get(xTrilho).getDistanciaTrilho())+" km");
            descricaoTrilho.setText(MainActivity.trilhos.get(xTrilho).getDescricaoTrilho());
            tituloDescricao = view.findViewById(R.id.tituloDescricao);
            tituloComentarios = view.findViewById(R.id.tituloComentarios);

            //Tamanho letra
            tituloDescricao.setTextSize(7 * getResources().getDisplayMetrics().density);
            tituloComentarios.setTextSize(7 * getResources().getDisplayMetrics().density);

            //Categorias
            ImageView iconCaminhadaS = view.findViewById(R.id.iconPéS);
            ImageView iconBicicletaS = view.findViewById(R.id.iconBikeS);
            ImageView iconMotoCrossS = view.findViewById(R.id.iconMotoCrossS);
            ImageView iconMotoQuatroS = view.findViewById(R.id.iconMotoQuatroS);

            iconMotoQuatroS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.black))));
            iconMotoCrossS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.black))));
            iconBicicletaS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.black))));
            iconCaminhadaS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.black))));


            //Icon Marcacao
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
            Image image = ImageFactory.INSTANCE.fromBitmap(bitmap);

            //Mapa
            mapOptions = new MapOptions("5xx6AW7jThtbA0ZG1OnqCT6ANVFSjyRY");
            mapFragment = MapFragment.Companion.newInstance(mapOptions);


            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mapaCriacaoLogin, mapFragment)
                    .commit();

            mapFragment.getMapAsync(new MapReadyCallback() {
                @Override
                public void onMapReady(@NonNull TomTomMap tomTomMap) {
                    mapFragment.getZoomControlsView().setVisible(true);

                    mapFragment.getView().post(new Runnable() {
                        @Override
                        public void run() {

                            ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
                            params.width = Resources.getSystem().getDisplayMetrics().widthPixels;;
                            mapFragment.getView().setLayoutParams(params);
                        }
                    });



                    if (MainActivity.lang == 0) {
                        tomTomMap.setLanguage(Locale.forLanguageTag("pt-PT"));
                    } else {
                        tomTomMap.setLanguage(Locale.forLanguageTag("en-GB"));
                    }

                    GeoPoint geoPoint = new GeoPoint(MainActivity.trilhos.get(xTrilho).getLatitude(),MainActivity.trilhos.get(xTrilho).getLongitude());
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

                    tomTomMap.addMarker(markerOptions);


                    tomTomMap.zoomToMarkers();


                }});


            //Butao Comentarios e Fav
            bComfirmC = view.findViewById(R.id.bConfirmarComentario);
            bComfirmC.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.white))));
            bComfirmC.setTextSize((float) (3.2 * getResources().getDisplayMetrics().density));

            //Butão Comfirmar Comentario
            bComfirmC.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if(view == bComfirmC && motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                        //Verificação conteudo comentario
                        if(contentC.getText().length()>0){
                            //Guardar os dados do comentario na Bd
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                  MainActivity.bd.insertComment(MainActivity.userId,MainActivity.trilhos.get(xTrilho).getIdTrilho(),contentC.getText().toString());
                                }
                            }).start();

                        }else{
                            //colocar maneira de avisar o user de que o campo esta fazio
                            return false;
                        }

                        //Limpa o texto e fecha o teclado
                        contentC.setText("");
                        MainActivity.imm.hideSoftInputFromWindow(bComfirmC.getWindowToken(), 0);

                        //Atualiza os Comentarios
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        rvComent.removeAllViews();
                                        rvComent.setAdapter(new ComentarioAdapter(idTrilho));
                                    }
                                });
                            }
                        }).start();

                        return true;
                    }
                    return false;
                }
            });

            //Butao Favorito
            bFav.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    //Verificação do estado do trilho de como favorito na bd e alteração do msm
                    AtomicBoolean favUpdate = new AtomicBoolean(false);
                    if(v == bFav && event.getAction() == MotionEvent.ACTION_DOWN){
                        if(bFav.getBackgroundTintList()==ColorStateList.valueOf((getResources().getColor(R.color.white)))){
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                AtomicBoolean temp = new AtomicBoolean(false);
                                                MainActivity.bdThread.execute(()->{
                                                    MainActivity.bd.deleteEntry("favtrail", "userId", String.valueOf(MainActivity.userId), "trailId", String.valueOf(idTrilho));
                                                    temp.set(true);
                                                });
                                                while (temp.get() == false){
                                                    try{
                                                        Thread.sleep(100);
                                                    }catch (Exception e){
                                                    }
                                                }
                                                bFav.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.black))));
                                                favUpdate.set(true);
                                            } catch (Exception e) {
                                                favUpdate.set(true);
                                            }
                                        }
                                    });
                                }
                            }).start();
                        }else {
                            try{
                                AtomicBoolean temp = new AtomicBoolean(false);
                                MainActivity.bdThread.execute(()->{
                                    MainActivity.bd.insertFav(idTrilho, MainActivity.userId);
                                    temp.set(true);
                                });
                                while (temp.get() == false){
                                    try{
                                        Thread.sleep(100);
                                    }catch (Exception e){
                                    }
                                }
                                bFav.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.white))));
                                favUpdate.set(true);
                             } catch (Exception e) {
                                favUpdate.set(true);
                             }
                        }


                    }
                    return false;
                }
            });

            //Alteração Lingua
            langUpdate();

            //Categorias Trilho
            for (int x = 0; x < MainActivity.trilhos.get(xTrilho).getCatgTrilho().length; x++) {
                char tempChar;
                switch (x) {
                    case 0:
                        tempChar = MainActivity.trilhos.get(xTrilho).getCatgTrilho()[x];
                        if (tempChar == '1') {
                            iconCaminhadaS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.white))));
                        }
                        break;
                    case 1:
                        tempChar = MainActivity.trilhos.get(xTrilho).getCatgTrilho()[x];
                        if (tempChar == '1') {
                            iconBicicletaS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.white))));
                        }
                        break;
                    case 2:
                        tempChar = MainActivity.trilhos.get(xTrilho).getCatgTrilho()[x];
                        if (tempChar == '1') {
                            iconMotoCrossS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.white))));
                        }
                        break;
                    case 3:
                        tempChar = MainActivity.trilhos.get(xTrilho).getCatgTrilho()[x];
                        if (tempChar == '1') {
                            iconMotoQuatroS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.white))));
                        }
                        break;
                }
            }
        }
    }

    private void defineInfoTrilhoDef(View view){
        context = Trilhos.rvTrilhos.getContext();

        //Scroll mapa e fts
        HorizontalScrollView mapaNFotosHolder = view.findViewById(R.id.mapNphotoHolder);
        mapaNFotosHolder.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL ){
                    //Faz com que os scroll de "Snap" nos items
                    int scrollX = v.getScrollX();
                    int widthItem = getResources().getDisplayMetrics().widthPixels;
                    int mActiveFeature = ((scrollX + (widthItem/2))/widthItem);
                    int scrollTo = (mActiveFeature*widthItem);
                    mapaNFotosHolder.smoothScrollTo(scrollTo, 0);
                    return true;
                }
                else{
                    return false;
                }
            }
        });

        //Hide keyboard on touch
        RelativeLayout relativeLayout = view.findViewById(R.id.conteudoTrilhoSelect);
        relativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(view == relativeLayout && motionEvent.getAction()== MotionEvent.ACTION_DOWN){
                    MainActivity.imm.hideSoftInputFromWindow(relativeLayout.getWindowToken(), 0);
                }
                return false;
            }
        });

        //Mapa
        //Mapa
        //Icon Marcacao
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pin);
        Image image = ImageFactory.INSTANCE.fromBitmap(bitmap);

        //Mapa
        mapOptions = new MapOptions("5xx6AW7jThtbA0ZG1OnqCT6ANVFSjyRY");
        mapFragment = MapFragment.Companion.newInstance(mapOptions);


        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.mapaCriacaoDef, mapFragment)
                .commit();

        mapFragment.getMapAsync(new MapReadyCallback() {
            @Override
            public void onMapReady(@NonNull TomTomMap tomTomMap) {
                mapFragment.getZoomControlsView().setVisible(true);

                mapFragment.getView().post(new Runnable() {
                    @Override
                    public void run() {

                        ViewGroup.LayoutParams params = mapFragment.getView().getLayoutParams();
                        params.width = Resources.getSystem().getDisplayMetrics().widthPixels;;
                        mapFragment.getView().setLayoutParams(params);
                    }
                });


                if (MainActivity.lang == 0) {
                    tomTomMap.setLanguage(Locale.forLanguageTag("pt-PT"));
                } else {
                    tomTomMap.setLanguage(Locale.forLanguageTag("en-GB"));
                }

                GeoPoint geoPoint = new GeoPoint(MainActivity.trilhos.get(xTrilho).getLatitude(),MainActivity.trilhos.get(xTrilho).getLongitude());
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

                tomTomMap.addMarker(markerOptions);


                tomTomMap.zoomToMarkers();


            }});
        //Fotos do trilho(Max 4)
        ImageView imgTrilho1 = view.findViewById(R.id.imgTrilhoS_1), imgTrilho2 = view.findViewById(R.id.imgTrilhoS_2),
                imgTrilho3 = view.findViewById(R.id.imgTrilhoS_3), imgTrilho4 = view.findViewById(R.id.imgTrilhoS_4);
        imgTrilho1.setScaleType(ImageView.ScaleType.FIT_XY);
        imgTrilho2.setScaleType(ImageView.ScaleType.FIT_XY);
        imgTrilho3.setScaleType(ImageView.ScaleType.FIT_XY);
        imgTrilho4.setScaleType(ImageView.ScaleType.FIT_XY);

        //Define o tamanho do mapa e das fotos do trilho
        imgTrilho1.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;
        imgTrilho2.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;
        imgTrilho3.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;
        imgTrilho4.getLayoutParams().width = Resources.getSystem().getDisplayMetrics().widthPixels;

        //Verifica a quantidade de imagens associadas ao trilho e remove as ImageViews não utilizadas
        for (int y = 0; y <4; y++) {
            if (MainActivity.trilhos.get(xTrilho).getImgTrilho(y)==null) {
                switch (y) {
                    case 0:
                        imgTrilho1.setVisibility(View.GONE);
                        break;
                    case 1:
                        imgTrilho2.setVisibility(View.GONE);
                        break;
                    case 2:
                        imgTrilho3.setVisibility(View.GONE);
                        break;
                    case 3:
                        imgTrilho4.setVisibility(View.GONE);
                        break;
                }
            } else {
                switch (y) {
                    case 0:
                        AtomicBoolean temp = new AtomicBoolean(false);
                        int finalY = y;
                        MainActivity.bdThread.execute(() -> {
                            try{
                                imgTrilho1.setImageDrawable(MainActivity.imgAdapter.decode(MainActivity.trilhos.get(xTrilho).getImgTrilho(finalY),MainActivity.trilhos.get(xTrilho).getIdTrilho()+MainActivity.trilhos.get(xTrilho).getTituloTrilho()+ finalY,getContext()));
                                temp.set(true);
                            }catch (Exception e){
                                temp.set(true);
                            }
                        });
                        while (temp.get()==false){
                            try{
                                Thread.sleep(100);
                            }catch (Exception e ){
                            }
                        }
                        break;

                    case 1:
                        AtomicBoolean temp2 = new AtomicBoolean(false);
                        int finalY2 = y;
                        MainActivity.bdThread.execute(() -> {
                            try{
                                imgTrilho2.setImageDrawable(MainActivity.imgAdapter.decode(MainActivity.trilhos.get(xTrilho).getImgTrilho(finalY2),MainActivity.trilhos.get(xTrilho).getIdTrilho()+MainActivity.trilhos.get(xTrilho).getTituloTrilho()+ finalY2,getContext()));
                                temp2.set(true);
                            }catch (Exception e){
                                temp2.set(true);
                            }
                        });
                        while (temp2.get()==false){
                            try{
                                Thread.sleep(100);
                            }catch (Exception e ){
                            }
                        }
                        break;

                    case 2:
                        AtomicBoolean temp3 = new AtomicBoolean(false);
                        int finalY3 = y;
                        MainActivity.bdThread.execute(() -> {
                            try{
                                imgTrilho3.setImageDrawable(MainActivity.imgAdapter.decode(MainActivity.trilhos.get(xTrilho).getImgTrilho(finalY3),MainActivity.trilhos.get(xTrilho).getIdTrilho()+MainActivity.trilhos.get(xTrilho).getTituloTrilho()+ finalY3,getContext()));
                                temp3.set(true);
                            }catch (Exception e){
                                temp3.set(true);
                            }
                        });
                        while (temp3.get()==false){
                            try{
                                Thread.sleep(100);
                            }catch (Exception e ){
                            }
                        }
                        break;

                    case 3:
                        AtomicBoolean temp4 = new AtomicBoolean(false);
                        int finalY4 = y;
                        MainActivity.bdThread.execute(() -> {
                            try{
                                imgTrilho4.setImageDrawable(MainActivity.imgAdapter.decode(MainActivity.trilhos.get(xTrilho).getImgTrilho(finalY4),MainActivity.trilhos.get(xTrilho).getIdTrilho()+MainActivity.trilhos.get(xTrilho).getTituloTrilho()+ finalY4,getContext()));
                                temp4.set(true);
                            }catch (Exception e){
                                temp4.set(true);
                            }
                        });
                        while (temp4.get()==false){
                            try{
                                Thread.sleep(100);
                            }catch (Exception e ){
                            }
                        }
                        break;
                }
            }

            //Textos
            TextView tituloTrilhoS = view.findViewById(R.id.tituloTrilhoS),
                    distanciaTrilho = view.findViewById(R.id.distanciaTrilhoS),
                    descricaoTrilho = view.findViewById(R.id.descricaoTrilho);
            tituloTrilhoS.setText(MainActivity.trilhos.get(xTrilho).getTituloTrilho());
            distanciaTrilho.setText(String.valueOf(MainActivity.trilhos.get(xTrilho).getDistanciaTrilho())+" km");
            descricaoTrilho.setText(MainActivity.trilhos.get(xTrilho).getDescricaoTrilho());
            tituloDescricao = view.findViewById(R.id.tituloDescricao);
            tituloComentarios = view.findViewById(R.id.tituloComentarios);
            avisoLoginTrilhoS = view.findViewById(R.id.avisoLoginTrilhoSelecionado);

            //Tamanho letra
            tituloDescricao.setTextSize(7 * getResources().getDisplayMetrics().density);
            tituloComentarios.setTextSize(7 * getResources().getDisplayMetrics().density);
            avisoLoginTrilhoS.setTextSize(6 * getResources().getDisplayMetrics().density);

            //Categorias
            ImageView iconCaminhadaS = view.findViewById(R.id.iconPéS);
            ImageView iconBicicletaS = view.findViewById(R.id.iconBikeS);
            ImageView iconMotoCrossS = view.findViewById(R.id.iconMotoCrossS);
            ImageView iconMotoQuatroS = view.findViewById(R.id.iconMotoQuatroS);

            iconMotoQuatroS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.black))));
            iconMotoCrossS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.black))));
            iconBicicletaS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.black))));
            iconCaminhadaS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.black))));

            //Alteração Lingua
            langUpdate();

            for (int x = 0; x < MainActivity.trilhos.get(xTrilho).getCatgTrilho().length; x++) {
                char temp;
                switch (x) {
                    case 0:
                        temp = MainActivity.trilhos.get(xTrilho).getCatgTrilho()[x];
                        if (temp == '1') {
                            iconCaminhadaS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.white))));
                        }
                        break;
                    case 1:
                        temp = MainActivity.trilhos.get(xTrilho).getCatgTrilho()[x];
                        if (temp == '1') {
                            iconBicicletaS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.white))));
                        }
                        break;
                    case 2:
                        temp = MainActivity.trilhos.get(xTrilho).getCatgTrilho()[x];
                        if (temp == '1') {
                            iconMotoCrossS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.white))));
                        }
                        break;
                    case 3:
                        temp = MainActivity.trilhos.get(xTrilho).getCatgTrilho()[x];
                        if (temp == '1') {
                            iconMotoQuatroS.setBackgroundTintList(ColorStateList.valueOf((getResources().getColor(R.color.white))));
                        }
                        break;
                }
            }
        }
    }

    public static void langUpdate(){

        if(MainActivity.lang == 0){
            tituloDescricao.setText(R.string.titulo_desc_PT);
            tituloComentarios.setText(R.string.titulo_comment_PT);
            if(MainActivity.logedIn==true){
                bComfirmC.setText(R.string.texto_bConfirmarComment_PT);
                contentC.setHint(R.string.hint_campo_conteudoComment_PT);

            }else{
                avisoLoginTrilhoS.setText(R.string.aviso_loginTrilhoS_PT);
            }

        }else{
            tituloDescricao.setText(R.string.titulo_desc_EN);
            tituloComentarios.setText(R.string.titulo_comment_EN);
            if(MainActivity.logedIn==true){
                bComfirmC.setText(R.string.texto_bConfirmarComment_EN);
                contentC.setHint(R.string.hint_campo_conteudoComment_EN);
            }else{
                avisoLoginTrilhoS.setText(R.string.aviso_loginTrilhoS_EN);
            }
        }

    }
}