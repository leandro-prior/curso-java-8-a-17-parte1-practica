---

# 📘 Práctica Parte 1 — Modernización de Código: De Java 8 a Java 17

### Curso Java 8 → 21 · Parte 1 (Actualizando código legacy)

En esta práctica vas a trabajar sobre un proyecto Java escrito con estilo **Java 8 “clásico”**, con POJOs verbosos, Optional mal utilizado, concatenación de strings, estructuras repetitivas, etc.
Tu misión será **actualizar progresivamente** este código para adoptar los estándares modernos de **Java 9–17**.

El objetivo es que aprendas a:

* Reconocer patrones antiguos y reemplazarlos.
* Escribir código más limpio, seguro e inmutable.
* Aprovechar las mejoras del lenguaje sin reescribir la arquitectura.

---

# 🧩 Contenido de la práctica

Actualizarás el proyecto aplicando estas mejoras en orden:

1. **Colecciones inmutables** (`List.of`, `Set.of`, `Map.of`)
2. **Optional moderno** (`ifPresentOrElse`, `or`, `stream`)
3. **Inferencia de tipos con `var`**
4. **Switch expressions**
5. **Text blocks (`"""`)**
6. **Records**
7. **Sealed Classes + Pattern Matching (`instanceof` mejorado)**

Al finalizar, tu código parecerá **de 2025** y no de 2014.

---

# 🚀 0. Preparación del entorno

Requisitos:

* Java **17** o superior.
* IntelliJ, VSCode, Eclipse o cualquier IDE moderno.
* Maven (o usar wrapper si lo incluye el repo).

Clona el repositorio:

```bash
git clone https://github.com/TU_USUARIO/curso-java-8-a-17-parte1-practica.git
cd curso-java-8-a-17-parte1-practica
```

Ejecuta:

```bash
mvn clean package
```

Finalmente, ejecuta el Main del proyecto.

---

# ▶️ 1. Ejecutar el proyecto base (estilo Java 8)

Abre `Main.java` y ejecuta el programa.

Verás:

* Listado de pedidos.
* Búsqueda de un pedido existente.
* Búsqueda de un pedido inexistente.
* Informe final por consola.

✏️ **Tarea:**
En `Main.java`, deja un comentario respondiendo:

```java
// ¿Qué parte del código te parece más anticuada, verbosa o innecesariamente compleja?
```

---

# 🧾 2. Modernizar colecciones con `List.of`, `Set.of`, `Map.of`

En `PedidoService.java` verás algo así:

```java
List<Pedido> pedidos = new ArrayList<>();
pedidos.add(...);
pedidos.add(...);
```

📌 Sustituye por:

```java
List<Pedido> pedidos = List.of(
    new Pedido(...),
    new Pedido(...),
    ...
);
```

Ahora la lista es inmutable.

### ✔️ Tareas

* [ ] Reemplaza la creación manual por `List.of(...)`.
* [ ] Intenta hacer `pedidos.add(...)` y observa la excepción.
* [ ] Escribe un comentario explicando por qué **la inmutabilidad es buena aquí**.

---

# 🎯 3. Modernizar Optional: `ifPresentOrElse`, `or`, `stream()`

Localiza código antiguo como:

```java
Optional<Pedido> opt = service.buscarPorId(1L);
if (opt.isPresent()) {
   System.out.println(opt.get());
} else {
   System.out.println("No encontrado");
}
```

📌 Cámbialo por:

```java
service.buscarPorId(1L)
    .ifPresentOrElse(
        pedido -> System.out.println("Encontrado: " + pedido),
        () -> System.out.println("No encontrado")
    );
```

Añade en `PedidoService`:

```java
public Optional<Pedido> buscarPorIdConFallback(Long id) {
    return buscarPorId(id)
        .or(() -> buscarPorId(1L)); // fallback si no existe
}
```

Incluye también un ejemplo con `optional.stream()`.

