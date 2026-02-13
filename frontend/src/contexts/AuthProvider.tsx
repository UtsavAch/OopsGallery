import { useState, useEffect, type ReactNode } from "react";
import { authService } from "../services/auth/authService";
import type { LoginRequest, LoginResponse } from "../services/auth/auth.types";
import { AuthContext } from "./auth.context";

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<LoginResponse | null>(null);

  // Start loading as TRUE for the initial localStorage check
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Initial check
    const initAuth = () => {
      try {
        const savedUser = authService.getCurrentUser();
        if (savedUser) {
          setUser(savedUser);
        }
      } catch (error) {
        console.error("Failed to parse stored user", error);
        authService.logout();
      } finally {
        //Initial check is done, stop loading
        setLoading(false);
      }
    };

    initAuth();
  }, []);

  const login = async (credentials: LoginRequest) => {
    setLoading(true);
    try {
      const data = await authService.login(credentials);
      setUser(data);
    } catch (error) {
      setUser(null);
      throw error;
    } finally {
      setLoading(false);
    }
  };

  const logout = () => {
    authService.logout();
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{ user, isAuthenticated: !!user, loading, login, logout }}
    >
      {children}
    </AuthContext.Provider>
  );
};
