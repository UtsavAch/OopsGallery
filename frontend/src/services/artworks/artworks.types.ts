/**
 * ArtCategory
 *
 * Represents the category classification of an artwork.
 *
 * Possible values:
 * - "PAINTING"
 * - "DRAWING"
 * - "DIGITAL_ART"
 * - "PHOTOGRAPHY"
 * - "SCULPTURE"
 * - "PRINT"
 * - "ILLUSTRATION"
 * - "MIXED_MEDIA"
 * - "CRAFT"
 * - "OTHER"
 *
 * Used for filtering, organizing, and displaying artworks.
 */
export type ArtCategory =
  | "PAINTING"
  | "DRAWING"
  | "DIGITAL_ART"
  | "PHOTOGRAPHY"
  | "SCULPTURE"
  | "PRINT"
  | "ILLUSTRATION"
  | "MIXED_MEDIA"
  | "CRAFT"
  | "OTHER";

/**
 * ArtworkRequest
 *
 * Represents the payload sent from the frontend
 * when creating or updating an artwork.
 *
 * Input:
 * - title: Name of the artwork
 * - description: Detailed description of the artwork
 * - category: Classification of the artwork
 * - label: Optional tag or custom label (e.g., "Featured", "New")
 * - price: Price of the artwork
 *
 * Notes:
 * - price is mapped from Java BigDecimal to TypeScript number.
 * - label is optional and can be omitted during creation.
 */
export interface ArtworkRequest {
  title: string;
  description: string;
  category: ArtCategory;
  label?: string;
  price: number;
}

/**
 * ArtworkResponse
 *
 * Represents the artwork object returned from the backend.
 * This reflects the persisted artwork entity.
 *
 * Contains:
 * - id: Unique identifier of the artwork
 * - title: Name of the artwork
 * - description: Detailed description
 * - category: Artwork category
 * - label: Tag or display label associated with the artwork
 * - price: Artwork price
 * - imgUrl: Public URL of the artwork image
 *
 * Used for:
 * - Displaying artwork listings
 * - Showing artwork details
 * - Managing artwork data in the frontend
 */
export interface ArtworkResponse {
  id: number;
  title: string;
  description: string;
  category: ArtCategory;
  label: string;
  price: number;
  imgUrl: string;
}
