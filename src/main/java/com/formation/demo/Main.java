package com.formation.demo;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) {
        PrixService service = new PrixService();
        BigDecimal ht = new BigDecimal(args.length > 0 ? args[0] : "100");
        System.out.println("Prix HT  : " + ht);
        System.out.println("Prix TTC : " + service.calculerTTC(ht, new BigDecimal("0.20")));
    }
}