const methodSelect = document.getElementById('methodSelect');
const pathInput = document.getElementById('pathInput');
const bodyInput = document.getElementById('bodyInput');
const requestInspector = document.getElementById('requestInspector');
const responseInspector = document.getElementById('responseInspector');
const statusBadge = document.getElementById('statusBadge');
const tableBody = document.getElementById('productTableBody');

const scenarios = {
    getAll: {
        method: 'GET', path: '/api/products', body: ''
    },
    getOne: {
        method: 'GET', path: '/api/products/1', body: ''
    },
    filter: {
        method: 'GET', path: '/api/products?brand=Wusthof', body: ''
    },
    create: {
        method: 'POST', path: '/api/products', body: JSON.stringify({
            name: 'Santoku Knife',
            price: 109.90,
            stockQuantity: 10,
            categoryId: 1,
            brand: 'Global'
        }, null, 2)
    },
    invalid: {
        method: 'POST', path: '/api/products', body: JSON.stringify({
            name: '',
            price: -10,
            stockQuantity: -2,
            categoryId: null,
            brand: 'Demo'
        }, null, 2)
    },
    put: {
        method: 'PUT', path: '/api/products/1', body: JSON.stringify({
            name: 'Chef Knife - Replaced',
            price: 109.90,
            stockQuantity: 15,
            categoryId: 1,
            brand: 'Wusthof'
        }, null, 2)
    },
    patch: {
        method: 'PATCH', path: '/api/products/1', body: JSON.stringify({
            price: 89.90,
            stockQuantity: 20
        }, null, 2)
    },
    delete: {
        method: 'DELETE', path: '/api/products/1', body: ''
    },
    notFound: {
        method: 'GET', path: '/api/products/999', body: ''
    }
};

function applyScenario(name) {
    const scenario = scenarios[name];
    methodSelect.value = scenario.method;
    pathInput.value = scenario.path;
    bodyInput.value = scenario.body;
    updateRequestPreview();
}

document.querySelectorAll('[data-scenario]').forEach(button => {
    button.addEventListener('click', () => applyScenario(button.dataset.scenario));
});

function updateRequestPreview() {
    const method = methodSelect.value;
    const path = pathInput.value.trim() || '/api/products';
    let preview = `${method} ${path} HTTP/1.1\nAccept: application/json`;
    if (['POST', 'PUT', 'PATCH'].includes(method)) {
        preview += '\nContent-Type: application/json';
        if (bodyInput.value.trim()) {
            preview += `\n\n${bodyInput.value.trim()}`;
        }
    }
    requestInspector.textContent = preview;
}

[methodSelect, pathInput, bodyInput].forEach(element => {
    element.addEventListener('input', updateRequestPreview);
    element.addEventListener('change', updateRequestPreview);
});

async function sendRequest() {
    const method = methodSelect.value;
    const path = pathInput.value.trim();
    const headers = { 'Accept': 'application/json' };
    const options = { method, headers };

    if (['POST', 'PUT', 'PATCH'].includes(method)) {
        headers['Content-Type'] = 'application/json';
        options.body = bodyInput.value.trim() || '{}';
    }

    updateRequestPreview();
    responseInspector.textContent = 'Sending request...';
    statusBadge.textContent = 'Sending';
    statusBadge.className = 'status-badge neutral';

    const started = performance.now();
    try {
        const response = await fetch(path, options);
        const elapsed = Math.round(performance.now() - started);
        const text = await response.text();
        let prettyBody = '(empty response body)';
        if (text) {
            try { prettyBody = JSON.stringify(JSON.parse(text), null, 2); }
            catch { prettyBody = text; }
        }

        const importantHeaders = [];
        for (const name of ['content-type', 'location']) {
            const value = response.headers.get(name);
            if (value) importantHeaders.push(`${name}: ${value}`);
        }
        responseInspector.textContent =
            `HTTP ${response.status} ${response.statusText}\n` +
            `${importantHeaders.join('\n')}\n` +
            `Elapsed: ${elapsed} ms\n\n${prettyBody}`;

        statusBadge.textContent = `${response.status} ${response.statusText}`;
        statusBadge.className = 'status-badge ' +
            (response.status < 300 ? 'success' : response.status < 500 ? 'client' : 'server');

        if (response.ok && ['POST', 'PUT', 'PATCH', 'DELETE'].includes(method)) {
            await refreshProducts();
            if (typeof window.refreshUiProductsSilently === 'function') {
                await window.refreshUiProductsSilently();
            }
        }
    } catch (error) {
        statusBadge.textContent = 'Network error';
        statusBadge.className = 'status-badge server';
        responseInspector.textContent = String(error);
    }
}

document.getElementById('sendButton').addEventListener('click', sendRequest);

async function refreshProducts() {
    try {
        const response = await fetch('/api/products');
        const products = await response.json();
        tableBody.innerHTML = '';
        products.forEach(product => {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${product.id}</td>
                <td>${escapeHtml(product.name)}</td>
                <td>${escapeHtml(product.brand ?? '')}</td>
                <td>${product.price}</td>
                <td>${product.stockQuantity}</td>
                <td>${product.categoryId}</td>`;
            tableBody.appendChild(row);
        });
    } catch (error) {
        tableBody.innerHTML = `<tr><td colspan="6">Unable to load products: ${escapeHtml(String(error))}</td></tr>`;
    }
}

document.getElementById('refreshProducts').addEventListener('click', refreshProducts);

document.getElementById('resetButton').addEventListener('click', async () => {
    const response = await fetch('/api/demo/reset', { method: 'POST' });
    if (response.ok) {
        applyScenario('getAll');
        await refreshProducts();
        if (typeof window.refreshUiProductsSilently === 'function') {
            await window.refreshUiProductsSilently();
        }
        responseInspector.textContent = 'Demo data reset. Product IDs are back to 1–5.';
        statusBadge.textContent = 'Reset complete';
        statusBadge.className = 'status-badge success';
    } else {
        responseInspector.textContent = 'Reset failed.';
        statusBadge.textContent = 'Reset failed';
        statusBadge.className = 'status-badge server';
    }
});

function escapeHtml(value) {
    return String(value)
        .replaceAll('&', '&amp;')
        .replaceAll('<', '&lt;')
        .replaceAll('>', '&gt;')
        .replaceAll('"', '&quot;')
        .replaceAll("'", '&#039;');
}

applyScenario('getAll');
refreshProducts();
