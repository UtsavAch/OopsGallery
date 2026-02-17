import { Routes, Route } from "react-router-dom";
import FeedPage from "./pages/feed/Feed.Page";
import RegisterPage from "./pages/register/Register.Page";
import LoginPage from "./pages/login/Login.Page";
import ArtworkPage from "./pages/artwork/Artwork.Page";
import CartPage from "./pages/cart/Cart.Page";
import OrdersPage from "./pages/checkout/Orders.Page";
import DashboardPage from "./pages/dashboard/Dashboard.Page";
import ProfilePage from "./pages/profile/Profile.Page";

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<FeedPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/login" element={<LoginPage />} />
      {/* Artwork page for a specific artwork by id */}
      <Route path="/artwork/:id" element={<ArtworkPage />} />
      <Route path="/cart" element={<CartPage />} />
      <Route path="/orders" element={<OrdersPage />} />
      <Route path="/profile" element={<ProfilePage />} />
      <Route path="/dashboard" element={<DashboardPage />} />
    </Routes>
  );
};

export default AppRoutes;
