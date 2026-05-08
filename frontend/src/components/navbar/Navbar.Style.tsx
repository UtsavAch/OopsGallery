import styled from "styled-components";

export const NavbarContainer = styled.nav`
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
  padding: var(--spacing-md) var(--spacing-lg);
  background-color: rgba(20, 17, 33, 0.8);
  backdrop-filter: blur(12px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  position: sticky;
  top: 0;
  z-index: 100;
`;

export const LogoSection = styled.div`
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);
  cursor: pointer;

  span {
    color: var(--color-primary);
    font-size: 24px;
  }
`;

export const NavbarLogo = styled.h1`
  font-size: var(--font-size-h1);
  font-weight: var(--font-weight-black);
  letter-spacing: -0.025em;
  color: var(--color-text-main);
  margin: 0;
`;

export const NavMenuDesktop = styled.div`
  display: none;
  gap: var(--spacing-lg);
  margin-left: var(--spacing-xl);
  margin-right: auto;

  a {
    color: var(--color-text-secondary);
    font-size: var(--font-size-sm);
    font-weight: var(--font-weight-semibold);
    cursor: pointer;
    &:hover {
      color: var(--color-primary);
    }
  }

  @media (min-width: 768px) {
    display: flex;
  }
`;

export const NavbarActions = styled.div`
  display: flex;
  align-items: center;
  gap: var(--spacing-md);
`;

export const AuthGroup = styled.div`
  display: flex;
  align-items: center;
  gap: var(--spacing-xs);

  .divider {
    color: var(--color-text-secondary);
    opacity: 0.3;
    font-size: var(--font-size-xs);
  }

  .profile-link {
    color: var(--color-text-secondary);
    font-size: var(--font-size-sm);
    margin-right: var(--spacing-sm);
    &:hover {
      color: var(--color-primary);
    }
  }
`;

export const NavbarActionButton = styled.button`
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-secondary);
  transition: color 0.2s ease;
  white-space: nowrap;

  &:hover {
    color: var(--color-primary);
  }
`;

export const MobileMenuToggle = styled.button`
  color: var(--color-text-main);
  display: flex;
  align-items: center;

  @media (min-width: 768px) {
    display: none;
  }
`;

export const MobileMenu = styled.div<{ isOpen: boolean }>`
  display: ${(props) => (props.isOpen ? "flex" : "none")};
  position: absolute;
  top: 100%;
  left: 0;
  right: 0;
  flex-direction: column;
  padding: var(--spacing-lg);
  background-color: var(--color-surface);
  border-bottom: 1px solid var(--color-border-highlight);
  gap: var(--spacing-md);
  align-items: center;

  a,
  button {
    font-size: var(--font-size-lg);
    color: var(--color-text-main);
    text-align: left;
    cursor: pointer;
  }
`;
