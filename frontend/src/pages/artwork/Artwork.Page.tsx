import React from "react";
import { useParams } from "react-router-dom";

const ArtworkPage: React.FC = () => {
  // read the `id` param from the route, e.g. /artwork/123
  const { id } = useParams<{ id: string }>();

  return (
    <div>
      <header>
        <h1>Artwork</h1>
        <p>Artwork ID: {id}</p>
      </header>
    </div>
  );
};

export default ArtworkPage;
