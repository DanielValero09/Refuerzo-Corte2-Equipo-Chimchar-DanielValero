'use strict';
const sedes = ['ECI', 'UNAL', 'UNIANDES', 'EAFIT'];
const state = {view: 'dashboard', scenario: 'normal', plan: undefined, sequence: 0, mission: 100,
  form: {origen: 'ECI', destino: 'UNAL', peso: 300, prioridad: 'NORMAL'},
  available: {ECI: 12, UNAL: 8, UNIANDES: 10, EAFIT: 15}};
const $ = id => document.getElementById(id);
const errorText = {
  clima: ['Clima adverso', 'Viento y lluvia no aptos en el escenario local. El vuelo queda bloqueado.'],
  drones: ['Sin drones aptos', 'No hay drones aptos disponibles. Puedes volver y reintentar más tarde.'],
  peso: ['Paquete demasiado pesado', 'El máximo soportado es 2000 g. Corrige el peso antes de continuar.'],
  aerocivil: ['Aerocivil rechaza', 'La autorización simulada fue rechazada. No se permite iniciar la ruta.'],
  sede: ['Sede inactiva', 'La sede de destino no está activa para operar. Elige otra sede o reintenta.'],
  estacion: ['Estación de carga no disponible', 'La alternativa requiere una estación cerrada. Replanifica con una estación disponible.']};

