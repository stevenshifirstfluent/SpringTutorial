/**
 * Session 1 teaching file: UI -> REST API integration.
 *
 * The important sequence is:
 *   1. user interacts with HTML
 *   2. JavaScript calls fetch(...)
 *   3. Spring @RestController returns JSON
 *   4. JavaScript converts JSON to objects
 *   5. JavaScript updates the DOM
 */

const uiProductCards = document.getElementById('uiProductCards');
const uiProductStatus = document.getElementById('uiProductStatus');
const uiBrandFilter = document.getElementById('uiBrandFilter');
const uiFetchCode = document.getElementById('uiFetchCode');
const uiApiResponse = document.getElementById('uiApiResponse');
const uiActionLabel = document.getElementById('uiActionLabel');
const uiEndpointLabel = document.getElementById('uiEndpointLabel');
const uiTraceBadge = document.getElementById('uiTraceBadge');

function setUiTrace({ action, endpoint, code, responseText, status, ok }) {
    uiActionLabel.textContent = action;
    uiEndpointLabel.textContent = endpoint;
    uiFetchCode.textContent = code;
    uiApiResponse.textContent = responseText;

    if (status) {
        uiTraceBadge.textContent = status;
        uiTraceBadge.className = 'status-badge ' + (ok ? 'success' : 'client');
    } else {
        uiTraceBadge.textContent = 'Calling API';
        uiTraceBadge.className = 'status-badge neutral';
    }
}

function prettyJson(text) {
    if (!text) return '(empty response body)';
    try { return JSON.stringify(JSON.parse(text), null, 2); }
    catch { return text; }
}

/** GET collection: UI filter -> @RequestParam -> JSON array -> product cards. */
async function loadUiProducts() {
    const brand = uiBrandFilter.value.trim();
    const endpoint = brand
        ? `/api/products?brand=${encodeURIComponent(brand)}`
        : '/api/products';

    const code = brand
        ? `const response = await fetch('${endpoint}');\nconst products = await response.json();\nrenderProducts(products);`
        : `const response = await fetch('/api/products');\nconst products = await response.json();\nrenderProducts(products);`;

    uiProductStatus.innerHTML = `Calling <code>GET ${escapeHtmlUi(endpoint)}</code>...`;
    setUiTrace({ action: brand ? `Filter products by brand: ${brand}` : 'Load the product list', endpoint: `GET ${endpoint}`, code, responseText: 'Waiting for response...' });

    try {
        const response = await fetch(endpoint, { headers: { Accept: 'application/json' } });
        const text = await response.text();
        const products = text ? JSON.parse(text) : [];

        setUiTrace({
            action: brand ? `Filter products by brand: ${brand}` : 'Load the product list',
            endpoint: `GET ${endpoint}`,
            code,
            responseText: `HTTP ${response.status} ${response.statusText}\n\n${prettyJson(text)}`,
            status: `${response.status} ${response.statusText}`,
            ok: response.ok
        });

        if (!response.ok) {
            uiProductStatus.textContent = 'The API returned an error.';
            uiProductCards.innerHTML = '';
            return;
        }

        renderUiProducts(products);
        uiProductStatus.innerHTML = `${products.length} product(s) rendered from JSON. <strong>No page reload occurred.</strong>`;
    } catch (error) {
        uiProductStatus.textContent = `Network error: ${error}`;
        uiProductCards.innerHTML = '';
        setUiTrace({ action: 'Load products', endpoint: `GET ${endpoint}`, code, responseText: String(error), status: 'Network error', ok: false });
    }
}

