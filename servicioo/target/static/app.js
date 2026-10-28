/*
 * Archivo: app.js
 * Descripción: Lógica del frontend. Consume el CRUD de estudiantes con fetch.
 * Origen: Elaborado con apoyo de Claude y adaptado por el autor. Basado en la
 *   documentación de MDN: https://developer.mozilla.org/es/docs/Web/API/Fetch_API/Using_Fetch
 */

// Variable global (constante): ruta de la API. Es relativa porque la página y el servicio
// comparten el mismo servidor (localhost:8080).
const API_URL = "/api/estudiantes";

// Variables globales: referencias a los elementos del HTML que se usan en varias funciones.
const formulario = document.getElementById("formulario");
const campoId = document.getElementById("id");
const campoNombre = document.getElementById("nombre");
const campoCarrera = document.getElementById("carrera");
const btnGuardar = document.getElementById("btnGuardar");
const btnCancelar = document.getElementById("btnCancelar");
const tabla = document.getElementById("tabla");
const mensaje = document.getElementById("mensaje");

/** Muestra un mensaje al usuario. @param texto mensaje; @param esError true si es un error. */
function mostrarMensaje(texto, esError = false) {
  mensaje.textContent = texto;
  mensaje.className = esError ? "error" : "";
}

/** READ: pide la lista de estudiantes al servicio (GET) y la dibuja en la tabla. */
async function cargarEstudiantes() {
  try {
    const respuesta = await fetch(API_URL);            // Petición GET
    if (!respuesta.ok) throw new Error("Error " + respuesta.status);
    const estudiantes = await respuesta.json();        // Convierte el JSON en arreglo (variable local)

    tabla.innerHTML = "";                              // Limpia la tabla antes de redibujar
    estudiantes.forEach(function (estudiante) {        // estudiante: variable local de cada vuelta
      tabla.appendChild(crearFila(estudiante));
    });
  } catch (error) {
    mostrarMensaje("No se pudo cargar la lista: " + error.message, true);
  }
}

/**
 * Construye una fila <tr> para un estudiante.
 * Se usa textContent (no innerHTML) para evitar inyectar código desde los datos.
 */
function crearFila(estudiante) {
  const fila = document.createElement("tr");

  [estudiante.id, estudiante.nombre, estudiante.carrera].forEach(function (valor) {
    const celda = document.createElement("td");
    celda.textContent = valor;
    fila.appendChild(celda);
  });

  const celdaAcciones = document.createElement("td");

  const btnEditar = document.createElement("button");
  btnEditar.textContent = "Editar";
  btnEditar.className = "secundario";
  btnEditar.addEventListener("click", function () { prepararEdicion(estudiante); });

  const btnEliminar = document.createElement("button");
  btnEliminar.textContent = "Eliminar";
  btnEliminar.className = "peligro";
  btnEliminar.addEventListener("click", function () { eliminarEstudiante(estudiante.id); });

  celdaAcciones.appendChild(btnEditar);
  celdaAcciones.appendChild(btnEliminar);
  fila.appendChild(celdaAcciones);

  return fila;
}

/** CREATE / UPDATE: si hay id guarda cambios (PUT); si no, crea uno nuevo (POST). */
async function guardarEstudiante(evento) {
  evento.preventDefault();                             // Evita que el formulario recargue la página

  const id = campoId.value;                            // Variable local
  const esEdicion = id !== "";                         // Variable local: ¿editamos o creamos?
  const datos = {                                      // Variable local: objeto que se enviará como JSON
    nombre: campoNombre.value.trim(),
    carrera: campoCarrera.value.trim()
  };

  const url = esEdicion ? API_URL + "/" + id : API_URL;
  const metodo = esEdicion ? "PUT" : "POST";

  try {
    const respuesta = await fetch(url, {
      method: metodo,
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(datos)                      // Convierte el objeto a texto JSON
    });
    if (!respuesta.ok) throw new Error("Error " + respuesta.status);

    mostrarMensaje(esEdicion ? "Estudiante actualizado." : "Estudiante creado.");
    limpiarFormulario();
    await cargarEstudiantes();                         // Refresca la tabla
  } catch (error) {
    mostrarMensaje("No se pudo guardar: " + error.message, true);
  }
}

/** Pasa los datos de una fila al formulario para editarlos. */
function prepararEdicion(estudiante) {
  campoId.value = estudiante.id;
  campoNombre.value = estudiante.nombre;
  campoCarrera.value = estudiante.carrera;
  btnGuardar.textContent = "Actualizar";
  campoNombre.focus();
}

/** DELETE: elimina un estudiante tras pedir confirmación. @param id id a eliminar. */
async function eliminarEstudiante(id) {
  if (!confirm("¿Seguro que deseas eliminar este estudiante?")) return;

  try {
    const respuesta = await fetch(API_URL + "/" + id, { method: "DELETE" });
    if (!respuesta.ok) throw new Error("Error " + respuesta.status);

    mostrarMensaje("Estudiante eliminado.");
    await cargarEstudiantes();
  } catch (error) {
    mostrarMensaje("No se pudo eliminar: " + error.message, true);
  }
}

/** Vacía el formulario y lo deja en modo "crear". */
function limpiarFormulario() {
  formulario.reset();
  campoId.value = "";
  btnGuardar.textContent = "Guardar";
}

// Eventos: conectan los elementos de la página con las funciones.
formulario.addEventListener("submit", guardarEstudiante);
btnCancelar.addEventListener("click", limpiarFormulario);

// Al abrir la página se carga la lista automáticamente.
cargarEstudiantes();