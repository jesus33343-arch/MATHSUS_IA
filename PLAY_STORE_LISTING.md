# MATHSUS_IA — Ficha de Google Play Store

Copia y pega estos textos directamente en los campos correspondientes de Play Console
(Presencia en la tienda → Ficha principal de Play Store).

## Descripción breve (máx. 80 caracteres)

```
Calculadora de métodos numéricos con tutor de IA: bisección, Newton, secante
```
(76 caracteres)

## Descripción completa (máx. 4000 caracteres)

```
MATHSUS es una calculadora de métodos numéricos pensada para estudiantes, ingenieros
e investigadores que necesitan encontrar raíces de funciones no lineales de una
variable, f(x) = 0.

La app implementa cuatro métodos clásicos:
• Bisección
• Regla Falsa (Falsi)
• Newton-Raphson
• Secante

Características principales:
1. Resuelve ecuaciones con cualquiera de los cuatro métodos.
2. Introduce funciones personalizadas con una sintaxis matemática estándar
   (sin(x), cos(x), tan(x), log(x), ln(x), sqrt(x), pi, e, potencias, etc.).
3. Visualiza el proceso de búsqueda de la raíz con gráficos interactivos.
4. Desglosa cada iteración en una tabla paso a paso, con fines educativos.
5. Compara la eficiencia y precisión de los diferentes métodos.
6. Incluye un asistente con inteligencia artificial (Gemini) para resolver dudas
   sobre los métodos, ayudarte a elegir un intervalo o valor inicial, y explicarte
   el procedimiento paso a paso.

Ya sea que estés resolviendo ecuaciones polinómicas, funciones trascendentales u
otras expresiones matemáticas complejas, MATHSUS te ofrece una herramienta robusta
e intuitiva para encontrar soluciones de manera rápida y precisa — ideal como
apoyo de estudio para cursos de cálculo numérico / métodos numéricos.

Desarrollada por Jesús Alirio Gurrute Campo, estudiante del programa de Matemáticas
de la Universidad del Cauca, como proyecto de grado.

Contacto: jesusalirio@unicauca.edu.co
```

## Categoría sugerida
Educación (o "Herramientas" como alternativa)

## Clasificación de contenido (cuestionario IARC)

Al completar el cuestionario en Play Console, ten en cuenta:
- No hay violencia, contenido sexual, lenguaje ofensivo, ni compras dentro de la app.
- No hay anuncios (no ads SDK integrado).
- **Sí debes marcar que la app incluye una función de IA generativa** (el asistente
  Gemini) — Play Console pregunta esto explícitamente desde 2025. Responde que sí,
  que genera texto a partir de instrucciones del usuario, y que existe un mecanismo
  de reporte dentro de la app (el botón "Reportar esta respuesta" ya está implementado).
- No hay contenido generado por otros usuarios (no hay chat entre usuarios, ni redes
  sociales dentro de la app).

Con esto la app debería calificar para "Apto para todo público" / "PEGI 3", pero el
cuestionario de IA generativa puede llevarla a "Guía parental" dependiendo de cómo
Google la procese — no es algo que puedas forzar, solo responder con honestidad.

## Data Safety (Seguridad de los datos)

Esta es la sección más importante y la que más suelen llenar mal. Con la app actual
(incluye la analítica de uso + feedback + reportes de IA conectados a InsForge):

- **App activity / App info and performance:** SÍ recopila. Declara:
  - Tipo de dato: "App interactions" (qué método se usó, función/parámetros
    ingresados, resultado) y "Diagnostics" no aplica realmente — es más cercano a
    "App interactions" + "Other app performance data".
  - Finalidad: "Analytics" (analítica propia) y "App functionality" (para el
    comentario en un ejercicio).
  - ¿Identifica al usuario? No — se usa un ID aleatorio generado en el dispositivo,
    sin cuenta ni datos personales. Puedes marcar "Data is collected" pero
    "not linked to a user identity, anonymous only".
- **Ubicación aproximada (por configuración regional, no GPS):** declara el país
  (derivado del idioma/región del teléfono) como "Approximate location" con
  finalidad "Analytics" — aclara en la descripción que NO es GPS ni requiere
  permiso de ubicación.
- **Mensajes / contenido generado por el usuario:** el texto que se escribe en la
  pantalla de IA y en Feedback.
  - ¿Se comparte con terceros? Sí — con Google (Gemini API, solo las preguntas de
    IA) y con InsForge (todo lo anterior, como proveedor de base de datos).
  - Finalidad: "App functionality" y "Analytics" (no publicidad).
  - ¿Se procesa en tránsito cifrado? Sí (HTTPS).
  - ¿El usuario puede pedir que se borre? No hay un flujo de autoservicio en la app
    (no hay cuentas); si alguien pide borrar sus datos, se hace manualmente vía
    soporte usando el ID de dispositivo si lo tiene, o se puede añadir un flujo de
    borrado en una próxima versión.
- Marca "No" en contactos, fotos/videos, identificadores de hardware reales
  (IMEI/Android ID), calendario, etc. — la app no accede a nada de eso.

## Política de privacidad

Google **exige una URL pública** con la política de privacidad. Ya redacté el texto
completo en `PRIVACY_POLICY.md` en esta misma carpeta. Necesitas publicarlo en algún
lugar con una URL estable antes de poder enviar la ficha a revisión. Opciones rápidas:
- GitHub Pages usando el repo público `JesusGurrute/MATHSUS` (gratis, recomendado).
- Un Google Doc publicado en la web ("Archivo → Compartir → Publicar en la Web").
- Una página en Notion/Outline pública.

## Capturas de pantalla y assets gráficos

Play Store exige mínimo 2 capturas de teléfono (JPEG/PNG, relación 16:9 o 9:16) y un
"feature graphic" de 1024×500 px. El ícono de 512×512 ya existe en el proyecto
(`app/src/main/ic_launcher-playstore.png`) y está correcto.

Capturas sugeridas (puedes tomarlas desde tu teléfono o un emulador):
1. Pantalla de inicio (splash) con los 4 métodos.
2. Un método (ej. Bisección) con una función cargada y su gráfico.
3. La tabla de iteraciones paso a paso de cualquier método.
4. La pantalla "MATHSUS con IA" con una pregunta y respuesta.
5. La pantalla "Sobre MATHSUS" (opcional).

Feature graphic (1024×500): puede ser el logo de MATHSUS sobre un fondo simple con
el texto "MATHSUS — Métodos Numéricos" — puedo ayudarte a generar uno si quieres.
