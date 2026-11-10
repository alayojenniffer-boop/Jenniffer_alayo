# Administradora de Viviendas – EP2 (JavaFX + Gson)

Adaptación de la EP1 (Vivienda / Departamento / Casa / ConEstacionamiento) al formato EP2.

## Ejecutar
- IntelliJ: abrir el proyecto, recargar Maven y ejecutar `cl.duoc.viviendas.Launcher`.
- Maven: `mvn javafx:run`

Los datos se guardan en `departamentos.json` y `casas.json` en la carpeta raíz del proyecto.

## Estructura (MVC + DAO/Repository)
| Paquete | Contenido |
|---|---|
| `model` | `Vivienda` (abstracta), `Departamento`, `Casa`, `ConEstacionamiento` |
| `dao` | `PersistenciaException`, `ViviendaDao`, `GsonViviendaDao` |
| `repository` | `Repository`, `ViviendaRepository` |
| `controller` | `MainController` (solo conoce `Repository`) |
| `view` + resources | `MainApp`, `main-view.fxml` |

## Equivalencias con el ejemplo del vivero
Planta→Vivienda · PlantaInterior→Departamento · PlantaExterior→Casa · Despachable→ConEstacionamiento
Altura→Superficie · Cantidad→Habitaciones · Precio unitario→Costo arriendo (`calcularCostoArriendo()`)

## Datos de prueba (EP1)
PROP-D01 Dpto 65 m2, 3 hab, piso 8, gasto común NO, con estacionamiento → $207.000
PROP-D02 Dpto 48 m2, 2 hab, piso 3, gasto común SÍ → $180.000
PROP-C01 Casa 120 m2, 4 hab, con patio → $275.000
PROP-C02 Casa 90 m2, 3 hab, sin patio → $250.000
