# Especificaciones y Diseño Técnico: Modularización y Plugins de Convención

Este plan detalla los cambios precisos a nivel de código y estructura física para lograr una modularización limpia y el correcto funcionamiento de los plugins de convención Gradle en **Mirainime**.

---

## Cambios de Namespace e Impacto en Tests

> [!IMPORTANT]
> * Se desacoplará el `namespace` del archivo genérico `KotlinAndroid.kt`.
> * El módulo `:feature:auth` pasará a tener el namespace `"com.miraitag.mirainime.feature.auth"`.
> * Se actualizará la aserción de packageName en `ExampleInstrumentedTest.kt` de `"com.mirainime.feature.auth.test"` a `"com.miraitag.mirainime.feature.auth.test"`.

> [!WARNING]
> * Se moverán los directorios físicos de `com/mirainime` a `com/miraitag/mirainime` en `src/main`, `src/test` y `src/androidTest` de `:feature:auth`. Esto afectará los paths de archivos en el repositorio.

---

## Proposed Changes

### Componente 1: Build Logic (Convenciones de Gradle)

#### 📝 [MODIFY] [KotlinAndroid.kt](file:///Users/tahuilan/AndroidStudioProjects/mirainime/build-logic/convention/src/main/kotlin/com/miraitag/mirainime/KotlinAndroid.kt)
* **Cambio**: Remover la línea 15 (`namespace = "com.miraitag.mirainime"`).
* **Código Resultante**:
  ```kotlin
  internal fun Project.configureKotlinAndroid(
      commonExtension: CommonExtension
  ) {
      commonExtension.apply {
          compileSdk {
              version = release(36) {
                  minorApiLevel = 1
              }
          }
          defaultConfig.minSdk = 24

          compileOptions.apply {
              sourceCompatibility = JavaVersion.VERSION_17
              targetCompatibility = JavaVersion.VERSION_17
          }
      }
      ...
  }
  ```

#### 📝 [MODIFY] [AndroidApplicationConventionPlugin.kt](file:///Users/tahuilan/AndroidStudioProjects/mirainime/build-logic/convention/src/main/kotlin/AndroidApplicationConventionPlugin.kt)
* **Cambio**: Configurar el namespace del app module explícitamente en la línea 13.
* **Código Resultante**:
  ```kotlin
  extensions.configure<ApplicationExtension> {
      namespace = "com.miraitag.mirainime"
      configureKotlinAndroid(this)
      buildFeatures {
          compose = true
      }
      ...
  }
  ```

#### 📝 [NEW] [AndroidLibraryConventionPlugin.kt](file:///Users/tahuilan/AndroidStudioProjects/mirainime/build-logic/convention/src/main/kotlin/AndroidLibraryConventionPlugin.kt)
* **Cambio**: Crear el plugin de convención para bibliotecas de Android (`mirainime.android.library`).
* **Código Completo**:
  ```kotlin
  import com.android.build.api.dsl.LibraryExtension
  import com.miraitag.mirainime.configureKotlinAndroid
  import org.gradle.api.Plugin
  import org.gradle.api.Project
  import org.gradle.kotlin.dsl.configure

  class AndroidLibraryConventionPlugin : Plugin<Project> {
      override fun apply(target: Project) {
          with(target) {
              with(pluginManager) {
                  apply("com.android.library")
              }
              extensions.configure<LibraryExtension> {
                  configureKotlinAndroid(this)
                  defaultConfig.apply {
                      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                  }
              }
          }
      }
  }
  ```

#### 📝 [MODIFY] [build.gradle.kts (build-logic)](file:///Users/tahuilan/AndroidStudioProjects/mirainime/build-logic/convention/build.gradle.kts)
* **Cambio**: Registrar el nuevo plugin de biblioteca en el bloque `gradlePlugin { plugins { ... } }`.
* **Código Resultante**:
  ```kotlin
  gradlePlugin {
      plugins {
          register("androidApplication") {
              id = "mirainime.android.application"
              implementationClass = "AndroidApplicationConventionPlugin"
          }
          register("androidLibrary") {
              id = "mirainime.android.library"
              implementationClass = "AndroidLibraryConventionPlugin"
          }
          register("firebase") {
              id = "mirainime.android.firebase"
              implementationClass = "AndroidFirebaseConventionPlugin"
          }
      }
  }
  ```

---

### Componente 2: Módulo `:feature:auth`

#### 📝 [MODIFY] [build.gradle.kts (feature:auth)](file:///Users/tahuilan/AndroidStudioProjects/mirainime/feature/auth/build.gradle.kts)
* **Cambio**: Configurar el archivo vacío para aplicar el plugin de librería y declarar su namespace y dependencias básicas de testing.
* **Código Completo**:
  ```kotlin
  plugins {
      id("mirainime.android.library")
  }

  android {
      namespace = "com.miraitag.mirainime.feature.auth"
  }

  dependencies {
      // Unit Test
      testImplementation(libs.junit)
      androidTestImplementation(libs.androidx.junit)
      androidTestImplementation(libs.androidx.espresso.core)
  }
  ```

#### 📁 [MODIFY] [Estructura física de directorios (feature:auth)](file:///Users/tahuilan/AndroidStudioProjects/mirainime/feature/auth/src)
* **Acción**: Mover la estructura de paquetes y actualizar las declaraciones en los archivos de testing.
* **Detalle**:
  1. Mover `src/main/java/com/mirainime/feature/auth` a `src/main/java/com/miraitag/mirainime/feature/auth`.
  2. Mover `src/test/java/com/mirainime/feature/auth/ExampleUnitTest.kt` a `src/test/java/com/miraitag/mirainime/feature/auth/ExampleUnitTest.kt`.
     * Cambiar línea 1 de `ExampleUnitTest.kt` a: `package com.miraitag.mirainime.feature.auth`.
  3. Mover `src/androidTest/java/com/mirainime/feature/auth/ExampleInstrumentedTest.kt` a `src/androidTest/java/com/miraitag/mirainime/feature/auth/ExampleInstrumentedTest.kt`.
     * Cambiar línea 1 de `ExampleInstrumentedTest.kt` a: `package com.miraitag.mirainime.feature.auth`.
     * Cambiar línea 22 a: `assertEquals("com.miraitag.mirainime.feature.auth.test", appContext.packageName)`.

---

## Plan de Verificación

### Pruebas Automatizadas
* Ejecutar `./gradlew compileDebugKotlin` para verificar que la compilación de todos los módulos compile sin problemas.
* Ejecutar `./gradlew feature:auth:test` para correr las pruebas unitarias locales en el módulo de biblioteca.
* Ejecutar `./gradlew app:assembleDebug` para certificar la consistencia del empaquetado del APK.

### Manual Verification
* Verificar que en Android Studio los directorios se sincronicen perfectamente y la clase `R` del feature compile sin conflictos.
