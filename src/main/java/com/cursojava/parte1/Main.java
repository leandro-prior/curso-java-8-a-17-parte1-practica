package com.cursojava.parte1;

public class Main {
    public static void main(String[] args) {
        var service = new PedidoService();
        var pedidos = service.listarTodos();
        System.out.println("=== LISTADO DE PEDIDOS ===");
        for (var p : pedidos) {
            System.out.println(p + " -> puntos: " + service.puntosPorEstado(p.getEstado()));
        }
        var total = 0.0;
        for (var p : pedidos) {
            total += p.getImporte();
        }
        var informe = """
                ==== INFORME ====
                Total pedidos: %d
                Importe total: %.2f
                """.formatted(pedidos.size(), total);
        System.out.println(informe);
    }
}