package com.arimayi.facturation.entities;

public enum TauxTVA {
    TVA_0(0.0),
    TVA_5_5(5.5),
    TVA_10(10.0),
    TVA_20(20.0);

    private final double taux;

    TauxTVA(double taux) {
        this.taux = taux;
    }

    public double getTaux() {
        return taux;
    }
}

