function togglePasswordVisibility() {
    const passwordInput = document.getElementById("password");
    const passwordIcon = document.querySelector(".toggle-password");
    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        passwordIcon.textContent = "🙈";
    } else {
        passwordInput.type = "password";
        passwordIcon.textContent = "👁️";
    }
}

const FRONTEND_BASE_URL = document.body.getAttribute("data-frontend-url");

function redirectToRegister() {
    window.location.href = `${FRONTEND_BASE_URL}/register`;
}

function redirectToForgotPassword() {
    window.location.href = `${FRONTEND_BASE_URL}/forgot-password`;
}
