package com.example.wildpaths.trilhos.selecionado.comentario;

public class Comentario {
    //dados trilho
    private String nomeUtilizador;
    private String conteudo;
    private byte [] ftUtilizador;


    public String getnomeUtilizador() { return nomeUtilizador; }

    public String getConteudo(){ return conteudo; }

    public byte [] getFtUtilizador(){ return ftUtilizador; }


    public static class GenComentario{

        private String nomeUtilizador;
        private String conteudo;
        private byte[] ftUtilizador;

        public Comentario.GenComentario setnomeUtilizador(String nomeUtilizador){
            this.nomeUtilizador = nomeUtilizador;
            return this;
        }

        public Comentario.GenComentario setConteudo(String conteudo){
            this.conteudo = conteudo;
            return this;
        }

        public Comentario.GenComentario setFtUtilizador(byte [] ftUtilizador){
            this.ftUtilizador = ftUtilizador;
            return this;
        }



        private GenComentario(){}

        public static Comentario.GenComentario gen(){ return new Comentario.GenComentario(); }

        public Comentario output(){
            Comentario comentario = new Comentario();
            comentario.nomeUtilizador = nomeUtilizador;
            comentario.conteudo = conteudo;
            comentario.ftUtilizador = ftUtilizador;

            return comentario;
        }
    }
}
