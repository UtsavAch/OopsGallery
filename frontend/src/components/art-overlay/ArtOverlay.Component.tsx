import React from "react";
import type { ArtworkResponse } from "../../services/artworks/artworks.types";

interface ArtOverlayProps {
  artwork: ArtworkResponse;
  onClose: () => void;
}

const ArtOverlay: React.FC<ArtOverlayProps> = ({ artwork, onClose }) => {
  return (
    <div>
      <button onClick={onClose}>×</button>

      <h4>{artwork.title}</h4>
      <p>{artwork.description}</p>
      <p>{artwork.label}</p>
      <p>{artwork.category}</p>
      <p>${artwork.price}</p>
    </div>
  );
};

export default ArtOverlay;
