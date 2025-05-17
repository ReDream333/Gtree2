document.getElementById("login-form")
    .addEventListener("submit", function (e)
    {
        e.preventDefault();

        const login = document.getElementById("login").value;
        const password = document.getElementById("password").value;
        const errorMessage = document.getElementById("error-message");

        const data = {
            login: login,
            password: password
        };

        fetch(contextPath + '/sing', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(response => response.json())
            .then(data => {
                if (data.status === "success") {
                    window.location.href = data.redirectUrl; //
                } else {
                    errorMessage.textContent = data.message;
                }
            })
            .catch(error => {
                console.error("Ошибка:", error);
                errorMessage.textContent = "Произошла ошибка. Попробуйте снова.";
            });
});