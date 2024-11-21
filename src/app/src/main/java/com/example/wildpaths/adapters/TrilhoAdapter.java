package com.example.wildpaths.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wildpaths.MainActivity;
import com.example.wildpaths.R;
import com.example.wildpaths.trilhos.Trilhos;
import com.example.wildpaths.trilhos.trilho.Trilho;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class TrilhoAdapter extends RecyclerView.Adapter<TrilhoAdapter.TrilhoViewHolder>{

    //Vai guardar os dados dos trilhos
    private List<Trilho> dadosTrilhos = new ArrayList<>();
    private Context context;
    private Resources recursos;

    private AtomicBoolean TRAIL_DATA_IS_UPDATED;

    public TrilhoAdapter( int userId ,byte filtro) {
        /*
        0 -> Todos os trilhos disponiveis sem qualquer tipo de especificação
        1 -> Apenas os trilhos criados pelo user
        2 -> Apenas os trilhos marcados como favoritos pelo user
        */
        TRAIL_DATA_IS_UPDATED = new AtomicBoolean(false);
        switch (filtro){

            case 0 :
                //Receber dados de todos os trilhos (sem filtro/todos os trilhos)
                MainActivity.bdThread.execute(() -> {
                    MainActivity.trilhos.clear();
                    dadosTrilhos.clear();
                    try{
                        for(Trilho trilho : MainActivity.bd.getTrails(userId,(byte) 0) ){
                            this.dadosTrilhos.add(trilho);
                            MainActivity.trilhos.add(trilho);
                        }
                    }catch (Exception e){
                        dadosTrilhos = null;
                    }
                    TRAIL_DATA_IS_UPDATED.set(true);
                });
                break;
            case 1:
                //Receber dados dos trilhos criados um user especifico
                MainActivity.bdThread.execute(() -> {
                    MainActivity.trilhos.clear();
                    dadosTrilhos.clear();
                    try{
                    for(Trilho trilho : MainActivity.bd.getTrails(userId,(byte)1) ){
                        this.dadosTrilhos.add(trilho);
                        MainActivity.trilhos.add(trilho);
                    }
                    }catch (Exception e){
                        dadosTrilhos = null;
                    }
                    TRAIL_DATA_IS_UPDATED.set(true);
                });
                break;

            case 2:
                //Receber dados dos trilhos favoritos de um user especificob
                AtomicBoolean dataUpdated = new AtomicBoolean(false);
                MainActivity.bdThread.execute(() -> {
                    MainActivity.trilhos.clear();
                    dadosTrilhos.clear();
                    try{
                      for(Trilho trilho : MainActivity.bd.getTrails(userId,(byte)2)){
                          this.dadosTrilhos.add(trilho);
                          MainActivity.trilhos.add(trilho);
                      }
                      dataUpdated.set(true);
                    }catch (Exception e){
                        dadosTrilhos = null;
                    }
                    TRAIL_DATA_IS_UPDATED.set(true);
                });
                while(dataUpdated.get() == false){
                    try{
                        Thread.sleep(100);
                    }catch (Exception e ){

                    }
                }
                break;
            }
        }

    public boolean getUpdateStatus(){
        return TRAIL_DATA_IS_UPDATED.get();
    }

    @NonNull
    @Override
    public TrilhoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trilho, parent, false);
        return new TrilhoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrilhoViewHolder holder, int position) {
        Trilho trilho = dadosTrilhos.get(position);
        holder.bind(trilho);
    }

    @Override
    public int getItemCount() {
        if(dadosTrilhos == null){
             return 0;
        }
        return dadosTrilhos.size();
    }

    //Alteração das informações do trilho criado
    class TrilhoViewHolder extends RecyclerView.ViewHolder{
        TextView tituloTrilho,distanciaTrilho;
        ImageView imgCapaTrilho,iconCaminhada,iconBicicleta,iconMotoCross,iconMotoQuatro;

        //Associação entre as views locais e as views do construtor do trilho
        public TrilhoViewHolder(@NonNull View itemView) {
            super(itemView);
            //Associa as views do percurso com novas views para q se possa alterar os valores das mesmas
            tituloTrilho = itemView.findViewById(R.id.tituloTrilho);
            distanciaTrilho = itemView.findViewById(R.id.distanciaTrilho);
            //imagem do trilho ( ver como funciona o caminho da foto)
            imgCapaTrilho = itemView.findViewById(R.id.imgTrilho);
            iconCaminhada = itemView.findViewById(R.id.iconPé);
            iconBicicleta = itemView.findViewById(R.id.iconBike);
            iconMotoCross = itemView.findViewById(R.id.iconMotoCross);
            iconMotoQuatro = itemView.findViewById(R.id.iconMotoQuatro);
        }

        //Preenche os campos de info do contrutor do trilho com dados obtidos da Base de Dados
        public void bind(Trilho criaTrilho) {
            if(criaTrilho != null){


             context = Trilhos.rvTrilhos.getContext();
             recursos = context.getResources();

             tituloTrilho.setText(criaTrilho.getTituloTrilho());
             distanciaTrilho.setText(criaTrilho.getDistanciaTrilho()+"Km");

             imgCapaTrilho.setImageDrawable(MainActivity.imgAdapter.decode(criaTrilho.getImgTrilho(0),criaTrilho.getIdTrilho()+criaTrilho.getIdTrilho()+"-1",context));

             //Categorias trilho
             for(int x = 0; x<criaTrilho.getCatgTrilho().length; x++){
                 char temp;
                 switch(x){
                     case 0:
                         temp = criaTrilho.getCatgTrilho()[x];
                         if(temp == '1'){ iconCaminhada.setBackgroundTintList(ColorStateList.valueOf((itemView.getResources().getColor(R.color.white)))); }
                         break;
                     case 1:
                         temp = criaTrilho.getCatgTrilho()[x];
                         if(temp == '1'){ iconBicicleta.setBackgroundTintList(ColorStateList.valueOf((itemView.getResources().getColor(R.color.white)))); }
                         break;
                     case 2:
                         temp = criaTrilho.getCatgTrilho()[x];
                         if(temp == '1'){ iconMotoCross.setBackgroundTintList(ColorStateList.valueOf((itemView.getResources().getColor(R.color.white)))); }
                         break;
                     case 3:
                         temp = criaTrilho.getCatgTrilho()[x];
                         if(temp == '1'){ iconMotoQuatro.setBackgroundTintList(ColorStateList.valueOf((itemView.getResources().getColor(R.color.white)))); }
                         break;
                 }
             }
            }

        }
    }
}
