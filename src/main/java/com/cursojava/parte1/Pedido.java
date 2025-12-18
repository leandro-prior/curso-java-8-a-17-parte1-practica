package com.cursojava.parte1;

public record Pedido(Long id, String cliente, double importe, EstadoPedido estado) {
    public Pedido {
        if (importe < 0) {
            throw new IllegalArgumentException("El importe no puede ser negativo");
        }
    }
}