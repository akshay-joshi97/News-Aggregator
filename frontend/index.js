document.addEventListener("DOMContentLoaded", () => {
  const loginForm = document.querySelector(".login-form form");
  const signupForm = document.querySelector(".signup-form form");

  // Validation functions
  const validateName = (name) => {
    const nameRegex = /^[a-zA-Z\s]{2,30}$/;
    const errors = [];

    if (!name.trim()) {
      errors.push("Name is required");
    } else if (name.length < 2) {
      errors.push("Name must be at least 2 characters long");
    } else if (name.length > 30) {
      errors.push("Name must not exceed 30 characters");
    } else if (!nameRegex.test(name)) {
      errors.push("Name can only contain letters and spaces");
    }

    return errors;
  };

  const validateEmail = (email) => {
    const emailRegex = /^[a-z0-9._%+\-]+@[a-z0-9.\-]+\.[a-z]{2,}$/i;
    const errors = [];

    if (!email.trim()) {
      errors.push("Email is required");
    } else if (!emailRegex.test(email)) {
      errors.push("Please enter a valid email address");
    }

    return errors;
  };

  const validatePassword = (password) => {
    const errors = [];

    if (!password) {
      errors.push("Password is required");
    } else {
      if (password.length < 8) {
        errors.push("Password must be at least 8 characters long");
      }
      if (!/(?=.*[a-z])/.test(password)) {
        errors.push("Password must contain at least one lowercase letter");
      }
      if (!/(?=.*[A-Z])/.test(password)) {
        errors.push("Password must contain at least one uppercase letter");
      }
      if (!/(?=.*\d)/.test(password)) {
        errors.push("Password must contain at least one number");
      }
    }

    return errors;
  };

  const validatePasswordMatch = (password, confirmPassword) => {
    const errors = [];

    if (!confirmPassword) {
      errors.push("Please confirm your password");
    } else if (password !== confirmPassword) {
      errors.push("Passwords do not match");
    }

    return errors;
  };

  // Display validation errors
  const displayErrors = (input, errors) => {
    // Remove existing error messages
    const existingError = input.parentElement.querySelector('.error-message');
    if (existingError) {
      existingError.remove();
    }

    if (errors.length > 0) {
      input.classList.add('error');
      const errorDiv = document.createElement('div');
      errorDiv.className = 'error-message';
      errorDiv.textContent = errors[0]; // Show only the first error
      errorDiv.style.color = '#ff3860';
      errorDiv.style.fontSize = '12px';
      errorDiv.style.marginTop = '5px';
      errorDiv.style.display = 'block';
      input.parentElement.appendChild(errorDiv);
    } else {
      input.classList.remove('error');
      input.classList.add('valid');
    }
  };

  // Real-time validation for signup form
  const setupRealTimeValidation = () => {
    const nameInput = document.getElementById('signup-name');
    const emailInput = document.getElementById('signup-email');
    const passwordInput = document.getElementById('signup-password');
    const confirmPasswordInput = document.getElementById('signup-confirm-password');
    const submitButton = document.getElementById('signup-submit');

    const validateAndUpdateSubmitButton = () => {
      const nameErrors = validateName(nameInput.value);
      const emailErrors = validateEmail(emailInput.value);
      const passwordErrors = validatePassword(passwordInput.value);
      const confirmPasswordErrors = validatePasswordMatch(passwordInput.value, confirmPasswordInput.value);

      const allValid = nameErrors.length === 0 &&
                      emailErrors.length === 0 &&
                      passwordErrors.length === 0 &&
                      confirmPasswordErrors.length === 0 &&
                      nameInput.value.trim() !== '' &&
                      emailInput.value.trim() !== '' &&
                      passwordInput.value !== '' &&
                      confirmPasswordInput.value !== '';

      submitButton.disabled = !allValid;
      submitButton.style.opacity = allValid ? '1' : '0.6';
      submitButton.style.cursor = allValid ? 'pointer' : 'not-allowed';
    };

    // Name validation
    nameInput.addEventListener('input', () => {
      const errors = validateName(nameInput.value);
      displayErrors(nameInput, errors);
      validateAndUpdateSubmitButton();
    });

    nameInput.addEventListener('blur', () => {
      const errors = validateName(nameInput.value);
      displayErrors(nameInput, errors);
    });

    // Email validation
    emailInput.addEventListener('input', () => {
      const errors = validateEmail(emailInput.value);
      displayErrors(emailInput, errors);
      validateAndUpdateSubmitButton();
    });

    emailInput.addEventListener('blur', () => {
      const errors = validateEmail(emailInput.value);
      displayErrors(emailInput, errors);
    });

    // Password validation
    passwordInput.addEventListener('input', () => {
      const errors = validatePassword(passwordInput.value);
      displayErrors(passwordInput, errors);

      // Also validate confirm password if it has a value
      if (confirmPasswordInput.value) {
        const confirmErrors = validatePasswordMatch(passwordInput.value, confirmPasswordInput.value);
        displayErrors(confirmPasswordInput, confirmErrors);
      }

      validateAndUpdateSubmitButton();
    });

    passwordInput.addEventListener('blur', () => {
      const errors = validatePassword(passwordInput.value);
      displayErrors(passwordInput, errors);
    });

    // Confirm password validation
    confirmPasswordInput.addEventListener('input', () => {
      const errors = validatePasswordMatch(passwordInput.value, confirmPasswordInput.value);
      displayErrors(confirmPasswordInput, errors);
      validateAndUpdateSubmitButton();
    });

    confirmPasswordInput.addEventListener('blur', () => {
      const errors = validatePasswordMatch(passwordInput.value, confirmPasswordInput.value);
      displayErrors(confirmPasswordInput, errors);
    });

    // Initial validation check
    validateAndUpdateSubmitButton();
  };

  // Setup real-time validation
  setupRealTimeValidation();

  // Login form validation
  const validateLoginForm = () => {
    const emailInput = loginForm.querySelector('input[placeholder="Enter your email"]');
    const passwordInput = loginForm.querySelector('input[placeholder="Enter your password"]');

    const emailErrors = validateEmail(emailInput.value);
    const passwordErrors = passwordInput.value.trim() === '' ? ['Password is required'] : [];

    displayErrors(emailInput, emailErrors);
    displayErrors(passwordInput, passwordErrors);

    return emailErrors.length === 0 && passwordErrors.length === 0;
  };

  // Login logic
  loginForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    if (!validateLoginForm()) {
      return;
    }

    const email = loginForm.querySelector('input[placeholder="Enter your email"]').value;
    const password = loginForm.querySelector('input[placeholder="Enter your password"]').value;

    console.log('Login attempt - Email:', email);

    // Show loading state
    const submitButton = loginForm.querySelector('input[type="submit"]');
    const originalValue = submitButton.value;
    submitButton.value = "Logging in...";
    submitButton.disabled = true;

    try {
      const response = await fetch("https://news-aggregator-0w52.onrender.com/login", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Accept": "application/json"
        },
        body: JSON.stringify({ email, password })
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();

      if (result.success || response.status == 200) {
        alert(result.message || "Login successful!");
        // Redirecting to feed
        window.location.href = "feed.html?userId="+result.userId;
      } else {
        alert(result.message || "Login failed. Please check your credentials.");
      }
    } catch (err) {
      console.error("Login error:", err);
      if (err.name === 'TypeError' && err.message.includes('fetch')) {
        alert("Cannot connect to server. Please make sure your Spark server is running on port 4567.");
      } else {
        alert("Login failed: " + (err.message || "Unknown error"));
      }
    } finally {
      // Reset button state
      submitButton.value = originalValue;
      submitButton.disabled = false;
    }
  });

  // Signup form validation
  const validateSignupForm = () => {
    const nameInput = document.getElementById('signup-name');
    const emailInput = document.getElementById('signup-email');
    const passwordInput = document.getElementById('signup-password');
    const confirmPasswordInput = document.getElementById('signup-confirm-password');

    const nameErrors = validateName(nameInput.value);
    const emailErrors = validateEmail(emailInput.value);
    const passwordErrors = validatePassword(passwordInput.value);
    const confirmPasswordErrors = validatePasswordMatch(passwordInput.value, confirmPasswordInput.value);

    displayErrors(nameInput, nameErrors);
    displayErrors(emailInput, emailErrors);
    displayErrors(passwordInput, passwordErrors);
    displayErrors(confirmPasswordInput, confirmPasswordErrors);

    return nameErrors.length === 0 &&
           emailErrors.length === 0 &&
           passwordErrors.length === 0 &&
           confirmPasswordErrors.length === 0;
  };

  // Signup logic
  signupForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    if (!validateSignupForm()) {
      return;
    }

    const name = signupForm.querySelector('input[placeholder="Enter your name"]').value;
    const email = signupForm.querySelector('input[placeholder="Enter your email"]').value;
    const password = signupForm.querySelector('input[placeholder="Enter your password"]').value;

    console.log('Registration attempt - Name:', name, 'Email:', email);

    // Show loading state
    const submitButton = document.getElementById('signup-submit');
    const originalValue = submitButton.value;
    submitButton.value = "Registering...";
    submitButton.disabled = true;

    try {
      const response = await fetch("https://news-aggregator-0w52.onrender.com/register", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "Accept": "application/json"
        },
        body: JSON.stringify({ name, email, password })
      });

      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }

      const result = await response.json();
      console.log('Registration response:', result);

      if (result.success || response.status === 200 || response.status === 201) {
        alert(result.message || "Registration successful!");
        // Clear form on successful registration
        signupForm.reset();
        // Remove validation classes
        const inputs = signupForm.querySelectorAll('input');
        inputs.forEach(input => {
          input.classList.remove('valid', 'error');
        });
        // Remove error messages
        const errorMessages = signupForm.querySelectorAll('.error-message');
        errorMessages.forEach(msg => msg.remove());
        //Redirect to interests page
        console.log('result>>>', result); // This is your parsed JSON body
        console.log('userId>>>', result.userId); // Correctly accesses the userId
        window.location.href = "interests.html?userId=" + result.userId;
      } else {
        alert(result.message || "Registration failed. Please try again.");
      }
    } catch (err) {
      console.error("Registration error:", err);
      if (err.name === 'TypeError' && err.message.includes('fetch')) {
        alert("Cannot connect to server. Please make sure your Spark server is running on port 4567.");
      } else {
        alert("Registration failed: " + (err.message || "Unknown error"));
      }
    } finally {
      // Reset button state
      submitButton.value = originalValue;
      submitButton.disabled = false;
    }
  });
});