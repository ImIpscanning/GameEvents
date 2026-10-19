---
description: file messages in spanish
---

# config en español

```yaml
settings:
  max-blocks-per-player: 3 # Número máximo de cargadores de chunks por jugador
  auto-save-interval: 300 # Intervalo de guardado automático en segundos (0 para desactivar)

economy:
  deduct-on-place: true # Si se debe cobrar a los jugadores al colocar cargadores de chunks
  place-price: 1000.0 # Precio base para colocar un cargador de chunks
  currency-symbol: "$" # Símbolo de moneda para mostrar en mensajes

blocks:
  basic: # Cargador de chunks básico
    material: CHEST # Material del bloque (nombre del Material de Bukkit)
    display-name: "&a&lCargador de Chunks Básico" # Nombre para mostrar del ítem
    lore:
      - "&7¡Mantén tus chunks cargados incluso cuando estés desconectado!"
      - "&7Clic derecho para gestionar tu cargador de chunks."
      - ""
      - "&eNivel: &f%level%"
      - "&eChunks: &f%chunks%"
    base-duration: 3600 # Duración base en segundos (1 hora = 3600 segundos)
    hologram:
      enabled: true
      lines:
        - "&a&lCargador de Chunks"
        - "&7Propietario: &e%owner%"
        - "&7Nivel: &e%level%"
        - "&7Chunks: &e%chunks%"
        - "&7Tiempo: &e%time%"
        - "&7Bloque: &e%block%"

    levels:
      1:
        chunks: 1
        upgrade-cost: 0
        renewal-cost: 100.0
      2:
        chunks: 4
        upgrade-cost: 2500.0
        renewal-cost: 200.0
      3:
        chunks: 9
        upgrade-cost: 5000.0
        renewal-cost: 350.0
      4:
        chunks: 16
        upgrade-cost: 10000.0
        renewal-cost: 500.0
      5:
        chunks: 25
        upgrade-cost: 20000.0
        renewal-cost: 750.0

  advanced:
    material: ENDER_CHEST
    display-name: "&b&lCargador de Chunks Avanzado"
    lore:
      - "&7¡Tecnología avanzada de carga de chunks!"
      - "&7Carga más chunks con mejor eficiencia."
      - "&7Clic derecho para gestionar tu cargador de chunks."
      - ""
      - "&eNivel: &f%level%"
      - "&eChunks: &f%chunks%"

    base-duration: 7200

    hologram:
      enabled: true
      lines:
        - "&b&lCargador Avanzado"
        - "&7Propietario: &e%owner%"
        - "&7Nivel: &e%level%"
        - "&7Chunks: &e%chunks%"
        - "&7Tiempo: &e%time%"

    levels:
      1:
        chunks: 4
        upgrade-cost: 0
        renewal-cost: 150.0
      2:
        chunks: 9
        upgrade-cost: 5000.0
        renewal-cost: 300.0
      3:
        chunks: 16
        upgrade-cost: 10000.0
        renewal-cost: 500.0
      4:
        chunks: 25
        upgrade-cost: 20000.0
        renewal-cost: 750.0
      5:
        chunks: 36
        upgrade-cost: 40000.0
        renewal-cost: 1000.0

  premium:
    material: BEACON
    display-name: "&6&lCargador de Chunks Premium"
    lore:
      - "&7¡La solución definitiva para cargar chunks!"
      - "&7Máxima eficiencia y cobertura."
      - "&7Clic derecho para gestionar tu cargador de chunks."
      - ""
      - "&eNivel: &f%level%"
      - "&eChunks: &f%chunks%"

    base-duration: 14400

    hologram:
      enabled: true
      lines:
        - "&6&lCargador Premium"
        - "&7Propietario: &e%owner%"
        - "&7Nivel: &e%level%"
        - "&7Chunks: &e%chunks%"
        - "&7Tiempo: &e%time%"

    levels:
      1:
        chunks: 9
        upgrade-cost: 0
        renewal-cost: 250.0
      2:
        chunks: 16
        upgrade-cost: 10000.0
        renewal-cost: 500.0
      3:
        chunks: 25
        upgrade-cost: 20000.0
        renewal-cost: 750.0
      4:
        chunks: 36
        upgrade-cost: 40000.0
        renewal-cost: 1000.0
      5:
        chunks: 49
        upgrade-cost: 80000.0
        renewal-cost: 1500.0

gui:
  main:
    title: "&8Gestor de Cargadores de Chunks"
    size: 27
  permissions:
    title: "&8Permisos del Cargador de Chunks"
    size: 27

messages:
  prefix: "&8[&aDChunkLoader&8] "
  no-permission: "&c¡No tienes permisos para hacer eso!"
  player-not-found: "&c¡Jugador no encontrado!"

  # Colocación de cargadores de chunks
  place-success: "&a¡Cargador de chunks colocado exitosamente! &7(Tipo: &e%block%&7, Nivel: &e%level%&7)"
  place-failed: "&c¡Error al colocar el cargador de chunks!"
  max-blocks-reached: "&c¡Has alcanzado el número máximo de cargadores de chunks! &7(Máximo: &e%max%&7)"
  break-success: "&a¡Cargador de chunks roto exitosamente!"

  # Eliminación de cargadores de chunks
  remove-success: "&a¡Cargador de chunks eliminado exitosamente!"
  remove-confirm: "&e¿Estás seguro de que quieres eliminar el cargador de chunks de &c%owner%&e? Rómpelo de nuevo para confirmar."
  not-authorized: "&c¡No estás autorizado para interactuar con este cargador de chunks!"

  # Mejoras
  upgrade-success: "&a¡Cargador de chunks mejorado al nivel &e%level%&a! &7(Chunks: &e%chunks%&7)"
  upgrade-failed: "&c¡Error al mejorar el cargador de chunks!"
  upgrade-max: "&c¡Este cargador de chunks ya está en el nivel máximo!"

  # Renovaciones
  renewal-success: "&a¡Cargador de chunks renovado por &e%time%&a!"
  renewal-failed: "&c¡Error al renovar el cargador de chunks!"

  # Economía
  not-enough-money: "&c¡No tienes suficiente dinero! Requerido: &e%s"
  payment-success: "&a¡Pago exitoso! Cantidad: &e%s"

  # Expiración
  expired: "&c¡Uno de tus cargadores de chunks ha expirado!"
  login-expired-notification: "&c¡Tienes &e%count% &ccargadores de chunks expirados!"
  login-stats: "&7Tienes &e%total% &7cargadores de chunks (&a%active% &7activos)"

  # Comandos
  give-success: "&aSe le dio a &e%player% &aun cargador de chunks &e%block% &a(Nivel %level%)"
  give-received: "&aRecibiste un cargador de chunks &e%block% &a(Nivel %level%)"
  invalid-block: "&cTipo de bloque inválido: &e%block%"
  invalid-level: "&cNivel inválido para el tipo de bloque: &e%block%"

  # Mensajes de GUI
  menu-title: "&8Menú del Cargador de Chunks"

  # Permisos
  user-already-authorized: "&cEl jugador %player% ya está autorizado."
  user-added-success: "&aJugador %player% agregado exitosamente."
  user-removed-success: "&aUsuario eliminado exitosamente."
  user-removed-error: "Hubo un error al intentar eliminar el usuario o no existe."
  chunk-loader-not-found: "&cError: Cargador de chunks no encontrado."
  already-max-level: "&c¡Este cargador de chunks ya está en el nivel máximo!"
  upgrade-success-simple: "&a¡Cargador de chunks mejorado exitosamente!"
  upgrade-failed-balance: "&c¡Mejora fallida! Revisa tu saldo e intenta de nuevo."

  # Mensajes de administración
  admin-cleanup-success: "&a¡Cargadores de chunks expirados limpiados!"
  admin-unknown-action: "&cAcción de administrador desconocida: %action%"

  # Mensajes de estadísticas detalladas
  stats-header: "&a=== Estadísticas de DChunkLoader ==="
  stats-total-loaders: "&7Cargadores Totales: &e%total%"
  stats-active-loaders: "&7Cargadores Activos: &a%active%"
  stats-inactive-loaders: "&7Cargadores Inactivos: &c%inactive%"
  stats-server-load: "&7Carga del Servidor: &b%load%%"

  # Mensajes de permisos en GUI
  no-permission-modify-users: "&cNo tienes permisos para modificar usuarios."
  no-permission-add-users: "&cNo tienes permisos para agregar usuarios."
  type-player-name: "&eEscribe en el chat el nombre del jugador a agregar:"
  user-removed-from-permissions: "&cUsuario eliminado de los permisos."

  # Mensajes de estado
  status-active: "&aActivo"
  status-expired: "&cExpirado"
  status-inactive: "&7Inactivo"

  # Formato de tiempo
  time-format:
    days: "%d días"
    hours: "%h horas"
    minutes: "%m minutos"
    seconds: "%s segundos"
    expired: "Expirado"

  # Nombres y descripciones de ítems de GUI
  gui:
    upgrade:
      name: "&aMejorar Cargador de Chunks"
      lore:
        - "&7Haz clic para mejorar este cargador"
        - "&7de chunks al siguiente nivel."
        - ""
        - "&7Nivel Actual: &e%level%"
        - "&7Siguiente Nivel: &e%next_level%"
        - "&7Costo: &e%cost%"

    renew:
      name: "&bRenovar Tiempo"
      lore:
        - "&7Haz clic para renovar el tiempo"
        - "&7de este cargador de chunks."
        - ""
        - "&7Costo por hora: &e%cost%"

    info:
      name: "&eInformación del Cargador"
      lore:
        - "&7Propietario: &e%owner%"
        - "&7Nivel: &e%level%"
        - "&7Chunks: &e%chunks%"
        - "&7Estado: %status%"
        - "&7Tiempo Restante: &e%time%"

    permissions:
      name: "&dGestionar Permisos"
      lore:
        - "&7Haz clic para gestionar quién puede"
        - "&7acceder a este cargador de chunks."

    remove:
      name: "&cEliminar Cargador de Chunks"
      lore:
        - "&7Haz clic para eliminar este"
        - "&7cargador de chunks permanentemente."
        - ""
        - "&c&lADVERTENCIA: ¡Esto no se puede deshacer!"

    close:
      name: "&cCerrar"
      lore:
        - "&7Haz clic para cerrar este menú."

    back:
      name: "&7Atrás"
      lore:
        - "&7Haz clic para regresar."

    confirm:
      name: "&aConfirmar"
      lore:
        - "&7Haz clic para confirmar la acción."

    cancel:
      name: "&cCancelar"
      lore:
        - "&7Haz clic para cancelar la acción."
```
