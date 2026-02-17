import React, { useState } from "react";
import { usersService } from "../../services/users/usersService";
import type { UserRequest } from "../../services/users/users.types";
import Verification from "../../components/verification/Verification.Component";

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
    <div>
      {step === "register" && (
        <form onSubmit={handleRegister}>
          <h2>Register</h2>

          {error && <p>{error}</p>}
          {message && <p>{message}</p>}

          <input
            name="firstName"
            placeholder="First Name"
            value={formData.firstName}
            onChange={handleChange}
            required
          />

          <input
            name="lastName"
            placeholder="Last Name"
            value={formData.lastName}
            onChange={handleChange}
            required
          />

          <input
            name="email"
            type="email"
            placeholder="Email"
            value={formData.email}
            onChange={handleChange}
            required
          />

          <input
            name="phoneNo"
            placeholder="Phone Number"
            value={formData.phoneNo}
            onChange={handleChange}
            required
          />

          <input
            name="address"
            placeholder="Address"
            value={formData.address}
            onChange={handleChange}
            required
          />

          <input
            name="password"
            type="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            required
          />

          <button type="submit" disabled={loading}>
            {loading ? "Registering..." : "Register"}
          </button>
        </form>
      )}

      {step === "verify" && (
        <Verification
          email={formData.email}
          onSuccess={() => setStep("success")}
        />
      )}

      {step === "success" && (
        <div>
          <h2>Registration Successful</h2>
          <p>Your account has been verified. You can now log in.</p>
          <a href="/login">Go to Login</a>
        </div>
      )}
    </div>
  );
};

export default RegisterPage;
