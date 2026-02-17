import React, { useEffect, useState } from "react";
import { artworksService } from "../../services/artworks/artworksService";
import ArtCard from "../../components/art-card/ArtCard.Component";
import Tabs from "../../components/tab/Tab.Component";
import { useAuth } from "../../contexts/useAuth";
import type { ArtworkResponse } from "../../services/artworks/artworks.types";

const FeedPage: React.FC = () => {
  const { user } = useAuth();

  const [artworks, setArtworks] = useState<ArtworkResponse[]>([]);
  const [categories, setCategories] = useState<string[]>([]);
  const [activeTab, setActiveTab] = useState<string>("ALL");

  // Load categories once
  useEffect(() => {
    const loadCategories = async () => {
      const data = await artworksService.getCategories();
      setCategories(["ALL", ...data]); // Add ALL manually
    };

    loadCategories();
  }, []);

  // Load artworks when tab changes
  useEffect(() => {
    const loadArtworks = async () => {
      if (activeTab === "ALL") {
        const data = await artworksService.findAll();
        setArtworks(data);
      } else {
        const data = await artworksService.findByCategory(activeTab);
        setArtworks(data);
      }
    };

    loadArtworks();
  }, [activeTab]);

  return (
    <div>
      <h1>Art Feed</h1>

      {/* Category Filter Tabs */}
      <Tabs
        tabs={categories}
        activeTab={activeTab}
        onTabChange={setActiveTab}
      />

      {/* Artworks */}
      <div>
        {artworks.map((art) => (
          <ArtCard key={art.id} artwork={art} isLoggedIn={!!user} />
        ))}
      </div>
    </div>
  );
};

export default FeedPage;
