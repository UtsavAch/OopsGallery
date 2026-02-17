import React, { useState } from "react";
import { useAuth } from "../../contexts/useAuth";
import Tabs from "../../components/tab/Tab.Component";
import ArtworksTab from "./tabs/artworks-tab/Artworks.Tab";
import UsersTab from "./tabs/users-tab/Users.Tab";
import OrdersTab from "./tabs/orders-tabs/Orders.Tab";
import PaymentsTab from "./tabs/payments-tab/Payments.Tab";

const DashboardPage: React.FC = () => {
  const { user } = useAuth(); // get logged-in user
  const [selectedTab, setSelectedTab] = useState("Artworks");

  const tabs = ["Artworks", "Users", "Orders", "Payments"];

  const renderTab = () => {
    switch (selectedTab) {
      case "Artworks":
        return <ArtworksTab />;
      case "Users":
        return <UsersTab />;
      case "Orders":
        return <OrdersTab />;
      case "Payments":
        return <PaymentsTab />;
      default:
        return null;
    }
  };

  // Check if user is owner
  if (!user || user.role !== "ROLE_OWNER") {
    return <div>Only owner has access to dashboard...</div>;
  }

  return (
    <div>
      <h1>Dashboard</h1>
      <Tabs tabs={tabs} activeTab={selectedTab} onTabChange={setSelectedTab} />
      {renderTab()}
    </div>
  );
};

export default DashboardPage;
