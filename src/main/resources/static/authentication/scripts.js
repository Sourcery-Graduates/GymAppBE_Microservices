function togglePasswordVisibility() {
    const passwordInput = document.getElementById("password");
    const passwordIcon = document.querySelector(".toggle-password i");

    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        passwordIcon.classList.remove("fa-eye");
        passwordIcon.classList.add("fa-eye-slash");
    } else {
        passwordInput.type = "password";
        passwordIcon.classList.remove("fa-eye-slash");
        passwordIcon.classList.add("fa-eye");
    }
}

const FRONTEND_BASE_URL = document.body.getAttribute("data-frontend-url");

function redirectToRegister() {
    window.location.href = `${FRONTEND_BASE_URL}/register`;
}

function redirectToForgotPassword() {
    window.location.href = `${FRONTEND_BASE_URL}/forgot-password`;
}
