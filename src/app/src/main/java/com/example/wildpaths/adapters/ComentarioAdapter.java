package com.example.wildpaths.adapters;

import android.content.Context;
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
import com.example.wildpaths.trilhos.selecionado.TrilhoSelecionado;
import com.example.wildpaths.trilhos.selecionado.comentario.Comentario;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ComentarioViewHolder> {

    //vai guardar os dados dos comentarios
    private List<Comentario> dadosComentarios = new ArrayList<>();
    private Context context;
    private Resources recursos;

    public ComentarioAdapter(int idTrilho){
        //Receber dados de todos os comentarios
        AtomicBoolean commentUpdated = new AtomicBoolean(false);
        MainActivity.bdThread.execute(()->{
            try{
                this.dadosComentarios.clear();
                for(Comentario comentario : MainActivity.bd.getComments(idTrilho)){
                    this.dadosComentarios.add(comentario);
                }
                commentUpdated.set(true);
            }catch (Exception e ){
                commentUpdated.set(true);
            }
        });

        while (commentUpdated.get() == false){
            try{
                Thread.sleep(100);
            }catch (Exception e ){
            }
        }
    }

    @NonNull
    @Override
    public ComentarioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario,parent, false);
        return new ComentarioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentarioViewHolder holder, int position) {
        Comentario comentario = dadosComentarios.get(position);
        holder.bind(comentario);
    }

    @Override
    public int getItemCount() { return dadosComentarios.size(); }

    //Alteração das informações do comentario criado e as views do construtor do comentario
    class ComentarioViewHolder extends RecyclerView.ViewHolder{
        TextView nomeUserComentario, conteudoComentario;
        ImageView ftUtilizadorComent;

        //Associação entre as views locais
        public ComentarioViewHolder(@NonNull View itemView) {
            super(itemView);
            //Associa as views do comentario com novas views para q se possa alterar os valores das mesmas
            nomeUserComentario = itemView.findViewById(R.id.nomeUserComentario);
            conteudoComentario = itemView.findViewById(R.id.conteudoComentario);
            //iamgen de utilizador (ver como funciona o caminho da foto)
            ftUtilizadorComent = itemView.findViewById(R.id.ftUtilizadorComent);
        }

        //Preenche os campos de info do contrutor do comentario com dados obtidos da Base de Dados
        public void bind(Comentario criaComentario){
            context = TrilhoSelecionado.rvComent.getContext();
            recursos = context.getResources();

            nomeUserComentario.setText(criaComentario.getnomeUtilizador());
            conteudoComentario.setText(criaComentario.getConteudo());

            if(criaComentario.getFtUtilizador()==null){
                ftUtilizadorComent.setImageDrawable(recursos.getDrawable(R.drawable.img_user_holder));
            }else {
                ftUtilizadorComent.setImageDrawable(MainActivity.imgAdapter.decode(criaComentario.getFtUtilizador(),/*O nome do ficherio devera ser o nome do user*/"ft" + criaComentario.getnomeUtilizador()+"Comment", MainActivity.navBar.getContext()));
            }
        }
    }

}
