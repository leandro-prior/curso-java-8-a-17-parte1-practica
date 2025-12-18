package com.cursojava.parte1;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        PedidoService service = new PedidoService();
        System.out.println("=== LISTADO DE PEDIDOS ===");
        List<Pedido> pedidos = service.listarTodos();
        for (Pedido p : pedidos) {
            System.out.println(p);
        }
        System.out.println("\n=== BUSCAR PEDIDO EXISTENTE ===");
        service.buscarPorId(1L)
                .ifPresentOrElse(
                        p -> System.out.println("Encontrado: " + p),
                        () -> System.out.println("No encontrado"));
        System.out.println("\n=== BUSCAR CON FALLBACK ===");
        service.buscarPorIdConFallback(99L)
                .ifPresent(p -> System.out.println("Resultado fallback: " + p));
        System.out.println("\n=== OPTIONAL STREAM ===");
        service.buscarPorId(1L)
                .stream()
                .map(Pedido::getCliente)
                .forEach(c -> System.out.println("Cliente: " + c));
        System.out.println("\n=== BUSCAR PEDIDO INEXISTENTE ===");
        ResultadoBusqueda res = service.buscarDetalle(99L);
        if (res.getPedido() != null) {
            System.out.println("Detalle: " + res.getPedido());
        } else {
            System.out.println("Error: " + res.getMensajeError());
        }
        double total = 0;
        for (Pedido p : pedidos) {
            total += p.getImporte();
        }
        String informe = "==== INFORME ====\n" +
                "Total pedidos: " + pedidos.size() + "\n" +
                "Importe total: " + total + "\n";
        System.out.println("\n" + informe);
    }
}