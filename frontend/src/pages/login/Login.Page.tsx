import React, { useState } from "react";
import { useAuth } from "../../contexts/useAuth";
import { useNavigate, Link } from "react-router-dom";
import * as S from "./Login.Style";

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
    <S.LoginContainer>
      <S.AmbientGlow position="top" />
      <S.AmbientGlow position="bottom" />

      <S.HeaderSection>
        <S.Title>Welcome Back</S.Title>
        <S.Subtitle>Sign in to continue your collection.</S.Subtitle>
      </S.HeaderSection>

      <S.FormSection onSubmit={handleSubmit}>
        <S.InputGroup>
          <S.Label>Email Address</S.Label>
          <S.InputWrapper>
            <span className="material-symbols-outlined">mail</span>
            <input
              type="email"
              placeholder="jane@example.com"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              required
            />
          </S.InputWrapper>
        </S.InputGroup>

        <S.InputGroup>
          <S.Label>Password</S.Label>
          <S.InputWrapper>
            <span className="material-symbols-outlined">lock</span>
            <input
              type="password"
              placeholder="••••••••"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
          </S.InputWrapper>
          {error && <S.ErrorMessage>{error}</S.ErrorMessage>}
        </S.InputGroup>

        <S.ActionSection>
          <S.SubmitButton type="submit">
            Sign In
            <span className="material-symbols-outlined">arrow_forward</span>
          </S.SubmitButton>
          <S.RedirectText>
            Don't have an account?
            <Link to="/register">Register</Link>
          </S.RedirectText>
        </S.ActionSection>
      </S.FormSection>
    </S.LoginContainer>
  );
};

export default LoginPage;