### ✔️ Tareas

* [ ] Refactorizar el Optional de Main con `ifPresentOrElse`
* [ ] Implementar y usar `buscarPorIdConFallback`
* [ ] Usar `stream()` en Optional en un ejemplo simple

---

# 🔎 4. Usar inferencia de tipos con `var`

Cambia código como:

```java
List<Pedido> pedidos = service.listarTodos();
```

por:

```java
var pedidos = service.listarTodos();
```

Pero **NO uses `var` donde el tipo no se entienda claramente**.

### ✔️ Tareas

* [ ] Cambia al menos 3 variables a `var`
* [ ] Deja un comentario explicando un caso donde decidiste **no usar `var`**

---

# 🔄 5. Convertir un switch clásico en switch expression

En `PedidoService`, crea:

```java
public int puntosPorEstado(EstadoPedido estado) {
    // versión vieja con switch clásico
}
```

Luego conviértelo en:

```java
public int puntosPorEstado(EstadoPedido estado) {
    return switch (estado) {
        case PENDIENTE  -> 1;
        case PAGADO     -> 5;
        case CANCELADO  -> 0;
    };
}
```

Muéstralo desde `Main`.

### ✔️ Tareas

* [ ] Implementar switch clásico
* [ ] Convertirlo a switch expression
* [ ] Mostrar los puntos por consola

---

# 🧱 6. Reemplazar concatenaciones por TEXT BLOCKS (`"""`)

En `Main.java` encontrarás un informe de varias líneas concatenado manualmente.

Cámbialo por:

```java
String informe = """
        ==== INFORME DE PEDIDOS ====
        Total pedidos: %d
        Importe total: %.2f
        """.formatted(totalPedidos, totalImporte);
```

### ✔️ Tareas

* [ ] Sustituye concatenaciones por text block
* [ ] Usa `.formatted(...)`

---

# 🧬 7. Convertir `Pedido` en un **record**

Antes tendrás un POJO con getters y toString.

Cámbialo por:

```java
public record Pedido(Long id, String cliente, double importe, EstadoPedido estado) {
    public Pedido {
        if (importe < 0) {
            throw new IllegalArgumentException("El importe no puede ser negativo");
        }
    }
}
```

### ✔️ Tareas

* [ ] Sustituir toda la clase por un record
* [ ] Añadir validación en el constructor compacto

---

# 🛡️ 8. Convertir `ResultadoBusqueda` en **sealed interface + records**

Actualmente es una clase con campos que pueden ser null. Sigue este diseño moderno:

```java
public sealed interface ResultadoBusqueda
        permits ResultadoExito, ResultadoError {}

public record ResultadoExito(Pedido pedido) implements ResultadoBusqueda {}
public record ResultadoError(String mensaje) implements ResultadoBusqueda {}
```

Refactoriza `buscarDetalle(id)` para devolver este tipo.

En `Main.java` usa pattern matching:

```java
String msg = switch (res) {
    case ResultadoExito ex -> "Detalle: " + ex.pedido();
    case ResultadoError err -> "Error: " + err.mensaje();
};
System.out.println(msg);
```

### ✔️ Tareas

* [ ] Crear sealed interface + records
* [ ] Adaptar el service
* [ ] Usar pattern matching en el Main

---

# 📝 9. Reflexión final del alumno

En un comentario o archivo `REFLEXION.md`, responde:

* ¿Qué feature te ha resultado más útil?
* ¿Cuál te ha reducido más líneas de código?
* ¿Cuál te ha costado más aprender?
* ¿Qué parte del proyecto mejoraría si también aplicáramos Java 21?

---

# 🎉 ¡Práctica completada!

Al terminar tendrás:

✔ Código migrado desde Java 8 a Java 17
✔ Uso real de todas las features modernas
✔ Mejor diseño, menos líneas, más claridad
✔ Preparación para la Parte 2 del curso

---
