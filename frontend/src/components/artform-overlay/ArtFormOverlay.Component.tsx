import React, { useState, useMemo } from "react";
import type {
  ArtworkRequest,
  ArtworkResponse,
  ArtCategory,
} from "../../services/artworks/artworks.types";
import Choose from "../choose/Choose.Component";

interface ArtFormOverlayProps {
  isOpen: boolean;
  categories: ArtCategory[];
  initialData?: ArtworkResponse | null; // for updates
  onClose: () => void;
  onSubmit: (
    data: ArtworkRequest,
    image: File | null,
    editingId?: number,
  ) => Promise<void>;
}

/**
 * Returns the initial form data for create or update
 */
const getInitialFormData = (
  initialData?: ArtworkResponse | null,
): ArtworkRequest => {
  if (initialData) {
    return {
      title: initialData.title,
      description: initialData.description,
      category: initialData.category,
      price: initialData.price,
      label: initialData.label,
    };
  }

  return {
    title: "",
    description: "",
    category: "OTHER",
    price: 0,
    label: "",
  };
};

const ArtFormOverlay: React.FC<ArtFormOverlayProps> = ({
  isOpen,
  categories,
  initialData,
  onClose,
  onSubmit,
}) => {
  // Selected new image (optional)
  const [selectedImage, setSelectedImage] = useState<File | null>(null);

  // **derive formData from props only when overlay is open**
  const initialFormData = useMemo(
    () => (isOpen ? getInitialFormData(initialData) : getInitialFormData()),
    [isOpen, initialData],
  );

  const [formData, setFormData] = useState<ArtworkRequest>(initialFormData);

  // Update formData whenever overlay opens (safely, synchronously)
  // using derived state
  React.useEffect(() => {
    setFormData(initialFormData);
    setSelectedImage(null);
  }, [initialFormData]);

  if (!isOpen) return null;

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    await onSubmit(formData, selectedImage, initialData?.id);
    onClose();
  };

  const previewImage = selectedImage
    ? URL.createObjectURL(selectedImage)
    : initialData?.imgUrl;

  return (
    <div>
      <button type="button" onClick={onClose}>
        ×
      </button>

      <h2>{initialData ? "Update Artwork" : "Create Artwork"}</h2>

      <form onSubmit={handleSubmit}>
        <input
          placeholder="Title"
          value={formData.title}
          onChange={(e) =>
            setFormData((prev) => ({ ...prev, title: e.target.value }))
          }
          required
        />

        <input
          placeholder="Description"
          value={formData.description}
          onChange={(e) =>
            setFormData((prev) => ({ ...prev, description: e.target.value }))
          }
          required
        />

        <Choose
          label="Category"
          value={formData.category}
          options={categories}
          onChange={(value) =>
            setFormData((prev) => ({ ...prev, category: value }))
          }
        />

        <input
          type="number"
          placeholder="Price"
          value={formData.price}
          onChange={(e) =>
            setFormData((prev) => ({
              ...prev,
              price: Number(e.target.value),
            }))
          }
          required
        />

        <input
          placeholder="Label"
          value={formData.label}
          onChange={(e) =>
            setFormData((prev) => ({ ...prev, label: e.target.value }))
          }
        />

        <input
          type="file"
          onChange={(e) =>
            e.target.files && setSelectedImage(e.target.files[0])
          }
        />

        {previewImage && (
          <div>
            <p>Preview:</p>
            <img
              src={previewImage}
              alt="Artwork preview"
              width={150}
              style={{ display: "block", marginTop: "8px" }}
            />
          </div>
        )}

        <button type="submit">{initialData ? "Update" : "Create"}</button>
      </form>
    </div>
  );
};

export default ArtFormOverlay;
