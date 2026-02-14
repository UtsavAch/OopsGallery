import "./index.css";
import AppRoutes from "./Routes";
import { AuthProvider } from "./contexts/AuthProvider";
import Navbar from "./components/navbar/Navbar.Component";

function App() {
  return (
    <AuthProvider>
      <Navbar />
      <AppRoutes />
    </AuthProvider>
  );
}

export default App;
