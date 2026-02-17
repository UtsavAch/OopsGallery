import React, { useEffect, useState } from "react";
import { artworksService } from "../../../../services/artworks/artworksService";
import type {
  ArtworkResponse,
  ArtworkRequest,
  ArtCategory,
} from "../../../../services/artworks/artworks.types";
import { useAuth } from "../../../../contexts/useAuth";
import ArtCard from "../../../../components/art-card/ArtCard.Component";
import ArtFormOverlay from "../../../../components/artform-overlay/ArtFormOverlay.Component";

const ArtworksTab: React.FC = () => {
  const { user } = useAuth();
  const isOwner = user?.role === "ROLE_OWNER";

  const [artworks, setArtworks] = useState<ArtworkResponse[]>([]);
  const [categories, setCategories] = useState<ArtCategory[]>([]);
  const [showForm, setShowForm] = useState(false);
  const [editingArtwork, setEditingArtwork] = useState<ArtworkResponse | null>(
    null,
  );

  const loadArtworks = async () => {
    const data = await artworksService.findAll();
    setArtworks(data);
  };

  useEffect(() => {
    let mounted = true;

    const init = async () => {
      try {
        const artworksData = await artworksService.findAll();
        const categoriesData = await artworksService.getCategories();

        if (mounted) {
          setArtworks(artworksData);
          setCategories(categoriesData as ArtCategory[]);
        }
      } catch (err) {
        console.error(err);
      }
    };

    init();

    return () => {
      mounted = false;
    };
  }, []);

  const handleDelete = async (id: number) => {
    await artworksService.deleteById(id);
    await loadArtworks();
  };

  const handleSubmit = async (
    data: ArtworkRequest,
    image: File | null,
    editingId?: number,
  ) => {
    if (editingId) {
      await artworksService.update(editingId, data, image || undefined);
    } else {
      if (!image) {
        alert("Image required");
        return;
      }
      await artworksService.save(data, image);
    }

    await loadArtworks();
  };

  const handleEdit = (artwork: ArtworkResponse) => {
    setEditingArtwork(artwork);
    setShowForm(true);
  };

  const handleCreate = () => {
    setEditingArtwork(null);
    setShowForm(true);
  };

  return (
    <div>
      {isOwner && <button onClick={handleCreate}>Create Artwork</button>}

      <ArtFormOverlay
        isOpen={showForm}
        categories={categories}
        initialData={editingArtwork}
        onClose={() => setShowForm(false)}
        onSubmit={handleSubmit}
      />

      <h2>Artworks List</h2>

      <div>
        {artworks.map((art) => (
          <ArtCard
            key={art.id}
            artwork={art}
            isOwner={isOwner}
            onEdit={handleEdit}
            onDelete={handleDelete}
            isLoggedIn={!!user}
          />
        ))}
      </div>
    </div>
  );
};

export default ArtworksTab;
