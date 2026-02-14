import React, { useState } from "react";
import { usersService } from "../../services/users/usersService";
import type { UserRequest } from "../../services/users/users.types";

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

  const [verificationCode, setVerificationCode] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  // --------------------------
  // Handle input change
  // --------------------------
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // --------------------------
  // Step 1: Register
  // --------------------------
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
      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError("Registration failed.");
      }
    } finally {
      setLoading(false);
    }
  };

  // --------------------------
  // Step 2: Verify
  // --------------------------
  const handleVerify = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setMessage("");

    try {
      const response = await usersService.verifyUser({
        email: formData.email,
        code: verificationCode,
      });

      setMessage(response);
      setStep("success");
    } catch (err: unknown) {
      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError("Verification failed.");
      }
    } finally {
      setLoading(false);
    }
  };

  // --------------------------
  // Resend Code
  // --------------------------
  const handleResend = async () => {
    setLoading(true);
    setError("");
    setMessage("");

    try {
      const response = await usersService.resendVerification(formData.email);
      setMessage(response);
    } catch (err: unknown) {
      if (err instanceof Error) {
        setError(err.message);
      } else {
        setError("Failed to resend verification code.");
      }
    } finally {
      setLoading(false);
    }
  };

  // =====================================================
  // UI
  // =====================================================

  return (
    <div className="max-w-md mx-auto p-6">
      {step === "register" && (
        <form onSubmit={handleRegister}>
          <h2 className="text-2xl font-semibold mb-4">Register</h2>

          {error && <p className="text-red-500 mb-2">{error}</p>}
          {message && <p className="text-green-600 mb-2">{message}</p>}

          <input
            name="firstName"
            placeholder="First Name"
            value={formData.firstName}
            onChange={handleChange}
            className="w-full mb-2 p-2 border"
            required
          />

          <input
            name="lastName"
            placeholder="Last Name"
            value={formData.lastName}
            onChange={handleChange}
            className="w-full mb-2 p-2 border"
            required
          />

          <input
            name="email"
            type="email"
            placeholder="Email"
            value={formData.email}
            onChange={handleChange}
            className="w-full mb-2 p-2 border"
            required
          />

          <input
            name="phoneNo"
            placeholder="Phone Number"
            value={formData.phoneNo}
            onChange={handleChange}
            className="w-full mb-2 p-2 border"
            required
          />

          <input
            name="address"
            placeholder="Address"
            value={formData.address}
            onChange={handleChange}
            className="w-full mb-2 p-2 border"
            required
          />

          <input
            name="password"
            type="password"
            placeholder="Password"
            value={formData.password}
            onChange={handleChange}
            className="w-full mb-4 p-2 border"
            required
          />

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-green-600 text-white py-2"
          >
            {loading ? "Registering..." : "Register"}
          </button>
        </form>
      )}

      {step === "verify" && (
        <form onSubmit={handleVerify}>
          <h2 className="text-2xl font-semibold mb-4">Verify Your Email</h2>

          <p className="text-sm mb-3 text-gray-600">
            A verification code was sent to {formData.email}
          </p>

          {error && <p className="text-red-500 mb-2">{error}</p>}
          {message && <p className="text-green-600 mb-2">{message}</p>}

          <input
            type="text"
            placeholder="Verification Code"
            value={verificationCode}
            onChange={(e) => setVerificationCode(e.target.value)}
            className="w-full mb-4 p-2 border"
            required
          />

          <button
            type="submit"
            disabled={loading}
            className="w-full bg-blue-600 text-white py-2 mb-2"
          >
            {loading ? "Verifying..." : "Verify"}
          </button>

          <button
            type="button"
            onClick={handleResend}
            className="w-full text-sm text-gray-600 underline"
          >
            Resend Code
          </button>
        </form>
      )}

      {step === "success" && (
        <div className="text-center">
          <h2 className="text-2xl font-semibold mb-4">
            Registration Successful 🎉
          </h2>
          <p className="text-green-600 mb-4">
            Your account has been verified. You can now log in.
          </p>
          <a href="/login" className="text-blue-600 underline">
            Go to Login
          </a>
        </div>
      )}
    </div>
  );
};

export default RegisterPage;
