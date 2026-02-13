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

export interface ArtworkRequest {
  title: string;
  description: string;
  category: ArtCategory;
  label?: string;
  price: number;
}

export interface ArtworkResponse {
  id: number;
  title: string;
  description: string;
  category: ArtCategory;
  label: string;
  price: number;
  imgUrl: string;
}
