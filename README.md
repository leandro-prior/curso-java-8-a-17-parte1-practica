# 📘 Práctica Parte 1 — Modernización de Código: De Java 8 a Java 17  
### Curso Java 8 → 21 · Parte 1 (Actualizando código legacy)

En esta práctica vas a trabajar sobre un proyecto Java escrito con estilo **Java 8 “clásico”**, con POJOs verbosos, Optional mal utilizado, concatenación de strings, estructuras repetitivas, etc.  
Tu misión será **actualizar progresivamente** este código para adoptar los estándares modernos de **Java 9–17**.

El objetivo es que aprendas a:

- Reconocer patrones antiguos y reemplazarlos.
- Escribir código más limpio, seguro e inmutable.
- Aprovechar las mejoras del lenguaje sin reescribir la arquitectura.

---

## 🧩 Contenido de la práctica

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

## 🚀 0. Preparación del entorno (GitHub Codespaces)

Para esta práctica **NO hace falta que instales nada en tu máquina**.  
Trabajaremos directamente en la nube con **GitHub Codespaces**.

### Pasos

1. Entra en el repositorio de la práctica en GitHub.  
2. Haz clic en el botón verde **“Code”**.
3. Selecciona la pestaña **“Codespaces”**.
4. Pulsa **“Create codespace on main”** (o en la rama indicada por el profesor).
5. Espera a que se abra el entorno de desarrollo en el navegador.

El Codespace ya tendrá:

- Java **17** instalado.
- Maven configurado.
- Editor tipo VS Code listo para trabajar.

Para comprobar que todo funciona, abre una terminal en el Codespace y ejecuta:


mvn -q -DskipTests package


Y luego ejecuta el `Main` desde el propio editor (botón de “Run” o `Run > Start Debugging`, según configuración).

---

## ▶️ 1. Ejecutar el proyecto base (estilo Java 8)

1. Abre `src/main/java/.../Main.java` en el Codespace.
2. Ejecuta el programa desde el botón de ejecución o con la configuración de run que te dé el profesor.

Deberías ver en la consola:

* Un listado de pedidos.
* Búsqueda de un pedido existente.
* Búsqueda de un pedido inexistente.
* Un informe final por consola.

✏️ **Tarea:**
En `Main.java`, deja un comentario respondiendo:

```java
// ¿Qué parte del código te parece más anticuada, verbosa o innecesariamente compleja?
```

---

## 🧾 2. Modernizar colecciones con `List.of`, `Set.of`, `Map.of`

En `PedidoService.java` verás algo parecido a:

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

Ahora la lista es **inmutable**.

### ✔️ Tareas

* [ ] Reemplaza la creación manual por `List.of(...)`.
* [ ] Intenta hacer `pedidos.add(...)` y observa la excepción.
* [ ] Escribe un comentario explicando por qué **la inmutabilidad es buena aquí**.

---

## 🎯 3. Modernizar Optional: `ifPresentOrElse`, `or`, `stream()`

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

Incluye también un ejemplo con `optional.stream()`:

```java
service.buscarPorId(1L)
    .stream()
    .map(Pedido::getCliente)
    .forEach(System.out::println);
```

### ✔️ Tareas

* [ ] Refactorizar el Optional de Main con `ifPresentOrElse`
* [ ] Implementar y usar `buscarPorIdConFallback`
* [ ] Usar `stream()` en Optional en un ejemplo simple

---

## 🔎 4. Usar inferencia de tipos con `var`

Cambia código como:

```java
List<Pedido> pedidos = service.listarTodos();
```

por:

```java
var pedidos = service.listarTodos();
```

Pero **NO uses `var` donde el tipo no se entienda claramente** solo leyendo el lado derecho.

### ✔️ Tareas

* [ ] Cambia al menos 3 variables a `var`
* [ ] Deja un comentario explicando un caso donde decidiste **no usar `var`**

---

## 🔄 5. Convertir un switch clásico en switch expression

En `PedidoService`, crea un método:

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

Después, desde `Main`, muestra estos puntos por consola.

### ✔️ Tareas

* [ ] Implementar `puntosPorEstado` con switch clásico
* [ ] Convertirlo a switch expression
* [ ] Mostrar los puntos por consola para varios pedidos

---

## 🧱 6. Reemplazar concatenaciones por TEXT BLOCKS (`"""`)

En `Main.java` encontrarás un informe de varias líneas construido con concatenaciones y `\n`.

Cámbialo por algo como:

```java
String informe = """
        ==== INFORME DE PEDIDOS ====
        Total pedidos: %d
        Importe total: %.2f
        """.formatted(totalPedidos, totalImporte);
```

### ✔️ Tareas

* [ ] Sustituye concatenaciones por un text block
* [ ] Usa `.formatted(...)` para insertar los datos

---

## 🧬 7. Convertir `Pedido` en un **record**

Antes tendrás una clase POJO con:

* Campos privados.
* Constructor.
* Getters.
* `toString`.

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

* [ ] Sustituir toda la clase por un `record`
* [ ] Añadir validación en el constructor compacto (no permitir importes negativos)

---

## 🛡️ 8. Convertir `ResultadoBusqueda` en **sealed interface + records**

Actualmente `ResultadoBusqueda` será una clase que admite `null` en algunos campos. Refactorízala siguiendo este modelo:

```java
public sealed interface ResultadoBusqueda
        permits ResultadoExito, ResultadoError {}

public record ResultadoExito(Pedido pedido) implements ResultadoBusqueda {}
public record ResultadoError(String mensaje) implements ResultadoBusqueda {}
```

Refactoriza `PedidoService.buscarDetalle(Long id)` para devolver `ResultadoBusqueda`.

En `Main.java`, usa un `switch` con pattern matching:

```java
ResultadoBusqueda res = service.buscarDetalle(1L);

String msg = switch (res) {
    case ResultadoExito ex -> "Detalle: " + ex.pedido();
    case ResultadoError err -> "Error: " + err.mensaje();
};

System.out.println(msg);
```

### ✔️ Tareas

* [ ] Crear la `sealed interface` y los dos `record`
* [ ] Adaptar el método `buscarDetalle` para que los use
* [ ] Usar `switch` con pattern matching en el Main

---

## 📝 9. Reflexión final del alumno

En un comentario dentro de `Main.java` o en un archivo `REFLEXION.md`, responde:

* ¿Qué feature te ha resultado más útil en tu día a día como desarrollador?
* ¿Cuál crees que reduce más líneas de código y por qué?
* ¿Cuál te ha costado más entender o aplicar en esta práctica?
* ¿Qué parte del proyecto crees que se podría mejorar aún más con futuras versiones de Java (por ejemplo, Java 21)?

---

## 🎉 ¡Práctica completada!

Al terminar tendrás:

✔ Código migrado desde Java 8 a Java 17
✔ Uso real de varias features modernas del lenguaje
✔ Menos líneas de código, más claridad y mejor diseño
✔ Experiencia real trabajando con GitHub Codespaces, como en un entorno profesional
