package com.gupiao;

public class JiaGe {

    private String riQi;
    private double gao;
    private double di;


    public JiaGe(String riQi, double gao, double di) {
        this.riQi = riQi;
        this.gao = gao;
        this.di = di;
    }

    public JiaGe(double gao, double di) {
        this.gao = gao;
        this.di = di;
    }

    public String getRiQi() {
        return riQi;
    }

    public void setRiQi(String riQi) {
        this.riQi = riQi;
    }

    public double getDi() {
        return di;
    }

    public void setDi(double di) {
        this.di = di;
    }

    public double getGao() {
        return gao;
    }

    public void setGao(double gao) {
        this.gao = gao;
    }
}
