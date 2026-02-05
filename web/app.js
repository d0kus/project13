const $ = (id) => document.getElementById(id);

function setStatus(msg, isError = false) {
    $("status").textContent = msg;
    $("status").style.color = isError ? "#ff7a7a" : "#a8ffbf";
}

async function http(method, path, bodyObj = null) {
    const opt = { method, headers: {} };
    if (bodyObj) {
        opt.headers["Content-Type"] = "application/json";
        opt.body = JSON.stringify(bodyObj);
    }
    const res = await fetch(path, opt);
    const text = await res.text();
    if (!res.ok) throw new Error(`${method} ${path} -> ${res.status}: ${text}`);
    return text ? JSON.parse(text) : null;
}

function portalQuery() {
    const params = new URLSearchParams();
    const w = $("workingFilter").value;
    const s = $("portalSort").value;
    if (w !== "") params.set("working", w);
    if (s !== "") params.set("sort", s);
    const qs = params.toString();
    return qs ? `?${qs}` : "";
}

async function loadStats() {
    const s = await http("GET", "/api/stats");
    $("c_portals").textContent = s.portalsTotal;
    $("c_working").textContent = `${s.portalsWorking} / ${s.portalsTotal}`;
    $("c_jobs").textContent = s.jobsTotal;
    $("c_active").textContent = `${s.jobsActive} / ${s.jobsTotal}`;
    $("c_top").textContent = s.topPortalByUsersActive
        ? `${s.topPortalByUsersActive.portalName} (${s.topPortalByUsersActive.usersActive})`
        : "—";
}

async function loadPortals() {
    const data = await http("GET", "/api/portals" + portalQuery());
    const tbody = $("portalsTable").querySelector("tbody");
    tbody.innerHTML = "";
    for (const p of data) {
        const tr = document.createElement("tr");
        tr.innerHTML = `
      <td>${p.id}</td>
      <td>${p.portalName}</td>
      <td><a href="${p.url}" target="_blank" rel="noreferrer">${p.url}</a></td>
      <td>${p.usersActive}</td>
      <td>${p.working}</td>
      <td>
        <button class="smallbtn btnGhost" data-kind="portal" data-act="toggle" data-id="${p.id}" data-val="${!p.working}">toggle</button>
        <button class="smallbtn btnDanger" data-kind="portal" data-act="del" data-id="${p.id}">delete</button>
      </td>`;
        tbody.appendChild(tr);
    }
}

async function loadJobs() {
    const data = await http("GET", "/api/joblistings");
    const tbody = $("jobsTable").querySelector("tbody");
    tbody.innerHTML = "";
    for (const j of data) {
        const tr = document.createElement("tr");
        tr.innerHTML = `
      <td>${j.id}</td>
      <td>${j.portalId}</td>
      <td>${j.jobTitle}</td>
      <td>${j.company}</td>
      <td>${j.sphere}</td>
      <td>${j.active}</td>
      <td>
        <button class="smallbtn btnGhost" data-kind="job" data-act="toggle" data-id="${j.id}" data-val="${!j.active}">toggle</button>
        <button class="smallbtn btnDanger" data-kind="job" data-act="del" data-id="${j.id}">delete</button>
      </td>`;
        tbody.appendChild(tr);
    }
}

async function reloadAll() {
    try {
        await Promise.all([loadStats(), loadPortals(), loadJobs()]);
        setStatus("Loaded.");
    } catch (e) {
        setStatus(e.message, true);
    }
}

$("reload").addEventListener("click", reloadAll);
$("workingFilter").addEventListener("change", reloadAll);
$("portalSort").addEventListener("change", reloadAll);

$("portalForm").addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const body = {
        id: $("p_id").value,
        portalName: $("p_name").value,
        url: $("p_url").value,
        usersActive: $("p_users").value,
        working: $("p_working").value
    };
    try {
        await http("POST", "/api/portals", body);
        await reloadAll();
        ev.target.reset();
    } catch (e) { setStatus(e.message, true); }
});

$("jobForm").addEventListener("submit", async (ev) => {
    ev.preventDefault();
    const body = {
        id: $("j_id").value,
        portalId: $("j_portalId").value,
        jobTitle: $("j_title").value,
        company: $("j_company").value,
        sphere: $("j_sphere").value,
        active: $("j_active").value
    };
    try {
        await http("POST", "/api/joblistings", body);
        await reloadAll();
        ev.target.reset();
    } catch (e) { setStatus(e.message, true); }
});

document.addEventListener("click", async (ev) => {
    const btn = ev.target.closest("button");
    if (!btn || !btn.dataset.act) return;

    const kind = btn.dataset.kind;
    const id = btn.dataset.id;

    try {
        if (kind === "portal") {
            if (btn.dataset.act === "del") {
                await http("DELETE", `/api/portals/${id}`);
            } else if (btn.dataset.act === "toggle") {
                await http("PATCH", `/api/portals/${id}/working`, { working: btn.dataset.val });
            }
        } else if (kind === "job") {
            if (btn.dataset.act === "del") {
                await http("DELETE", `/api/joblistings/${id}`);
            } else if (btn.dataset.act === "toggle") {
                await http("PATCH", `/api/joblistings/${id}/active`, { active: btn.dataset.val });
            }
        }

        await reloadAll();
    } catch (e) {
        setStatus(e.message, true);
    }
});

reloadAll();