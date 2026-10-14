# Mejora de diseño: web + app

Copia las carpetas encima de tu proyecto (mantienen la misma ruta).

## Web (solo CSS, no hay que tocar HTML)
backend/src/main/resources/static/css/
  theme.css (nuevo, colores y tipografía compartidos), login.css, dashboard.css,
  admin.css, edit-form.css, user-form.css

## App Android
AppAndroid/TFGApp/app/src/main/res/
  values/colors.xml, values/themes.xml        (sustituyen a los actuales)
  values-night/themes.xml                     (BÓRRALO: era idéntico y pisaría el tema nuevo)
  drawable/ic_*.xml                           (8 iconos nuevos)
  layout/activity_main.xml, activity_operario.xml,
         activity_supervisor.xml, activity_consultas_stock.xml

Los ids de las vistas son los mismos, así que el código Kotlin no cambia.

## Descarga de la APK en el login
- templates/login.html (enlace al final del formulario) y css/login.css (estilos).
- Copia tu APK a: backend/src/main/resources/static/downloads/tfgapp.apk
  (en Android Studio: Build > Build APK(s)).
- No hace falta tocar la seguridad: /downloads/** no esta en ninguna cadena de Spring Security.
