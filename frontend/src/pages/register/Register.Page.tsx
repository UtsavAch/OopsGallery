import React, { useState } from "react";
import { Link } from "react-router-dom";
import { usersService } from "../../services/users/usersService";
import type { UserRequest } from "../../services/users/users.types";
import Verification from "../../components/verification/Verification.Component";
import * as S from "./Register.Style";

type Step = "register" | "verify" | "success";

const RegisterPage = () => {
  const [step, setStep] = useState<Step>("register");

  const [formData, setFormData] = useState<UserRequest>({
    firstName: "",
    lastName: "",
    email: "",
    phoneNo: "",
    password: "",
    address: "",
  });

  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleRegister = async (e: React.FormEvent) => {
    e.preventDefault();

    setLoading(true);
    setError("");
    setMessage("");

    try {
      const response = await usersService.register(formData);

      setMessage(response);
      setStep("verify");
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Registration failed.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <S.RegisterContainer>
      <S.AmbientGlow position="top" />
      <S.AmbientGlow position="bottom" />

      {step === "register" && (
        <>
          <S.HeaderSection>
            <S.Title>Create Account</S.Title>
            <S.Subtitle>
              Join the gallery and start your collection journey.
            </S.Subtitle>
          </S.HeaderSection>

          <S.FormSection onSubmit={handleRegister}>
            <S.InputGrid>
              <S.InputGroup>
                <S.Label>First Name</S.Label>
                <S.InputWrapper>
                  <span className="material-symbols-outlined">person</span>
                  <input
                    name="firstName"
                    placeholder="John"
                    value={formData.firstName}
                    onChange={handleChange}
                    required
                  />
                </S.InputWrapper>
              </S.InputGroup>

              <S.InputGroup>
                <S.Label>Last Name</S.Label>
                <S.InputWrapper>
                  <span className="material-symbols-outlined">person</span>
                  <input
                    name="lastName"
                    placeholder="Doe"
                    value={formData.lastName}
                    onChange={handleChange}
                    required
                  />
                </S.InputWrapper>
              </S.InputGroup>

              <S.FullWidthGroup>
                <S.Label>Email Address</S.Label>
                <S.InputWrapper>
                  <span className="material-symbols-outlined">mail</span>
                  <input
                    type="email"
                    name="email"
                    placeholder="john@example.com"
                    value={formData.email}
                    onChange={handleChange}
                    required
                  />
                </S.InputWrapper>
              </S.FullWidthGroup>

              <S.FullWidthGroup>
                <S.Label>Phone Number</S.Label>
                <S.InputWrapper>
                  <span className="material-symbols-outlined">call</span>
                  <input
                    name="phoneNo"
                    placeholder="+351..."
                    value={formData.phoneNo}
                    onChange={handleChange}
                    required
                  />
                </S.InputWrapper>
              </S.FullWidthGroup>

              <S.FullWidthGroup>
                <S.Label>Password</S.Label>
                <S.InputWrapper>
                  <span className="material-symbols-outlined">lock</span>
                  <input
                    type="password"
                    name="password"
                    placeholder="••••••••"
                    value={formData.password}
                    onChange={handleChange}
                    required
                  />
                </S.InputWrapper>
              </S.FullWidthGroup>

              <S.FullWidthGroup>
                <S.Label>Address</S.Label>
                <S.InputWrapper>
                  <span className="material-symbols-outlined">location_on</span>
                  <input
                    name="address"
                    placeholder="Street, City, Country"
                    value={formData.address}
                    onChange={handleChange}
                    required
                  />
                </S.InputWrapper>
              </S.FullWidthGroup>
            </S.InputGrid>

            {error && <S.ErrorMessage>{error}</S.ErrorMessage>}
            {message && <S.SuccessMessage>{message}</S.SuccessMessage>}

            <S.ActionSection>
              <S.SubmitButton type="submit" disabled={loading}>
                {loading ? "Creating Account..." : "Register"}
                <span className="material-symbols-outlined">arrow_forward</span>
              </S.SubmitButton>

              <S.RedirectText>
                Already have an account?
                <Link to="/login">Sign In</Link>
              </S.RedirectText>
            </S.ActionSection>
          </S.FormSection>
        </>
      )}

      {step === "verify" && (
        <Verification
          email={formData.email}
          onSuccess={() => setStep("success")}
        />
      )}

      {step === "success" && (
        <S.SuccessContainer>
          <h2>Registration Successful</h2>
          <p>Your account has been verified. You can now log in.</p>
          <Link to="/login">Go to Login</Link>
        </S.SuccessContainer>
      )}
    </S.RegisterContainer>
  );
};

export default RegisterPage;
