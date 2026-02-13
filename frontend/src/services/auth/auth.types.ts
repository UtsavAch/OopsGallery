import { type UserRole } from "../users/users.types";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  token: string;
  userId: number;
  email: string;
  role: UserRole;
}

// Helper type for decoded token if needed it later (OPTIONAL)
export interface AuthState {
  user: LoginResponse | null;
  isAuthenticated: boolean;
}
