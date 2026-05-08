import { useState, useEffect } from "react";
import { useAuth } from "../../contexts/useAuth";
import { useNavigate } from "react-router-dom";
import {
  NavbarContainer,
  LogoSection,
  NavbarLogo,
  NavbarActions,
  NavbarActionButton,
  MobileMenuToggle,
  MobileMenu,
  NavMenuDesktop,
  AuthGroup,
} from "./Navbar.Style";

export const Navbar = () => {
  const { user, isAuthenticated, logout } = useAuth();
  const navigate = useNavigate();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);

  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth >= 768) {
        setMobileMenuOpen(false);
      }
    };
    window.addEventListener("resize", handleResize);
    return () => {
      window.removeEventListener("resize", handleResize);
    };
  }, []);

  const handleLogout = () => {
    logout();
    navigate("/");
    setMobileMenuOpen(false);
  };

  const handleNavigation = (path: string) => {
    navigate(path);
    setMobileMenuOpen(false);
  };

  return (
    <NavbarContainer>
      <LogoSection onClick={() => handleNavigation("/")}>
        <NavbarLogo>Oops</NavbarLogo>
      </LogoSection>

      {/* Desktop Navigation Links */}
      <NavMenuDesktop>
        {isAuthenticated && (
          <>
            <a onClick={() => handleNavigation("/orders")}>Orders</a>
            <a onClick={() => handleNavigation("/cart")}>Cart</a>
            {user?.role === "ROLE_OWNER" && (
              <a onClick={() => handleNavigation("/dashboard")}>Dashboard</a>
            )}
          </>
        )}
      </NavMenuDesktop>

      <NavbarActions>
        {isAuthenticated ? (
          <AuthGroup>
            <a
              onClick={() => handleNavigation("/profile")}
              className="profile-link"
            >
              Profile
            </a>
            <NavbarActionButton onClick={handleLogout}>
              Logout
            </NavbarActionButton>
          </AuthGroup>
        ) : (
          <AuthGroup>
            <NavbarActionButton onClick={() => handleNavigation("/login")}>
              Sign In
            </NavbarActionButton>
            <span className="divider">/</span>
            <NavbarActionButton onClick={() => handleNavigation("/register")}>
              Register
            </NavbarActionButton>
          </AuthGroup>
        )}

        {isAuthenticated && (
          <MobileMenuToggle onClick={() => setMobileMenuOpen(!mobileMenuOpen)}>
            <span className="material-symbols-outlined">menu</span>
          </MobileMenuToggle>
        )}
      </NavbarActions>

      {/* Mobile Dropdown */}
      <MobileMenu isOpen={mobileMenuOpen}>
        {isAuthenticated && (
          <>
            <a onClick={() => handleNavigation("/orders")}>Orders</a>
            <a onClick={() => handleNavigation("/cart")}>Cart</a>
            <a onClick={() => handleNavigation("/profile")}>Profile</a>
            <a onClick={handleLogout}>Logout</a>
          </>
        )}
      </MobileMenu>
    </NavbarContainer>
  );
};

export default Navbar;