function dashboard() {
  $('sedes').replaceChildren(...sedes.map(sede => {
    const card = document.createElement('article'); card.className = 'sede';
    const available = state.available[sede], flying = 20 - available;
    card.innerHTML = `<h3>${sede}</h3><strong>25 drones</strong><dl><dt>✓ Disponibles</dt><dd>${available}</dd><dt>↗ En vuelo</dt><dd>${flying}</dd><dt>⚡ En carga</dt><dd>3</dd><dt>⚙ Mantenimiento</dt><dd>2</dd></dl><p>● Operación local simulada</p>`;
    return card;
  }));
}
function show(view) {
  state.view = view;
  ['dashboard','solicitud','ruta','confirmacion','completada'].forEach(id => {$(id).hidden = id !== view;});
  document.querySelectorAll('.pasos li').forEach((li,i) => {
    li.classList.toggle('actual', i === ['dashboard','solicitud','ruta','confirmacion'].indexOf(view));
  });
  const heading = $(view).querySelector('h2'); heading.focus();
}
function syncForm() {
  ['origen','destino','prioridad'].forEach(id => {state.form[id] = $(id).value;});
  state.form.peso = Number($('peso').value);
}
function setScenario() {
  state.sequence++; state.scenario = $('escenario').value; state.plan = undefined;
  if (state.scenario === 'peso') {$('peso').value = '2100'; syncForm();}
  $('condiciones').textContent = state.scenario === 'normal' ? '✓ Clima apto · autorización simulada disponible.' : '⚠ Escenario activo: ' + errorText[state.scenario][0];
  $('continuar').disabled = true; $('confirmar').disabled = true;
  $('planificar').disabled = false; $('planificar').textContent = 'Planificar ruta';
  if (['ruta','confirmacion'].includes(state.view)) show('solicitud');
  $('aviso').textContent = 'Escenario actualizado. Debes planificar de nuevo.';
}
function failure() {
  if (state.form.origen === state.form.destino) return ['Sedes iguales', 'Selecciona dos sedes distintas para una misión inter-sede.'];
  if (!Number.isInteger(state.form.peso) || state.form.peso < 1) return ['Peso inválido', 'Ingresa un peso entero positivo.'];
  if (state.form.peso > 2000) return errorText.peso;
  if (state.scenario !== 'normal') return errorText[state.scenario];
  if (state.available[state.form.origen] < 1) return errorText.drones;
  return undefined;
}
function renderError(error) {
  state.plan = undefined;
  $('aviso').className = 'error'; $('aviso').replaceChildren();
  const title = document.createElement('strong'); title.textContent = '⚠ ' + error[0];
  const reason = document.createElement('p'); reason.textContent = error[1];
  const recovery = document.createElement('button'); recovery.id = 'recuperar'; recovery.textContent = 'Corregir / reintentar';
  recovery.addEventListener('click', () => {show('solicitud'); $('peso').focus();});
  $('aviso').append(title, reason, recovery);
  $('confirmar').disabled = true; $('continuar').disabled = true;
}
async function plan(event) {
  event.preventDefault(); syncForm(); state.plan = undefined;
  const ticket = ++state.sequence;
  $('aviso').className = ''; $('aviso').textContent = '⏳ Consultando condiciones y planificando datos locales…';
  $('planificar').disabled = true; $('planificar').textContent = 'Planificando…';
  await new Promise(resolve => setTimeout(resolve, 350));
  if (ticket !== state.sequence) return;
  $('planificar').disabled = false; $('planificar').textContent = 'Planificar ruta';
  const error = failure();
  if (error) {renderError(error); return;}
  const type = state.form.peso > 800 ? 'CARGO' : state.form.prioridad === 'URGENTE' ? 'EXPRESS' : state.form.peso > 500 ? 'EXPRESS' : 'MINI';
  const serial = String(26 - state.available[state.form.origen]).padStart(2, '0');
  state.plan = {type, battery: type === 'EXPRESS' ? 70 : 91, drone: 'DE-' + state.form.origen + '-' + serial};
  $('detalle-ruta').innerHTML = `<p class="estado">✓ Autorización simulada apta</p><ol><li>${state.form.origen} → Estación C-SIM: 3 km ilustrativos.</li><li>Estación C-SIM → ${state.form.destino}: 3 km ilustrativos.</li></ol><p>⚡ Estación C-SIM disponible. Dos etapas; ninguna supera 5 km con carga.</p><p>Drone candidato <span class="id">${state.plan.drone}</span> · ${type} · ${state.plan.battery}%.</p><p>Los trayectos son ejemplos; no distancias geográficas medidas.</p>`;
  $('aviso').textContent = 'Ruta lista. Revisa las etapas antes de continuar.';
  $('continuar').disabled = false; show('ruta');
}
function review() {
  if (!state.plan || failure()) {renderError(failure() || ['Plan ausente','Planifica antes de confirmar.']); return;}
  $('resumen').innerHTML = `<dl class="resumen-datos"><dt>Origen / destino</dt><dd>${state.form.origen} → ${state.form.destino}</dd><dt>Paquete</dt><dd>${state.form.peso} g</dd><dt>Prioridad</dt><dd>${state.form.prioridad}</dd><dt>Drone</dt><dd class="id">${state.plan.drone}</dd><dt>Tipo / batería</dt><dd>${state.plan.type} / ${state.plan.battery}%</dd><dt>Etapas</dt><dd>2, con C-SIM disponible</dd></dl><p>ETA ilustrativa: 12 min. No constituye un tiempo real medido.</p><p>La confirmación solo actualiza la simulación local.</p>`;
  $('confirmar').disabled = false; show('confirmacion');
}
function confirm() {
  if (!state.plan || failure()) {renderError(failure() || ['Plan ausente','Vuelve a planificar.']); return;}
  const id = 'ME-SIM-' + state.mission++;
  state.available[state.form.origen]--;
  const entry = document.createElement('li'); entry.textContent = `${id}: ${state.plan.drone} inició vuelo simulado ${state.form.origen} → ${state.form.destino}.`;
  $('eventos').prepend(entry); dashboard();
  $('resultado').textContent = `✓ ${id} — EN_VUELO. ${state.plan.drone} ya no está disponible en la simulación.`;
  $('aviso').className = 'exito'; $('aviso').textContent = 'Misión simulada confirmada y resumen local actualizado.';
  state.plan = undefined; $('confirmar').disabled = true; show('completada');
}
$('tema').addEventListener('change', () => {document.body.dataset.theme = $('tema').value;});
$('escenario').addEventListener('change', setScenario);
$('nueva').addEventListener('click', () => {$('aviso').className=''; $('aviso').textContent=''; show('solicitud');});
$('formulario').addEventListener('submit', plan);
document.querySelectorAll('#formulario input,#formulario select').forEach(el => el.addEventListener('change', syncForm));
$('continuar').addEventListener('click', review);
$('confirmar').addEventListener('click', confirm);
$('cancelar').addEventListener('click', () => {state.plan=undefined; $('aviso').textContent='Planificación cancelada. Los datos se conservan.'; show('dashboard');});
document.querySelectorAll('[data-volver]').forEach(button => button.addEventListener('click', () => {
  state.sequence++; $('planificar').disabled=false; $('planificar').textContent='Planificar ruta'; show(button.dataset.volver);
}));
dashboard(); $('confirmar').disabled = true; $('continuar').disabled = true;
