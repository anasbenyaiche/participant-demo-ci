package com.formation.demo;

public class CalculRemiseService {

    public double calculerRemise(double montant, int age) {
        if (montant <= 0) {
            return 0;
        }

        if (age < 18) {
            return montant * 0.10;
        }

        if (age >= 65) {
            return montant * 0.20;
        }

        if (montant > 1000) {
            return montant * 0.15;
        }

        return montant * 0.05;
    }
}