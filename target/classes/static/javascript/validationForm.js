document.addEventListener('DOMContentLoaded', function () {
    // Add event listeners to input fields
    document.getElementById('floatingName').addEventListener('input', validateName);
    document.getElementById('floatingSurname').addEventListener('input', validateSurname);
    document.getElementById('floatingUsername').addEventListener('input', validateUsername);
    document.getElementById('floatingPassword').addEventListener('input', validatePassword);
    document.getElementById('floatingPasswordConfirm').addEventListener('input', validatePasswordConfirm);

    // Initial validation
    validateAllFields();

    function validateName() {
        var nameInput = document.getElementById('floatingName');
        var nameRegex = /^[a-zA-Z]+$/;
        updateValidationStatus(nameInput, nameRegex.test(nameInput.value));
        validateAllFields();
    }

    function validateSurname() {
        var surnameInput = document.getElementById('floatingSurname');
        var surnameRegex = /^[a-zA-Z]+$/;
        updateValidationStatus(surnameInput, surnameRegex.test(surnameInput.value));
        validateAllFields();
    }

    function validateUsername() {
        var usernameInput = document.getElementById('floatingUsername');
        var usernameRegex = /^[a-zA-Z0-9_]+$/;
        updateValidationStatus(usernameInput, usernameRegex.test(usernameInput.value));
        validateAllFields();
    }

    function validatePassword() {
        var passwordInput = document.getElementById('floatingPassword');
        var passwordRegex = /^[a-zA-Z0-9_]{8,15}$/;
        updateValidationStatus(passwordInput, passwordRegex.test(passwordInput.value));
        validateAllFields();
    }

    function validatePasswordConfirm() {
        var passwordConfirmInput = document.getElementById('floatingPasswordConfirm');
        var passwordInput = document.getElementById('floatingPassword');
        var passwordsMatch = passwordInput.value === passwordConfirmInput.value;
        updateValidationStatus(passwordConfirmInput, passwordsMatch);
        validateAllFields();
    }

    function updateValidationStatus(inputField, isValid) {
        if (isValid) {
            inputField.classList.remove('is-invalid');
            inputField.classList.add('is-valid');
        } else {
            inputField.classList.remove('is-valid');
            inputField.classList.add('is-invalid');
        }
    }

    function validateAllFields() {
        var nameValid = document.getElementById('floatingName').classList.contains('is-valid');
        var surnameValid = document.getElementById('floatingSurname').classList.contains('is-valid');
        var usernameValid = document.getElementById('floatingUsername').classList.contains('is-valid');
        var passwordValid = document.getElementById('floatingPassword').classList.contains('is-valid');
        var passwordConfirmValid = document.getElementById('floatingPasswordConfirm').classList.contains('is-valid');

        var submitButton = document.getElementById("submitButton");
        if(submitButton==null)
        {
            submitButton = document.createElement('button');
            submitButton.className = "btn btn-primary btn-lg";
            submitButton.textContent = "Register";
            submitButton.id = "submitButton";
        }

        if (nameValid && surnameValid && usernameValid && passwordValid && passwordConfirmValid) {
                document.getElementById('divButton').appendChild(submitButton);
        }
        else
        {
            submitButton.remove();
        }

    }
});
