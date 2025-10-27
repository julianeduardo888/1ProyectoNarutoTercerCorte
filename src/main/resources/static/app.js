// Espera a que el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', () => {

    // Referencias a los formularios y botones
    const formNinja = document.getElementById('form-ninja');
    const formMision = document.getElementById('form-mision');
    const btnReporte = document.getElementById('btn-reporte');
    const btnCargarMisiones = document.getElementById('btn-cargar-misiones');

    // Referencias a las salidas de log
    const outputLog = document.getElementById('output-log');
    const outputReporte = document.getElementById('output-reporte');
    const misionListOutput = document.getElementById('mision-list-output');

    // --- Event Listeners ---

    // 1. Registrar Ninja
    formNinja.addEventListener('submit', async (e) => {
        e.preventDefault(); // Evita que el formulario recargue la página
        const jutsusInput = document.getElementById('ninja-jutsus').value;
        const jutsusArray = jutsusInput.split(',')
                                     .map(s => s.trim()) // Limpia espacios
                                     .filter(s => s.length > 0); // Filtra vacíos
        const ninjaData = {
            nombre: document.getElementById('ninja-nombre').value,
            rango: document.getElementById('ninja-rango').value,
            nombreAldea: document.getElementById('ninja-aldea').value,
            ataque: parseInt(document.getElementById('ninja-ataque').value),
            defensa: parseInt(document.getElementById('ninja-defensa').value),
            nombresJutsu: jutsusArray
        };
        logApiCall('Registrando Ninja...', ninjaData);
        try {
            const response = await fetch('/api/ninjas', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(ninjaData)
            });
            if (!response.ok) {
                // Captura errores del servidor (ej. "Jutsu no encontrado")
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error desconocido');
            }
            const result = await response.json();
            logApiCall('Respuesta (Ninja):', result);
        } catch (error) {
            logApiCall('Error (Ninja):', error.message);
        }
    });

    // 2. Registrar Misión
    formMision.addEventListener('submit', async (e) => {
        e.preventDefault();
        const misionData = {
            titulo: document.getElementById('mision-titulo').value,
            descripcion: "Misión registrada desde el frontend.", 
            rango: document.getElementById('mision-rango').value,
            recompensa: parseFloat(document.getElementById('mision-recompensa').value),
            rangoRequerido: document.getElementById('mision-rango-req').value
        };
        logApiCall('Registrando Misión...', misionData);
        try {
            const response = await fetch('/api/misiones', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(misionData)
            });
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error desconocido');
            }
            const result = await response.json();
            logApiCall('Respuesta (Misión):', result);
        } catch (error) {
            logApiCall('Error (Misión):', error.message);
        }
    });

    // 3. Generar Reporte TXT y Descargarlo
    btnReporte.addEventListener('click', async () => {
        logApiCall('Generando Reporte TXT...');
        outputReporte.textContent = 'Cargando...';
        
        try {
            const response = await fetch('/api/reporte/txt');
            if (!response.ok) throw new Error('Error al cargar el reporte');
            
            const reportText = await response.text(); // El reporte es texto plano
            
            // 1. Mostrar el reporte en la página
            outputReporte.textContent = reportText;
            logApiCall('Reporte generado exitosamente. Iniciando descarga...');

            // --- INICIO DE LÓGICA DE DESCARGA ---

            // 2. Crear un Blob (un objeto de archivo en memoria)
            const blob = new Blob([reportText], { type: 'text/plain;charset=utf-8' });

            // 3. Crear una URL temporal para ese Blob
            const url = URL.createObjectURL(blob);

            // 4. Crear un enlace <a> fantasma para iniciar la descarga
            const a = document.createElement('a');
            a.href = url;
            a.download = 'reporte_ninjas_misiones.txt'; // El nombre del archivo que se descargará
            document.body.appendChild(a); // El enlace debe estar en la página para funcionar
            a.click(); // Simular clic

            // 5. Limpiar
            document.body.removeChild(a);
            URL.revokeObjectURL(url);
            // --- FIN DE LÓGICA DE DESCARGA ---

        } catch (error) {
            logApiCall('Error (Reporte):', error.message);
            outputReporte.textContent = 'Error al generar el reporte.';
        }
    });

    // 4. Cargar Misiones con Ninjas Elegibles (Y ASIGNAR EQUIPOS)
    btnCargarMisiones.addEventListener('click', async () => {
        logApiCall('Cargando misiones con ninjas elegibles...');
        misionListOutput.innerHTML = '<p>Cargando...</p>';

        try {
            const response = await fetch('/api/misiones');
            if (!response.ok) throw new Error('Error al cargar misiones');
            
            const misiones = await response.json();
            logApiCall('Misiones recibidas:', misiones);

            if (misiones.length === 0) {
                misionListOutput.innerHTML = '<p>No hay misiones registradas.</p>';
                return;
            }

            // Construir el HTML
            let html = '';
            for (const mision of misiones) {
                html += `<div class="mision-card" id="mision-${mision.id}">`; 
                html += `<h3>${mision.titulo} [Rango: ${mision.rango}]</h3>`;
                html += `<p>Recompensa: ${mision.recompensa} | Requiere: <strong>${mision.rangoRequerido}</strong></p>`;
                
                html += '<h4>Ninjas Elegibles:</h4>';
                html += '<ul>';
                if (mision.ninjasElegibles.length === 0) {
                    html += '<li>Ningún ninja registrado cumple el rango.</li>';
                } else {
                    for (const ninja of mision.ninjasElegibles) {
                        html += `<li>
                                   <input type="checkbox" class="ninja-selector" data-ninja-id="${ninja.id}">
                                   <label>${ninja.nombre} (Rango: ${ninja.rango})</label>
                                 </li>`;
                    }
                }
                html += '</ul>';
                
                html += `<button class="btn-asignar" data-mision-id="${mision.id}">Asignar Equipo</button>`;
                html += '<div class="asignar-log"></div>'; 
                html += '</div>';
            }
            misionListOutput.innerHTML = html;

        } catch (error) {
            logApiCall('Error (Cargar Misiones):', error.message);
            misionListOutput.innerHTML = '<p>Error al cargar las misiones.</p>';
        }
    });

    // 5. NUEVO EVENT LISTENER (DELEGADO) PARA ASIGNAR
    misionListOutput.addEventListener('click', async (e) => {
        // Si el clic NO fue en un botón de asignar, no hacemos nada
        if (!e.target.classList.contains('btn-asignar')) {
            return;
        }

        e.preventDefault();
        const misionId = e.target.dataset.misionId;
        const misionCard = document.getElementById(`mision-${misionId}`);
        const logElement = misionCard.querySelector('.asignar-log');
        
        logElement.textContent = 'Asignando...';

        // 1. Encontrar todos los checkboxes marcados DENTRO de esta tarjeta
        const ninjaCheckboxes = misionCard.querySelectorAll('.ninja-selector:checked');
        
        // 2. Extraer sus IDs
        const ninjaIds = [];
        ninjaCheckboxes.forEach(checkbox => {
            ninjaIds.push(parseInt(checkbox.dataset.ninjaId));
        });

        if (ninjaIds.length === 0) {
            logElement.textContent = 'Error: Debes seleccionar al menos un ninja.';
            return;
        }

        // 3. Construir el DTO
        const asignacionData = {
            misionId: parseInt(misionId),
            ninjaIds: ninjaIds
        };

        logApiCall('Asignando equipo...', asignacionData);

        // 4. Enviar la petición
        try {
            const response = await fetch('/api/misiones/asignar-equipo', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(asignacionData)
            });

            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.message || 'Error del servidor');
            }

            const result = await response.json();
            logApiCall('Respuesta (Asignación):', result);
            logElement.textContent = `¡Equipo asignado! (${result.length} ninjas)`;
            
            // Recargamos el reporte TXT para ver los cambios
            btnReporte.click();

        } catch (error) {
            logApiCall('Error (Asignación):', error.message);
            logElement.textContent = `Error: ${error.message}`;
        }
    });

    // --- Funciones de Utilidad ---
    function logApiCall(message, data = null) {
        const time = new Date().toLocaleTimeString();
        let logMessage = `[${time}] ${message}\n`;
        
        if (data) {
            if (typeof data === 'object') {
                logMessage += JSON.stringify(data, null, 2) + '\n';
            } else {
                logMessage += data + '\n';
            }
        }
        
        console.log(message, data); // También en la consola del navegador
        outputLog.textContent = logMessage + '-----------------\n' + outputLog.textContent;
    }
});