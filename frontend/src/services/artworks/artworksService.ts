import api from "../api";
import type {
  ArtworkResponse,
  ArtworkRequest,
  ArtCategory,
} from "./artworks.types";

export const artworksService = {
  /**
   * Retrieves all artworks.
   *
   * @see {@link ArtworkResponse}
   */
  async findAll(): Promise<ArtworkResponse[]> {
    const response = await api.get<ArtworkResponse[]>("/artworks");
    return response.data;
  },

  /**
   * Retrieves artwork by its ID.
   *
   * @see {@link ArtworkResponse}
   */
  async findById(id: number): Promise<ArtworkResponse> {
    const response = await api.get<ArtworkResponse>(`/artworks/${id}`);
    return response.data;
  },

  /**
   * Retrieves artworks by category.
   *
   * @see {@link ArtCategory}
   * @see {@link ArtworkResponse}
   */
  async findByCategory(
    category: string | ArtCategory,
  ): Promise<ArtworkResponse[]> {
    const response = await api.get<ArtworkResponse[]>(
      `/artworks/category/${category}`,
    );
    return response.data;
  },

  /**
   * Creates a new artwork with optional image upload.
   * Takes(Multipart: JSON + Image)
   *
   * @see {@link ArtworkRequest}
   * @see {@link ArtworkResponse}
   */
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

  /**
   * Updates an existing artwork by ID. Image upload is optional.
   *
   * @see {@link ArtworkRequest}
   * @see {@link ArtworkResponse}
   */
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

  /**
   * Deletes artwork by its ID.
   */
  async deleteById(id: number): Promise<void> {
    await api.delete(`/artworks/${id}`);
  },

  /**
   * Retrieves all available art categories.
   */
  async getCategories(): Promise<string[]> {
    const response = await api.get<string[]>("/meta/art-categories");
    return response.data;
  },
};
