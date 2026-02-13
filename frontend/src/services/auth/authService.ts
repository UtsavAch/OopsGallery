import api from "../api";
import type { LoginRequest, LoginResponse } from "./auth.types";

export const authService = {
  /**
   * Sends credentials to the backend.
   * On success, returns the JWT and user basic info.
   *
   * @see {@link LoginRequest}
   * @see {@link LoginResponse}
   */
  async login(credentials: LoginRequest): Promise<LoginResponse> {
    const response = await api.post<LoginResponse>("/auth/login", credentials);

    // Saves the token right after a successful call
    if (response.data.token) {
      localStorage.setItem("token", response.data.token);
      localStorage.setItem("user", JSON.stringify(response.data));
    }

    return response.data;
  },

  /**
   * Clears the session data from the browser.
   */
  logout(): void {
    localStorage.removeItem("token");
    localStorage.removeItem("user");
  },

  /**
   * Helper to check if a user is currently stored in local storage
   *
   * @see {@link LoginResponse}
   */
  getCurrentUser(): LoginResponse | null {
    const userStr = localStorage.getItem("user");
    if (userStr) return JSON.parse(userStr);
    return null;
  },
};
