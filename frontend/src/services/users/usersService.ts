import api from "../api";
import type {
  UserResponse,
  UserRequest,
  VerifyRegistrationRequest,
  ResendVerificationRequest,
  UserRole,
} from "./users.types";

export const usersService = {
  // 1. Create a user directly (OWNER ONLY)
  async save(data: UserRequest): Promise<UserResponse> {
    const response = await api.post<UserResponse>("/users", data);
    return response.data;
  },

  // 2. Public Registration
  async register(data: UserRequest): Promise<string> {
    const response = await api.post<string>("/users/register", data);
    return response.data;
  },

  // 3. Verify Account
  async verifyUser(data: VerifyRegistrationRequest): Promise<string> {
    const response = await api.post<string>("/users/verify", data);
    return response.data;
  },

  // 4. Resend Verification Code
  async resendVerification(email: string): Promise<string> {
    const payload: ResendVerificationRequest = { email };
    const response = await api.post<string>(
      "/users/resend-verification",
      payload,
    );
    return response.data;
  },

  // 5. Update User (OWNER or Self)
  async update(id: number, data: UserRequest): Promise<UserResponse> {
    const response = await api.put<UserResponse>(`/users/${id}`, data);
    return response.data;
  },

  // 6. Update Role (OWNER ONLY)
  async updateRole(id: number, role: UserRole): Promise<UserResponse> {
    const response = await api.patch<UserResponse>(`/users/${id}/role`, null, {
      params: { role }, // Passes role as a Query Parameter like @RequestParam in Java
    });
    return response.data;
  },

  // 7. Get All Users (OWNER ONLY)
  async findAll(): Promise<UserResponse[]> {
    const response = await api.get<UserResponse[]>("/users");
    return response.data;
  },

  // 8. Find by ID (OWNER or Self)
  async findById(id: number): Promise<UserResponse> {
    const response = await api.get<UserResponse>(`/users/${id}`);
    return response.data;
  },

  // 9. Find by Email (OWNER or Self)
  async findByEmail(email: string): Promise<UserResponse> {
    const response = await api.get<UserResponse>(`/users/email/${email}`);
    return response.data;
  },

  // 10. Delete User (OWNER or Self)
  async deleteById(id: number): Promise<void> {
    await api.delete(`/users/${id}`);
  },

  // 11. Check if Email Exists
  async existsByEmail(email: string): Promise<boolean> {
    const response = await api.get<boolean>(`/users/exists/${email}`);
    return response.data;
  },
};
