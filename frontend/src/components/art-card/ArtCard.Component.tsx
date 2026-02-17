import React, { useState } from "react";
import type { ArtworkResponse } from "../../services/artworks/artworks.types";
import ArtOverlay from "../art-overlay/ArtOverlay.Component";

interface ArtCardProps {
  artwork: ArtworkResponse;
  isOwner?: boolean;
  onEdit?: (artwork: ArtworkResponse) => void;
  onDelete?: (id: number) => void;
  isLoggedIn?: boolean;
}

const ArtCard: React.FC<ArtCardProps> = ({
  artwork,
  isOwner = false,
  onEdit,
  onDelete,
  isLoggedIn = false,
}) => {
  const [showOverlay, setShowOverlay] = useState(false);

  return (
    <div>
      <div onClick={() => setShowOverlay(true)}>
        <img src={artwork.imgUrl} alt={artwork.title} width={150} />
        <h3>{artwork.title}</h3>
        <p>{artwork.category}</p>
        <p>${artwork.price}</p>
      </div>

      {showOverlay && (
        <ArtOverlay artwork={artwork} onClose={() => setShowOverlay(false)} />
      )}

      {isOwner && (
        <div>
          <button onClick={() => onEdit?.(artwork)}>Update</button>
          <button onClick={() => onDelete?.(artwork.id)}>Delete</button>
        </div>
      )}

      {!isOwner && isLoggedIn && <button>Add to Cart</button>}
    </div>
  );
};

export default ArtCard;
