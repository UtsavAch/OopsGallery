import React, { useEffect, useState } from "react";
import { useAuth } from "../../contexts/useAuth";
import { usersService } from "../../services/users/usersService";
import type {
  UserRequest,
  UserResponse,
} from "../../services/users/users.types";
import { useNavigate } from "react-router-dom";

const ProfilePage: React.FC = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  const [profile, setProfile] = useState<UserResponse | null>(null);
  const [formData, setFormData] = useState<UserRequest>({
    firstName: "",
    lastName: "",
    email: "",
    phoneNo: "",
    address: "",
    password: "",
  });

  const [editing, setEditing] = useState(false);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");

  // ---------------------------
  // Load profile
  // ---------------------------
  useEffect(() => {
    const loadProfile = async () => {
      if (!user) return;

      try {
        const data = await usersService.findById(user.userId);
        setProfile(data);
        setFormData({
          firstName: data.firstName,
          lastName: data.lastName,
          email: data.email,
          phoneNo: data.phoneNo,
          address: data.address,
          password: "",
        });
      } catch (err) {
        setError("Failed to load profile." + err);
      }
    };

    loadProfile();
  }, [user]);

  // ---------------------------
  // Handle input change
  // ---------------------------
  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  // ---------------------------
  // Update profile
  // ---------------------------
  const handleUpdate = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!user) return;

    setError("");
    setMessage("");

    try {
      const updated = await usersService.update(user.userId, formData);
      setProfile(updated);
      setEditing(false);
      setMessage("Profile updated successfully.");
    } catch (err) {
      setError("Failed to update profile." + err);
    }
  };

  // ---------------------------
  // Delete profile
  // ---------------------------
  const handleDelete = async () => {
    if (!user) return;

    const confirmDelete = window.confirm(
      "Are you sure you want to delete your account?",
    );
    if (!confirmDelete) return;

    try {
      await usersService.deleteById(user.userId);
      logout();
      navigate("/"); // redirect after delete
    } catch (err) {
      setError("Failed to delete account." + err);
    }
  };

  if (!profile) return <p>Loading...</p>;

  return (
    <div>
      <h1>Profile</h1>

      {error && <p>{error}</p>}
      {message && <p>{message}</p>}

      {!editing ? (
        <div>
          <p>First Name: {profile.firstName}</p>
          <p>Last Name: {profile.lastName}</p>
          <p>Email: {profile.email}</p>
          <p>Phone: {profile.phoneNo}</p>
          <p>Address: {profile.address}</p>
          <p>Role: {profile.role}</p>

          <button onClick={() => setEditing(true)}>Edit</button>
          <button onClick={handleDelete}>Delete Account</button>
        </div>
      ) : (
        <form onSubmit={handleUpdate}>
          <input
            name="firstName"
            value={formData.firstName}
            onChange={handleChange}
            required
          />

          <input
            name="lastName"
            value={formData.lastName}
            onChange={handleChange}
            required
          />

          <input
            name="email"
            type="email"
            value={formData.email}
            onChange={handleChange}
            required
          />

          <input
            name="phoneNo"
            value={formData.phoneNo}
            onChange={handleChange}
            required
          />

          <input
            name="address"
            value={formData.address}
            onChange={handleChange}
            required
          />

          <input
            name="password"
            type="password"
            placeholder="New Password (optional)"
            value={formData.password}
            onChange={handleChange}
          />

          <button type="submit">Save</button>
          <button type="button" onClick={() => setEditing(false)}>
            Cancel
          </button>
        </form>
      )}
    </div>
  );
};

export default ProfilePage;
