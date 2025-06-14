// app.js
const SUPABASE_URL = 'https://xfyltptitzikpzwnzblo.supabase.co';
const SUPABASE_KEY = 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InhmeWx0cHRpdHppa3B6d256YmxvIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NDc2NzY4NjAsImV4cCI6MjA2MzI1Mjg2MH0.gm44EA5b4wF_QpC4fk8r1Ja65Ve7Vqbu2LqFgxZ87cQ';
const supabase = supabase.createClient(SUPABASE_URL, SUPABASE_KEY);

async function getUser() {
  const { data, error } = await supabase.auth.getUser();
  if (error || !data.user) {
    window.location.href = 'index.html';
    return null;
  }
  return data.user;
}

let isLogin = false;

function toggleMode() {
  isLogin = !isLogin;
  document.getElementById("form-title").textContent = isLogin ? "Log In" : "Sign Up";
  document.querySelector("p.toggle-text").textContent = isLogin ? "New user? Sign up" : "Already have an account? Log in";
}

async function handleAuth() {
  const email = document.getElementById("email").value;
  const password = document.getElementById("password").value;

  const authFn = isLogin
    ? supabase.auth.signInWithPassword
    : supabase.auth.signUp;

  const { data, error } = await authFn({ email, password });

  if (error) return alert(error.message);

  const user = data.user;
  // Call backend API with user.id and user.email here
  // Then redirect to interests page
  window.location.href = "interests.html";
}



