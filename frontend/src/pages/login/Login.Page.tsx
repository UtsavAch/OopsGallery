import React, { useState } from "react";
import { useAuth } from "../../contexts/useAuth";
import { useNavigate } from "react-router-dom";

export const LoginPage = () => {
  const { login } = useAuth();
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError("");

    try {
      await login({ email, password });
      navigate("/");
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Login failed");
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Login</h2>

      {error && <p>{error}</p>}

      <div>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
      </div>

      <div>
        <input
          type="password"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
      </div>

      <div>
        <button type="submit">Login</button>
      </div>
    </form>
  );
};

export default LoginPage;
