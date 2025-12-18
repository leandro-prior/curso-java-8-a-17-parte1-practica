package com.cursojava.parte1;

public class Main {
    public static void main(String[] args) {
        var service = new PedidoService();
        var pedidos = service.listarTodos();
        System.out.println("=== LISTADO DE PEDIDOS ===");
        for (var p : pedidos) {
            System.out.println(p + " -> puntos: " + service.puntosPorEstado(p.getEstado()));
        }
        System.out.println("\n=== BUSCAR PEDIDO EXISTENTE ===");
        service.buscarPorId(1L)
                .ifPresentOrElse(
                        p -> System.out.println("Encontrado: " + p),
                        () -> System.out.println("No encontrado"));
        System.out.println("\n=== BUSCAR CON FALLBACK ===");
        service.buscarPorIdConFallback(99L)
                .ifPresent(p -> System.out.println("Resultado fallback: " + p));
        var total = 0.0;
        for (var p : pedidos) {
            total += p.getImporte();
        }
        var informe = "==== INFORME ====\n" +
                "Total pedidos: " + pedidos.size() + "\n" +
                "Importe total: " + total + "\n";
        System.out.println("\n" + informe);
    }
}