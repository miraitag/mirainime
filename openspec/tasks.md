# Checklist de Tareas: Reestructuración de Modularización

- [x] **Fase 1: Ajustes en Build Logic (Plugins de Convención)**
  - [x] Paso 1.1: Modificar [KotlinAndroid.kt](file:///Users/tahuilan/AndroidStudioProjects/mirainime/build-logic/convention/src/main/kotlin/com/miraitag/mirainime/KotlinAndroid.kt) para remover `namespace = "com.miraitag.mirainime"`.
  - [x] Paso 1.2: Modificar [AndroidApplicationConventionPlugin.kt](file:///Users/tahuilan/AndroidStudioProjects/mirainime/build-logic/convention/src/main/kotlin/AndroidApplicationConventionPlugin.kt) para declarar explícitamente el `namespace = "com.miraitag.mirainime"`.
  - [x] Paso 1.3: Crear el nuevo archivo [AndroidLibraryConventionPlugin.kt](file:///Users/tahuilan/AndroidStudioProjects/mirainime/build-logic/convention/src/main/kotlin/AndroidLibraryConventionPlugin.kt) con la configuración de biblioteca.
  - [x] Paso 1.4: Modificar [build.gradle.kts (build-logic)](file:///Users/tahuilan/AndroidStudioProjects/mirainime/build-logic/convention/build.gradle.kts) para registrar el plugin `mirainime.android.library`.

- [x] **Fase 2: Ajustes en Módulo `:feature:auth`**
  - [x] Paso 2.1: Configurar [build.gradle.kts (feature:auth)](file:///Users/tahuilan/AndroidStudioProjects/mirainime/feature/auth/build.gradle.kts) aplicando el plugin de biblioteca y declarando su namespace.

- [/] **Fase 3: Migración Física de Paquetes**
  - [x] Paso 3.1: Crear estructura de directorios `com/miraitag/mirainime/feature/auth` y migrar código en `src/main/java`.
  - [x] Paso 3.2: Migrar unit tests en `src/test/java`, actualizando package e imports de `ExampleUnitTest.kt`.
  - [x] Paso 3.3: Migrar instrumented tests en `src/androidTest/java`, actualizando package, imports y aserción de packageName en `ExampleInstrumentedTest.kt`.
  - [ ] Paso 3.4: Eliminar las carpetas vacías obsoletas `com/mirainime` (pendiente aprobación de comando o eliminación manual).

- [ ] **Fase 4: Verificación y Compilación**
  - [ ] Paso 4.1: Ejecutar `./gradlew compileDebugKotlin` para verificar que la compilación de Kotlin sea exitosa.
  - [ ] Paso 4.2: Ejecutar `./gradlew feature:auth:test` para correr los tests unitarios locales del módulo.
  - [ ] Paso 4.3: Ejecutar `./gradlew app:assembleDebug` para empaquetar el APK final y certificar el build completo.
