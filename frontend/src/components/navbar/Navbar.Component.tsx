import { useAuth } from "../../contexts/useAuth";

export const Navbar = () => {
  const { user, isAuthenticated, logout } = useAuth();

  return (
    <nav className="flex items-center justify-between p-4 bg-gray-800 text-white">
      {/* Logo */}
      <div className="text-xl font-bold">ArtStore</div>

      {/* Middle message when not logged in */}
      {!isAuthenticated && (
        <div className="hidden md:block text-center flex-1 text-gray-300">
          Login or register to purchase
        </div>
      )}

      {/* Menu items */}
      <div className="flex items-center gap-4">
        {isAuthenticated ? (
          <>
            <a href="/orders" className="hover:underline">
              Orders
            </a>
            <a href="/cart" className="hover:underline">
              Cart
            </a>
            <a href="/profile" className="hover:underline">
              Profile
            </a>
            <button onClick={logout} className="hover:underline">
              Logout
            </button>
          </>
        ) : (
          <>
            <a href="/login" className="hover:underline">
              Login
            </a>
            <a href="/register" className="hover:underline">
              Register
            </a>
          </>
        )}
      </div>
    </nav>
  );
};

export default Navbar;
