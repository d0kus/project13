const $ = (id) => document.getElementById(id);

const auth = {
    user: localStorage.getItem("auth_user") || "",
    pass: localStorage.getItem("auth_pass") || ""
};

const toBool = (v) => v === true || v === "true";

function basicHeader(user, pass) {
    return "Basic " + btoa(`${user}:${pass}`);
}

function setAuthStatus() {
    const el = $("authStatus");
    if (!el) return;
    el.textContent = (auth.user && auth.pass) ? `logged in as ${auth.user}` : "guest";
}

function installAuthUi() {
    const form = $("loginForm");
    if (!form) return;

    const u = $("login_user");
    const p = $("login_pass");
    const logoutBtn = $("logoutBtn");

    if (u) u.value = auth.user;
    if (p) p.value = auth.pass;

    form.addEventListener("submit", async (ev) => {
        ev.preventDefault();
        auth.user = (u?.value || "").trim();
        auth.pass = (p?.value || "");
        localStorage.setItem("auth_user", auth.user);
        localStorage.setItem("auth_pass", auth.pass);
        setAuthStatus();
        await reloadAll();
    });

    logoutBtn?.addEventListener("click", async () => {
        auth.user = "";
        auth.pass = "";
        localStorage.removeItem("auth_user");
        localStorage.removeItem("auth_pass");
        if (u) u.value = "";
        if (p) p.value = "";
        setAuthStatus();
        await reloadAll();
    });

    setAuthStatus();
}

function setStatus(msg, isError = false) {
    $("status").textContent = msg;
    $("status").style.color = isError ? "#ff7a7a" : "#a8ffbf";
}

async function http(method, path, bodyObj = null) {
    const opt = { method, headers: {} };

    if (auth.user && auth.pass) {
        opt.headers["Authorization"] = basicHeader(auth.user, auth.pass);
    }

    if (bodyObj) {
        opt.headers["Content-Type"] = "application/json";
        opt.body = JSON.stringify(bodyObj);
    }

    const res = await fetch(path, opt);
    const text = await res.text();
    if (!res.ok) {
        if (res.status === 401) throw new Error(`${method} ${path} -> 401 (login required): ${text}`);
        throw new Error(`${method} ${path} -> ${res.status}: ${text}`);
    }
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

async function loadUsers() {
    const data = await http("GET", "/api/users");
    const table = $("usersTable");
    if (!table) return;

    const tbody = table.querySelector("tbody");
    tbody.innerHTML = "";

    for (const u of data) {
        const tr = document.createElement("tr");
        tr.innerHTML = `
      <td>${u.id}</td>
      <td>${u.name}</td>
      <td>${u.role}</td>
      <td>${u.country}</td>
      <td>${u.sphere}</td>
      <td>
        <button class="smallbtn btnGhost" data-kind="user" data-act="work" data-id="${u.id}">work</button>
      </td>`;
        tbody.appendChild(tr);
    }
}

async function reloadAll() {
    try {
        await Promise.all([loadStats(), loadPortals(), loadJobs(), loadUsers()]);
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
        id: Number($("p_id").value),
        portalName: $("p_name").value,
        url: $("p_url").value,
        usersActive: Number($("p_users").value),
        working: $("p_working").value === "true"
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
        id: Number($("j_id").value),
        portalId: Number($("j_portalId").value),
        jobTitle: $("j_title").value,
        company: $("j_company").value,
        sphere: $("j_sphere").value,
        active: $("j_active").value === "true"
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
                await http("PATCH", `/api/portals/${id}/working`, { working: toBool(btn.dataset.val) });
            }
        } else if (kind === "job") {
            if (btn.dataset.act === "del") {
                await http("DELETE", `/api/joblistings/${id}`);
            } else if (btn.dataset.act === "toggle") {
                await http("PATCH", `/api/joblistings/${id}/active`, { active: toBool(btn.dataset.val) });
            }
        } else if (kind === "user") {
            if (btn.dataset.act === "work") {
                const r = await http("POST", `/api/users/${id}/work`, {});
                setStatus(r.message);
            }
        }

        // для user->work не обязательно reloadAll, но пусть будет ок
        if (!(kind === "user" && btn.dataset.act === "work")) {
            await reloadAll();
        }
    } catch (e) {
        setStatus(e.message, true);
    }
});

installAuthUi();
reloadAll();