package com.cursojava.parte1;

import java.util.List;
import java.util.Optional;

public class PedidoService {
    private List<Pedido> pedidos = List.of(
            new Pedido(1L, "Juan", 120.50, EstadoPedido.PAGADO),
            new Pedido(2L, "Ana", 75.00, EstadoPedido.PENDIENTE),
            new Pedido(3L, "Luis", 210.30, EstadoPedido.CANCELADO));

    public PedidoService() {
    }

    public List<Pedido> listarTodos() {
        return pedidos;
    }

    public Optional<Pedido> buscarPorId(Long id) {
        for (Pedido p : pedidos) {
            if (p.getId().equals(id)) {
                return Optional.of(p);
            }
        }
        return Optional.empty();
    }

    public Optional<Pedido> buscarPorIdConFallback(Long id) {
        return buscarPorId(id)
                .or(() -> buscarPorId(1L));
    }

    public ResultadoBusqueda buscarDetalle(Long id) {
        Optional<Pedido> pedido = buscarPorId(id);
        if (pedido.isPresent()) {
            return new ResultadoBusqueda(pedido.get());
        }
        return new ResultadoBusqueda("Pedido no encontrado");
    }
}