const fs = require('node:fs');
const path = require('node:path');
const {spawn} = require('node:child_process');
const {pathToFileURL} = require('node:url');
const temp = fs.mkdtempSync(path.join(require('node:os').tmpdir(), 'skycampus-prototipo-'));
const root = path.resolve(__dirname, '../../..');
const profile = path.join(temp, 'chrome-prototype-'+Date.now());
const chrome = spawn(process.env.CHROME_PATH || 'C:/Program Files/Google/Chrome/Application/chrome.exe',
  ['--headless=new','--disable-gpu','--disable-background-networking','--no-first-run',
   '--no-default-browser-check','--remote-debugging-port=0','--user-data-dir='+profile,'--window-size=1440,1100'],
  {windowsHide:true, stdio:'ignore'});
const pause = ms => new Promise(r=>setTimeout(r,ms));
let ws, n=0;
const pending = new Map();
const checks=[];
function command(method,params={}) {
  return new Promise((resolve,reject)=>{const id=++n; pending.set(id,{resolve,reject}); ws.send(JSON.stringify({id,method,params}));});
}
async function evaluate(expression) {
  const r=await command('Runtime.evaluate',{expression,returnByValue:true,awaitPromise:true});
  if(r.exceptionDetails) throw new Error(JSON.stringify(r.exceptionDetails));
  return r.result.value;
}
async function check(name,expression) {
  const passed=await evaluate(expression); checks.push({name,passed}); if(!passed) throw new Error(name);
}
async function click(id) {await evaluate(`document.getElementById('${id}').click()`);}
async function value(id,v) {await evaluate(`document.getElementById('${id}').value=${JSON.stringify(v)};document.getElementById('${id}').dispatchEvent(new Event('change',{bubbles:true}))`);}
async function navigate() {
  await command('Page.navigate',{url:pathToFileURL(path.join(root,'docs/infernape/reto11-prototipo/index.html')).href});
  for(let i=0;i<80;i++){if(await evaluate("document.readyState==='complete' && !!document.getElementById('sedes')?.children.length"))return;await pause(100);}
  throw new Error('load timeout');
}
async function plan() {await click('planificar');await pause(500);}
async function screenshot(name) {
  const r=await command('Page.captureScreenshot',{format:'png',captureBeyondViewport:true});
  fs.writeFileSync(path.join(temp,name+'.png'),Buffer.from(r.data,'base64'));
}
(async()=>{
  for(let i=0;i<100&&!fs.existsSync(path.join(profile,'DevToolsActivePort'));i++)await pause(100);
  const port=fs.readFileSync(path.join(profile,'DevToolsActivePort'),'utf8').split('\n')[0];
  const targets=await (await fetch('http://127.0.0.1:'+port+'/json')).json();
  const page=targets.find(t=>t.type==='page');
  ws=new WebSocket(page.webSocketDebuggerUrl);
  ws.onmessage=e=>{const r=JSON.parse(e.data);if(r.id&&pending.has(r.id)){const p=pending.get(r.id);pending.delete(r.id);r.error?p.reject(new Error(JSON.stringify(r.error))):p.resolve(r.result);}};
  await new Promise((resolve,reject)=>{ws.onopen=resolve;ws.onerror=reject;});
  await command('Page.enable'); await navigate();
  await check('Cuatro sedes visibles',"document.querySelectorAll('.sede').length===4 && ['ECI','UNAL','UNIANDES','EAFIT'].every(s=>document.getElementById('sedes').textContent.includes(s))");
  await check('Identidad ECI real',"getComputedStyle(document.body).getPropertyValue('--color-primary').trim()==='#00457C'");
  await value('tema','unal');
  await check('Cambio a UNAL por tokens sin duplicar DOM',"getComputedStyle(document.body).getPropertyValue('--color-primary').trim()==='#7B0000' && document.querySelectorAll('#formulario').length===1");
  await screenshot('dashboard-unal');
  await click('nueva');await value('peso','700');await value('prioridad','URGENTE');await plan();
  await check('Ruta calculada y estación visible',"!document.getElementById('ruta').hidden && document.getElementById('detalle-ruta').textContent.includes('C-SIM disponible')");
  await click('continuar');await screenshot('confirmacion');
  await check('Confirmación habilitada y prioridad conservada',"!document.getElementById('confirmar').disabled && document.getElementById('resumen').textContent.includes('URGENTE')");
  await evaluate("document.querySelector('#confirmacion [data-volver]').click();document.querySelector('#ruta [data-volver]').click()");
  await check('Volver conserva peso y prioridad',"document.getElementById('peso').value==='700' && document.getElementById('prioridad').value==='URGENTE'");
  await plan();await click('continuar');await click('confirmar');
  await check('Confirmar genera misión y evento local',"!document.getElementById('completada').hidden && document.getElementById('resultado').textContent.includes('EN_VUELO') && document.getElementById('eventos').children.length===2");
  await check('Disponibilidad se actualiza por evento propio',"document.querySelector('.sede dd').textContent==='11'");
  for(const scenario of ['clima','drones','peso','aerocivil','sede','estacion']) {
    await navigate();await value('escenario',scenario);await click('nueva');await plan();
    await check('Bloqueo visible: '+scenario,"document.getElementById('aviso').classList.contains('error') && document.getElementById('aviso').textContent.length>40 && document.getElementById('confirmar').disabled && document.getElementById('continuar').disabled");
    if(scenario==='estacion')await screenshot('error-estacion');
    await click('recuperar');await value('escenario','normal');await value('peso','300');await plan();
    await check('Recuperación: '+scenario,"!document.getElementById('ruta').hidden && !document.getElementById('continuar').disabled");
  }
  await navigate();await click('nueva');await value('destino','ECI');await plan();
  await check('Origen igual a destino bloquea',"document.getElementById('aviso').textContent.includes('Sedes iguales') && document.getElementById('confirmar').disabled");
  await value('destino','UNAL');await plan();await click('continuar');await click('cancelar');
  await check('Cancelar funciona y conserva datos',"!document.getElementById('dashboard').hidden && document.getElementById('peso').value==='300'");
  await check('Sin recorte horizontal',"document.documentElement.scrollWidth<=innerWidth");
  await navigate();await command('Page.bringToFront');await evaluate("document.getElementById('nueva').focus()");
  await command('Input.dispatchKeyEvent',{type:'rawKeyDown',key:'Enter',code:'Enter',windowsVirtualKeyCode:13,nativeVirtualKeyCode:13});
  await command('Input.dispatchKeyEvent',{type:'char',key:'Enter',code:'Enter',text:'\r',unmodifiedText:'\r',windowsVirtualKeyCode:13,nativeVirtualKeyCode:13});
  await command('Input.dispatchKeyEvent',{type:'keyUp',key:'Enter',code:'Enter',windowsVirtualKeyCode:13});
  await pause(100);
  console.log('KEYBOARD:',await evaluate("JSON.stringify({hidden:document.getElementById('solicitud').hidden,focus:document.activeElement.id})"));
  await check('Acción primaria accesible por teclado',"!document.getElementById('solicitud').hidden");
  await evaluate("document.querySelector('summary').click()");
  await check('Ayuda de demostración accesible',"document.querySelector('details').open && document.querySelector('details').textContent.includes('Inicia una misión')");
  await command('Emulation.setDeviceMetricsOverride',{width:390,height:844,deviceScaleFactor:1,mobile:false});
  await check('Diseño móvil sin desbordamiento',"document.documentElement.scrollWidth<=innerWidth");
  const result={engine:'Chrome headless real / CDP',date:new Date().toISOString(),checks,passed:checks.length,failed:0};
  fs.writeFileSync(path.join(temp,'browser-results.json'),JSON.stringify(result,null,2));
  console.log(JSON.stringify(result,null,2));
})().catch(e=>{console.error(e);process.exitCode=1;}).finally(async()=>{if(ws)ws.close();chrome.kill();});
