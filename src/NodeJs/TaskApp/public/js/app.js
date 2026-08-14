/* ============================================
   TaskFlow — Frontend Application Logic
   ============================================ */

(function () {
    "use strict";

    // ---- Configuration ----
    const API_BASE = "";          // Same origin
    const PAGE_SIZE = 10;

    // ---- State ----
    let authToken = localStorage.getItem("taskflow_token") || null;
    let currentUser = null;
    let tasks = [];
    let currentFilter = "all";    // all | pending | completed
    let currentSort = "createdAt:desc";
    let currentSkip = 0;
    let hasMore = true;
    let isLoading = false;


    // ============================================
    //  DOM References
    // ============================================
    const $ = (sel) => document.querySelector(sel);
    const $$ = (sel) => document.querySelectorAll(sel);

    const dom = {
        // Views
        authView: $("#auth-view"),
        dashboardView: $("#dashboard-view"),

        // Auth
        tabLogin: $("#tab-login"),
        tabRegister: $("#tab-register"),
        loginForm: $("#login-form"),
        registerForm: $("#register-form"),
        loginEmail: $("#login-email"),
        loginPassword: $("#login-password"),
        loginBtn: $("#login-btn"),
        registerName: $("#register-name"),
        registerEmail: $("#register-email"),
        registerPassword: $("#register-password"),
        registerBtn: $("#register-btn"),

        // Dashboard
        userAvatar: $("#user-avatar"),
        userNameDisplay: $("#user-name-display"),
        logoutBtn: $("#logout-btn"),
        statTotal: $("#stat-total"),
        statDone: $("#stat-done"),
        statPending: $("#stat-pending"),
        addTaskForm: $("#add-task-form"),
        taskInput: $("#task-input"),
        addTaskBtn: $("#add-task-btn"),
        sortSelect: $("#sort-select"),
        taskList: $("#task-list"),
        emptyState: $("#empty-state"),
        loadingIndicator: $("#loading-indicator"),
        loadMoreArea: $("#load-more-area"),
        loadMoreBtn: $("#load-more-btn"),
        toastContainer: $("#toast-container"),
    };


    // ============================================
    //  API Helpers
    // ============================================

    async function apiRequest(method, path, body = null) {
        const headers = { "Content-Type": "application/json" };
        if (authToken) {
            headers["Authorization"] = "Bearer " + authToken;
        }

        const opts = { method, headers };
        if (body) {
            opts.body = JSON.stringify(body);
        }

        const res = await fetch(API_BASE + path, opts);
        const data = await res.json().catch(() => null);

        if (!res.ok) {
            const msg = (data && data.error) || `Request failed (${res.status})`;
            throw new Error(msg);
        }

        return data;
    }


    // ============================================
    //  Toast Notifications
    // ============================================

    function showToast(message, type = "error") {
        const toast = document.createElement("div");
        toast.className = `toast toast-${type}`;

        const icon = type === "success"
            ? `<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/><polyline points="22 4 12 14.01 9 11.01"/></svg>`
            : `<svg xmlns="http://www.w3.org/2000/svg" width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><circle cx="12" cy="12" r="10"/><line x1="15" y1="9" x2="9" y2="15"/><line x1="9" y1="9" x2="15" y2="15"/></svg>`;

        toast.innerHTML = icon + `<span>${escapeHTML(message)}</span>`;
        dom.toastContainer.appendChild(toast);

        setTimeout(() => {
            toast.classList.add("toast-exit");
            toast.addEventListener("animationend", () => toast.remove());
        }, 3500);
    }


    // ============================================
    //  Auth Tab Switching
    // ============================================

    function switchAuthTab(tab) {
        if (tab === "login") {
            dom.tabLogin.classList.add("active");
            dom.tabRegister.classList.remove("active");
            dom.loginForm.classList.remove("hidden");
            dom.registerForm.classList.add("hidden");
        } else {
            dom.tabRegister.classList.add("active");
            dom.tabLogin.classList.remove("active");
            dom.registerForm.classList.remove("hidden");
            dom.loginForm.classList.add("hidden");
        }
    }

    dom.tabLogin.addEventListener("click", () => switchAuthTab("login"));
    dom.tabRegister.addEventListener("click", () => switchAuthTab("register"));


    // ============================================
    //  Auth — Login
    // ============================================

    dom.loginForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const email = dom.loginEmail.value.trim();
        const password = dom.loginPassword.value;

        if (!email || !password) return;

        dom.loginBtn.disabled = true;
        dom.loginBtn.textContent = "Signing in…";

        try {
            const data = await apiRequest("POST", "/users/login", { email, password });
            authToken = data.token;
            localStorage.setItem("taskflow_token", authToken);
            showToast("Welcome back!", "success");
            await enterDashboard();
        } catch (err) {
            showToast(err.message);
        } finally {
            dom.loginBtn.disabled = false;
            dom.loginBtn.textContent = "Sign In";
        }
    });


    // ============================================
    //  Auth — Register
    // ============================================

    dom.registerForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const name = dom.registerName.value.trim();
        const email = dom.registerEmail.value.trim();
        const password = dom.registerPassword.value;

        if (!name || !email || !password) return;

        dom.registerBtn.disabled = true;
        dom.registerBtn.textContent = "Creating account…";

        try {
            // Register the user
            await apiRequest("POST", "/users", { name, email, password });

            // Auto-login after registration
            const loginData = await apiRequest("POST", "/users/login", { email, password });
            authToken = loginData.token;
            localStorage.setItem("taskflow_token", authToken);
            showToast("Account created! Welcome aboard.", "success");
            await enterDashboard();
        } catch (err) {
            showToast(err.message);
        } finally {
            dom.registerBtn.disabled = false;
            dom.registerBtn.textContent = "Create Account";
        }
    });


    // ============================================
    //  Logout
    // ============================================

    dom.logoutBtn.addEventListener("click", () => {
        authToken = null;
        currentUser = null;
        tasks = [];
        localStorage.removeItem("taskflow_token");
        showDashboard(false);
        // Reset forms
        dom.loginForm.reset();
        dom.registerForm.reset();
        switchAuthTab("login");
    });


    // ============================================
    //  Dashboard Entry
    // ============================================

    async function enterDashboard() {
        try {
            // Fetch user profile
            currentUser = await apiRequest("GET", "/users/me");
            updateUserUI();
            showDashboard(true);
            await loadTasks(true);
        } catch (err) {
            // Token is invalid — force logout
            showToast("Session expired. Please sign in again.");
            dom.logoutBtn.click();
        }
    }

    function showDashboard(show) {
        if (show) {
            dom.authView.classList.add("hidden");
            dom.dashboardView.classList.remove("hidden");
        } else {
            dom.dashboardView.classList.add("hidden");
            dom.authView.classList.remove("hidden");
        }
    }

    function updateUserUI() {
        if (!currentUser) return;
        const name = currentUser.name || "User";
        dom.userNameDisplay.textContent = name;
        dom.userAvatar.textContent = name.charAt(0).toUpperCase();
    }


    // ============================================
    //  Load Tasks
    // ============================================

    async function loadTasks(reset = false) {
        if (isLoading) return;
        isLoading = true;

        if (reset) {
            currentSkip = 0;
            tasks = [];
            hasMore = true;
            dom.taskList.innerHTML = "";
        }

        dom.loadingIndicator.classList.remove("hidden");
        dom.emptyState.classList.add("hidden");
        dom.loadMoreArea.classList.add("hidden");

        try {
            // Build query params
            let query = `?limit=${PAGE_SIZE}&skip=${currentSkip}&sortBy=${currentSort}`;

            if (currentFilter === "pending") {
                query += "&completed=false";
            } else if (currentFilter === "completed") {
                query += "&completed=true";
            }

            const newTasks = await apiRequest("GET", "/tasks" + query);

            tasks = reset ? newTasks : [...tasks, ...newTasks];
            currentSkip += newTasks.length;
            hasMore = newTasks.length === PAGE_SIZE;

            renderTasks(reset ? tasks : null, newTasks);
            updateStats();

        } catch (err) {
            showToast(err.message);
        } finally {
            isLoading = false;
            dom.loadingIndicator.classList.add("hidden");
        }
    }


    // ============================================
    //  Render Tasks
    // ============================================

    function renderTasks(allTasks = null, appendOnly = null) {
        if (allTasks !== null) {
            // Full re-render
            dom.taskList.innerHTML = "";
            allTasks.forEach((task, i) => {
                const el = createTaskElement(task);
                el.style.animationDelay = `${i * 0.04}s`;
                dom.taskList.appendChild(el);
            });
        } else if (appendOnly) {
            appendOnly.forEach((task, i) => {
                const el = createTaskElement(task);
                el.style.animationDelay = `${i * 0.04}s`;
                dom.taskList.appendChild(el);
            });
        }

        // Toggle empty state
        if (tasks.length === 0) {
            dom.emptyState.classList.remove("hidden");
        } else {
            dom.emptyState.classList.add("hidden");
        }

        // Toggle load more
        if (hasMore && tasks.length > 0) {
            dom.loadMoreArea.classList.remove("hidden");
        } else {
            dom.loadMoreArea.classList.add("hidden");
        }
    }

    function createTaskElement(task) {
        const div = document.createElement("div");
        div.className = "task-item" + (task.completed ? " completed" : "");
        div.dataset.id = task._id;

        const createdDate = new Date(task.createdAt);
        const dateStr = formatDate(createdDate);

        div.innerHTML = `
            <label class="task-checkbox">
                <input type="checkbox" ${task.completed ? "checked" : ""} />
                <span class="checkmark">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" stroke-width="3" stroke-linecap="round" stroke-linejoin="round">
                        <polyline points="20 6 9 17 4 12"></polyline>
                    </svg>
                </span>
            </label>
            <span class="task-description">${escapeHTML(task.description)}</span>
            <span class="task-date">${dateStr}</span>
            <div class="task-actions">
                <button class="task-action-btn delete" title="Delete task">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <polyline points="3 6 5 6 21 6"></polyline>
                        <path d="M19 6v14a2 2 0 0 1-2 2H7a2 2 0 0 1-2-2V6m3 0V4a2 2 0 0 1 2-2h4a2 2 0 0 1 2 2v2"></path>
                    </svg>
                </button>
            </div>
        `;

        // Toggle completed
        const checkbox = div.querySelector('input[type="checkbox"]');
        checkbox.addEventListener("change", () => toggleTask(task._id, checkbox.checked, div));

        // Delete
        const deleteBtn = div.querySelector(".delete");
        deleteBtn.addEventListener("click", () => deleteTask(task._id, div));

        return div;
    }


    // ============================================
    //  Add Task
    // ============================================

    dom.addTaskForm.addEventListener("submit", async (e) => {
        e.preventDefault();
        const description = dom.taskInput.value.trim();
        if (!description) return;

        dom.addTaskBtn.disabled = true;

        try {
            const newTask = await apiRequest("POST", "/tasks", { description });

            // Add to local state
            tasks.unshift(newTask);
            currentSkip++;

            // Add to DOM at top
            const el = createTaskElement(newTask);
            if (dom.taskList.firstChild) {
                dom.taskList.insertBefore(el, dom.taskList.firstChild);
            } else {
                dom.taskList.appendChild(el);
            }

            dom.emptyState.classList.add("hidden");
            dom.taskInput.value = "";
            updateStats();
            showToast("Task added!", "success");

        } catch (err) {
            showToast(err.message);
        } finally {
            dom.addTaskBtn.disabled = false;
            dom.taskInput.focus();
        }
    });

    // Escape key clears input
    dom.taskInput.addEventListener("keydown", (e) => {
        if (e.key === "Escape") {
            dom.taskInput.value = "";
            dom.taskInput.blur();
        }
    });


    // ============================================
    //  Toggle Task Completed
    // ============================================

    async function toggleTask(taskId, completed, element) {
        try {
            await apiRequest("PATCH", `/tasks/${taskId}`, { completed });

            // Update local state
            const task = tasks.find(t => t._id === taskId);
            if (task) task.completed = completed;

            // Update DOM
            if (completed) {
                element.classList.add("completed");
            } else {
                element.classList.remove("completed");
            }

            updateStats();

            // If a filter is active, remove from view if it no longer matches
            if (currentFilter === "pending" && completed) {
                await animateRemove(element);
                tasks = tasks.filter(t => t._id !== taskId);
                if (tasks.length === 0) dom.emptyState.classList.remove("hidden");
            } else if (currentFilter === "completed" && !completed) {
                await animateRemove(element);
                tasks = tasks.filter(t => t._id !== taskId);
                if (tasks.length === 0) dom.emptyState.classList.remove("hidden");
            }

        } catch (err) {
            showToast(err.message);
            // Revert checkbox
            const checkbox = element.querySelector('input[type="checkbox"]');
            checkbox.checked = !completed;
        }
    }


    // ============================================
    //  Delete Task
    // ============================================

    async function deleteTask(taskId, element) {
        try {
            await apiRequest("DELETE", `/tasks/${taskId}`);

            await animateRemove(element);
            tasks = tasks.filter(t => t._id !== taskId);
            currentSkip = Math.max(0, currentSkip - 1);

            updateStats();
            showToast("Task deleted", "success");

            if (tasks.length === 0) {
                dom.emptyState.classList.remove("hidden");
            }
        } catch (err) {
            showToast(err.message);
        }
    }

    function animateRemove(el) {
        return new Promise((resolve) => {
            el.style.transition = "all 0.3s ease";
            el.style.opacity = "0";
            el.style.transform = "translateX(40px) scale(0.95)";
            el.style.maxHeight = el.offsetHeight + "px";

            setTimeout(() => {
                el.style.maxHeight = "0";
                el.style.padding = "0 20px";
                el.style.marginBottom = "0";
                el.style.borderWidth = "0";
            }, 200);

            setTimeout(() => {
                el.remove();
                resolve();
            }, 400);
        });
    }


    // ============================================
    //  Update Stats
    // ============================================

    function updateStats() {
        const total = tasks.length;
        const done = tasks.filter(t => t.completed).length;
        const pending = total - done;

        animateNumber(dom.statTotal, total);
        animateNumber(dom.statDone, done);
        animateNumber(dom.statPending, pending);
    }

    function animateNumber(el, target) {
        const current = parseInt(el.textContent) || 0;
        if (current === target) return;

        const duration = 300;
        const start = performance.now();

        function step(now) {
            const elapsed = now - start;
            const progress = Math.min(elapsed / duration, 1);
            // Ease out quad
            const eased = 1 - (1 - progress) * (1 - progress);
            const value = Math.round(current + (target - current) * eased);
            el.textContent = value;
            if (progress < 1) requestAnimationFrame(step);
        }

        requestAnimationFrame(step);
    }


    // ============================================
    //  Filters
    // ============================================

    $$(".filter-tab").forEach(tab => {
        tab.addEventListener("click", () => {
            $$(".filter-tab").forEach(t => t.classList.remove("active"));
            tab.classList.add("active");
            currentFilter = tab.dataset.filter;
            loadTasks(true);
        });
    });


    // ============================================
    //  Sorting
    // ============================================

    dom.sortSelect.addEventListener("change", () => {
        currentSort = dom.sortSelect.value;
        loadTasks(true);
    });


    // ============================================
    //  Load More
    // ============================================

    dom.loadMoreBtn.addEventListener("click", () => {
        loadTasks(false);
    });


    // ============================================
    //  Utilities
    // ============================================

    function escapeHTML(str) {
        const div = document.createElement("div");
        div.textContent = str;
        return div.innerHTML;
    }

    function formatDate(date) {
        const now = new Date();
        const diffMs = now - date;
        const diffMins = Math.floor(diffMs / 60000);
        const diffHours = Math.floor(diffMs / 3600000);
        const diffDays = Math.floor(diffMs / 86400000);

        if (diffMins < 1) return "Just now";
        if (diffMins < 60) return `${diffMins}m ago`;
        if (diffHours < 24) return `${diffHours}h ago`;
        if (diffDays < 7) return `${diffDays}d ago`;

        return date.toLocaleDateString("en-US", {
            month: "short",
            day: "numeric",
        });
    }


    // ============================================
    //  Initialization
    // ============================================

    async function init() {
        if (authToken) {
            // Attempt to restore session
            await enterDashboard();
        } else {
            showDashboard(false);
        }
    }

    init();

})();
