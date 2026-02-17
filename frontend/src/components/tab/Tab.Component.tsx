import React from "react";

interface TabProps {
  tabs: string[];
  activeTab: string;
  onTabChange: (tab: string) => void;
}

const Tabs: React.FC<TabProps> = ({ tabs, activeTab, onTabChange }) => {
  return (
    <div>
      {tabs.map((tab) => (
        <button key={tab} onClick={() => onTabChange(tab)} type="button">
          {tab}
        </button>
      ))}
    </div>
  );
};

export default Tabs;
