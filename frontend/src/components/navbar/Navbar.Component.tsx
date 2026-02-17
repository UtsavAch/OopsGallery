import { useAuth } from "../../contexts/useAuth";
import { useNavigate } from "react-router-dom";

export const Navbar = () => {
  const { user, isAuthenticated, logout } = useAuth();

  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/");
  };

  return (
    <nav style={{ display: "flex", justifyContent: "space-between" }}>
      <div>
        <a href="/">ArtStore</a>
      </div>

      {!isAuthenticated && <div>Login or register to purchase</div>}

      <div>
        {isAuthenticated ? (
          <div style={{ display: "flex", gap: "var(--spacing-xs)" }}>
            <a href="/">Shop</a>
            <a href="/orders">Orders</a>
            <a href="/cart">Cart</a>
            <a href="/profile">Profile</a>
            {user?.role === "ROLE_OWNER" && <a href="/dashboard">Dashboard</a>}
            <button onClick={handleLogout}>Logout</button>
          </div>
        ) : (
          <div style={{ display: "flex", gap: "var(--spacing-xs)" }}>
            <a href="/login">Login</a>
            <a href="/register">Register</a>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
