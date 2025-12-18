package com.cursojava.parte1;

public class Main {
    public static void main(String[] args) {
        var service = new PedidoService();
        var pedidos = service.listarTodos();
        System.out.println("=== LISTADO DE PEDIDOS ===");
        for (var p : pedidos) {
            System.out.println(p + " -> puntos: " + service.puntosPorEstado(p.estado()));
        }
        System.out.println("\n=== BUSCAR DETALLE (SEALED + INSTANCEOF PATTERN MATCHING) ===");
        var res = service.buscarDetalle(99L);
        String msg;
        if (res instanceof ResultadoExito ex) {
            msg = "Detalle: " + ex.pedido();
        } else if (res instanceof ResultadoError err) {
            msg = "Error: " + err.mensaje();
        } else {
            // Por seguridad: en teoría no debería ocurrir porque es sealed
            msg = "Resultado desconocido";
        }
        System.out.println(msg);
        var total = 0.0;
        for (var p : pedidos) {
            total += p.importe();
        }
        var informe = """
                ==== INFORME ====
                Total pedidos: %d
                Importe total: %.2f
                """.formatted(pedidos.size(), total);
        System.out.println("\n" + informe);
    }
}