function renderUiProducts(products) {
    uiProductCards.innerHTML = '';

    if (products.length === 0) {
        uiProductCards.innerHTML = '<div class="empty-state">No products matched the current filter.</div>';
        return;
    }

    products.forEach(product => {
        const card = document.createElement('article');
        card.className = 'product-card';
        card.innerHTML = `
            <div class="product-card-topline">
                <span class="product-id">#${product.id}</span>
                <span class="stock-chip">Stock ${product.stockQuantity}</span>
            </div>
            <h4>${escapeHtmlUi(product.name)}</h4>
            <p>${escapeHtmlUi(product.brand ?? 'No brand')} · Category ${product.categoryId}</p>
            <div class="product-price">$${Number(product.price).toFixed(2)}</div>
            <div class="product-actions">
                <button class="ghost-button view-product" type="button" data-id="${product.id}">View API</button>
                <button class="ghost-button restock-product" type="button" data-id="${product.id}" data-stock="${product.stockQuantity}">+1 Stock</button>
                <button class="danger-button delete-product" type="button" data-id="${product.id}" data-name="${escapeHtmlUi(product.name)}">Delete</button>
            </div>`;
        uiProductCards.appendChild(card);
    });

    document.querySelectorAll('.view-product').forEach(button => {
        button.addEventListener('click', () => viewUiProduct(button.dataset.id));
    });
    document.querySelectorAll('.restock-product').forEach(button => {
        button.addEventListener('click', () => patchUiProductStock(button.dataset.id, Number(button.dataset.stock)));
    });
    document.querySelectorAll('.delete-product').forEach(button => {
        button.addEventListener('click', () => deleteUiProduct(button.dataset.id, button.dataset.name));
    });
}

async function refreshUiProductsSilently() {
    const brand = uiBrandFilter.value.trim();
    const endpoint = brand
        ? `/api/products?brand=${encodeURIComponent(brand)}`
        : '/api/products';
    try {
        const response = await fetch(endpoint, { headers: { Accept: 'application/json' } });
        if (!response.ok) return;
        const products = await response.json();
        renderUiProducts(products);
        uiProductStatus.innerHTML = `${products.length} product(s) rendered from JSON. <strong>No page reload occurred.</strong>`;
    } catch (_) {
        // Keep the teaching trace focused on the user's previous action.
    }
}

window.refreshUiProductsSilently = refreshUiProductsSilently;

/** GET one: demonstrates @PathVariable. */
async function viewUiProduct(id) {
    const endpoint = `/api/products/${id}`;
    const code = `const response = await fetch('${endpoint}');\nconst product = await response.json();\nshowProduct(product);`;
    setUiTrace({ action: `View product ${id}`, endpoint: `GET ${endpoint}`, code, responseText: 'Waiting for response...' });

    const response = await fetch(endpoint, { headers: { Accept: 'application/json' } });
    const text = await response.text();
    setUiTrace({
        action: `View product ${id}`,
        endpoint: `GET ${endpoint}`,
        code,
        responseText: `HTTP ${response.status} ${response.statusText}\n\n${prettyJson(text)}`,
        status: `${response.status} ${response.statusText}`,
        ok: response.ok
    });
}

/** POST: HTML form -> JavaScript object -> JSON.stringify -> @RequestBody. */
async function createUiProduct(event) {
    event.preventDefault();

    const payload = {
        name: document.getElementById('uiName').value,
        price: Number(document.getElementById('uiPrice').value),
        stockQuantity: Number(document.getElementById('uiStock').value),
        categoryId: Number(document.getElementById('uiCategory').value),
        brand: document.getElementById('uiBrand').value
    };

    const code = `const payload = ${JSON.stringify(payload, null, 2)};\n\nconst response = await fetch('/api/products', {\n  method: 'POST',\n  headers: { 'Content-Type': 'application/json' },\n  body: JSON.stringify(payload)\n});\n\nconst created = await response.json();`;

    setUiTrace({ action: 'Submit the Create Product form', endpoint: 'POST /api/products', code, responseText: 'Waiting for response...' });

    try {
        const response = await fetch('/api/products', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(payload)
        });
        const text = await response.text();
        const location = response.headers.get('location');
        const headerLine = location ? `\nLocation: ${location}` : '';

        setUiTrace({
            action: 'Submit the Create Product form',
            endpoint: 'POST /api/products',
            code,
            responseText: `HTTP ${response.status} ${response.statusText}${headerLine}\n\n${prettyJson(text)}`,
            status: `${response.status} ${response.statusText}`,
            ok: response.ok
        });

        if (response.ok) {
            uiProductStatus.innerHTML = 'Product created through the REST API. The product browser was refreshed from <code>GET /api/products</code>.';
            await refreshUiProductsSilently();
            if (typeof refreshProducts === 'function') await refreshProducts();
        }
    } catch (error) {
        setUiTrace({ action: 'Submit the Create Product form', endpoint: 'POST /api/products', code, responseText: String(error), status: 'Network error', ok: false });
    }
}

