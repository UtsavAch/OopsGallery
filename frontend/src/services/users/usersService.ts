import api from "../api";
import type {
  UserResponse,
  UserRequest,
  VerifyRegistrationRequest,
  ResendVerificationRequest,
  UserRole,
} from "./users.types";

export const usersService = {
  /**
   * Creates a new user directly. Only accessible by OWNER.
   * This bypasses email verification.
   *
   * @see {@link UserRequest}
   * @see {@link UserResponse}
   */
  async save(data: UserRequest): Promise<UserResponse> {
    const response = await api.post<UserResponse>("/users", data);
    return response.data;
  },

  /**
   * Registers a new user. Sends a verification email.
   *
   * @see {@link UserRequest}
   */
  async register(data: UserRequest): Promise<string> {
    const response = await api.post<string>("/users/register", data);
    return response.data;
  },

  /**
   * Verifies a user account using the email and verification code.
   *
   * @see {@link VerifyRegistrationRequest}
   */
  async verifyUser(data: VerifyRegistrationRequest): Promise<string> {
    const response = await api.post<string>("/users/verify", data);
    return response.data;
  },

  /**
   * Resends a verification code to the user's email.
   */
  async resendVerification(email: string): Promise<string> {
    const payload: ResendVerificationRequest = { email };
    const response = await api.post<string>(
      "/users/resend-verification",
      payload,
    );
    return response.data;
  },

  /**
   * Updates a user's information. Accessible by OWNER or the user themselves.
   *
   * @see {@link VerifyRegistrationRequest}
   * @see {@link VerifyRegistrationRequest}
   */
  async update(id: number, data: UserRequest): Promise<UserResponse> {
    const response = await api.put<UserResponse>(`/users/${id}`, data);
    return response.data;
  },

  /**
   * Updates a user's role. Only accessible by OWNER.
   *
   * @see {@link UserRole}
   * @see {@link UserResponse}
   */
  async updateRole(id: number, role: UserRole): Promise<UserResponse> {
    const response = await api.patch<UserResponse>(`/users/${id}/role`, null, {
      params: { role }, // Passes role as a Query Parameter like @RequestParam in Java
    });
    return response.data;
  },

  /**
   * Retrieves all users. OWNER only.
   *
   * @see {@link UserResponse}
   */
  async findAll(): Promise<UserResponse[]> {
    const response = await api.get<UserResponse[]>("/users");
    return response.data;
  },

  /**
   * Retrieves a user by ID. Accessible by OWNER or the user themselves.
   *
   * @see {@link UserResponse}
   */
  async findById(id: number): Promise<UserResponse> {
    const response = await api.get<UserResponse>(`/users/${id}`);
    return response.data;
  },

  /**
   * Retrieves a user by email. Accessible by OWNER or the user themselves.
   *
   * @see {@link UserResponse}
   */
  async findByEmail(email: string): Promise<UserResponse> {
    const response = await api.get<UserResponse>(`/users/email/${email}`);
    return response.data;
  },

  /**
   * Deletes a user by ID. Accessible by OWNER or the user themselves.
   */
  async deleteById(id: number): Promise<void> {
    await api.delete(`/users/${id}`);
  },

  /**
   * Checks if an email is already registered in the system.
   */
  async existsByEmail(email: string): Promise<boolean> {
    const response = await api.get<boolean>(`/users/exists/${email}`);
    return response.data;
  },
};
