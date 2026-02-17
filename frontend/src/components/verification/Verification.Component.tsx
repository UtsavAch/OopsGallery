import React, { useState } from "react";
import { usersService } from "../../services/users/usersService";

type VerificationProps = {
  email: string;
  onSuccess: () => void;
};

const Verification = ({ email, onSuccess }: VerificationProps) => {
  const [code, setCode] = useState("");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  const handleVerify = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setMessage("");

    try {
      const response = await usersService.verifyUser({
        email,
        code,
      });

      setMessage(response);
      onSuccess();
    } catch (err: unknown) {
      setError(err instanceof Error ? err.message : "Verification failed.");
    } finally {
      setLoading(false);
    }
  };

  const handleResend = async () => {
    setLoading(true);
    setError("");
    setMessage("");

    try {
      const response = await usersService.resendVerification(email);
      setMessage(response);
    } catch (err: unknown) {
      setError(
        err instanceof Error
          ? err.message
          : "Failed to resend verification code.",
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handleVerify}>
      <h2>Verify Your Email</h2>

      <p>A verification code was sent to {email}</p>
      <p>The code might go to the "spam" folder.</p>

      {error && <p>{error}</p>}
      {message && <p>{message}</p>}

      <input
        type="text"
        placeholder="Verification Code"
        value={code}
        onChange={(e) => setCode(e.target.value)}
        required
      />

      <div style={{ display: "flex", gap: "var(--spacing-xs)" }}>
        <button type="submit" disabled={loading}>
          {loading ? "Verifying..." : "Verify"}
        </button>

        <button type="button" onClick={handleResend}>
          Resend Code
        </button>
      </div>
    </form>
  );
};

export default Verification;
