package com.cursojava.parte1;

import java.util.List;
import java.util.Optional;

public class PedidoService {
    private final List<Pedido> pedidos = List.of(
            new Pedido(1L, "Juan", 120.50, EstadoPedido.PAGADO),
            new Pedido(2L, "Ana", 75.00, EstadoPedido.PENDIENTE),
            new Pedido(4L, "Leandro", 75.00, EstadoPedido.PENDIENTE),
            new Pedido(3L, "Luis", 210.30, EstadoPedido.CANCELADO));

    public List<Pedido> listarTodos() {
        return pedidos;
    }

    public Optional<Pedido> buscarPorId(Long id) {
        for (var p : pedidos) {
            if (p.id().equals(id)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public Optional<Pedido> buscarPorIdConFallback(Long id) {
        return buscarPorId(id)
                .or(() -> buscarPorId(1L));
    }

    public int puntosPorEstado(EstadoPedido estado) {
        return switch (estado) {
            case PENDIENTE -> 1;
            case PAGADO -> 5;
            case CANCELADO -> 0;
        };
    }

    public ResultadoBusqueda buscarDetalle(Long id) {
        return buscarPorId(id)
                .<ResultadoBusqueda>map(ResultadoExito::new)
                .orElseGet(() -> new ResultadoError("Pedido no encontrado"));
    }
}