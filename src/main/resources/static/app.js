// Espera a que el DOM esté completamente cargado
document.addEventListener('DOMContentLoaded', () => {

    // Referencias a los formularios y botones
    const formNinja = document.getElementById('form-ninja');
    const formMision = document.getElementById('form-mision');
    const btnReporte = document.getElementById('btn-reporte');
    const btnCargarMisiones = document.getElementById('btn-cargar-misiones'); // NUEVO

    // Referencias a las salidas de log
    const outputLog = document.getElementById('output-log');
    const outputReporte = document.getElementById('output-reporte');
    const misionListOutput = document.getElementById('mision-list-output'); // NUEVO

    // --- Event Listeners ---

    // 1. Registrar Ninja
    formNinja.addEventListener('submit', async (e) => {
        e.preventDefault(); // Evita que el formulario recargue la página

        const jutsusInput = document.getElementById('ninja-jutsus').value;
        const jutsusArray = jutsusInput.split(',')
                                     .map(s => s.trim()) // Limpia espacios
                                     .filter(s => s.length > 0); // Filtra vacíos

        // Construye el objeto DTO como lo espera el NinjaService
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

            // Manejo de respuesta HTTP
            if (!response.ok) {
                const errorData = await response.json();
                throw new Error(errorData.error || 'Error desconocido');
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

        // Construye el objeto DTO como lo espera el MisionService
        const misionData = {
            titulo: document.getElementById('mision-titulo').value,
            descripcion: "Misión registrada desde el frontend.", // Puedes añadir un campo si quieres
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
                throw new Error(errorData.error || 'Error desconocido');
            }

            const result = await response.json();
            logApiCall('Respuesta (Misión):', result);
        } catch (error) {
            logApiCall('Error (Misión):', error.message);
        }
    });

    // 3. Generar Reporte
    btnReporte.addEventListener('click', async () => {
        logApiCall('Generando Reporte TXT...');
        outputReporte.textContent = 'Cargando...';
        
        try {
            const response = await fetch('/api/reporte/txt');
            
            if (!response.ok) throw new Error('Error al cargar el reporte');

            const result = await response.text(); // El reporte es texto plano
            outputReporte.textContent = result;
            logApiCall('Reporte generado exitosamente.');
        } catch (error) {
            logApiCall('Error (Reporte):', error.message);
            outputReporte.textContent = 'Error al generar el reporte.';
        }
    });

    // 4. Cargar Misiones con Ninjas Elegibles (NUEVO)
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
                html += '<div class="mision-card">';
                html += `<h3>${mision.titulo} [Rango: ${mision.rango}]</h3>`;
                html += `<p>Recompensa: ${mision.recompensa} | Requiere: <strong>${mision.rangoRequerido}</strong></p>`;
                
                html += '<h4>Ninjas Elegibles:</h4>';
                html += '<ul>';
                if (mision.ninjasElegibles.length === 0) {
                    html += '<li>Ningún ninja registrado cumple el rango.</li>';
                } else {
                    for (const ninja of mision.ninjasElegibles) {
                        html += `<li>${ninja.nombre} (Rango: ${ninja.rango})</li>`;
                    }
                }
                html += '</ul>';
                html += '</div>';
            }
            // Insertar el HTML en la página
            misionListOutput.innerHTML = html;

        } catch (error) {
            logApiCall('Error (Cargar Misiones):', error.message);
            misionListOutput.innerHTML = '<p>Error al cargar las misiones.</p>';
        }
    });

    // --- Funciones de Utilidad ---

    // Función para mostrar logs en la pantalla
    function logApiCall(message, data = null) {
        const time = new Date().toLocaleTimeString();
        let logMessage = `[${time}] ${message}\n`;
        
        if (data) {
            // Si data es un objeto, lo convierte a JSON string. Si ya es un string, lo usa tal cual.
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