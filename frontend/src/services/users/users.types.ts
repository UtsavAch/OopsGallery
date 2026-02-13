/**
 * UserRole
 *
 * Represents the role assigned to a user in the system.
 *
 * Possible values:
 * - "ROLE_USER"  → Standard customer account
 * - "ROLE_OWNER" → Owner account (e.g., gallery owner, admin-level privileges)
 *
 * Used to control authorization and access to protected resources.
 */
export type UserRole = "ROLE_USER" | "ROLE_OWNER";

/**
 * UserResponse
 *
 * Represents the user object returned from the backend.
 * This reflects the persisted user entity stored in the system.
 *
 * Contains:
 * - id: Unique identifier of the user
 * - firstName: User's first name
 * - lastName: User's last name
 * - email: User's registered email address
 * - phoneNo: User's contact phone number
 * - address: User's saved address
 * - role: Assigned role determining access level
 *
 * This interface is typically used when:
 * - Fetching user profile information
 * - Returning user details after login
 * - Displaying account information
 */
export interface UserResponse {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  phoneNo: string;
  address: string;
  role: UserRole;
}

/**
 * UserRequest
 *
 * Represents the payload sent from the frontend
 * when creating or updating a user account.
 *
 * Input:
 * - firstName: User's first name
 * - lastName: User's last name
 * - email: User's email address
 * - phoneNo: User's contact number
 * - password: User's password (optional for updates)
 * - address: User's address
 *
 * Notes:
 * - password is optional to allow profile updates
 *   without forcing a password change.
 * - Used for registration and profile update operations.
 */
export interface UserRequest {
  firstName: string;
  lastName: string;
  email: string;
  phoneNo: string;
  password?: string; // Optional for updates
  address: string;
}

/**
 * VerifyRegistrationRequest
 *
 * Represents the payload sent when verifying
 * a newly registered user account.
 *
 * Input:
 * - email: Email address used during registration
 * - code: Verification code sent to the user's email
 *
 * Used to confirm and activate a user account
 * after registration.
 */
export interface VerifyRegistrationRequest {
  email: string;
  code: string;
}

/**
 * ResendVerificationRequest
 *
 * Represents the payload sent when requesting
 * a new verification code.
 *
 * Input:
 * - email: Email address of the user requesting
 *   a new verification code
 *
 * Used when:
 * - The original verification code expired
 * - The user did not receive the initial code
 */
export interface ResendVerificationRequest {
  email: string;
}
