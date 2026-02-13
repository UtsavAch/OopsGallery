export type UserRole = "ROLE_USER" | "ROLE_OWNER";

export interface UserResponse {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNo: string;
  address: string;
  role: UserRole;
}

export interface UserRequest {
  firstName: string;
  lastName: string;
  email: string;
  phoneNo: string;
  password?: string; // Optional for updates
  address: string;
}

export interface VerifyRegistrationRequest {
  email: string;
  code: string;
}

export interface ResendVerificationRequest {
  email: string;
}
