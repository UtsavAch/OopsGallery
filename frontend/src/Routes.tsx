import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import FeedPage from "./pages/feed/Feed.Page";
import RegisterPage from "./pages/register/Register.Page";
import LoginPage from "./pages/login/Login.Page";
import ArtworkPage from "./pages/artwork/Artwork.Page";
import CartPage from "./pages/cart/Cart.Page";
import CheckoutPage from "./pages/checkout/Checkout.Page";
import DashboardPage from "./pages/dashboard/Dashboard.Page";

const AppRoutes = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<FeedPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/login" element={<LoginPage />} />
        {/* Artwork page for a specific artwork by id */}
        <Route path="/artwork/:id" element={<ArtworkPage />} />
        <Route path="/cart" element={<CartPage />} />
        <Route path="/checkout" element={<CheckoutPage />} />
        <Route path="/dashboard" element={<DashboardPage />} />
      </Routes>
    </Router>
  );
};

export default AppRoutes;
