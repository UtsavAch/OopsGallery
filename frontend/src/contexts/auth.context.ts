import { createContext } from "react";
import type { LoginRequest, LoginResponse } from "../services/auth/auth.types";

export interface AuthContextType {
  user: LoginResponse | null;
  isAuthenticated: boolean;
  loading: boolean;
  login: (credentials: LoginRequest) => Promise<void>;
  logout: () => void;
}

export const AuthContext = createContext<AuthContextType | undefined>(
  undefined,
);
