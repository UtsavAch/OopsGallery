import { type UserRole } from "../users/users.types";

/**
 * LoginRequest
 *
 * Represents the payload sent from the frontend
 * when a user attempts to authenticate.
 *
 * Input:
 * - email: Registered email address of the user
 * - password: User's account password
 *
 * Used for:
 * - Authenticating a user
 * - Requesting a JWT or access token from the backend
 */
export interface LoginRequest {
  email: string;
  password: string;
}

/**
 * LoginResponse
 *
 * Represents the response returned after
 * a successful authentication.
 *
 * Contains:
 * - token: JWT or access token used for authenticated requests
 * - userId: Unique identifier of the authenticated user
 * - email: Email of the authenticated user
 * - role: Role assigned to the user (used for authorization)
 *
 * This object is typically stored in:
 * - Application state (e.g., Redux, Context API)
 * - Local storage or secure storage (for token persistence)
 */
export interface LoginResponse {
  token: string;
  userId: number;
  email: string;
  role: UserRole;
}

/**
 * AuthState
 *
 * Represents the authentication state
 * maintained on the frontend.
 *
 * Contains:
 * - user: The authenticated user information (or null if not logged in)
 * - isAuthenticated: Boolean flag indicating authentication status
 *
 * Used for:
 * - Managing protected routes
 * - Controlling UI visibility based on login state
 * - Persisting authentication session across refreshes
 */
export interface AuthState {
  user: LoginResponse | null;
  isAuthenticated: boolean;
}