/** PATCH: a small UI action sends only the field that changed. */
async function patchUiProductStock(id, currentStock) {
    const endpoint = `/api/products/${id}`;
    const payload = { stockQuantity: currentStock + 1 };
    const code = `const payload = ${JSON.stringify(payload, null, 2)};\n\nconst response = await fetch('${endpoint}', {\n  method: 'PATCH',\n  headers: { 'Content-Type': 'application/json' },\n  body: JSON.stringify(payload)\n});`;

    setUiTrace({ action: `Increase product ${id} stock by 1`, endpoint: `PATCH ${endpoint}`, code, responseText: 'Waiting for response...' });

    const response = await fetch(endpoint, {
        method: 'PATCH',
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(payload)
    });
    const text = await response.text();
    setUiTrace({
        action: `Increase product ${id} stock by 1`,
        endpoint: `PATCH ${endpoint}`,
        code,
        responseText: `HTTP ${response.status} ${response.statusText}\n\n${prettyJson(text)}`,
        status: `${response.status} ${response.statusText}`,
        ok: response.ok
    });

    if (response.ok) {
        await refreshUiProductsSilently();
        if (typeof refreshProducts === 'function') await refreshProducts();
    }
}

/** DELETE: button -> DELETE endpoint -> 204 -> refresh UI collection. */
async function deleteUiProduct(id, name) {
    const endpoint = `/api/products/${id}`;
    const code = `const response = await fetch('${endpoint}', {\n  method: 'DELETE'\n});\n\nif (response.status === 204) {\n  await loadProducts();\n}`;
    setUiTrace({ action: `Delete ${name}`, endpoint: `DELETE ${endpoint}`, code, responseText: 'Waiting for response...' });

    const response = await fetch(endpoint, { method: 'DELETE', headers: { Accept: 'application/json' } });
    const text = await response.text();
    setUiTrace({
        action: `Delete ${name}`,
        endpoint: `DELETE ${endpoint}`,
        code,
        responseText: `HTTP ${response.status} ${response.statusText}\n\n${prettyJson(text)}`,
        status: `${response.status} ${response.statusText}`,
        ok: response.ok
    });

    if (response.ok) {
        await refreshUiProductsSilently();
        if (typeof refreshProducts === 'function') await refreshProducts();
    }
}

function escapeHtmlUi(value) {
    return String(value)
        .replaceAll('&', '&amp;')
        .replaceAll('<', '&lt;')
        .replaceAll('>', '&gt;')
        .replaceAll('"', '&quot;')
        .replaceAll("'", '&#039;');
}

document.getElementById('uiReloadProducts').addEventListener('click', loadUiProducts);
document.getElementById('uiApplyFilter').addEventListener('click', loadUiProducts);
document.getElementById('uiClearFilter').addEventListener('click', () => {
    uiBrandFilter.value = '';
    loadUiProducts();
});
document.getElementById('uiCreateForm').addEventListener('submit', createUiProduct);
uiBrandFilter.addEventListener('keydown', event => {
    if (event.key === 'Enter') {
        event.preventDefault();
        loadUiProducts();
    }
});

loadUiProducts();
