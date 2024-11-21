package com.example.wildpaths.trilhos.trilho;

public class Trilho {
    // dados trilho
    private int idTrilho, idCriador;
    private String tituloTrilho;
    private String descricaoTrilho;
    private double distanciaTrilho, latitude,longitude;
    private byte[] imgsTrilho1 = null, imgsTrilho2 = null, imgsTrilho3 = null, imgsTrilho4 = null;
    private char[] catgTrilho = { '0', '0', '0', '0' };


    public int getIdTrilho() {
        return idTrilho;
    }

    public int getIdCreador() {
        return idCriador;
    }

    public String getTituloTrilho() {
        return tituloTrilho;
    }

    public double getDistanciaTrilho() {
        return distanciaTrilho;
    }

    public String getDescricaoTrilho() {
        return descricaoTrilho;
    }

    public char[] getCatgTrilho() {
        return catgTrilho;
    }

    public byte[] getImgTrilho(int nImg) {
        switch (nImg) {
            case 0:
                return imgsTrilho1;
            case 1:
                return imgsTrilho2;
            case 2:
                return imgsTrilho3;
            case 3:
                return imgsTrilho4;
        }
        return null;
    }

    public double getLatitude(){return latitude;}

    public double getLongitude(){return longitude;}

    public static class GenTrilho {

        private int idTrilho, idCriador;
        private String tituloTrilho;
        private String descricaoTrilho;
        private double distanciaTrilho, latitude,longitude;
        private byte[] imgsTrilho1 = null, imgsTrilho2 = null, imgsTrilho3 = null, imgsTrilho4 = null;
        private char[] catgTrilho = { '0', '0', '0', '0' };

        public GenTrilho setIdTrilho(int idTrilho) {
            this.idTrilho = idTrilho;
            return this;
        }

        public GenTrilho setIdCriador(int idCreador) {
            this.idCriador = idCreador;
            return this;
        }

        public GenTrilho setTituloTrilho(String tituloTrilho) {
            this.tituloTrilho = tituloTrilho;
            return this;
        }

        public GenTrilho setDistanciaTrilho(double distanciaTrilho) {
            this.distanciaTrilho = distanciaTrilho;
            return this;
        }

        public GenTrilho setDescricaoTrilho(String descricaoTrilho) {
            this.descricaoTrilho = descricaoTrilho;
            return this;
        }

        public GenTrilho setCatgTrilho(char[] catgTrilho) {
            byte i = 0;
            for (char x : catgTrilho) {
                this.catgTrilho[i] = x;
                catgTrilho[i] = 0;
                i++;
            }
            return this;
        }

        public GenTrilho setImgsTrilho(byte[] imgTrilho1, byte[] imgTrilho2, byte[] imgTrilho3, byte[] imgTrilho4) {
            this.imgsTrilho1 = imgTrilho1;
            this.imgsTrilho2 = imgTrilho2;
            this.imgsTrilho3 = imgTrilho3;
            this.imgsTrilho4 = imgTrilho4;
            return this;
        }

        public GenTrilho setLatitude(double latitude){
            this.latitude = latitude;
            return this;
        }

        public GenTrilho setLongitude (double longitude){
            this.longitude = longitude;
            return this;
        }

        private GenTrilho() {
        }

        public static GenTrilho gen() {
            return new GenTrilho();
        }

        public Trilho output() {
            Trilho trilho = new Trilho();
            trilho.idTrilho = idTrilho;
            trilho.idCriador = idCriador;
            trilho.tituloTrilho = tituloTrilho;
            trilho.descricaoTrilho = descricaoTrilho;
            trilho.imgsTrilho1 = imgsTrilho1;
            trilho.imgsTrilho2 = imgsTrilho2;
            trilho.imgsTrilho3 = imgsTrilho3;
            trilho.imgsTrilho4 = imgsTrilho4;
            trilho.distanciaTrilho = distanciaTrilho;
            trilho.catgTrilho = catgTrilho;
            trilho.latitude = latitude;
            trilho.longitude = longitude;
            return trilho;
        }
    }
}
