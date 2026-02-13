import api from "../api";
import type {
  ArtworkResponse,
  ArtworkRequest,
  ArtCategory,
} from "./artworks.types";

export const artworksService = {
  // 1. GET ALL ARTWORKS
  async findAll(): Promise<ArtworkResponse[]> {
    const response = await api.get<ArtworkResponse[]>("/artworks");
    return response.data;
  },

  // 2. GET BY ID
  async findById(id: number): Promise<ArtworkResponse> {
    const response = await api.get<ArtworkResponse>(`/artworks/${id}`);
    return response.data;
  },

  // 3. GET BY CATEGORY
  async findByCategory(
    category: string | ArtCategory,
  ): Promise<ArtworkResponse[]> {
    const response = await api.get<ArtworkResponse[]>(
      `/artworks/category/${category}`,
    );
    return response.data;
  },

  // 4. CREATE ARTWORK (Multipart: JSON + Image)
  async save(data: ArtworkRequest, image: File): Promise<ArtworkResponse> {
    const formData = new FormData();

    // Wrap the JSON in a Blob to specify the Content-Type
    const jsonBlob = new Blob([JSON.stringify(data)], {
      type: "application/json",
    });
    formData.append("data", jsonBlob);

    // The image file
    formData.append("image", image);

    const response = await api.post<ArtworkResponse>("/artworks", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    });
    return response.data;
  },

  // 5. UPDATE ARTWORK (Image is optional)
  async update(
    id: number,
    data: ArtworkRequest,
    image?: File,
  ): Promise<ArtworkResponse> {
    const formData = new FormData();

    const jsonBlob = new Blob([JSON.stringify(data)], {
      type: "application/json",
    });
    formData.append("data", jsonBlob);

    if (image) {
      formData.append("image", image);
    }

    const response = await api.put<ArtworkResponse>(
      `/artworks/${id}`,
      formData,
      {
        headers: { "Content-Type": "multipart/form-data" },
      },
    );
    return response.data;
  },

  // 6. DELETE ARTWORK
  async deleteById(id: number): Promise<void> {
    await api.delete(`/artworks/${id}`);
  },

  // 7. METADATA: Get Categories
  async getCategories(): Promise<string[]> {
    const response = await api.get<string[]>("/meta/art-categories");
    return response.data;
  },
};
