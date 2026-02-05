"use strict";

/**
 * Simple UI for project13
 * - GET endpoints are public
 * - POST/PATCH/DELETE require Basic Auth (admin/admin)
 * - Login form expected in index.html with:
 *   #loginForm, #login_user, #login_pass, #logoutBtn, #authStatus
 *
 * Also expects:
 * - #status, #reload
 * - Portals filters: #workingFilter (""|"true"|"false"), #portalSort (""|"usersActiveAsc"|"usersActiveDesc")
 * - Portal form inputs: #p_id #p_name #p_url #p_users #p_working
 * - Job form inputs: #j_id #j_portalId #j_title #j_company #j_sphere #j_active
 * - Tables: #portalsTable tbody, #jobsTable tbody
 * - Stats: #c_portals #c_working #c_jobs #c_active #c_top
 */

const $ = (id) => document.getElementById(id);

// ===== AUTH (Basic) =====
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

function installLoginUi() {
    const form = $("loginForm");
    if (!form) {
        // login UI not present, but app can still run (GET-only)
        setAuthStatus();
        return;
    }

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

// ===== UI helpers =====
function setStatus(msg, isError = false) {
    const el = $("status");
    if (!el) return;
    el.textContent = msg;
    el.style.color = isError ? "#ff7a7a" : "#a8ffbf";
}

// ===== HTTP =====
async function http(method, path, bodyObj = null) {
    const opt = { method, headers: {} };

    // attach auth only if user provided creds
    if (auth.user && auth.pass) {
        opt.headers["Authorization"] = basicHeader(auth.user, auth.pass);
    }

    if (bodyObj !== null) {
        opt.headers["Content-Type"] = "application/json";
        opt.body = JSON.stringify(bodyObj);
    }

    const res = await fetch(path, opt);
    const text = await res.text();

    if (!res.ok) {
        // show 401 friendly hint
        if (res.status === 401) {
            throw new Error(`${method} ${path} -> 401 Unauthorized (login required) ${text ? ": " + text : ""}`);
        }
        throw new Error(`${method} ${path} -> ${res.status}${text ? ": " + text : ""}`);
    }

    if (!text) return null;

    // some endpoints may return plain text; try JSON, fallback to text
    try {
        return JSON.parse(text);
    } catch {
        return text;
    }
}

// ===== Queries =====
function portalQuery() {
    const params = new URLSearchParams();

    const w = $("workingFilter")?.value ?? "";
    const s = $("portalSort")?.value ?? "";

    if (w !== "") params.set("working", w);
    if (s !== "") params.set("sort", s);

    const qs = params.toString();
    return qs ? `?${qs}` : "";
}

// ===== Loaders =====
async function loadStats() {
    const s = await http("GET", "/api/stats");

    if ($("c_portals")) $("c_portals").textContent = s.portalsTotal;
    if ($("c_working")) $("c_working").textContent = `${s.portalsWorking} / ${s.portalsTotal}`;
    if ($("c_jobs")) $("c_jobs").textContent = s.jobsTotal;
    if ($("c_active")) $("c_active").textContent = `${s.jobsActive} / ${s.jobsTotal}`;
    if ($("c_top")) {
        $("c_top").textContent = s.topPortalByUsersActive
            ? `${s.topPortalByUsersActive.portalName} (${s.topPortalByUsersActive.usersActive})`
            : "—";
    }
}

async function loadPortals() {
    const data = await http("GET", "/api/portals" + portalQuery());
    const tbody = $("portalsTable")?.querySelector("tbody");
    if (!tbody) return;

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
        <button class="smallbtn btnGhost"
          data-kind="portal" data-act="toggle" data-id="${p.id}" data-val="${!p.working}">toggle</button>
        <button class="smallbtn btnDanger"
          data-kind="portal" data-act="del" data-id="${p.id}">delete</button>
      </td>`;
        tbody.appendChild(tr);
    }
}

async function loadJobs() {
    const data = await http("GET", "/api/joblistings");
    const tbody = $("jobsTable")?.querySelector("tbody");
    if (!tbody) return;

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
        <button class="smallbtn btnGhost"
          data-kind="job" data-act="toggle" data-id="${j.id}" data-val="${!j.active}">toggle</button>
        <button class="smallbtn btnDanger"
          data-kind="job" data-act="del" data-id="${j.id}">delete</button>
      </td>`;
        tbody.appendChild(tr);
    }
}

async function reloadAll() {
    try {
        await Promise.all([loadStats(), loadPortals(), loadJobs()]);
        setStatus("Loaded.");
    } catch (e) {
        setStatus(e.message || String(e), true);
    }
}

// ===== Forms =====
function installForms() {
    $("reload")?.addEventListener("click", reloadAll);
    $("workingFilter")?.addEventListener("change", reloadAll);
    $("portalSort")?.addEventListener("change", reloadAll);

    $("portalForm")?.addEventListener("submit", async (ev) => {
        ev.preventDefault();

        const body = {
            id: Number($("p_id")?.value),
            portalName: $("p_name")?.value ?? "",
            url: $("p_url")?.value ?? "",
            usersActive: Number($("p_users")?.value),
            working: ($("p_working")?.value ?? "false") === "true"
        };

        try {
            await http("POST", "/api/portals", body);
            await reloadAll();
            ev.target.reset();
        } catch (e) {
            setStatus(e.message || String(e), true);
        }
    });

    $("jobForm")?.addEventListener("submit", async (ev) => {
        ev.preventDefault();

        const body = {
            id: Number($("j_id")?.value),
            portalId: Number($("j_portalId")?.value),
            jobTitle: $("j_title")?.value ?? "",
            company: $("j_company")?.value ?? "",
            sphere: $("j_sphere")?.value ?? "",
            active: ($("j_active")?.value ?? "true") === "true"
        };

        try {
            await http("POST", "/api/joblistings", body);
            await reloadAll();
            ev.target.reset();
        } catch (e) {
            setStatus(e.message || String(e), true);
        }
    });
}

// ===== Row buttons (toggle/delete) =====
function installRowActions() {
    document.addEventListener("click", async (ev) => {
        const btn = ev.target.closest("button");
        if (!btn || !btn.dataset.act) return;

        const kind = btn.dataset.kind;
        const act = btn.dataset.act;
        const id = btn.dataset.id;

        try {
            if (kind === "portal") {
                if (act === "del") {
                    await http("DELETE", `/api/portals/${id}`);
                } else if (act === "toggle") {
                    await http("PATCH", `/api/portals/${id}/working`, { working: toBool(btn.dataset.val) });
                }
            } else if (kind === "job") {
                if (act === "del") {
                    await http("DELETE", `/api/joblistings/${id}`);
                } else if (act === "toggle") {
                    await http("PATCH", `/api/joblistings/${id}/active`, { active: toBool(btn.dataset.val) });
                }
            }

            await reloadAll();
        } catch (e) {
            setStatus(e.message || String(e), true);
        }
    });
}

// ===== Boot =====
document.addEventListener("DOMContentLoaded", async () => {
    installLoginUi();
    installForms();
    installRowActions();
    await reloadAll();
});