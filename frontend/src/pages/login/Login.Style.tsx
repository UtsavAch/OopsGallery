import styled from "styled-components";

export const LoginContainer = styled.div`
  position: relative;
  display: flex;
  flex-direction: column;
  min-height: 70vh;
  width: 100%; /* Default to full width for mobile */
  margin: 0 auto;
  background-color: var(--color-background);
  overflow: hidden;
  text-align: left;

  /* Desktop/Tablet dynamic sizing */
  @media (min-width: 550px) {
    width: 500px; /* Fixed width for larger screens to look like a mobile app */
    margin: 0 auto var(--spacing-3xl) auto;
    border-radius: var(--border-radius-2xl);
    box-shadow: 0 25px 50px -12px rgba(0, 0, 0, 0.5);
    padding-bottom: var(--spacing-xl);
  }
`;

export const AmbientGlow = styled.div<{ position: "top" | "bottom" }>`
  position: absolute;
  width: 300px;
  height: 300px;
  background-color: rgba(54, 23, 207, 0.15);
  border-radius: var(--border-radius-full);
  filter: blur(100px);
  pointer-events: none;
  z-index: 0;
  ${(props) =>
    props.position === "top"
      ? "top: -10%; right: -10%;"
      : "bottom: -5%; left: -10%;"}
`;

export const HeaderSection = styled.div`
  padding: var(--spacing-2xl) var(--spacing-lg) var(--spacing-lg);
  z-index: 10;
`;

export const Title = styled.h1`
  font-size: var(--font-size-display-sm);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-main);
  margin-bottom: var(--spacing-xs);
`;

export const Subtitle = styled.p`
  font-size: var(--font-size-base);
  color: var(--color-text-secondary);
  font-weight: var(--font-weight-medium);
`;

export const FormSection = styled.form`
  display: flex;
  flex-direction: column;
  gap: var(--spacing-lg);
  padding: 0 var(--spacing-lg);
  z-index: 10;
`;

export const InputGroup = styled.div`
  display: flex;
  flex-direction: column;
  gap: var(--spacing-xs);
`;

export const Label = styled.span`
  font-size: var(--font-size-sm);
  font-weight: var(--font-weight-bold);
  color: var(--color-text-secondary);
`;

export const InputWrapper = styled.div`
  position: relative;
  display: flex;
  align-items: center;

  .material-symbols-outlined {
    position: absolute;
    left: var(--spacing-md);
    color: var(--color-text-secondary);
    font-size: 20px;
  }

  input {
    padding-left: var(--spacing-3xl);
    height: 3.5rem;
    background-color: var(--color-input);
    border-radius: var(--border-radius-xl);
    border: 1px solid transparent;
    transition: all 0.2s ease;

    &:focus {
      border-color: var(--color-primary);
      background-color: var(--color-surface-alt-2);
      outline: none;
    }
  }
`;

export const ErrorMessage = styled.p`
  color: var(--color-error);
  font-size: var(--font-size-sm);
  margin-top: var(--spacing-xs);
`;

export const ActionSection = styled.div`
  margin-top: var(--spacing-xl);
  padding: 0;
  z-index: 10;
`;

export const SubmitButton = styled.button`
  width: 100%;
  height: 3.5rem;
  background-color: var(--color-primary);
  color: var(--color-text-main);
  border-radius: var(--border-radius-2xl);
  font-size: var(--font-size-lg);
  font-weight: var(--font-weight-bold);
  display: flex;
  align-items: center;
  justify-content: center;
  gap: var(--spacing-sm);
  box-shadow: 0 10px 20px rgba(54, 23, 207, 0.25);
  transition: transform 0.2s ease;

  &:active {
    transform: scale(0.98);
  }

  &:hover {
    filter: brightness(1.1);
  }
`;

export const RedirectText = styled.p`
  text-align: center;
  margin-top: var(--spacing-lg);
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);

  a {
    color: var(--color-primary);
    font-weight: var(--font-weight-bold);
    margin-left: var(--spacing-xs);
    text-decoration: none;

    &:hover {
      text-decoration: underline;
    }
  }
`;
